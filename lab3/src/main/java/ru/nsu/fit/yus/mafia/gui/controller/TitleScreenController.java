package ru.nsu.fit.yus.mafia.gui.controller;

import ru.nsu.fit.yus.mafia.PlayerData;
import ru.nsu.fit.yus.mafia.gui.view.InfoWindow;
import ru.nsu.fit.yus.mafia.gui.view.StartGameListener;
import ru.nsu.fit.yus.mafia.gui.view.TitleScreenView;

public class TitleScreenController {
    private final TitleScreenView view;
    private final StartGameListener startGameListener;

    public TitleScreenController(TitleScreenView view, StartGameListener startGameListener) {
        this.view = view;
        this.startGameListener = startGameListener;
        runTitleScreen();
    }

    public void runTitleScreen() {
        view.getNewGameButton().addActionListener(e -> {
            // Заводим объекты для запуска игры
            PlayerData data = startGame();
            if (data != null) {
                startGameListener.onNewGame(data);
            }
        });

        view.getAboutButton().addActionListener(e -> InfoWindow.showAbout());

        view.getHelpButton().addActionListener(e -> InfoWindow.showHelp());

        view.getLegalInfoButton().addActionListener(e -> InfoWindow.showLegalInfo());

        view.getExitButton().addActionListener(e -> System.exit(0));
    }

    public PlayerData startGame() {
        // Получаем настройки игры (нужно во view)
        String playerName;
        while (true) {
            playerName = view.displayEnterYourName();
            if (playerName != null && !playerName.trim().isEmpty()) {
                break;
            }
            view.displayInvalidNameInput();
        }

        int number;
        while (true) {
            String numberStr = view.displayEnterTheNumberOfPlayers();

            if (numberStr == null) {
                return null;
            }

            try {
                number = Integer.parseInt(numberStr.trim());
                if (number >= 6 && number <= 10) {
                    break;
                }
                throw new NumberFormatException();
            } catch (NumberFormatException ex) {
                view.displayInvalidNumberInput();
            }
        }

        // Закрываем стартовое окно
        view.dispose();
        return new PlayerData(playerName, number);
    }

    public void show() {
        view.setVisible(true);
    }
}
