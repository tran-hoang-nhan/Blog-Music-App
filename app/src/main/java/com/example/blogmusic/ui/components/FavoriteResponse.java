package com.example.blogmusic.ui.components;

public class FavoriteResponse {
    private final boolean status;
    private final String message;
    private final int favorites;

    public FavoriteResponse(boolean status, String message, int favorites) {
        this.status = status;
        this.message = message;
        this.favorites = favorites;
    }

    public boolean isStatus() {
        return status;
    }
    public String getMessage() {
        return message;
    }
    public int getFavorites() {
        return favorites;
    }

}
