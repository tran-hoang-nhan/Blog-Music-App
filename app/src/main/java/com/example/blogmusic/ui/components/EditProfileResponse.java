package com.example.blogmusic.ui.components;

public class EditProfileResponse {
private final boolean status;
private final String message;
    public EditProfileResponse(boolean status, String message) {
        this.status = status;
        this.message = message;
    }
    public boolean isStatus() { return status; }
    public String getMessage() { return message; }
}
