package ru.nsu.fit.yus.mafia.gui.controller;

import ru.nsu.fit.yus.mafia.Controller;
import ru.nsu.fit.yus.mafia.gui.view.GuiObserver;
import ru.nsu.fit.yus.mafia.gui.view.GuiView;
import ru.nsu.fit.yus.mafia.model.GameContext;
import ru.nsu.fit.yus.mafia.model.Model;
import ru.nsu.fit.yus.mafia.model.Player;
import ru.nsu.fit.yus.mafia.model.decisionProvider.BotEngine;
import ru.nsu.fit.yus.mafia.model.decisionProvider.DecisionProvider;
import ru.nsu.fit.yus.mafia.model.decisionProvider.HumanController;
import ru.nsu.fit.yus.mafia.model.messages.LastWord;
import ru.nsu.fit.yus.mafia.model.messages.LastWordType;
import ru.nsu.fit.yus.mafia.model.messages.Message;
import ru.nsu.fit.yus.mafia.model.messages.MessageType;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GuiController implements Controller {
    private final Model model;
    private final GuiView view;
    private final GuiObserver guiObserver;

    private String playerName = "DEFAULT";
    private int numberOfPlayers = 6;
    private Player self; // Реальный игрок

    public GuiController(Model model, GuiView view, String playerName, int numberOfPlayers, GuiObserver observer) {
        this.model = model;
        this.view = view;
        this.playerName = playerName;
        this.numberOfPlayers = numberOfPlayers;
        this.guiObserver = observer;

    }

    public void runGame() {
        // Запуск игрового цикла


        gameStart();
        view.setVisible(true);

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
        }
    }

    private void gameStart() {
        List<Player> playersList = new ArrayList<>();

        // Подготовка
        int numberOfRealPlayers = getNumberOfRealPlayers(); // в перспективе изменю, если буду делать мультиплеер

        // 1. Список ролей
        model.generateAvailableRoles(numberOfPlayers);

        // 2. Создание списка игроков с назначенными ролями
        // Создание реального игрока
        for (int i = 0; i < numberOfRealPlayers; i++) {
            DecisionProvider humanController = new HumanController(this);
            self = new Player(playerName, model.getRoleForNewPlayer(), false, humanController);
            playersList.add(self);
            // Вывод игрока, закрепление его за Observer для проверок
            view.addPlayer(self.getPlayerName());
            guiObserver.assignTheSubscriber(self);
        }

        //Создание ботов
        for (int i = 0; i < numberOfPlayers - numberOfRealPlayers; i++) {
            DecisionProvider botEngine = new BotEngine();
            Player bot = new Player("Bot " + (i + 1), model.getRoleForNewPlayer(), true, botEngine);
            playersList.add(bot);
            view.addPlayer("Bot" + (i + 1));
        }

        // Model посылает Observer сообщение о роли через Observer
        model.playerRoleReveal(guiObserver, playerName, self.getPlayerRole().getRoleName());

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
        model.startDay();
        model.announcement();
        model.lastWordOfTheMurdered();
        model.discussion(); // Там Controller от provider проведен
        model.vote();       // Аналогично
    }

    public class BackgroundRunner {
        public static void run(Runnable task) {
            new SwingWorker<Void, Void>() {
                @Override protected Void doInBackground() {
                    task.run();
                    return null;
                }
            }.execute();
        }
    }

    /// Далее: то, что использует HumanController
    /// Не применяю Observer за ненадобностью (назначу его на изменение основного окна?)

    @Override
    public Player requestPlayerTargetChoice(Player self, List<Player> livingCandidates) {
        List<String> livingCandidatesNames = livingCandidates.stream()
                .map(Player::getPlayerName)
                .toList();

        // Нет цикла, как в console, так как нет нет нужды
        String targetName = view.askForPlayerTarget(self, livingCandidatesNames);
        return livingCandidates.stream()
                .filter(p -> p.getPlayerName().equalsIgnoreCase(targetName))
                .findFirst()
                .orElse(null);
    }

    @Override
    public Message requestPlayerMessage(Player self, GameContext context) {
        List<String> candidates = context.getAlivePlayersExcept(self).stream()
                .map(Player::getPlayerName)
                .toList();
        List<MessageType> types = List.of(MessageType.values());

        Map<String, Object> data;

        while (true) {
            data = view.askForMessageInput(self, candidates, types);
            if (data != null)
                break;
            else
                view.askNewMessage();
        }


        String targetName = (String) data.get("target");
        Player target = context.getAlivePlayersExcept(self).stream()
                .filter(p -> p.getPlayerName().equalsIgnoreCase(targetName))
                .findFirst()
                .orElse(null);
        MessageType type = (MessageType) data.get("type");
        String message = generateMessage(type, targetName);

        return new Message(self, target, type, message);
    }

    @Override
    public Message requestMafiaMessage(Player self, GameContext context) {
        return requestPlayerMessage(self, context);
    }

    @Override
    public Message requestShefiffMessage(Player self, GameContext context) {
        return requestPlayerMessage(self, context);
    }

    @Override
    public LastWord requestLastWord(Player self, GameContext context) {
        List<String> candidates = context.getAlivePlayersExcept(self).stream()
                .map(Player::getPlayerName)
                .toList();
        List<LastWordType> types = List.of(LastWordType.values());
        Map<String, Object> data  = view.askForLastWordInput(self, candidates, types);

        LastWordType type = (LastWordType) data.get("type");
        Player suspect;
        String suspectName;

        if (type == LastWordType.NAME_THE_KILLER) {
            suspectName = (String) data.get("suspect");
            suspect = context.getAlivePlayersExcept(self).stream()
                    .filter(p -> p.getPlayerName().equalsIgnoreCase(suspectName))
                    .findFirst()
                    .orElse(null);
        } else {
            suspectName = self.getPlayerName();
            suspect = self;
        }

        String text = generateLastWord(type, suspectName);

        return new LastWord(self, suspect, type, text);
    }


    private String generateMessage(MessageType type, String targetName) {
        return switch (type) {
            case SUSPICION -> "There's something wrong with " + targetName + ". I suspect them.";
            case SUPPORT -> "I think " + targetName + " can be trusted.";
            case SHERIFF_MAFIA_REPORT -> "I've checked " + targetName + ". They're a mafia member.";
            default -> "I'm not sure for now.";
        };
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
