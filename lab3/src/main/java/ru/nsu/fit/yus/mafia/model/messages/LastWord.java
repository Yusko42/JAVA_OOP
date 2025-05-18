package ru.nsu.fit.yus.mafia.model.messages;

import ru.nsu.fit.yus.mafia.model.Player;

public class LastWord {
    private final Player victim;
    private final Player suspect;
    private final LastWordType lastWordType;
    private final String text; //Что будет видно игрокам

    public LastWord(Player victim, Player suspect, LastWordType lastWordType, String text) {
        this.victim = victim;
        this.suspect = suspect;
        this.lastWordType = lastWordType;
        this.text = text;
    }

    public Player getVictim() { return victim; }
    public Player getSuspect() { return suspect; }
    public LastWordType getLastWordType() { return lastWordType; }
    public String getText() { return text; }
}
