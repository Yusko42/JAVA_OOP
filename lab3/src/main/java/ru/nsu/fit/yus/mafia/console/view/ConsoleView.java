package ru.nsu.fit.yus.mafia.console.view;
/*
import ru.nsu.fit.yus.mafia.model.Player;
import ru.nsu.fit.yus.mafia.model.messages.LastWord;
import ru.nsu.fit.yus.mafia.model.messages.Message;
*/

import ru.nsu.fit.yus.mafia.EventType;
import ru.nsu.fit.yus.mafia.Observer;
import ru.nsu.fit.yus.mafia.dto_objects.LastWordInfo;
import ru.nsu.fit.yus.mafia.dto_objects.MessageInfo;
import ru.nsu.fit.yus.mafia.dto_objects.PlayerInfo;

import java.util.List;
import java.util.Map;

public class ConsoleView implements Observer {

    private List<String> livingPlayers = List.of(); // сохраняем список живых

    @Override
    public void onGameEvent(EventType type, Map<String, Object> data) {
        switch (type) {
            case GAME_STARTED -> displayGameStart();
            case NIGHT_STARTED -> displayNightStart();
            case NIGHT_ENDED -> displayNightEnd();
            case DAY_STARTED -> displayDayStart();
            case PLAYER_KILLED -> displayPlayerKilled((String) data.get("player"));
            case PLAYER_SPOKEN -> displayPlayerSpoken((String) data.get("player"), (String) data.get("message"));
            case PLAYER_VOTED -> displayVote((String) data.get("voter"), (String) data.get("voted"));
            case PLAYER_ELIMINATED -> displayElimination((String) data.get("player"));
            case GAME_ENDED -> displayGameEnd((String) data.get("winner"));
            case TRUST_UPDATED -> displayTrustUpdate((Map<String, Double>) data.get("trustMap"));
            case PLAYER_ROLE_REVEALED -> displayPlayerRole((String) data.get("player"), (String) data.get("role"));
            case PLAYER_MESSAGE_OPTIONS -> displayMessageOptions((List<String>) data.get("options"));
            case PLAYER_CHOOSE_TARGET -> displayTargetChoices((String) data.get("player"), (List<String>) data.get("targets"));
            case LIVING_PLAYERS_UPDATED -> updateLivingPlayers((List<String>) data.get("players"));
            case PLAYER_LAST_WORD -> displayLastWord((String) data.get("player"), (String) data.get("message"));
            case SHOW_MESSAGE -> displayMessage((String) data.get("message"));
        }
    }

    private void displayGameStart() {
        System.out.println("🟢 Игра началась!");
    }

    private void displayNightStart() {
        System.out.println("🌙 Наступает ночь...");
    }

    private void displayNightEnd() {
        System.out.println("☀️ Ночь закончилась.");
    }

    private void displayDayStart() {
        System.out.println("🌞 Наступил день.");
        displayLivingPlayers();
    }

    private void displayPlayerKilled(String player) {
        System.out.println("💀 Ночью убит игрок: " + player);
    }

    private void displayPlayerSpoken(String player, String message) {
        System.out.println("💬 " + player + ": " + message);
    }

    private void displayVote(String voter, String voted) {
        System.out.println("🗳️ " + voter + " голосует за " + voted);
    }

    private void displayElimination(String player) {
        System.out.println("❌ По итогам голосования исключён игрок: " + player);
    }

    private void displayGameEnd(String winner) {
        System.out.println("🏁 Игра окончена! Победила команда: " + winner);
    }

    private void displayTrustUpdate(Map<String, Double> trustMap) {
        System.out.println("📊 Уровень доверия:");
        trustMap.forEach((player, trust) ->
                System.out.printf(" - %s: %.2f%n", player, trust));
    }

    private void displayPlayerRole(String player, String role) {
        System.out.println("🕵️ " + player + ", ваша роль: " + role);
    }

    private void displayMessageOptions(List<String> options) {
        System.out.println("💬 Возможные реплики:");
        for (int i = 0; i < options.size(); i++) {
            System.out.printf("  [%d] %s%n", i + 1, options.get(i));
        }
    }

    private void displayTargetChoices(String player, List<String> targets) {
        System.out.println("🎯 " + player + ", выберите цель:");
        for (int i = 0; i < targets.size(); i++) {
            System.out.printf("  [%d] %s%n", i + 1, targets.get(i));
        }
    }

    private void updateLivingPlayers(List<String> players) {
        this.livingPlayers = players;
        System.out.println("🧍 Живые игроки:");
        displayLivingPlayers();
    }

    private void displayLivingPlayers() {
        for (String player : livingPlayers) {
            System.out.println(" - " + player);
        }
    }

    private void displayLastWord(String player, String message) {
        System.out.printf("🗣️ Последнее слово %s: \"%s\"%n", player, message);
    }

    private void displayMessage(String message) {
        System.out.println(message);
    }

    // Оставлю может для вывода ошибок?

    /*
    public void showAnnouncement(String text) {
        System.out.println("ANNOUNCEMENT: \n" + text);
    }
    
    public void showMessage(String message) {
        System.out.println(message);
    }

    public void showCandidates(List<String> livingCandidatesNames) {
        int number = 1;
        for (String name : livingCandidatesNames) {
            System.out.println(number++ + ". " + name);
        }
    }
    
    public void showDiscussion(List<MessageInfo> messages) {
        System.out.println("Discussion:");
        for (MessageInfo msg : messages) {
            System.out.println("- " + msg.sender() + ": " + msg.text());
        }
    }

    public void showLastWord(LastWordInfo lastWord) {
        System.out.println("Last Word of " + lastWord.victim() + ": " + lastWord.text());
    }

    public void showVotingResults(Map<PlayerInfo, Integer> votes) {
        System.out.println("Voting results:");
        votes.forEach((player, count) ->
                System.out.println("- " + player.name() + ": " + count + " votes"));
    }

    public void showPlayerRoles(List<PlayerInfo> players) {
        for (PlayerInfo p : players) {
            System.out.println(p.name() + ": " + p.getPlayerRole().getClass().getSimpleName());
        }
    }

    public void showFinalResult(String result) {
        System.out.println("🏁 Game Over: " + result);
    }*/



}
