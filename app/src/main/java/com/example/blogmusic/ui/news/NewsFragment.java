package com.example.blogmusic.ui.news;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.blogmusic.R;
import com.example.blogmusic.ui.components.Post;
import com.example.blogmusic.ui.components.PostAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.blogmusic.databinding.FragmentNewsBinding;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class NewsFragment extends Fragment {

    private FragmentNewsBinding binding;
    private PostAdapter postAdapter;
    private NewsViewModel viewModel;

    private List<Post> allPosts = new ArrayList<>();
    private static final int POSTS_PER_PAGE = 8;
    private int currentPage = 1;
    private int totalPages = 1;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentNewsBinding.inflate(inflater, container, false);

        NavController navController = NavHostFragment.findNavController(this);
        postAdapter = new PostAdapter(post -> {
            Bundle bundle = new Bundle();
            bundle.putInt("post_id", post.getId());
            navController.navigate(R.id.postDetailFragment, bundle);
        }, PostAdapter.PostLayoutType.LIST);
        binding.newsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.newsRecyclerView.setAdapter(postAdapter);
        binding.newsRecyclerView.setLayoutAnimation(android.view.animation.AnimationUtils.loadLayoutAnimation(requireContext(), R.anim.layout_animation_slide_in));


        int userId = requireContext()
                .getSharedPreferences("auth", Context.MODE_PRIVATE)
                .getInt("userId", -1);
        NewsViewModelFactory factory = new NewsViewModelFactory(userId);
        viewModel = new ViewModelProvider(this, factory).get(NewsViewModel.class);


        // TabLayout: All - Recent
        binding.newsTabLayout.addTab(binding.newsTabLayout.newTab().setText("Mới nhất"));
        binding.newsTabLayout.addTab(binding.newsTabLayout.newTab().setText("Phổ biến"));
        binding.newsTabLayout.addTab(binding.newsTabLayout.newTab().setText("Yêu thích"));

        binding.newsTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getText().toString()) {
                    case "Recent":
                        viewModel.fetchPostsBySort("recent");
                        break;
                    case "Popular":
                        viewModel.fetchPostsBySort("views");
                        break;
                    case "Favorited":
                        viewModel.fetchPostsBySort("favorites");
                        break;
                }
            }

            @Override public void onTabUnselected(TabLayout.Tab tab) {}
            @Override public void onTabReselected(TabLayout.Tab tab) {}
        });

        // Quan sát dữ liệu từ ViewModel
        viewModel.getPosts().observe(getViewLifecycleOwner(), posts -> {
            allPosts = posts;
            totalPages = (int) Math.ceil((double) allPosts.size() / POSTS_PER_PAGE);
            currentPage = 1;
            updatePosts();
        });

        binding.prevPage.setOnClickListener(v -> {
            if (currentPage > 1) {
                currentPage--;
                updatePosts();
            }
        });

        binding.nextPage.setOnClickListener(v -> {
            if (currentPage < totalPages) {
                currentPage++;
                updatePosts();
            }
        });

        viewModel.fetchPostsBySort("recent");

        return binding.getRoot();
    }

    private void updatePosts() {
        binding.newsRecyclerView.setAlpha(0f);
        binding.newsRecyclerView.animate().alpha(1f).setDuration(300).start();
        int startIndex = (currentPage - 1) * POSTS_PER_PAGE;
        int endIndex = Math.min(startIndex + POSTS_PER_PAGE, allPosts.size());
        List<Post> pagedPosts = allPosts.subList(startIndex, endIndex);
        postAdapter.submitList(pagedPosts);
        binding.newsRecyclerView.scheduleLayoutAnimation(); // chạy animation
        binding.pageIndicator.setText(String.valueOf(currentPage));

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

