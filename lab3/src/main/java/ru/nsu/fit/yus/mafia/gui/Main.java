package ru.nsu.fit.yus.mafia.gui;

import ru.nsu.fit.yus.mafia.gui.controller.TitleScreenController;
import ru.nsu.fit.yus.mafia.gui.view.TitleScreenView;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GameLauncher gameLauncher = new GameLauncher();
            TitleScreenView titleView = new TitleScreenView();


            TitleScreenController titleController = new TitleScreenController(titleView, gameLauncher);
            titleController.show();
        });
    }
}
