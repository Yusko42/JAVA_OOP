package ru.nsu.fit.yus.mafia.model.roles;

import ru.nsu.fit.yus.mafia.model.GameContext;
import ru.nsu.fit.yus.mafia.model.Player;
import ru.nsu.fit.yus.mafia.model.messages.LastWord;
import ru.nsu.fit.yus.mafia.model.messages.Message;

import java.util.List;

public interface Role {
    String getRoleName();
    String getRoleDescription();
    String getAbilityDescription();

    default boolean isMafia() { return false; }
    default boolean isSheriff() { return false; }
    default boolean isDoctor() { return false; }

    /**
     * Игрок игнорирует изменение доверия к другому игроку.
     * Например, мафия не меняет отношение к другим мафам.
     */
    default boolean ignoresTrustShift(Player from, Player to, GameContext context) { return false; }

    default boolean isAlly(Player self, Player other, GameContext context) { return false; }


    Player nightAction(Player self, List<Player> playersList);

    default LastWord dayLastWord(Player self, GameContext context) {
        return self.getDecisionProvider().chooseLastWord(self, context);
    }

    default Message dayDiscussion(Player self, GameContext context) {
        return self.getDecisionProvider().chooseMessage(self, context);
    }

    default Player dayVote(Player self, List<Player> livingPlayers) {
        return self.getDecisionProvider().chooseLowestTrust(self, livingPlayers);
    }
}
