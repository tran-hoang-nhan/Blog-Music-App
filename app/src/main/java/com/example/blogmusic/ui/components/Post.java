package com.example.blogmusic.ui.components;

public class Post {
    private final int id;
    private final int views;
    private int favorites;
    private final String title;
    private final String author;
    private final String date;
    private final String image_cover;
    private boolean favorited;

    public Post(int id, String title, String author, String date, String image_cover, int views, int favorites) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.date = date;
        this.image_cover = image_cover;
        this.views = views;
        this.favorites = favorites;
        this.favorited = false;
    }
    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getDate() { return date; }
    public String getImageCover() { return image_cover; }
    public int getViews() { return views; }
    public int getFavorites() { return favorites; }
    public boolean isFavorited() { return favorited; }

    public void setFavorited(boolean favorited) { this.favorited = favorited; }

    public void setFavorites(int favorites) { this.favorites = favorites; }
}

