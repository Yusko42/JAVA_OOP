package ru.nsu.fit.yus.mafia.model.messages;

import ru.nsu.fit.yus.mafia.model.Player;

public class Message {
    private final Player author;
    private final Player target;
    private final MessageType messageType;
    private final String text; //Что будет видно игрокам

    public Message(Player author, Player target, MessageType messageType, String text) {
        this.author = author;
        this.target = target;
        this.messageType = messageType;
        this.text = text;
    }

    public Player getAuthor() { return author; }
    public Player getTarget() { return target; }
    public MessageType getMessageType() { return messageType; }
    public String getText() { return text; }
}

