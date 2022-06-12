package com.example.maps.entity;

public class Message {
    public int id;
    public String message;
    public String name;
    public int clientId;
    public int eventId;
    public long messageDate;

    public Message(int id, String message, String name, int clientId, int eventId, long messageDate) {
        this.id = id;
        this.message = message;
        this.name = name;
        this.clientId = clientId;
        this.eventId = eventId;
        this.messageDate = messageDate;
    }
}
