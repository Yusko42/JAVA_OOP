package ru.nsu.fit.yus.mafia.console.view;

public class TitleScreenView {
    public void showTitleScreen() {
        cleanConsole();
        System.out.println("                  -- MAFIA: The Game --                     \n\n\n");
        System.out.println("(c) 1986, 1987, 2025  Based on the game by Dimitry Davidoff\n");
        System.out.println("Enter the command below:");
    }
    public void showAbout() {
        cleanConsole();
        System.out.println("Welcome to the world of MAFIA!");
        System.out.println("-- STORY --");
        System.out.println("A war broke out between two groups: the MAFIA & the CIVILIANS.");
        System.out.println("The goal of the mafia is to reach numerical parity with the civilians.");
        System.out.println("The goal of the civilians is to get rid of every mafia member before it's too late.");

        System.out.println("\n-- GAMEPLAY --");
        System.out.println("The game consists of two phases: NIGHT and DAY.");
        System.out.println("At NIGHT, the mafia secretly eliminates a player.");
        System.out.println("During the DAY, all players discuss and vote to eliminate a suspect.");

        System.out.println("\n-- ROLES --");
        System.out.println("MAFIA     - Knows other mafia. Eliminates players at night.");
        System.out.println("CIVILIAN  - No special powers. Tries to find and vote out mafia.");
        System.out.println("DOCTOR    - Can save one player each night.");
        System.out.println("DETECTIVE - Can check if one player is mafia each night.");

        System.out.println("\nWork together, trust no one... and survive.");
    }

    public void showLegalInfo() {
        cleanConsole();
        System.out.println("(c) Original game by Dimitry Davidoff     1986, 1987");
        System.out.println("(c) Java Adaptation by @yusk42            2025");
        System.out.println("\n");
        System.out.println("The program is a free implementation of 'Mafia', a social deduction game created by Dimitry Davidoff.");
        System.out.println("The creator of this implementation does not claim intellectual property rights of");
        System.out.println("authors and owners of the trademark.");
        System.out.println("The software is freeware. If you paid for it, you have been SCAMMED!");
    }
    public void showInvalidInput() {
        cleanConsole();
        System.out.println("Invalid input! Enter 'HELP' for the list of commands");
    }

    public void showHelp() {
        cleanConsole();
        System.out.println("START - Start the game");
        System.out.println("ABOUT - Information about the game");
        System.out.println("TITLE - Title screen");
        System.out.println("LEGAL - Legal information");
        System.out.println("HELP  - List of available commands");
        System.out.println("EXIT  - Exit the game");

    }

    public void showInvalidNumberInput() {
        System.out.println("Invalid input of the number! Enter only the number from 6 to 10 inclusive.");
    }

    public void showEnterThePlayersName() {
        System.out.println("Enter your name: ");
    }

    public void showEnterTheNumberOfPlayers() {
        System.out.println("Enter the number of players (from 6 to 10): ");
    }

    private void cleanConsole() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
