package com.example.blogmusic.ui.components;

import java.util.List;

public class Post {
    private int id;
    private String title, author, date, imageCover;
    private List<Media> mediaList;

    public Post(int id, String title, String author, String date, String imageCover) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.date = date;
        this.imageCover = imageCover;

    }
    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getDate() { return date; }
    public String getImageCover() { return imageCover; }

}
