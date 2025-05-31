package ru.nsu.fit.yus.mafia.gui.controller;

import ru.nsu.fit.yus.mafia.gui.view.TitleScreenView;

import javax.swing.*;

public class TitleScreenController {
    private final TitleScreenView view;

    public TitleScreenController(TitleScreenView view) {
        this.view = view;
        initListeners();
    }

    private void initListeners() {
        view.getNewGameButton().addActionListener(e -> {
            // Запуск контроллера игры
            JOptionPane.showMessageDialog(view, "Запуск новой игры...");
            // new GameController(...).start(); // например
        });

        view.getAboutButton().addActionListener(e -> {
            JOptionPane.showMessageDialog(view, "Загрузка пока не реализована.");
        });

        view.getHelpButton().addActionListener(e -> {
            JOptionPane.showMessageDialog(view, "Загрузка пока не реализована.");
        });

        view.getLegalInfoButton().addActionListener(e -> {
            JOptionPane.showMessageDialog(view, "Загрузка пока не реализована.");
        });

        view.getExitButton().addActionListener(e -> System.exit(0));
    }

    public void show() {
        view.setVisible(true);
    }
}
