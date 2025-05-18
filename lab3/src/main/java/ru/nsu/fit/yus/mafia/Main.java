package ru.nsu.fit.yus.mafia;

import ru.nsu.fit.yus.mafia.console.controller.Controller;
import ru.nsu.fit.yus.mafia.model.Model;
import ru.nsu.fit.yus.mafia.model.Player;
import ru.nsu.fit.yus.mafia.model.decisionProvider.BotEngine;
import ru.nsu.fit.yus.mafia.model.decisionProvider.DecisionProvider;
import ru.nsu.fit.yus.mafia.model.decisionProvider.HumanController;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Controller controller = new Controller();
        Model model = new Model();
        //private View view;

        List<Player> playersList = new ArrayList<>();

        // Подготовка
        int numberOfPlayers = controller.getNumberOfPlayers();
        int numberOfRealPlayers = 1; //controller.getNumberOfRealPlayers; - в перспективе, если буду делать мультиплеер
        String realPlayerName = controller.getPlayerName();


        // 1. Список ролей
        model.generateAvailableRoles(numberOfPlayers);

        // 2. Создание списка игроков с назначенными ролями
        // Создание реального игрока (в будущем - новых игроков?)
        for (int i = 0; i < numberOfRealPlayers; i++) {
            DecisionProvider humanController = new HumanController(controller);
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



}
