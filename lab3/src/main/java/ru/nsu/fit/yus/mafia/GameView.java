package ru.nsu.fit.yus.mafia;

import ru.nsu.fit.yus.mafia.model.Player;
import ru.nsu.fit.yus.mafia.model.messages.LastWord;
import ru.nsu.fit.yus.mafia.model.messages.Message;

import java.util.List;
import java.util.Map;

// НЕ ДОЛЖЕН ЗНАТЬ НИЧЕГО О МОДЕЛИ!

public interface GameView {
    void showAnnouncement(String text);
    void showMessage(String message);
    void showDiscussion(List<Message> messages);
    void showLastWord(LastWord lastWord);
    void showVotingResults(Map<Player, Integer> votes);
    void showPlayerRoles(List<Player> players); // можно скрывать роли у мирных
    void showFinalResult(String result);
}