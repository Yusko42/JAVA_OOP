package ru.nsu.fit.yus.mafia.model.roles;

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

    public void nightAction(){
        //Выбор игрока
        //Узнает от ведущего, является ли он мафией
    }
}