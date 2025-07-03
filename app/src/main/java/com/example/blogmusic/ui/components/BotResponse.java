package com.example.blogmusic.ui.components;

public class BotResponse {
    private final boolean status;
    private final String message;

    public BotResponse(boolean status, String message) {
        this.status = status;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public boolean isStatus() {
        return status;
    }
}

