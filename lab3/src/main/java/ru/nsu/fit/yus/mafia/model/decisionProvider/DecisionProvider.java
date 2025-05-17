package ru.nsu.fit.yus.mafia.model.decisionProvider;

import ru.nsu.fit.yus.mafia.model.GameContext;
import ru.nsu.fit.yus.mafia.model.Player;
import ru.nsu.fit.yus.mafia.model.messages.Message;

import java.util.List;

public interface DecisionProvider {
    Player chooseLowestTrust(Player self, List<Player> livingCandidates);
    Player chooseHighestTrust(Player self, List<Player> livingCandidates);
    Message chooseMessage(Player self, GameContext context);
}
