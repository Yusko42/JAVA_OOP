package ru.nsu.fit.yus.mafia.model.roles;

import ru.nsu.fit.yus.mafia.model.Player;

import java.util.List;

public class Sheriff implements Role {
    public String getRoleName() {
        return "Sheriff";
    }

    public String getRoleDescription() {
        return "Your task is to find mafia members and not get caught by them.";
    }

    public String getAbilityDescription() {
        return "You can ask the host whether someone is a mafiosi during the night and take part in the discussion during the day.";
    }

    // Search for the most suspicious player
    public Player nightAction(Player self, List<Player> livingPlayers) {
        return self.getDecisionProvider().chooseLowestTrust(self, livingPlayers);
    }
}
