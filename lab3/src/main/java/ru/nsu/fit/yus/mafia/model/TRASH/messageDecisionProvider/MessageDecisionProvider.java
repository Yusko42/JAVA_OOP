package ru.nsu.fit.yus.mafia.model.TRASH.messageDecisionProvider;

import ru.nsu.fit.yus.mafia.model.Player;
import ru.nsu.fit.yus.mafia.model.GameContext;
import ru.nsu.fit.yus.mafia.model.messages.Message;

public interface MessageDecisionProvider {
    Message chooseMessage(Player self, GameContext context);
}
