package ru.nsu.fit.yus.mafia.console.controller;

import ru.nsu.fit.yus.mafia.PlayerData;
import ru.nsu.fit.yus.mafia.console.view.TitleScreenView;

import java.util.Scanner;

import static java.lang.System.exit;

public class TitleScreenController {
    private final TitleScreenView view;
    private final Scanner input;

    public TitleScreenController(TitleScreenView view) {
        this.view = view;
        this.input = new Scanner(System.in);
    }

    public PlayerData runTitleScreen() {
        view.showTitleScreen();
        while (true) {
            String choice = input.nextLine();

            switch (choice) {
                case "START" -> { return startGame(); }
                case "ABOUT" -> view.showAbout();
                case "TITLE" -> view.showTitleScreen();
                case "LEGAL" -> view.showLegalInfo();
                case "HELP" -> view.showHelp();
                case "EXIT" -> exit(0);
                default -> view.showInvalidInput();
            }
        }
    }

    public PlayerData startGame() {
        view.showEnterThePlayersName();
        String name = input.nextLine().trim();
        if (name.isEmpty()) {
            name = "Player"; // Значение по умолчанию
        }

        view.showEnterTheNumberOfPlayers();
        int number;

        while (true) {
            String numberStr = input.nextLine().trim();
            try {
                number = Integer.parseInt(numberStr);
                if ((number > 5) && (number < 11))
                    break;
                else
                    view.showInvalidNumberInput();
            } catch (NumberFormatException e) {
                view.showInvalidNumberInput();
            }
        }

        return new PlayerData(name, number);
    }
}
