package ru.nsu.fit.yus.mafia.model.messages;

import ru.nsu.fit.yus.mafia.model.Player;

enum MessageType {
    SUSPICION,
    SUPPORT,
    SHERIFF_REPORT
}

public class Message {
    private Player author;
    private Player target;
    private MessageType messageType;


    Message(Player self) {
        author = self;
    }



}
