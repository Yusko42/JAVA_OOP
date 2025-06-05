package ru.nsu.fit.yus.mafia.gui.view;

import ru.nsu.fit.yus.mafia.EventType;
import ru.nsu.fit.yus.mafia.Observer;
import ru.nsu.fit.yus.mafia.model.Player;

import java.util.List;
import java.util.Map;

public class GuiObserver implements Observer {
    private final GuiView view;
    private Player subscriber; // для фильтрации

    public GuiObserver(GuiView view) {
        this.view = view;
    }

    public void assignTheSubscriber(Player subscriber) {
        this.subscriber = subscriber;
    }

    @Override
    public void onGameEvent(EventType type, Map<String, Object> data) {
        switch (type) {
            case GAME_STARTED -> view.displayGameStart();
            case NIGHT_STARTED -> view.displayNightStart();
            case DAY_STARTED -> view.displayDayStart();
            case SHERIFF_CHECK -> {
                if (subscriber.getPlayerRole().isSheriff())
                    view.displaySheriffInvestigation((String) data.get("player"), (boolean) data.get("isMafia"));
            }
            case PATIENT_CHOSEN -> {
                if (subscriber.getPlayerRole().isDoctor())
                    view.displayDoctorChosen((String) data.get("patient"));
            }
            case PLAYER_KILLED -> view.displayPlayerKilled((String) data.get("player"));
            case PLAYER_SPOKEN -> view.displayPlayerSpoken((String) data.get("player"), (String) data.get("message"));
            case PLAYER_VOTED -> view.displayVote((String) data.get("voter"), (String) data.get("voted"));
            case MAFIA_VOTED -> {
                if (subscriber.getPlayerRole().isMafia())
                    view.displayMafiaVote((String) data.get("voter"), (String) data.get("voted"));
            }
            case VICTIM_CHOSEN -> {
                if (subscriber.getPlayerRole().isMafia())
                    view.displayMafiaDecision((String) data.get("victim"));
            }
            case PLAYER_ELIMINATED -> view.displayElimination((String) data.get("player"));
            case GAME_ENDED -> view.displayGameEnd((String) data.get("winner"));
            case GAME_OVER -> view.displayGameOver();
            case PLAYER_ROLE_REVEALED -> view.displayPlayerRole((String) data.get("player"), (String) data.get("role"));
            //case TRUST_UPDATED -> displayTrustUpdate((Map<String, Double>) data.get("trustMap"));
            //case PLAYER_MESSAGE_OPTIONS -> view.displayMessageOptions((Player) data.get("player"), (List<String>) data.get("options"));
            //case PLAYER_CHOOSE_TARGET -> view.displayTargetChoices((Player) data.get("player"), (List<String>) data.get("targets"));
            case SHOW_LIVING_PLAYERS -> view.updateLivingPlayers((List<String>) data.get("players"));
            case SHOW_LIVING_MAFIA -> {
                if (subscriber.getPlayerRole().isMafia())
                    view.displayLivingMafia((List<String>) data.get("members"));
            }
            case SHOW_LIVING_SHERIFF -> {
                if (subscriber.getPlayerRole().isSheriff())
                    view.displayLivingSheriff((String) data.get("sheriff"));
            }
            case SHOW_LIVING_DOCTOR -> {
                if (subscriber.getPlayerRole().isDoctor())
                    view.displayLivingDoctor((String) data.get("doctor"));
            }
            case PLAYER_LAST_WORD -> view.displayLastWord((String) data.get("player"), (String) data.get("message"));
            case SHOW_MESSAGE -> view.displayMessage((String) data.get("message"));
            case NEW_VOTE -> view.displayNewVote();
            case NOBODY_PRISONED -> view.displayNobodyPrisoned();
        }
        view.revalidate();
        view.repaint();
    }
}
