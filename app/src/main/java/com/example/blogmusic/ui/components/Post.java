package com.example.blogmusic.ui.components;

public class Post {
    private int id;
    private String title;
    private String author;
    private String date;
    private String imageUrl; // đường dẫn ảnh từ server

    public Post(int id, String title, String author, String date, String imageUrl) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.date = date;
        this.imageUrl = imageUrl;
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getDate() { return date; }
    public String getImageUrl() { return imageUrl; }
}
