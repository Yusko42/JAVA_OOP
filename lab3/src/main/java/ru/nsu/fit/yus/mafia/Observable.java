package ru.nsu.fit.yus.mafia;

import java.util.List;
import java.util.Map;

public interface Observable {
    void addObserver(Observer observer);
    void removeObserver(Observer observer);
    List<Observer> getObservers(); // не обязательно, но может быть полезен

    void notifyObservers(EventType type, Map<String, Object> data);

    /*
    // Вывод роли игрока
    void notifyPlayersRoleAssignment();

    // NIGHT
    void notifyNightBegin();
    // NIGHT, Mafia
    void notifyMafiaMembers();
    void notifyChooseTheVictim();
    void notifyUpdatedResultOfVoting(); // После нового голоса мафиози
    void notifyResult();

    // NIGHT, Sheriff
    void notifyAlivePlayers();
    void notifyChooseThePlayer();
    void notifyIsMafiaMember();

    //NIGHT, Doctor - notifyAlivePlayers(); и notifyChooseThePlayer();

    // DAY
    void notifyDayBegin();

    void notifyAnnouncement();
    void notifyChooseLastWord();
    void notifyLastWord();

    // DAY, Discussion
    void notifyChooseTheMessage();
    void notifyPlayerMessage();


    // DAY, Voting
    //void notifyChooseThePlayer();
    void notifyAccusation();
    void notifyVotingResult();

    // 3. GAME OVER
    void notifyGameOver();*/
}
