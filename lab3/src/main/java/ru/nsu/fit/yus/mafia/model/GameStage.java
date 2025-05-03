package ru.nsu.fit.yus.mafia.model;

public class GameStage {
    private int numberOfStage;
    private boolean isDay;

    public GameStage() {
        numberOfStage = 0;
        isDay = false;
    }

    public void newNight() {
        ++numberOfStage;
        isDay = false;
    }

    public void newDay() {
        isDay = true;
    }

    public boolean isDay() {
        return isDay;
    }

    public int getNumberOfStage() {
        return numberOfStage;
    }
}
