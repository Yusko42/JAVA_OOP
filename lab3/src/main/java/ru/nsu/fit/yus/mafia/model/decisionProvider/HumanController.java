package ru.nsu.fit.yus.mafia.model.decisionProvider;

import ru.nsu.fit.yus.mafia.console.controller.Controller;
import ru.nsu.fit.yus.mafia.model.GameContext;
import ru.nsu.fit.yus.mafia.model.Player;
import ru.nsu.fit.yus.mafia.model.messages.LastWord;
import ru.nsu.fit.yus.mafia.model.messages.Message;

import java.util.List;

public class HumanController implements DecisionProvider {
    private final Controller controller;

    public HumanController(Controller controller) {
        this.controller = controller;
    }

    public Player chooseLowestTrust(Player self, List<Player> livingCandidates) {
        return controller.requestPlayerTargetChoice(self, livingCandidates);
    }

    public Player chooseHighestTrust(Player self, List<Player> livingCandidates) {
        return controller.requestPlayerTargetChoice(self, livingCandidates);
    }

    public Message chooseMessage(Player self, GameContext context) {
        return controller.requestPlayerMessage(self, context);
    }

    public Message chooseMafiaMessage(Player self, GameContext context) {}
    public Message chooseSheriffMessage(Player self, GameContext context, int jailedMafiaCount, int totalMafiaCount) {}
    public LastWord chooseLastWord(Player self, GameContext context) {}
    //Пусть будет private метод, который будут вызывать остальные методы, если это человек?
}
