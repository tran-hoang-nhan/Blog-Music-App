package com.example.blogmusic.ui.search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.blogmusic.R;
import com.example.blogmusic.databinding.FragmentSearchBinding;
import com.example.blogmusic.ui.components.PostAdapter;
import com.example.blogmusic.ui.components.ReviewAlbumAdapter;

public class SearchFragment extends Fragment {

    private FragmentSearchBinding binding;
    private SearchViewModel viewModel;
    private PostAdapter postAdapter;
    private ReviewAlbumAdapter reviewAlbumAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSearchBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(SearchViewModel.class);

        setupRecyclerViews();
        observeData();

        String query = getArguments() != null ? getArguments().getString("query") : null;
        if (query != null && !query.trim().isEmpty()) {
            viewModel.setContext(requireContext()); // nếu cần context cho Volley
            viewModel.search(query);
        }
    }

    private void setupRecyclerViews() {
        NavController navController = NavHostFragment.findNavController(this);
        postAdapter = new PostAdapter(post -> {
            Bundle bundle = new Bundle();
            bundle.putInt("post_id", post.getId());  // Đảm bảo có getId()
            navController.navigate(R.id.postDetailFragment, bundle);
        }, PostAdapter.PostLayoutType.LIST);

        reviewAlbumAdapter = new ReviewAlbumAdapter(review -> {
            Bundle bundle = new Bundle();
            bundle.putInt("review_id", review.getId());
            navController.navigate(R.id.reviewDetailFragment, bundle);
        }, ReviewAlbumAdapter.ReviewLayoutType.LIST);

        binding.postRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.postRecyclerView.setAdapter(postAdapter);

        binding.reviewRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.reviewRecyclerView.setAdapter(reviewAlbumAdapter);
    }

    private void observeData() {
        viewModel.posts.observe(getViewLifecycleOwner(), posts -> {
            postAdapter.submitList(posts);
        });

        viewModel.reviews.observe(getViewLifecycleOwner(), reviews -> {
            reviewAlbumAdapter.submitList(reviews);
        });

        viewModel.postCount.observe(getViewLifecycleOwner(), count -> {
            binding.postCountText.setText("Bài viết (" + count + ")");
        });

        viewModel.reviewCount.observe(getViewLifecycleOwner(), count -> {
            binding.reviewCountText.setText("Đánh giá Album (" + count + ")");
        });
    }
}
