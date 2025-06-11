package com.example.blogmusic.ui.components;

import com.google.gson.annotations.SerializedName;

public class ReviewAlbum {
    private int id;
    private String title;
    private String artist;
    private String reviewer;
    private String content;
    private String rating;

    @SerializedName("image_url")
    private String imageUrl;

    @SerializedName("created_at")
    private String reviewDate;

    // Getter & Setter đầy đủ
    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getArtist() { return artist; }
    public String getReviewer() { return reviewer; }
    public String getContent() { return content; }
    public String getRating() { return rating; }
    public String getImageUrl() { return imageUrl; }
    public String getReviewDate() { return reviewDate; }

    // Setter nếu cần
}
