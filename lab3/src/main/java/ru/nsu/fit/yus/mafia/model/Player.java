package ru.nsu.fit.yus.mafia.model;

import ru.nsu.fit.yus.mafia.model.decisionProvider.BotEngine;
import ru.nsu.fit.yus.mafia.model.decisionProvider.DecisionProvider;
import ru.nsu.fit.yus.mafia.model.decisionProvider.HumanController;
import ru.nsu.fit.yus.mafia.model.roles.Role;
import java.util.Map;
import java.util.HashMap;

public class Player {
    private final String playerName;                                // Gamer42
    private final Role playerRole;                                  // Mafia
    private boolean isAlive = true;
    private final boolean isBot;                                    // Yes
    private Map<Player, Double> trustLevels = new HashMap<>();      // For bots
    private DecisionProvider decisionProvider = null;               // How to act

    public Player(String playerName, Role playerRole, boolean isBot, DecisionProvider decisionProvider) {
        this.playerName = playerName;
        this.playerRole = playerRole;
        this.isBot = isBot;
        //setDecisionProvider();
        this.decisionProvider = decisionProvider; // and thus, I changed the responsibilities of classes... again.
    }

    /*private void setDecisionProvider() {
        if (isBot)
            this.decisionProvider = new BotEngine();
        else
            this.decisionProvider = new HumanController(controller);
    }*/



    public DecisionProvider getDecisionProvider() {
        if (decisionProvider == null)
            throw new IllegalStateException("DecisionProvider not set for role: " + getClass().getSimpleName());
        return decisionProvider;
    }

    public String getPlayerName() { return playerName; }
    public Role getPlayerRole() { return playerRole; }
    public boolean isAlive() { return isAlive; }
    public boolean isBot() { return isBot; }
    public Map<Player, Double> getTrustLevels() { return trustLevels; }

    public void kill() {
        isAlive = false;
    }

    public void heal() {
        isAlive = true;
    }
}
