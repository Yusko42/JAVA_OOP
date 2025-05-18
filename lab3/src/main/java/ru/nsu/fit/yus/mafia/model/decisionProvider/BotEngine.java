package ru.nsu.fit.yus.mafia.model.decisionProvider;

import ru.nsu.fit.yus.mafia.model.GameContext;
import ru.nsu.fit.yus.mafia.model.Player;
import ru.nsu.fit.yus.mafia.model.messages.LastWord;
import ru.nsu.fit.yus.mafia.model.messages.LastWordType;
import ru.nsu.fit.yus.mafia.model.messages.Message;
import ru.nsu.fit.yus.mafia.model.messages.MessageType;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class BotEngine implements DecisionProvider {
    public Player chooseLowestTrust(Player self, List<Player> livingCandidates) {
        double ratingScore = 1.0;
        for (Player target: livingCandidates) {
            //Выбираем самого нежелательного игрока по рейтингу
            ratingScore = Math.min(ratingScore, self.getTrustLevels().get(target));
        }
        double finalRatingScore = ratingScore;
        List<Player> minRatingVictims = livingCandidates.stream()
                .filter(p -> (self.getTrustLevels().get(p) == finalRatingScore))
                .toList();
        return minRatingVictims.get(ThreadLocalRandom.current().nextInt(minRatingVictims.size()));
    }

    public Player chooseHighestTrust(Player self, List<Player> livingCandidates) {
        double ratingScore = -1.0;
        for (Player target: livingCandidates) {
            //Выбираем, кому игрок доверяет больше всего из всех живых
            ratingScore = Math.max(ratingScore, self.getTrustLevels().get(target));
        }
        double finalRatingScore = ratingScore;
        List<Player> minRatingVictims = livingCandidates.stream()
                .filter(p -> (self.getTrustLevels().get(p) == finalRatingScore))
                .toList();
        return minRatingVictims.get(ThreadLocalRandom.current().nextInt(minRatingVictims.size()));
    }

    // Для мирных (почти)
    public Message chooseMessage(Player self, GameContext context) {
        Map<Player, Double> trustLevels = self.getTrustLevels();
        List<Player> otherPlayers = context.getAlivePlayersExcept(self);

        double trustThreshold = 0.6;
        double suspicionThreshold = -0.7;

        MessageType type;
        Player target;

        // Идея с однозначным выбором - неактуальна для мафии/шерифа, у них - фиксированные значения
        // 2.0 - мирный/союзник, -2.0 - обнаруженный мафиозо
        Player mostTrusted = chooseHighestTrust(self, otherPlayers); //Collections.max(otherPlayers, Comparator.comparingDouble(trustLevels::get));
        if (trustLevels.get(mostTrusted) > trustThreshold) {
            type = MessageType.SUPPORT;
            target = mostTrusted;
            String text = generateBotMessage(type, target.getPlayerName());
            return new Message(self, target, type, text);
        }

        Player leastTrusted = chooseLowestTrust(self, otherPlayers); //Collections.min(otherPlayers, Comparator.comparingDouble(trustLevels::get));
        if (trustLevels.get(leastTrusted) < suspicionThreshold) {
            type = MessageType.SUSPICION;
            target = leastTrusted;
            String text = generateBotMessage(type, target.getPlayerName());
            return new Message(self, target, type, text);
        }

        // Идея: случайный выбор в случае, если диапозон крайних значений довольно маленький
        return getRandomMessage(self, mostTrusted, leastTrusted);
    }

    public Message chooseMafiaMessage(Player self, GameContext context) {
        Map<Player, Double> trustLevels = self.getTrustLevels();
        List<Player> otherPlayers = context.getAlivePlayersExcept(self);

        Player mostTrusted = chooseHighestTrust(self, otherPlayers);
        Player leastTrusted = chooseLowestTrust(self, otherPlayers);

        // Груповое подстрекательство против своих врагов
        if (trustLevels.get(leastTrusted) < -0.7) {
            MessageType type = MessageType.SUSPICION;
            String text = generateBotMessage(type, leastTrusted.getPlayerName());
            return new Message(self, leastTrusted, type, text);
        }

        return getRandomMessage(self, mostTrusted, leastTrusted);
    }

    public Message chooseSheriffMessage(Player self, GameContext context, int jailedMafiaCount, int totalMafiaCount) {
        Map<Player, Double> trustLevels = self.getTrustLevels();
        List<Player> otherPlayers = context.getAlivePlayersExcept(self);

        List<Player> confirmedMafia = otherPlayers.stream()
                .filter(p -> trustLevels.get(p) == -2.0)
                .toList();

        // Если есть кто-то из признанных мафией
        for (Player target : confirmedMafia) {
            // Если ещё недостаточно мафии посажено — просто подозрение
            if (jailedMafiaCount < totalMafiaCount - 1) {
                String text = generateBotMessage(MessageType.SUSPICION, target.getPlayerName());
                return new Message(self, target, MessageType.SUSPICION, text);
            } else {
                String text = generateBotMessage(MessageType.SHERIFF_MAFIA_REPORT, target.getPlayerName());
                return new Message(self, target, MessageType.SHERIFF_MAFIA_REPORT, text);
            }
        }

        // Пока не найдено мафии, действуем случайно
        Player mostTrusted = chooseHighestTrust(self, otherPlayers); //Collections.max(otherPlayers, Comparator.comparingDouble(trustLevels::get));
        Player leastTrusted = chooseLowestTrust(self, otherPlayers); //Collections.min(otherPlayers, Comparator.comparingDouble(trustLevels::get));

        return getRandomMessage(self, mostTrusted, leastTrusted);
    }

    public LastWord chooseLastWord(Player self, GameContext context) {
        Map<Player, Double> trustLevels = self.getTrustLevels();
        List<Player> otherPlayers = context.getAlivePlayersExcept(self);

        double suspicionThreshold = -0.6;
        Player leastTrusted = chooseLowestTrust(self, otherPlayers);

        LastWordType type;

        if (trustLevels.get(leastTrusted) < suspicionThreshold) {
            type = LastWordType.NAME_THE_KILLER;
            String text = generateBotLastWord(type, leastTrusted.getPlayerName());
            return new LastWord(self, leastTrusted, type, text);
        } else {
            type = LastWordType.NOT_SURE;
            String text = generateBotLastWord(type, leastTrusted.getPlayerName());
            return new LastWord(self, leastTrusted, type, text);
        }
    }

    // Здесь - вывод сообщения для игрока.
    private String generateBotMessage(MessageType type, String targetName) {
        return switch (type) {
            case SUSPICION -> {
                String[] templates = {
                        "I don't like %s. I think they're a mafia.",
                        "I have a bad feeling about %s.",
                        "There's something wrong with %s. I suspect them."
                };
                yield String.format(templates[new Random().nextInt(templates.length)], targetName);
            }
            case SUPPORT -> "I think " + targetName + " can be trusted.";
            case SHERIFF_MAFIA_REPORT -> "I've checked " + targetName + ". They're a mafia member.";
            //case SHERIFF_CITIZEN_REPORT -> "I've checked " + targetName + ". They're an innocent civilian."; //надо потом придумать сценарий...
            default -> "I'm not sure for now.";
        };
    }

    private String generateBotLastWord(LastWordType type, String targetName) {
        return switch (type) {
            case NAME_THE_KILLER -> "I think " + targetName + "killed me...";
            case NOT_SURE -> "I'm not sure who killed me...";
            default -> "...";
        };
    }

    private Message getRandomMessage(Player self, Player mostTrusted, Player leastTrusted) {
        Random rnd = new Random();
        if (rnd.nextDouble() < 0.5) {
            String text = generateBotMessage(MessageType.SUPPORT, mostTrusted.getPlayerName());
            return new Message(self, mostTrusted, MessageType.SUPPORT, text);
        } else {
            String text = generateBotMessage(MessageType.SUSPICION, leastTrusted.getPlayerName());
            return new Message(self, leastTrusted, MessageType.SUSPICION, text);
        }
    }
}
