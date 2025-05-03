package ru.nsu.fit.yus.mafia.model.roles;

public class Sheriff implements Role {
    public String getRoleName() {
        return "Sheriff";
    }

    public String getRoleDescription(){
        return "Your task is to find mafia members and not get caught by them.";
    }

    public String getAbilityDescription(){
        return "You can ask the host whether someone is a mafioso during the night and take part in the discussion during the day.";
    }

    public void nightAction(){
        //Выбор игрока
        //Узнает от ведущего, является ли он мафией
    }
}
