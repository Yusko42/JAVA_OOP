package ru.nsu.fit.yus.mafia.model.roles;

import ru.nsu.fit.yus.mafia.model.GameContext;
import ru.nsu.fit.yus.mafia.model.Player;
import ru.nsu.fit.yus.mafia.model.messages.Message;

import java.util.List;

public class Sheriff implements Role {
    private int totalMafiaCount;
    private int jailedMafiaCount;

    public Sheriff(int totalMafiaCount) {
        this.totalMafiaCount = totalMafiaCount;
        this.jailedMafiaCount = 0;
    }

    public String getRoleName() {
        return "Sheriff";
    }

    public String getRoleDescription() {
        return "Your task is to find mafia members and not get caught by them.";
    }

    public String getAbilityDescription() {
        return "You can ask the host whether someone is a mafiosi during the night and take part in the discussion during the day.";
    }

    @Override
    public boolean isSheriff() {
        return true;
    }

    @Override
    public boolean ignoresTrustShift(Player from, Player to, GameContext context) {
        double trust = to.getTrustLevels().getOrDefault(from, 0.0);
        return trust == 2.0 || trust == -2.0;
    }

    // Search for the most suspicious player
    public Player nightAction(Player self, List<Player> livingPlayers) {
        return self.getDecisionProvider().chooseLowestTrust(self, livingPlayers);
    }

    @Override
    public Message dayDiscussion(Player self, GameContext context) {
        return self.getDecisionProvider().chooseSheriffMessage(self, context, jailedMafiaCount, totalMafiaCount);
    }

    // Изменять, если к концу дня выбрали его
    public void changeJailedMafiaCount() { ++jailedMafiaCount; }
}
