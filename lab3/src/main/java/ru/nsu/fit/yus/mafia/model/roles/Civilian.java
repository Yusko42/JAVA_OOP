package ru.nsu.fit.yus.mafia.model.roles;

public class Civilian implements Role {
    public String getRoleName() {
        return "Civilian";
    }

    public String getRoleDescription(){
        return "Your task as a good citizen is to find a mafia member and put him behind bars before they kill anyone.";
    }

    public String getAbilityDescription(){
        return "You can accuse someone of being a mafioso players during the day.";
    }

    public void nightAction(){
        return;
    }
}
