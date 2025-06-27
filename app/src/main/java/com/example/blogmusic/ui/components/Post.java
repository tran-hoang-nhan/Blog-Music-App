package com.example.blogmusic.ui.components;

public class Post {
    private int id, views, favorites;
    private String title, author, date, image_cover;

    public Post(int id, String title, String author, String date, String image_cover, int views, int favorites) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.date = date;
        this.image_cover = image_cover;
        this.views = views;
        this.favorites = favorites;
    }
    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getDate() { return date; }
    public String getImageCover() { return image_cover; }
    public int getViews() { return views; }
    public int getFavorites() { return favorites; }
}

