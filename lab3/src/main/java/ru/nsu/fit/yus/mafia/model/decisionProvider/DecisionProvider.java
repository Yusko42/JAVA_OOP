package ru.nsu.fit.yus.mafia.model.decisionProvider;

import ru.nsu.fit.yus.mafia.model.GameContext;
import ru.nsu.fit.yus.mafia.model.Player;
import ru.nsu.fit.yus.mafia.model.messages.LastWord;
import ru.nsu.fit.yus.mafia.model.messages.Message;

import java.util.List;

public interface DecisionProvider {
    Player chooseLowestTrust(Player self, List<Player> livingCandidates);
    Player chooseHighestTrust(Player self, List<Player> livingCandidates);
    Message chooseMessage(Player self, GameContext context);
    Message chooseMafiaMessage(Player self, GameContext context);
    Message chooseSheriffMessage(Player self, GameContext context, int jailedMafiaCount, int totalMafiaCount);
    LastWord chooseLastWord(Player self, GameContext context);
}
