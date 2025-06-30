package com.example.blogmusic.ui.components;
public class ReviewAlbum {

    private final int id;
    private final int views;
    private int favorites;
    private final String album_title, release_date, review_date, artist, rating, image_cover;
    private boolean favorited;

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
        this.favorited = false;
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
    public boolean isFavorited() { return favorited; }

    public void setFavorited(boolean favorited) { this.favorited = favorited; }

    public void setFavorites(int favorites) { this.favorites = favorites; }
}
