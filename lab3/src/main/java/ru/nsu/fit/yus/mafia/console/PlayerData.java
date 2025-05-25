package ru.nsu.fit.yus.mafia.console;

public class PlayerData {
    private final String name;
    private final int number;

    public PlayerData(String name, int number) {
        this.name = name;
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }
}
