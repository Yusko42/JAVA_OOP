package ru.nsu.fit.yus.mafia.model.roles;

import ru.nsu.fit.yus.mafia.model.Player;

import java.util.List;

public class Doctor implements Role {
    public String getRoleName() {
        return "Doctor";
    }

    public String getRoleDescription(){
        return "Your task is to save people from fatal wounds.";
    }

    public String getAbilityDescription(){
        return "You can point to someone who can be healed during the night and take part in the discussion during the day.";
    }

    @Override
    public boolean isDoctor() {
        return true;
    }

    //Лечит того, у кого к нему наибольшее доверие
    public Player nightAction(Player self, List<Player> livingCivilians){
        return self.getDecisionProvider().chooseHighestTrust(self, livingCivilians);
    }
}