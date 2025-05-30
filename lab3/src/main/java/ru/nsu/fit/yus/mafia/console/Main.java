package ru.nsu.fit.yus.mafia.console;

import ru.nsu.fit.yus.mafia.console.controller.ConsoleController;
import ru.nsu.fit.yus.mafia.console.controller.TitleScreenController;
import ru.nsu.fit.yus.mafia.console.view.ConsoleView;
import ru.nsu.fit.yus.mafia.console.view.TitleScreenView;
import ru.nsu.fit.yus.mafia.model.Model;

public class Main {
    public static void main(String[] args) {
        // Инициализация титульника
        TitleScreenView titleScreenView = new TitleScreenView();
        TitleScreenController titleScreenController = new TitleScreenController(titleScreenView);

        // Пойдём дальне только когда выбрали получим количество игроков и имя реального игрока
        PlayerData data = titleScreenController.runTitleScreen();

        //Начало игры
        Model model = new Model();
        ConsoleView view = new ConsoleView(model); // View - подписчик на model
        ConsoleController controller = new ConsoleController(model, view, data.name(), data.number());
        controller.runGame();
    }
}