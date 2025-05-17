package ru.nsu.fit.yus.mafia.model.roles;

import ru.nsu.fit.yus.mafia.model.Player;

import java.util.List;

public class Civilian implements Role {
    public String getRoleName() {
        return "Civilian";
    }

    public String getRoleDescription(){
        return "Your task as a good citizen is to find a mafia member and put him behind bars before they kill anyone.";
    }

    public String getAbilityDescription(){
        return "You can accuse someone of being a mafiosi players during the day.";
    }

    public Player nightAction(Player self, List<Player> livingCivilians){
        return self;
    }
}
