package com.example.blogmusic.ui.components;

public class Music {
    private int imageResourceId;
    private String title;
    private String artist;

    public Music(int imageResourceId, String title, String artist) {
        this.imageResourceId = imageResourceId;
        this.title = title;
        this.artist = artist;
    }

    public int getImageResourceId() {
        return imageResourceId;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }
    } 