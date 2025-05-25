package ru.nsu.fit.yus.mafia;

import ru.nsu.fit.yus.mafia.model.GameContext;
import ru.nsu.fit.yus.mafia.model.Player;
import ru.nsu.fit.yus.mafia.model.messages.LastWord;
import ru.nsu.fit.yus.mafia.model.messages.Message;

import java.util.List;

public interface Controller {
    Player requestPlayerTargetChoice(Player self, List<Player> livingCandidates);
    Message requestPlayerMessage(Player self, GameContext context);
    Message requestMafiaMessage(Player self, GameContext context);
    Message requestShefiffMessage(Player self, GameContext context);
    LastWord requestLastWord(Player self, GameContext context);
}
