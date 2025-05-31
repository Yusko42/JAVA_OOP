package ru.nsu.fit.yus.mafia.gui;

import ru.nsu.fit.yus.mafia.gui.controller.TitleScreenController;
import ru.nsu.fit.yus.mafia.gui.view.TitleScreenView;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TitleScreenView view = new TitleScreenView();
            TitleScreenController controller = new TitleScreenController(view);
            controller.show();
        });
    }
}
