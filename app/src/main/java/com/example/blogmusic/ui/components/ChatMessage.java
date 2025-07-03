package com.example.blogmusic.ui.components;

public class ChatMessage {
    private final String sender; // "user" hoặc "bot"
    private final String text;

    public ChatMessage(String sender, String text) {
        this.sender = sender;
        this.text = text;
    }

    public String getSender() {
        return sender;
    }

    public String getText() {
        return text;
    }
}
