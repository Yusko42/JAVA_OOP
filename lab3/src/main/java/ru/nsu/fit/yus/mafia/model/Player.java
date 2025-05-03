package ru.nsu.fit.yus.mafia.model;

import ru.nsu.fit.yus.mafia.model.roles.Role;
import java.util.Map;
import java.util.HashMap;

public class Player {
    private final String playerName;                                // Gamer42
    private final Role playerRole;                                  // Mafia
    private boolean isAlive = true;
    private boolean isBot;                                          // Yes
    private Map<Player, Double> trustLevels = new HashMap<>();      // For bots

    public Player(String playerName, Role playerRole, boolean isBot) {
        this.playerName = playerName;
        this.playerRole = playerRole;
        this.isBot = isBot;
    }

    public String getPlayerName() { return playerName; }
    public Role getPlayerRole() { return playerRole; }
    public boolean isAlive() { return isAlive; }
    //На основе этого значения = меняется механизм действия роли
    public boolean isBot() { return isBot; }
    public Map<Player, Double> getTrustLevels() { return trustLevels; }

    public void kill() {
        isAlive = false;
    }

    public void heal() {
        isAlive = true;
    }
}
