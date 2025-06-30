package com.example.blogmusic.ui.components;

import java.util.List;

public class PostResponse {
    private final List<Post> posts;

    public PostResponse(List<Post> posts) {
        this.posts = posts;
    }

    public List<Post> getPosts() {
        return posts;
    }
}
