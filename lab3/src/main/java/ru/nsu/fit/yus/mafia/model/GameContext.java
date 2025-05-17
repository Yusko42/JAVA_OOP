package ru.nsu.fit.yus.mafia.model;

import ru.nsu.fit.yus.mafia.model.roles.Mafia;
import ru.nsu.fit.yus.mafia.model.roles.Sheriff;

import java.util.List;
import java.util.stream.Collectors;

public class GameContext {
    private final List<Player> playersList;
    private int numberOfStage;
    private boolean isDay;

    public GameContext(List<Player> playersList) {
        this.playersList = playersList;
        numberOfStage = 0;
        isDay = false;
    }

    // Stage operations

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

    //Players operations

    public List<Player> getAllCivilian() {
        return playersList.stream()
                .filter(p -> !(p.getPlayerRole() instanceof Mafia))
                .toList();
    }

    public List<Player> getAliveCivilian() {
        return playersList.stream()
                .filter(Player::isAlive)
                .filter(p -> !(p.getPlayerRole() instanceof Mafia))
                .toList();
    }

    public List<Player> getAllMafia() {
        return playersList.stream()
                .filter(p -> (p.getPlayerRole() instanceof Mafia))
                .toList();
    }

    public List<Player> getAliveMafia() {
        return playersList.stream()
                .filter(Player::isAlive)
                .filter(p -> (p.getPlayerRole() instanceof Mafia))
                .toList();
    }

    public Player getAliveSheriff() {
        Player sheriff = null;
        sheriff = playersList.stream()
                .filter(Player::isAlive)
                .filter(p -> (p.getPlayerRole() instanceof Sheriff)).toList().removeFirst();
        if (sheriff == null)
            throw new IllegalStateException("Sorry, the sheriff's dead...");
        return sheriff;
    }

    public List<Player> getAlivePlayers() {
        return playersList.stream()
                .filter(Player::isAlive)
                .toList();
    }
}
