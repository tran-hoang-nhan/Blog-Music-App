package com.example.blogmusic.ui.dashboard;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.blogmusic.R;
import com.example.blogmusic.ui.components.Post;
import com.example.blogmusic.ui.components.Music;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DashboardViewModel extends ViewModel {

    private final MutableLiveData<List<Post>> posts;
    private final MutableLiveData<List<Music>> albums;
    private final MutableLiveData<List<Music>> singles;
    private final MutableLiveData<List<Music>> currentMusicList;
    private final MutableLiveData<List<Post>> searchResults;

    public DashboardViewModel() {
        posts = new MutableLiveData<>();
        albums = new MutableLiveData<>();
        singles = new MutableLiveData<>();
        currentMusicList = new MutableLiveData<>();
        searchResults = new MutableLiveData<>();
        loadPosts();
        loadMusic();
    }

    public LiveData<List<Post>> getPosts() {
        return posts;
    }

    public LiveData<List<Music>> getMusicList() {
        return currentMusicList;
    }

    public LiveData<List<Music>> getReviewAlbums() {
        return albums;
    }

    public void loadAlbums() {
        currentMusicList.setValue(albums.getValue());
    }

    public void loadSingles() {
        currentMusicList.setValue(singles.getValue());
    }

    private void loadPosts() {
        // TODO: Implement loading posts from a data source
        // For now, we'll just set some dummy data
        List<Post> dummyPosts = new ArrayList<>(Arrays.asList(
            new Post(R.drawable.ic_dashboard_black_24dp, "Post 1", "Author 1", "2023-10-01"),
            new Post(R.drawable.ic_dashboard_black_24dp, "Post 2", "Author 2", "2023-10-02"),
            new Post(R.drawable.ic_dashboard_black_24dp, "Post 3", "Author 3", "2023-10-03")
        ));
        posts.setValue(dummyPosts);
    }

    private void loadMusic() {
        // TODO: Implement loading music from a data source
        // For now, we'll just set some dummy data
        List<Music> dummyAlbums = new ArrayList<>(Arrays.asList(
            new Music(R.drawable.ic_dashboard_black_24dp, "Album 1", "Artist 1"),
            new Music(R.drawable.ic_dashboard_black_24dp, "Album 2", "Artist 2")
        ));
        albums.setValue(dummyAlbums);
        currentMusicList.setValue(dummyAlbums); // Set initial value

        List<Music> dummySingles = new ArrayList<>(Arrays.asList(
            new Music(R.drawable.ic_dashboard_black_24dp, "Single 1", "Artist 3"),
            new Music(R.drawable.ic_dashboard_black_24dp, "Single 2", "Artist 4")
        ));
        singles.setValue(dummySingles);
    }

    public void searchPosts(String query) {
        if (query == null || query.isEmpty()) {
            searchResults.setValue(posts.getValue());
            return;
        }
        // TODO: Implement search logic
        // For now, we'll just filter the dummy data
        List<Post> filteredPosts = posts.getValue().stream()
                .filter(post -> post.getTitle().toLowerCase().contains(query.toLowerCase()) || 
                              post.getAuthor().toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList());
        searchResults.setValue(filteredPosts);
    }
}