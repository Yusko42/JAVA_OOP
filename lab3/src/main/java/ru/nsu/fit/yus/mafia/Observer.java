package ru.nsu.fit.yus.mafia;

import java.util.Map;

public interface Observer {
    void onGameEvent(EventType type, Map<String, Object> data);
    /*
    // Назначенная роль
    void showPlayersRoleAssignment();

    // NIGHT
    void showNightBegin();
    // NIGHT, Mafia
    void showMafiaMembers();
    void showChooseTheVictim();
    void showUpdatedResultOfVoting(); // После нового голоса мафиози
    void showResult();

    // NIGHT, Sheriff
    void showAlivePlayers();
    void showChooseThePlayer();
    void showIsMafiaMember();

    //NIGHT, Doctor - showAlivePlayers(); и showChooseThePlayer();

    // DAY
    void showDayBegin();

    void showAnnouncement();
    void showChooseLastWord();
    void showLastWord();

    // DAY, Discussion
    void showChooseTheMessage();
    void showPlayerMessage();


    // DAY, Voting
    //void showChooseThePlayer();
    void showAccusation();
    void showVotingResult();

    // 3. GAME OVER
    void showGameOver();


    // 4. OTHER STUFF
    void showInvalidInput();
*/
}
