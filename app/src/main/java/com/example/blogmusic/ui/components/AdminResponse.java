package com.example.blogmusic.ui.components;

public class AdminResponse {
    private final boolean status;
    private final String message;

    public AdminResponse(boolean status, String message) {
        this.status = status;
        this.message = message;
    }

    public boolean isStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
