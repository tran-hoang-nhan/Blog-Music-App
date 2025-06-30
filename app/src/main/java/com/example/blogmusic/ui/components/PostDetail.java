package com.example.blogmusic.ui.components;

import java.util.List;
import java.util.Map;

public class PostDetail {
    private final int post_id;
    private final String title;
    private final String author;
    private final String date;

    private final String subtitle;
    private final String introduction;
    private final String main_content;
    private final String conclusion;
    private final String tags;
    private final String image_cover;
    private final Map<String, List<Media>> media;

    public PostDetail(int post_id, String title, String author, String date, String subtitle, String introduction, String mainContent, String conclusion, String tags, String image_cover, Map<String, List<Media>> media) {
        this.post_id = post_id;
        this.title = title;
        this.author = author;
        this.date = date;
        this.subtitle = subtitle;
        this.introduction = introduction;
        this.main_content = mainContent;
        this.conclusion = conclusion;
        this.tags = tags;
        this.image_cover = image_cover;
        this.media = media;
    }

    public int getPostId() { return post_id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getDate() { return date; }
    public String getSubtitle() { return subtitle; }
    public String getIntroduction() { return introduction; }
    public String getMainContent() { return main_content; }
    public String getConclusion() { return conclusion; }
    public String getTags() { return tags; }
    public String getImageCover() {return image_cover;}
    public Map<String, List<Media>> getMedia() { return media; }
}
