package com.example.blogmusic.ui.components;

import java.util.List;
import java.util.Map;

public class ReviewAlbumDetail {
    private int reviewId;
    private String subtitle;
    private String summary;
    private String tracklist;
    private String main_content;
    private int score;
    private String conclusion;
    private String tags;
    private Map<String, List<Media>> media;

    public ReviewAlbumDetail(int reviewId, String subtitle, String summary, String tracklist, String main_content, int score, String conclusion, String tags, Map<String, List<Media>> media) {
        this.reviewId = reviewId;
        this.subtitle = subtitle;
        this.summary = summary;
        this.tracklist = tracklist;
        this.main_content = main_content;
        this.score = score;
        this.conclusion = conclusion;
        this.tags = tags;
        this.media = media;
    }

    public int getReviewId() { return reviewId; }
    public String getSubtitle() { return subtitle; }
    public String getSummary() { return summary; }
    public String getTracklist() { return tracklist; }
    public String getMain_content() { return main_content; }
    public int getScore() { return score; }
    public String getConclusion() { return conclusion; }
    public String getTags() { return tags; }
    public Map<String, List<Media>> getMedia() { return media; }
}
