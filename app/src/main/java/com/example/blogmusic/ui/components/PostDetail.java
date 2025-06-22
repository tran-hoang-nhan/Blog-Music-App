package com.example.blogmusic.ui.components;

import java.util.List;
import java.util.Map;

public class PostDetail {
    private int post_id;
    private String title;
    private String author;
    private String date;

    private String subtitle;
    private String introduction;
    private String main_content;
    private String conclusion;
    private String tags;
    private Map<String, List<Media>> media;

    public PostDetail(int post_id, String title, String author, String date, String subtitle, String introduction, String mainContent, String conclusion, String tags, Map<String, List<Media>> media) {
        this.post_id = post_id;
        this.title = title;
        this.author = author;
        this.date = date;
        this.subtitle = subtitle;
        this.introduction = introduction;
        this.main_content = mainContent;
        this.conclusion = conclusion;
        this.tags = tags;
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
    public Map<String, List<Media>> getMedia() { return media; }
}
