package com.example.blogmusic.ui.components;

public class ReviewAlbum {
    private int imageResourceId;
    private String title;
    private String artist;
    private String rating;
    private String reviewDate;

    public ReviewAlbum(int imageResourceId, String title, String artist, String rating, String reviewDate) {
        this.imageResourceId = imageResourceId;
        this.title = title;
        this.artist = artist;
        this.rating = rating;
        this.reviewDate = reviewDate;
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

    public String getRating() {
        return rating;
    }

    public String getReviewDate() {
        return reviewDate;
    }
} 