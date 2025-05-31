package ru.nsu.fit.yus.mafia.console.view;

import ru.nsu.fit.yus.mafia.EventType;
import ru.nsu.fit.yus.mafia.Observer;
import ru.nsu.fit.yus.mafia.model.Model;
import ru.nsu.fit.yus.mafia.model.Player;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ConsoleView implements Observer {
    private Player subscriber; // для фильтрации

    // Создаем подписчика в лице ConsoleView
    public ConsoleView (Model model) {
        model.addObserver(this);
    }

    public void assignTheSubscriber(Player subscriber) {
        this.subscriber = subscriber;
    }


    @Override
    public void onGameEvent(EventType type, Map<String, Object> data) {
        switch (type) {
            case GAME_STARTED -> displayGameStart();
            case NIGHT_STARTED -> displayNightStart();
            case DAY_STARTED -> displayDayStart();
            case SHERIFF_CHECK -> displaySheriffInvestigation((String) data.get("player"), (boolean) data.get("isMafia"));
            case PATIENT_CHOSEN -> displayDoctorChosen((String) data.get("patient"));
            case PLAYER_KILLED -> displayPlayerKilled((String) data.get("player"));
            case PLAYER_SPOKEN -> displayPlayerSpoken((String) data.get("player"), (String) data.get("message"));
            case PLAYER_VOTED -> displayVote((String) data.get("voter"), (String) data.get("voted"));
            case MAFIA_VOTED -> displayMafiaVote((String) data.get("voter"), (String) data.get("voted"));
            case VICTIM_CHOSEN -> displayMafiaDecision((String) data.get("victim"));
            case PLAYER_ELIMINATED -> displayElimination((String) data.get("player"));
            case GAME_ENDED -> displayGameEnd((String) data.get("winner"));
            case GAME_OVER -> displayGameOver();
            case PLAYER_ROLE_REVEALED -> displayPlayerRole((String) data.get("player"), (String) data.get("role"));
            //case TRUST_UPDATED -> displayTrustUpdate((Map<String, Double>) data.get("trustMap"));
            case PLAYER_MESSAGE_OPTIONS -> displayMessageOptions((Player) data.get("player"), (List<String>) data.get("options"));
            case PLAYER_CHOOSE_TARGET -> displayTargetChoices((Player) data.get("player"), (List<String>) data.get("targets"));
            case SHOW_LIVING_PLAYERS -> updateLivingPlayers((List<String>) data.get("players"));
            case SHOW_LIVING_MAFIA -> displayLivingMafia((List<String>) data.get("members"));
            case SHOW_LIVING_SHERIFF -> displayLivingSheriff((String) data.get("sheriff"));
            case SHOW_LIVING_DOCTOR -> displayLivingDoctor((String) data.get("doctor"));
            case PLAYER_LAST_WORD -> displayLastWord((String) data.get("player"), (String) data.get("message"));
            case SHOW_MESSAGE -> displayMessage((String) data.get("message"));
            case NEW_VOTE -> displayNewVote();
            case NOBODY_PRISONED -> displayNobodyPrisoned();
        }
    }

    private void displayGameStart() {
        System.out.println("Игра началась!");
    }

    private void displayNightStart() {
        System.out.println("🌙 Наступает ночь...");
    }

    private void displayDayStart() {
        System.out.println("🌞 Наступил день.");
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


    // ВИДНО ТОЛЬКО ЧЛЕНАМ МАФИИ
    private void displayMafiaVote(String voter, String voted) {
        if (subscriber.getPlayerRole().isMafia()) {
            System.out.println("Мафиозо " + voter + " голосует за " + voted);
        }
    }

    private void displayMafiaDecision(String victim) {
        if (subscriber.getPlayerRole().isMafia()) {
            System.out.println("По итогам была выбрана жертва: " + victim);
        }
    }

    private void displayElimination(String player) {
        System.out.println("❌ По итогам голосования исключён игрок: " + player);
    }

    private void displayGameEnd(String winner) {
        System.out.println("🏁 Игра окончена! Победила команда: " + winner);
    }

    private void displayGameOver() {
        System.out.println("🏁 Игра окончена! Увы, вы были убиты.");
    }

    /*private void displayTrustUpdate(Map<String, Double> trustMap) {
        System.out.println("📊 Уровень доверия:");
        trustMap.forEach((player, trust) ->
                System.out.printf(" - %s: %.2f%n", player, trust));
    }*/

    private void displayPlayerRole(String player, String role) {
        System.out.println("🕵️ " + player + ", ваша роль: " + role);
    }

    private void displayMessageOptions(Player player, List<String> options) {
        if (player != subscriber) { return; }
        System.out.println("💬 Варианты ответа:");
        for (int i = 0; i < options.size(); i++) {
            System.out.printf("  [%d] %s%n", i + 1, options.get(i));
        }
    }

    private void displayTargetChoices(Player player, List<String> targets) {
        if (player != subscriber) { return; } //Выходим, если сообщение предназначено не нам
        System.out.println("🎯 " + player.getPlayerName() + ", выберите цель:");
        for (int i = 0; i < targets.size(); i++) {
            System.out.printf("  [%d] %s%n", i + 1, targets.get(i));
        }
    }

    private void updateLivingPlayers(List<String> players) {
        // сохраняем список живых
        System.out.println("Живые игроки:");
        displayLivingPlayers(players);
    }


    // ТОЛЬКО МАФИИ
    private void displayLivingMafia(List<String> mafiaMembers) {
        if (subscriber.getPlayerRole().isMafia()) {
            System.out.println("Просыпается мафия: ");
            displayLivingPlayers(mafiaMembers);
        }
    }

    // ТОЛЬКО ШЕРИФУ
    private void displayLivingSheriff(String sheriff) {
        if (subscriber.getPlayerRole().isSheriff()) {
            System.out.println("Шериф: ");
            displayLivingPlayers(Collections.singletonList(sheriff));
        }
    }

    public void displaySheriffInvestigation(String target, boolean isMafia) {
        if (subscriber.getPlayerRole().isSheriff()) {
            if (isMafia)
                System.out.println(target + " - мафия!");
            else
                System.out.println(target + " - не мафия.");
        }
    }

    // ТОЛЬКО ДОКТОРУ
    private void displayLivingDoctor(String doctor) {
        if (subscriber.getPlayerRole().isDoctor()) {
            System.out.println("Доктор: ");
            displayLivingPlayers(Collections.singletonList(doctor));
        }
    }

    private void displayDoctorChosen(String target) {
        if (subscriber.getPlayerRole().isDoctor()) {
            System.out.println("Доктор выбрал пациента: " + target);
        }
    }

    private void displayLivingPlayers(List<String> playersList) {
        for (String player : playersList) {
            System.out.println(" - " + player);
        }
    }

    private void displayLastWord(String player, String message) {
        System.out.printf("🗣 Последнее слово %s: \"%s\"%n", player, message);
    }

    private void displayMessage(String message) {
        System.out.println(message);
    }

    private void displayNewVote() {
        System.out.println("Итог неоднозначен, назначаем повторное голосование!");
    }

    private void displayNobodyPrisoned() {
        System.out.println("По итогам голосования никто не был исключён!");
    }

}
