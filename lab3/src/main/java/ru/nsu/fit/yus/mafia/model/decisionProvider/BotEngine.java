package ru.nsu.fit.yus.mafia.model.decisionProvider;

import ru.nsu.fit.yus.mafia.model.GameContext;
import ru.nsu.fit.yus.mafia.model.Player;
import ru.nsu.fit.yus.mafia.model.messages.Message;

import java.util.List;
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

    // Один источник, так как:
    // 1. Вся логика ботов - тут;
    // 2. Человек в этом месте выбирает сам сообщение из МНОЖЕСТВА доступных
    // (в отличие от действия, привязанного к роли), а бот - в зависимости от массива с доверием и прочего...

    //p.s. может private еще наделать для разного типа сообщений?...
    public Message chooseMessage(Player self, GameContext context) {

    }
}
