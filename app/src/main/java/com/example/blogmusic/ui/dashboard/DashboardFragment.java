package com.example.blogmusic.ui.dashboard;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blogmusic.R;
import com.example.blogmusic.databinding.FragmentDashboardBinding;
import com.example.blogmusic.ui.components.PostAdapter;
import com.example.blogmusic.ui.components.ReviewAlbumAdapter;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;
    private DashboardViewModel dashboardViewModel;
    private PostAdapter postAdapter;
    private ReviewAlbumAdapter reviewAlbumAdapter;
    private final Handler autoScrollHandler = new Handler();
    private int currentPosition = 0;
    private LinearLayout newsIndicatorLayout;

    private final Runnable autoScrollRunnable = new Runnable() {
        @Override
        public void run() {
            int itemCount = postAdapter.getItemCount();
            if (itemCount == 0) return;

            currentPosition = (currentPosition + 1) % itemCount;
            binding.newsRecyclerView.smoothScrollToPosition(currentPosition);

            autoScrollHandler.postDelayed(this, 3000); // Trượt mỗi 3 giây
        }
    };
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        dashboardViewModel = new ViewModelProvider(this).get(DashboardViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        newsIndicatorLayout = binding.newsIndicator;
        setupRecyclerViews();
        observeViewModel();
        setupClickListeners();
    }

    private void setupRecyclerViews() {
        postAdapter = new PostAdapter(post -> {
            // TODO: Navigate to post detail
        });
        binding.newsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL, false));
        binding.newsRecyclerView.setAdapter(postAdapter);

        PagerSnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(binding.newsRecyclerView);

        reviewAlbumAdapter = new ReviewAlbumAdapter(review -> {
            // TODO: Navigate to review detail
        });
        binding.reviewAlbumRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        binding.reviewAlbumRecyclerView.setAdapter(reviewAlbumAdapter);
    }

    private void observeViewModel() {
        dashboardViewModel.getPosts().observe(getViewLifecycleOwner(), posts -> {
            postAdapter.submitList(posts);
            setupNewsIndicators(posts.size());
            currentPosition = 0;
            autoScrollHandler.removeCallbacks(autoScrollRunnable);
            autoScrollHandler.postDelayed(autoScrollRunnable, 3000);
        });
        dashboardViewModel.getReviews().observe(getViewLifecycleOwner(), reviewAlbums -> {
            reviewAlbumAdapter.submitList(reviewAlbums);
        });
    }
    private void setupNewsIndicators(int count) {
        newsIndicatorLayout.removeAllViews();
        for (int i = 0; i < count; i++) {
            ImageView dot = new ImageView(getContext());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(8, 0, 8, 0);
            dot.setLayoutParams(params);
            dot.setImageResource(i == 0 ? R.drawable.dot_active : R.drawable.dot_inactive);
            newsIndicatorLayout.addView(dot);
        }

        // Lắng nghe sự kiện scroll để cập nhật chấm
        binding.newsRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (layoutManager != null) {
                    int visiblePosition = layoutManager.findFirstVisibleItemPosition();
                    updateNewsIndicator(visiblePosition);
                }
            }
        });
    }
    private void updateNewsIndicator(int index) {
        int childCount = newsIndicatorLayout.getChildCount();
        for (int i = 0; i < childCount; i++) {
            ImageView dot = (ImageView) newsIndicatorLayout.getChildAt(i);
            dot.setImageResource(i == index ? R.drawable.dot_active : R.drawable.dot_inactive);
        }
    }

    private void setupClickListeners() {
        binding.viewAllNews.setOnClickListener(v -> {
            NavHostFragment.findNavController(this).navigate(R.id.navigation_news);
        });
        binding.viewAllReviews.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.navigation_review);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        autoScrollHandler.removeCallbacks(autoScrollRunnable);
        binding = null;
    }
}
