package com.example.blogmusic.ui.news;

import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.List;

public class NewsFragment extends Fragment {

    private FragmentNewsBinding binding;
    private PostAdapter postAdapter;
    private List<Post> allPosts = new ArrayList<>();
    private static final int POSTS_PER_PAGE = 8;
    private int currentPage = 1;
    private int totalPages = 1;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentNewsBinding.inflate(inflater, container, false);

        NavController navController = NavHostFragment.findNavController(this);
        postAdapter = new PostAdapter(post -> {
            Bundle bundle = new Bundle();
            bundle.putInt("post_id", post.getId());  // Đảm bảo có getId()
            navController.navigate(R.id.postDetailFragment, bundle);
        });
        binding.newsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.newsRecyclerView.setAdapter(postAdapter);

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

        // Giả sử bạn đã gọi API và lấy được danh sách allPosts
        NewsViewModel viewModel = new ViewModelProvider(this).get(NewsViewModel.class);
        viewModel.getPosts().observe(getViewLifecycleOwner(), posts -> {
            allPosts = posts;
            totalPages = (int) Math.ceil((double) allPosts.size() / POSTS_PER_PAGE);
            currentPage = 1;
            updatePosts();
        });

        return binding.getRoot();
    }

    private void updatePosts() {
        int startIndex = (currentPage - 1) * POSTS_PER_PAGE;
        int endIndex = Math.min(startIndex + POSTS_PER_PAGE, allPosts.size());
        List<Post> pagedPosts = allPosts.subList(startIndex, endIndex);
        postAdapter.submitList(pagedPosts);
        binding.pageIndicator.setText(String.valueOf(currentPage));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
