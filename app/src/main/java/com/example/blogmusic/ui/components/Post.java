package com.example.blogmusic.ui.components;

public class Post {
    private int imageResourceId;
    private String title;
    private String author;
    private String date;

    public Post(int imageResourceId, String title, String author, String date) {
        this.imageResourceId = imageResourceId;
        this.title = title;
        this.author = author;
        this.date = date;
    }

    public int getImageResourceId() {
        return imageResourceId;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getDate() {
        return date;
    }
} 