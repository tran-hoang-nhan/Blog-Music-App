package com.example.blogmusic.ui.components;
public class ReviewAlbum {

    private int id, views, favorites;
    private String album_title, release_date, review_date, artist, rating, image_cover;

    public ReviewAlbum(int id, String album_title, String artist, String rating, String review_date, String release_date, String imageCover,  int views, int favorites) {
        this.id = id;
        this.album_title = album_title;
        this.artist = artist;
        this.rating = rating;
        this.release_date = release_date;
        this.review_date = review_date;
        this.image_cover = imageCover;
        this.views = views;
        this.favorites = favorites;
    }

    public int getId() { return id;}

    public String getAlbumTitle() { return album_title; }

    public String getArtist() { return artist; }

    public String getRating() { return rating; }

    public String getReviewDate() { return review_date; }
    public String getReleaseDate() { return release_date;}

    public String getImageCover() { return image_cover; }
    public int getViews() { return views; }
    public int getFavorites() { return favorites; }
}
