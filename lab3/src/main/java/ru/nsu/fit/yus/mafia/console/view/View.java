package ru.nsu.fit.yus.mafia.console.view;
/*
import ru.nsu.fit.yus.mafia.model.Player;
import ru.nsu.fit.yus.mafia.model.messages.LastWord;
import ru.nsu.fit.yus.mafia.model.messages.Message;
*/

import ru.nsu.fit.yus.mafia.console.view.dto_objects.LastWordInfo;
import ru.nsu.fit.yus.mafia.console.view.dto_objects.MessageInfo;
import ru.nsu.fit.yus.mafia.console.view.dto_objects.PlayerInfo;

import java.util.List;
import java.util.Map;

public class View {

    // Оставлю может для вывода ошибок?

    public void enterPlayerName() {
        System.out.println("Enter your name: ");
    }

    public void enterTheNumberOfPlayers() {
        System.out.println("Enter the number of players (from 6 to 10): ");
    }


    public void showAnnouncement(String text) {
        System.out.println("ANNOUNCEMENT: \n" + text);
    }
    
    public void showMessage(String message) {
        System.out.println(message);
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

    /*public void showPlayerRoles(List<Player> players) {
        for (Player p : players) {
            System.out.println(p.getPlayerName() + ": " + p.getPlayerRole().getClass().getSimpleName());
        }
    }*/

    public void showFinalResult(String result) {
        System.out.println("🏁 Game Over: " + result);
    }
}
