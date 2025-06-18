package com.example.blogmusic.ui.components;
public class ReviewAlbum {

    private int id;
    private String album_title, review_date, artist, rating, image_cover;

    public ReviewAlbum(int id, String albumTitle, String artist, String rating, String reviewDate, String imageCover) {
        this.id = id;
        this.album_title = albumTitle;
        this.artist = artist;
        this.rating = rating;
        this.review_date = reviewDate;
        this.image_cover = imageCover;
    }

    public int getId() { return id;}

    public String getAlbumTitle() { return album_title; }

    public String getArtist() { return artist; }

    public String getRating() { return rating; }

    public String getReviewDate() { return review_date; }

    public String getImageCover() { return image_cover; }
}
