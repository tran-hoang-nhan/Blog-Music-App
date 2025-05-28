package com.example.blogmusic.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.blogmusic.R;
import com.example.blogmusic.databinding.FragmentDashboardBinding;
import com.example.blogmusic.ui.components.Post;
import com.example.blogmusic.ui.components.PostAdapter;
import com.example.blogmusic.ui.components.ReviewAlbum;
import com.example.blogmusic.ui.components.ReviewAlbumAdapter;
import com.example.blogmusic.ui.components.Music;
import com.example.blogmusic.ui.components.MusicAdapter;
import com.google.android.material.tabs.TabLayout;
import java.util.ArrayList;
import java.util.List;

public class DashboardFragment extends Fragment {
    private FragmentDashboardBinding binding;
    private PostAdapter postAdapter;
    private ReviewAlbumAdapter reviewAlbumAdapter;
    private MusicAdapter musicAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupRecyclerViews();
        setupTabLayout();
        setupClickListeners();
        loadData();
    }

    private void setupRecyclerViews() {
        // Setup News Blog RecyclerView
        postAdapter = new PostAdapter(post -> {
            // TODO: Navigate to post detail
        });
        binding.newsRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        binding.newsRecyclerView.setAdapter(postAdapter);

        // Setup New Music RecyclerView
        musicAdapter = new MusicAdapter(music -> {
            // TODO: Navigate to music detail
        });
        binding.musicRecyclerView.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false)
        );
        binding.musicRecyclerView.setAdapter(musicAdapter);

        // Setup Review Album RecyclerView
        reviewAlbumAdapter = new ReviewAlbumAdapter(review -> {
            // TODO: Navigate to review detail
        });
        binding.reviewAlbumRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        binding.reviewAlbumRecyclerView.setAdapter(reviewAlbumAdapter);
    }

    private void setupTabLayout() {
        binding.musicTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0: // Albums
                        loadAlbums();
                        break;
                    case 1: // Singles
                        loadSingles();
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    private void setupClickListeners() {
        binding.viewAllNews.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.navigation_news);
        });

        binding.viewAllReviews.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.navigation_review);
        });
    }

    private void loadData() {
        // Load News Blog data
        List<Post> posts = new ArrayList<>();
        posts.add(new Post(R.drawable.ic_dashboard_black_24dp, "Post 1", "Author 1", "2024-03-20"));
        posts.add(new Post(R.drawable.ic_dashboard_black_24dp, "Post 2", "Author 2", "2024-03-19"));
        posts.add(new Post(R.drawable.ic_dashboard_black_24dp, "Post 3", "Author 3", "2024-03-18"));
        postAdapter.submitList(posts);

        // Load initial music data (Albums)
        loadAlbums();

        // Load Review Album data
        List<ReviewAlbum> reviews = new ArrayList<>();
        reviews.add(new ReviewAlbum(R.drawable.ic_dashboard_black_24dp, "Album 1", "Artist 1", "4.5/5", "2024-03-20"));
        reviews.add(new ReviewAlbum(R.drawable.ic_dashboard_black_24dp, "Album 2", "Artist 2", "4.0/5", "2024-03-19"));
        reviews.add(new ReviewAlbum(R.drawable.ic_dashboard_black_24dp, "Album 3", "Artist 3", "4.8/5", "2024-03-18"));
        reviews.add(new ReviewAlbum(R.drawable.ic_dashboard_black_24dp, "Album 4", "Artist 4", "3.9/5", "2024-03-17"));
        reviewAlbumAdapter.submitList(reviews);
    }

    private void loadAlbums() {
        List<Music> albums = new ArrayList<>();
        albums.add(new Music(R.drawable.ic_dashboard_black_24dp, "Album 1", "Artist 1"));
        albums.add(new Music(R.drawable.ic_dashboard_black_24dp, "Album 2", "Artist 2"));
        albums.add(new Music(R.drawable.ic_dashboard_black_24dp, "Album 3", "Artist 3"));
        musicAdapter.submitList(albums);
    }

    private void loadSingles() {
        List<Music> singles = new ArrayList<>();
        singles.add(new Music(R.drawable.ic_dashboard_black_24dp, "Single 1", "Artist 1"));
        singles.add(new Music(R.drawable.ic_dashboard_black_24dp, "Single 2", "Artist 2"));
        singles.add(new Music(R.drawable.ic_dashboard_black_24dp, "Single 3", "Artist 3"));
        musicAdapter.submitList(singles);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}