package com.example.blogmusic.ui.review;

import android.content.Context;
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
import com.example.blogmusic.databinding.FragmentReviewBinding;
import com.example.blogmusic.ui.components.ReviewAlbum;
import com.example.blogmusic.ui.components.ReviewAlbumAdapter;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class ReviewFragment extends Fragment {

    private FragmentReviewBinding binding;
    private ReviewAlbumAdapter reviewAlbumAdapter;
    private ReviewViewModel viewModel;

    private List<ReviewAlbum> allReviews = new ArrayList<>();
    private static final int REVIEWS_PER_PAGE = 8;
    private int currentPage = 1;
    private int totalPages = 1;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentReviewBinding.inflate(inflater, container, false);
        NavController navController = NavHostFragment.findNavController(this);
        // Khởi tạo adapter
        reviewAlbumAdapter = new ReviewAlbumAdapter(review -> {
            Bundle bundle = new Bundle();
            bundle.putInt("review_id", review.getId());
            navController.navigate(R.id.reviewDetailFragment, bundle);
        }, ReviewAlbumAdapter.ReviewLayoutType.LIST);
        binding.reviewRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.reviewRecyclerView.setAdapter(reviewAlbumAdapter);
        binding.reviewRecyclerView.setLayoutAnimation(android.view.animation.AnimationUtils.loadLayoutAnimation(requireContext(), R.anim.layout_animation_slide_in));


        int userId = requireContext()
                .getSharedPreferences("auth", Context.MODE_PRIVATE)
                .getInt("userId", -1);
        ReviewViewModelFactory factory = new ReviewViewModelFactory(userId);
        viewModel = new ViewModelProvider(this, factory).get(ReviewViewModel.class);


        // TabLayout: All - Recent
        binding.reviewTabLayout.addTab(binding.reviewTabLayout.newTab().setText("Mới nhất"));
        binding.reviewTabLayout.addTab(binding.reviewTabLayout.newTab().setText("Phổ biến"));
        binding.reviewTabLayout.addTab(binding.reviewTabLayout.newTab().setText("Yêu thích"));
        binding.reviewTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getText().toString()) {
                    case "Recent":
                        viewModel.fetchReviewsBySort("recent");
                        break;
                    case "Popular":
                        viewModel.fetchReviewsBySort("views");
                        break;
                    case "Favorited":
                        viewModel.fetchReviewsBySort("favorites");
                        break;
                }
            }
            @Override public void onTabUnselected(TabLayout.Tab tab) {}
            @Override public void onTabReselected(TabLayout.Tab tab) {}
        });

        // Lắng nghe dữ liệu cập nhật từ ViewModel
        viewModel.getReviews().observe(getViewLifecycleOwner(), reviews -> {
            allReviews = reviews;
            totalPages = (int) Math.ceil((double) allReviews.size() / REVIEWS_PER_PAGE);
            currentPage = 1;
            updateReviews();
        });

        // Nút phân trang
        binding.prevPage.setOnClickListener(v -> {
            if (currentPage > 1) {
                currentPage--;
                updateReviews();
            }
        });

        binding.nextPage.setOnClickListener(v -> {
            if (currentPage < totalPages) {
                currentPage++;
                updateReviews();
            }
        });

        // Mặc định gọi dữ liệu All
        viewModel.fetchReviewsBySort("recent");

        return binding.getRoot();
    }

    private void updateReviews() {
        int startIndex = (currentPage - 1) * REVIEWS_PER_PAGE;
        int endIndex = Math.min(startIndex + REVIEWS_PER_PAGE, allReviews.size());
        List<ReviewAlbum> paged = allReviews.subList(startIndex, endIndex);
        reviewAlbumAdapter.submitList(paged);
        reviewAlbumAdapter.submitList(paged);
        binding.reviewRecyclerView.scheduleLayoutAnimation(); // chạy animation
        binding.pageIndicator.setText(String.valueOf(currentPage));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
