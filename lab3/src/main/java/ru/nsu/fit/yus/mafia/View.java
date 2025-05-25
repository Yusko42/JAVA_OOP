package ru.nsu.fit.yus.mafia;

import ru.nsu.fit.yus.mafia.dto_objects.LastWordInfo;
import ru.nsu.fit.yus.mafia.dto_objects.MessageInfo;
import ru.nsu.fit.yus.mafia.dto_objects.PlayerInfo;
import ru.nsu.fit.yus.mafia.model.Player;
import ru.nsu.fit.yus.mafia.model.messages.LastWord;
import ru.nsu.fit.yus.mafia.model.messages.Message;

import java.util.List;
import java.util.Map;

public interface View {
    void showAnnouncement(String text);
    void showMessage(String message);
    void showDiscussion(List<MessageInfo> messages);
    void showLastWord(LastWordInfo lastWord);
    void showVotingResults(Map<PlayerInfo, Integer> votes);
    //void showPlayerRoles(List<PlayerInfo> players); // можно скрывать роли у мирных
    void showFinalResult(String result);
}

