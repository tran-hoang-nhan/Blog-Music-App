package com.example.blogmusic.ui.components;

import com.example.blogmusic.ui.components.Post;
import com.example.blogmusic.ui.components.ReviewAlbum;

import java.util.List;

public class SearchResponse {
    private List<Post> posts;
    private List<ReviewAlbum> reviews;

    public List<Post> getPosts() {
        return posts;
    }

    public List<ReviewAlbum> getReviews() {
        return reviews;
    }
}
