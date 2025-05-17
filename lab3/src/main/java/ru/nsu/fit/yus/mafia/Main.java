package ru.nsu.fit.yus.mafia;

import ru.nsu.fit.yus.mafia.console.controller.Controller;
import ru.nsu.fit.yus.mafia.model.Model;
import ru.nsu.fit.yus.mafia.model.Player;
import ru.nsu.fit.yus.mafia.model.decisionProvider.BotEngine;
import ru.nsu.fit.yus.mafia.model.decisionProvider.DecisionProvider;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Controller controller = new Controller();
        Model model = new Model(...);
        //private View view;

        List<Player> playersList = new ArrayList<>();

        // 2. Создание списка игроков с назначенными ролями
        int numberOfPlayers = controller.getNumberOfPlayers();
        int numberOfRealPlayers = 1; //controller.getNumberOfRealPlayers; - в перспективе, если буду делать мультиплеер
        String realPlayerName = controller.getPlayerName();

        // Создание реального игрока (в будущем - новых игроков?)
        for (int i = 1; i < numberOfRealPlayers + 1; i++) {
            Player realPlayer = new Player(realPlayerName, model.getRoleForNewPlayer(), false, controller);
            playersList.add(realPlayer);
        }

        //Создание ботов
        for (int i = 1; i < numberOfPlayers - numberOfRealPlayers + 1; i++) {
            DecisionProvider botEngine = new BotEngine();
            Player bot = new Player("Bot " + i, model.getRoleForNewPlayer(), true, botEngine);
            playersList.add(bot);
        }

        // 3. Передача в модель

    }



}
