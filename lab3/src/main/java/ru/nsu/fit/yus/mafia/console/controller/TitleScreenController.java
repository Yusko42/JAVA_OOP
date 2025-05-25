package ru.nsu.fit.yus.mafia.console.controller;

import ru.nsu.fit.yus.mafia.console.PlayerData;
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
        while (true) {
            view.showTitleScreen();
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

        view.showEnterTheNumberOfPlayers();
        int number;

        while (true) {
            String number_str = input.nextLine().trim();
            try {
                number = Integer.parseInt(number_str);
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
