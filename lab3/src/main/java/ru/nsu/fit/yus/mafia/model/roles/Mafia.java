package ru.nsu.fit.yus.mafia.model.roles;

import ru.nsu.fit.yus.mafia.model.Player;

import java.util.List;

public class Mafia implements Role {

    public String getRoleName() {
        return "Mafia member";
    }

    public String getRoleDescription(){
        return "Your task is to reduce the population to at least the number of mafia members.";
    }

    public String getAbilityDescription(){
        return "You can discuss whom to kill during the night and take part in the discussion during the day.";
    }

    public Player nightAction(Player self, List<Player> livingCivilians){
        return self.getDecisionProvider().chooseLowestTrust(self, livingCivilians);
    }
}
