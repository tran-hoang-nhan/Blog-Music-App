package com.example.blogmusic.ui.components;

public class Media {
    private final String file_url;
    private final String type;

    public Media(String fileUrl, String type) {
        file_url = fileUrl;
        this.type = type;
    }

    public String getFileUrl() { return file_url; }
    public String getType() { return type; }
}
