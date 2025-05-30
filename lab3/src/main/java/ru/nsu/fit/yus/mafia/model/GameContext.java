package ru.nsu.fit.yus.mafia.model;

import ru.nsu.fit.yus.mafia.model.messages.LastWord;
import ru.nsu.fit.yus.mafia.model.messages.Message;
import ru.nsu.fit.yus.mafia.model.roles.Doctor;
import ru.nsu.fit.yus.mafia.model.roles.Mafia;
import ru.nsu.fit.yus.mafia.model.roles.Sheriff;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameContext {
    private final List<Player> playersList;
    private final Map<Integer, List<Message>> messageLog = new HashMap<>();
    private final Map<Integer, LastWord> lastWordLog = new HashMap<>();
    private int numberOfStage;
    private boolean isDay;

    public GameContext(List<Player> playersList) {
        this.playersList = playersList;
        numberOfStage = 0;
        isDay = false;
    }

    /// Stage operations

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

    /// Players operations

    // CIVILIANS

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

    // MAFIA

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

    // SHERIFF

    public Player getAliveSheriff() {
        /*Player sheriff = null;
        sheriff = playersList.stream()
                .filter(Player::isAlive)
                .filter(p -> (p.getPlayerRole() instanceof Sheriff)).toList().removeFirst();
        if (sheriff == null)
            throw new IllegalStateException("Sorry, the sheriff's dead...");
        return sheriff;*/

        List<Player> list = new ArrayList<> (playersList.stream()
                .filter(Player::isAlive)
                .filter(p -> (p.getPlayerRole() instanceof Sheriff)).toList()); // оборачиваем в изменяемый

        if (list.isEmpty())
            return null;
            //throw new IllegalStateException("Sorry, the sheriff's dead...");
        return list.remove(0);
    }

    // DOCTOR

    public Player getAliveDoctor() {
        /*Player doctor = null;
        doctor = playersList.stream()
                .filter(Player::isAlive)
                .filter(p -> (p.getPlayerRole() instanceof Doctor)).toList().removeFirst();
        if (doctor == null)
            throw new IllegalStateException("Sorry, the doctor's dead...");
        return doctor;*/
        List<Player> list = new ArrayList<> (playersList.stream()
                .filter(Player::isAlive)
                .filter(p -> (p.getPlayerRole() instanceof Doctor)).toList()); // оборачиваем в изменяемый

        if (list.isEmpty())
            return null;
            //throw new IllegalStateException("Sorry, the doctor's dead...");
        return list.remove(0);
    }

    // ALL PLAYERS

    public List<Player> getAlivePlayers() {
        return playersList.stream()
                .filter(Player::isAlive)
                .toList();
    }

    public List<Player> getAlivePlayersExcept(Player self) {
        return playersList.stream()
                .filter(Player::isAlive)
                .filter(p -> (p != self))
                .toList();
    }

    public Map<Integer, List<Message>> getMessageLog() {
        return messageLog;
    }

    public Map<Integer, LastWord> getLastWordLog() {
        return lastWordLog;
    }
}
