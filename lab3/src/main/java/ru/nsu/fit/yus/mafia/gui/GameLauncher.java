package ru.nsu.fit.yus.mafia.gui;

import ru.nsu.fit.yus.mafia.Observer;
import ru.nsu.fit.yus.mafia.PlayerData;
import ru.nsu.fit.yus.mafia.gui.controller.GuiController;
import ru.nsu.fit.yus.mafia.gui.view.GuiObserver;
import ru.nsu.fit.yus.mafia.gui.view.GuiView;
import ru.nsu.fit.yus.mafia.gui.view.StartGameListener;
import ru.nsu.fit.yus.mafia.model.Model;

public class GameLauncher implements StartGameListener {
    public void onNewGame(PlayerData data) {
        Model model = new Model();
        GuiView view = new GuiView();
        GuiObserver observer = new GuiObserver(view);
        GuiController controller = new GuiController(model, view, data.name(), data.number(), observer);
        model.addObserver(observer);
        controller.runGame();
    }
}
