package com.example.blogmusic.ui.review;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.blogmusic.R;
import com.example.blogmusic.api.ApiService;
import com.example.blogmusic.network.RetrofitClient;
import com.example.blogmusic.ui.components.Media;
import com.example.blogmusic.ui.components.ReviewAlbum;
import com.example.blogmusic.ui.components.ReviewAlbumAdapter;
import com.example.blogmusic.ui.components.ReviewAlbumDetail;
import com.example.blogmusic.ui.components.ReviewAlbumResponse;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReviewAlbumDetailFragment extends Fragment {

    private NavController navController;
    private ImageView albumCoverImage;
    private TextView albumTitleText, albumArtistText, albumScoreText, detailSubtitleText;
    private TextView detailSummaryText, detailTracklistText, detailMainContentText, detailConclusionText, relatedReviewsLabel;
    private ChipGroup tagsChipGroup;
    private LinearLayout summaryMediaContainer, tracklistMediaContainer, mainMediaContainer, conclusionMediaContainer, tagsMediaContainer;
    private RecyclerView relatedReviewsRecyclerView;
    private ReviewAlbumAdapter reviewAdapter;

    private int reviewId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_review_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

        // Khởi tạo view
        albumCoverImage = view.findViewById(R.id.album_cover_image);
        albumTitleText = view.findViewById(R.id.album_title_text);
        albumArtistText = view.findViewById(R.id.album_artist_text);
        albumScoreText = view.findViewById(R.id.album_score_text);
        detailSubtitleText = view.findViewById(R.id.detail_subtitle_text);
        detailSummaryText = view.findViewById(R.id.detail_summary_text);
        detailTracklistText = view.findViewById(R.id.detail_tracklist_text);
        detailMainContentText = view.findViewById(R.id.detail_main_content_text);
        detailConclusionText = view.findViewById(R.id.detail_conclusion_text);
        relatedReviewsLabel = view.findViewById(R.id.relatedReviewsLabel);
        tagsChipGroup = view.findViewById(R.id.tags_chip_group);

        summaryMediaContainer = view.findViewById(R.id.summaryMediaContainer);
        tracklistMediaContainer = view.findViewById(R.id.tracklistMediaContainer);
        mainMediaContainer = view.findViewById(R.id.mainMediaContainer);
        conclusionMediaContainer = view.findViewById(R.id.conclusionMediaContainer);
        tagsMediaContainer = view.findViewById(R.id.tagsMediaContainer);

        relatedReviewsRecyclerView = view.findViewById(R.id.relatedReviewRecyclerView);
        relatedReviewsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        reviewAdapter = new ReviewAlbumAdapter(review -> {
            Bundle args = new Bundle();
            args.putInt("review_id", review.getId());
            navController.navigate(R.id.reviewDetailFragment, args);
        }, ReviewAlbumAdapter.ReviewLayoutType.GRID);
        relatedReviewsRecyclerView.setAdapter(reviewAdapter);

        // Nhận review_id
        Bundle args = getArguments();
        if (args != null && args.containsKey("review_id")) {
            reviewId = args.getInt("review_id");
            increaseViews("review", reviewId);
            fetchReviewDetail(reviewId);
        }
    }

    private void fetchReviewDetail(int id) {
        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        apiService.getReviewDetail(id).enqueue(new Callback<>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<ReviewAlbumDetail> call, @NonNull Response<ReviewAlbumDetail> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ReviewAlbumDetail detail = response.body();

                    Glide.with(requireContext())
                            .load(detail.getImageCover())
                            .placeholder(R.drawable.placeholder)
                            .into(albumCoverImage);

                    albumTitleText.setText(detail.getAlbumTitle());
                    albumArtistText.setText(detail.getArtist());
                    albumScoreText.setText(String.valueOf(detail.getScore()));
                    detailSubtitleText.setText(detail.getSubtitle());
                    detailSummaryText.setText(detail.getSummary());
                    detailMainContentText.setText(detail.getMain_content());
                    detailConclusionText.setText(detail.getConclusion());

                    // Format tracklist
                    String[] tracks = detail.getTracklist().split(",");
                    StringBuilder formatted = new StringBuilder();
                    for (int i = 0; i < tracks.length; i++) {
                        formatted.append(i + 1).append(". ").append(tracks[i].trim()).append("\n");
                    }
                    detailTracklistText.setText(formatted.toString().trim());

                    // Tags
                    String[] tags = detail.getTags().split(",");
                    tagsChipGroup.removeAllViews();
                    for (String tag : tags) {
                        Chip chip = new Chip(requireContext());
                        chip.setText(tag.trim());
                        tagsChipGroup.addView(chip);
                    }

                    fetchRelatedReviews(detail.getArtist(), reviewId);

                    Map<String, List<Media>> mediaMap = detail.getMedia();
                    if (mediaMap != null) {
                        addMediaToSection(mediaMap.get("summary"), summaryMediaContainer);
                        addMediaToSection(mediaMap.get("tracklist"), tracklistMediaContainer);
                        addMediaToSection(mediaMap.get("main_content"), mainMediaContainer);
                        addMediaToSection(mediaMap.get("conclusion"), conclusionMediaContainer);
                        addMediaToSection(mediaMap.get("tags"), tagsMediaContainer);
                    }
                } else {
                    Toast.makeText(getContext(), "Không tìm thấy review", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ReviewAlbumDetail> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "Lỗi khi tải dữ liệu", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchRelatedReviews(String artist, int currentId) {
        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        apiService.getRelatedReviews(artist, reviewId).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ReviewAlbumResponse> call, @NonNull Response<ReviewAlbumResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<ReviewAlbum> reviews = response.body().getReviews();
                    if (!reviews.isEmpty()) {
                        relatedReviewsLabel.setVisibility(View.VISIBLE);
                        reviewAdapter.submitList(reviews);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ReviewAlbumResponse> call, @NonNull Throwable t) {
                Log.e("RelatedReviews", "Error: " + t.getMessage());
            }
        });
    }

    private void addMediaToSection(List<Media> mediaList, LinearLayout sectionLayout) {
        if (mediaList == null || mediaList.isEmpty()) return;
        LayoutInflater inflater = LayoutInflater.from(requireContext());

        for (Media item : mediaList) {
            String type = item.getType();
            String url = item.getFileUrl();

            if ("image".equalsIgnoreCase(type)) {
                View imageItem = inflater.inflate(R.layout.item_images, sectionLayout, false);
                ImageView imageView = imageItem.findViewById(R.id.imageView);
                Glide.with(requireContext()).load(Uri.parse(url)).into(imageView);
                sectionLayout.addView(imageItem);
            } else if ("video".equalsIgnoreCase(type) && isYouTubeUrl(url)) {
                View youtubeItem = inflater.inflate(R.layout.item_youtube_player, sectionLayout, false);
                YouTubePlayerView playerView = youtubeItem.findViewById(R.id.youtubePlayerView);
                getLifecycle().addObserver(playerView);
                playerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                    @Override
                    public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                        youTubePlayer.loadVideo(extractYoutubeVideoId(url), 0);
                    }
                });
                sectionLayout.addView(youtubeItem);
            }
        }
    }

    private boolean isYouTubeUrl(String url) {
        return url.contains("youtube.com") || url.contains("youtu.be");
    }

    private String extractYoutubeVideoId(String url) {
        if (url.contains("v=")) return Uri.parse(url).getQueryParameter("v");
        if (url.contains("youtu.be/")) return Uri.parse(url).getLastPathSegment();
        return "";
    }

    public void increaseViews(String type, int id) {
        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        apiService.increaseViews(type, id).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                Log.d("Views", "View increased for " + type + " ID: " + id);
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Log.e("Views", "Failed to increase views: " + t.getMessage());
            }
        });
    }
}
