package ru.nsu.fit.yus.mafia.console.controller;

import ru.nsu.fit.yus.mafia.console.view.View;
import ru.nsu.fit.yus.mafia.console.view.dto_objects.PlayerInfo;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

//Простая накидочка того, что должно здесь быть

public class Controller {
    //private Model model = new Model(); //ОБЯЗАТЕЛЬНО В МЕЙН!
    /*public void game(){
        while (true) {
            model.mafiaVote();
            model.sheriffCheck();

            model.startDay();
            model.announcement();
            // Если мафия устранила достаточное кол-во игроков
            if (model.isGameOver()) { break; }
            model.discussion();
            model.vote();

            // Если избавились от мафии совсем
            if (model.isGameOver()) { break; }
            model.startNight();
        }

    }*/

    private final Model model;
    private final View view;
    private final Scanner input; // Для ввода

    public Controller(Model model, View view) {
        this.model = model;
        this.view = view;
        this.input = new Scanner(System.in);
    }

    public void runGame() {
        gameStart();  // Заводим игроков!
        while (!model.isGameOver()) {
            handlePhase();
        }
        view.showGameOver(game.getWinners());
    }

    private void gameStart() {
        List<Player> playersList = new ArrayList<>();

        // Подготовка
        int numberOfPlayers = getNumberOfPlayers();
        int numberOfRealPlayers = getNumberOfRealPlayers(); // в перспективе изменю, если буду делать мультиплеер
        String realPlayerName = getPlayerName();

        // 1. Список ролей
        model.generateAvailableRoles(numberOfPlayers);

        // 2. Создание списка игроков с назначенными ролями
        // Создание реального игрока (в будущем - новых игроков?)
        for (int i = 0; i < numberOfRealPlayers; i++) {
            DecisionProvider humanController = new HumanController(this);
            Player realPlayer = new Player(realPlayerName, model.getRoleForNewPlayer(), false, humanController);
            playersList.add(realPlayer);
        }

        //Создание ботов
        for (int i = 0; i < numberOfPlayers - numberOfRealPlayers; i++) {
            DecisionProvider botEngine = new BotEngine();
            Player bot = new Player("Bot " + (i + 1), model.getRoleForNewPlayer(), true, botEngine);
            playersList.add(bot);
        }

        // Передача в модель
        model.initializePlayersAndContext(playersList);
    }

    private void handlePhase() {
        /*switch (game.getCurrentPhase()) {
            case NIGHT -> handleNight();
            case DAY -> handleDay();
            case VOTING -> handleVoting();
        }
        game.advancePhase();*/


        model.mafiaVote();
        model.sheriffCheck();

        model.startDay();
        model.announcement();
        // Если мафия устранила достаточное кол-во игроков
        if (model.isGameOver()) { break; }
        model.discussion();
        model.vote();

        // Если избавились от мафии совсем
        if (model.isGameOver()) { break; }
        model.startNight();
    }

    private void handleNight() {
        view.showNightStart();
        for (Player p : game.getAlivePlayers()) {
            if (p.canActAtNight()) {
                view.promptNightAction(p);
                String targetName = input.nextLine();
                Player target = game.findPlayerByName(targetName);
                game.performNightAction(p, target);
            }
        }
        view.showNightResults(game.getNightSummary());
    }

    private void handleDay() {
        view.showDayStart();
        for (Message message : game.getNewMessages()) {
            view.showMessage(message);
            //Задержка!
        }

        for (Player p : game.getAlivePlayers()) {
            view.promptMessage(p);
            String msg = input.nextLine();
            game.postMessage(p, msg);
        }
    }

    private void handleVoting() {
        view.showVotingStart();
        for (Player voter : game.getAlivePlayers()) {
            view.promptVote(voter);
            String voteName = input.nextLine();
            Player voteTarget = game.findPlayerByName(voteName);
            game.vote(voter, voteTarget);
        }
        view.showVotingResults(game.getVotingResult());
        game.eliminateVotedPlayer();
    }

    /// Кажется, нужно еще добавить запрет на повторяющиеся имена...

    // Дневная роль / ночная доктора, шерифа
    public Player requestPlayerTargetChoice(Player self, List<Player> livingCandidates) {
        List<String> livingCandidatesNames = livingCandidates.stream()
                .map(Player::getPlayerName)
                .toList();

        view.chooseTheTarget(); // Choose the target: [list]
        view.showCandidates(livingCandidatesNames);

        while (true) {
            String targetName = input.nextLine().trim();
            Player target = livingCandidates.stream()
                    .filter(p -> p.getPlayerName().equalsIgnoreCase(targetName))
                    .findFirst()
                    .orElse(null);

            if (target != null) {
                return target;
            }

            view.showMessage("Wrong name. Please try again.");
        }
    }

    public Message requestPlayerMessage(Player self, GameContext context) {
        view.showPossibleMessages(); // Choose a message:
        MessageType[] types = MessageType.values();
        for (int i = 0; i < types.length; i++) {
            view.showMessage((i + 1) + ". " + generateBotMessage(types[i], "#"));
        }

        while (true) {
            String inputStr = input.nextLine().trim();
            try {
                int index = Integer.parseInt(inputStr);
                if (index >= 1 && index <= types.length) {
                    Player target = requestPlayerTargetChoice(self, context.getAlivePlayersExcept(self));
                    return new Message(self, target, types[index - 1], generateBotMessage(types[index - 1], target.getPlayerName()));
                }
            } catch (NumberFormatException e) {
                // игнорируем — ниже покажется сообщение об ошибке
            }

            view.showMessage("Invalid input. Enter the number from 1 to " + types.length + ".");
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
        view.showPossibleLastWord(); // Who you think could kill you:
        LastWordType[] types = LastWordType.values();
        for (int i = 0; i < types.length; i++) {
            view.showMessage((i + 1) + ". " + generateLastWord(types[i], "#"));
        }

        while (true) {
            String inputStr = input.nextLine().trim();
            try {
                int index = Integer.parseInt(inputStr);
                if (index >= 1 && index <= types.length) {
                    if (index == 1) {
                        Player target = requestPlayerTargetChoice(self, context.getAlivePlayersExcept(self));
                        return new LastWord(self, target, types[index - 1], generateLastWord(types[index - 1], target.getPlayerName()));
                    } else {
                        return new LastWord(self, self, types[index - 1], generateLastWord(types[index - 1], self.getPlayerName()));
                    }
                }
            } catch (NumberFormatException e) {
                // игнорируем — ниже покажется сообщение об ошибке
            }

            view.showMessage("Invalid input. Enter the number from 1 to " + types.length + ".");
        }

    }

    private String generateLastWord(LastWordType type, String targetName) {
        return switch (type) {
            case NAME_THE_KILLER -> "I think " + targetName + "killed me...";
            case NOT_SURE -> "I'm not sure who killed me...";
            default -> "...";
        };
    }

    public int getNumberOfPlayers() {
        int numberOfPlayers = 6; //Default value
        try {
            view.enterTheNumberOfPlayers();
            numberOfPlayers = Integer.parseInt(input.nextLine().trim());
            if (numberOfPlayers < 6 || numberOfPlayers > 10)
                throw new IllegalArgumentException("From 6 to 10 only!");
        } catch (NumberFormatException e) {

        } catch (IllegalArgumentException e) {

        }
        return numberOfPlayers;
    }

    public int getNumberOfRealPlayers() {
        return 1;
    }

    public String getPlayerName() {
        view.enterPlayerName();
        String playerName;
        playerName = input.nextLine();
        return playerName;
    }
}

