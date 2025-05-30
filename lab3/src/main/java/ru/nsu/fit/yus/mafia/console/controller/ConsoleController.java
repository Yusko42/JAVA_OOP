package ru.nsu.fit.yus.mafia.console.controller;

import ru.nsu.fit.yus.mafia.Controller;
import ru.nsu.fit.yus.mafia.EventType;
import ru.nsu.fit.yus.mafia.console.view.ConsoleView;
import ru.nsu.fit.yus.mafia.model.GameContext;
import ru.nsu.fit.yus.mafia.model.Model;
import ru.nsu.fit.yus.mafia.model.Player;
import ru.nsu.fit.yus.mafia.model.decisionProvider.BotEngine;
import ru.nsu.fit.yus.mafia.model.messages.LastWord;
import ru.nsu.fit.yus.mafia.model.messages.LastWordType;
import ru.nsu.fit.yus.mafia.model.messages.Message;
import ru.nsu.fit.yus.mafia.model.decisionProvider.DecisionProvider;
import ru.nsu.fit.yus.mafia.model.decisionProvider.HumanController;
import ru.nsu.fit.yus.mafia.model.messages.MessageType;

import java.util.*;

public class ConsoleController implements Controller {
    private final Model model;
    private final ConsoleView view;
    private final Scanner input; // Для ввода

    private String playerName = "DEFAULT";
    private int numberOfPlayers = 6;
    private Player self; // Реальный игрок

    public ConsoleController(Model model, ConsoleView view, String playerName, int numberOfPlayers) {
        this.model = model;
        this.view = view;
        this.input = new Scanner(System.in);
        this.playerName = playerName;
        this.numberOfPlayers = numberOfPlayers;
    }

    public void runGame() {
        gameStart();  // Заводим игроков!

        while (true) {
            handleNight();
            if (model.isMafiaWon() || !self.isAlive()) break;
            handleDay();
            if (model.isCiviliansWon() || !self.isAlive()) break;
        }
        if (!self.isAlive()) {
            model.gameOver();
        } else if (model.isMafiaWon()) {
            model.mafiaWon();
        } else if (model.isCiviliansWon()) {
            model.civiliansWon();
        } else
            System.out.println("Something else happened!");

    }

    private void gameStart() {
        List<Player> playersList = new ArrayList<>();

        // Подготовка
        int numberOfRealPlayers = getNumberOfRealPlayers(); // в перспективе изменю, если буду делать мультиплеер

        // 1. Список ролей
        model.generateAvailableRoles(numberOfPlayers);

        // 2. Создание списка игроков с назначенными ролями
        // Создание реального игрока (в будущем - новых игроков?)
        for (int i = 0; i < numberOfRealPlayers; i++) {
            DecisionProvider humanController = new HumanController(this);
            self = new Player(playerName, model.getRoleForNewPlayer(), false, humanController);
            playersList.add(self);

            // Вывод роли игрока. Вроде как можно выводить инфу от контроллера?
            model.playerRoleReveal(view, playerName, self.getPlayerRole().getRoleName());
        }
        view.assignTheSubscriber(self);

        //Создание ботов
        for (int i = 0; i < numberOfPlayers - numberOfRealPlayers; i++) {
            DecisionProvider botEngine = new BotEngine();
            Player bot = new Player("Bot " + (i + 1), model.getRoleForNewPlayer(), true, botEngine);
            playersList.add(bot);
        }

        // Передача в модель
        model.initializePlayersAndContext(playersList);
    }

    private void handleNight() {
        model.startNight();
        model.mafiaVote();
        model.sheriffCheck();
        model.doctorCheck();
        model.killPossibleVictim();
    }

    private void handleDay() {
        model.announcement();
        model.lastWordOfTheMurdered();
        model.startDay();
        model.discussion(); // Там Controller от provider проведен
        model.vote();       // Аналогично
    }


    /// Кажется, нужно еще добавить запрет на повторяющиеся имена...
    /// Далее: то, что использует HumanController

    // Дневная роль / ночная доктора, шерифа
    @Override
    public Player requestPlayerTargetChoice(Player self, List<Player> livingCandidates) {
        List<String> livingCandidatesNames = livingCandidates.stream()
                .map(Player::getPlayerName)
                .toList();

        Map<String, Object> data = new HashMap<>();
        data.put("player", self);
        data.put("targets", livingCandidatesNames);
        view.onGameEvent(EventType.PLAYER_CHOOSE_TARGET, data);

        while (true) {
            String targetName = input.nextLine().trim();
            Player target = livingCandidates.stream()
                    .filter(p -> p.getPlayerName().equalsIgnoreCase(targetName))
                    .findFirst()
                    .orElse(null);

            if (target != null) {
                return target;
            }

            /*
            * try {
                int index = Integer.parseInt(inputStr);
                if (index >= 1 && index <= types.length) { // Надо выбрать номер игрока
                    Player target = livingCandidates.stream()
                    .filter(p -> p.getPlayerName().equalsIgnoreCase(targetName))
                    .findFirst()
                    .orElse(null);
                    return new Message(self, target, types[index - 1], generateMessage(types[index - 1], target.getPlayerName()));
                }
            } catch (NumberFormatException e) {
                // игнорируем — ниже покажется сообщение об ошибке
            }*/


            // Если неправильный индекс
            Map<String, Object> mes = new HashMap<>();
            mes.put("message", "Wrong name. Please try again");
            view.onGameEvent(EventType.SHOW_MESSAGE, mes);
        }
    }

    public Message requestPlayerMessage(Player self, GameContext context) {
        MessageType[] types = MessageType.values();

        List<String> options = new ArrayList<>();

        for (int i = 0; i < types.length; i++) {
            //view.showMessage((i + 1) + ". " + generateMessage(types[i], "#"));
            options.add((i + 1) + ". " + generateMessage(types[i], "#"));
        }

        Map<String, Object> mes = new HashMap<>();
        mes.put("player", self);
        mes.put("options", options);
        view.onGameEvent(EventType.PLAYER_MESSAGE_OPTIONS, mes);

        while (true) {
            String inputStr = input.nextLine().trim();
            try {
                int index = Integer.parseInt(inputStr);
                if (index >= 1 && index <= types.length) { // Надо выбрать номер игрока
                    Player target = this.requestPlayerTargetChoice(self, context.getAlivePlayersExcept(self));
                    return new Message(self, target, types[index - 1], generateMessage(types[index - 1], target.getPlayerName()));
                }
            } catch (NumberFormatException e) {
                // игнорируем — ниже покажется сообщение об ошибке
            }

            //view.showMessage("Invalid input. Enter the number from 1 to " + types.length + ".");
            Map<String, Object> inv_mes = new HashMap<>();
            inv_mes.put("message", "Invalid input. Enter the number from 1 to " + types.length + ".");
            view.onGameEvent(EventType.SHOW_MESSAGE, inv_mes);
        }

    }

    private String generateMessage(MessageType type, String targetName) {
        return switch (type) {
            case SUSPICION -> "There's something wrong with " + targetName + ". I suspect them.";
            case SUPPORT -> "I think " + targetName + " can be trusted.";
            case SHERIFF_MAFIA_REPORT -> "I've checked " + targetName + ". They're a mafia member.";
            default -> "I'm not sure for now.";
        };
    }

    public Message requestMafiaMessage(Player self, GameContext context) {
        return requestPlayerMessage(self, context);
    }

    // Надо потом прописать отдельно???
    public Message requestShefiffMessage(Player self, GameContext context) {
        return requestPlayerMessage(self, context);
    }

    public LastWord requestLastWord(Player self, GameContext context) {
        LastWordType[] types = LastWordType.values();

        List<String> options = new ArrayList<>();

        for (int i = 0; i < types.length; i++) {
            options.add((i + 1) + ". " + generateLastWord(types[i], "#"));
        }
        Map<String, Object> mes = new HashMap<>();
        mes.put("player", self);
        mes.put("options", options);
        view.onGameEvent(EventType.PLAYER_MESSAGE_OPTIONS, mes);

        while (true) {
            String inputStr = input.nextLine().trim();
            try {
                int index = Integer.parseInt(inputStr);
                if (index >= 1 && index <= types.length) {
                    if (index == 1) {
                        Player target = this.requestPlayerTargetChoice(self, context.getAlivePlayersExcept(self));
                        return new LastWord(self, target, types[index - 1], generateLastWord(types[index - 1], target.getPlayerName()));
                    } else {
                        return new LastWord(self, self, types[index - 1], generateLastWord(types[index - 1], self.getPlayerName()));
                    }
                }
            } catch (NumberFormatException e) {
                // игнорируем — ниже покажется сообщение об ошибке
            }
            //view.showMessage("Invalid input. Enter the number from 1 to " + types.length + ".");
            Map<String, Object> inv_mes = new HashMap<>();
            inv_mes.put("message", "Invalid input. Enter the number from 1 to " + types.length + ".");
            view.onGameEvent(EventType.SHOW_MESSAGE, inv_mes);
        }

    }

    private String generateLastWord(LastWordType type, String targetName) {
        return switch (type) {
            case NAME_THE_KILLER -> "I think " + targetName + " killed me...";
            case NOT_SURE -> "I'm not sure who killed me...";
            default -> "...";
        };
    }

    public int getNumberOfPlayers() {
        return this.numberOfPlayers;
    }

    public int getNumberOfRealPlayers() {
        return 1;
    }

    public String getPlayerName() {
        return playerName;
    }
}

