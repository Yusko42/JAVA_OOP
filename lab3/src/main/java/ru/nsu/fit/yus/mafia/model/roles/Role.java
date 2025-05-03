package ru.nsu.fit.yus.mafia.model.roles;

public interface Role {
    String getRoleName();

    String getRoleDescription();

    String getAbilityDescription();

    void nightAction();

    default void dayAction(){
        //Голосование
    }
}
