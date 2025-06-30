package com.example.blogmusic.ui.components;

import java.util.List;
import java.util.Map;

public class ReviewAlbumDetail {
    private final int reviewId;
    private final double score;
    private final String album_title, image_cover, summary ,subtitle, tracklist, main_content, artist, release_date, conclusion, tags;
    private final Map<String, List<Media>> media;

    public ReviewAlbumDetail(int reviewId, String album_title, String image_cover, String subtitle, String summary, String tracklist, String release_date, String artist,String main_content, double score, String conclusion, String tags, Map<String, List<Media>> media) {
        this.reviewId = reviewId;
        this.album_title = album_title;
        this.image_cover = image_cover;
        this.subtitle = subtitle;
        this.summary = summary;
        this.tracklist = tracklist;
        this.artist = artist;
        this.release_date = release_date;
        this.main_content = main_content;
        this.score = score;
        this.conclusion = conclusion;
        this.tags = tags;
        this.media = media;
    }

    public int  getReviewId() { return reviewId; }
    public String getAlbumTitle() { return album_title;}
    public String getImageCover() { return image_cover; }
    public String getSubtitle() { return subtitle; }
    public String getSummary() { return summary; }
    public String getTracklist() { return tracklist; }
    public String getArtist() { return artist; }
    public String getReleaseDate() { return release_date; }
    public String getMain_content() { return main_content; }
    public double getScore() { return score; }
    public String getConclusion() { return conclusion; }
    public String getTags() { return tags; }
    public Map<String, List<Media>> getMedia() { return media; }
}
