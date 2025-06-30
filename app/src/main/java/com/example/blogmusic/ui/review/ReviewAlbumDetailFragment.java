package com.example.blogmusic.ui.review;

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
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;


import com.bumptech.glide.Glide;
import com.example.blogmusic.R;
import com.example.blogmusic.api.ApiService;
import com.example.blogmusic.network.RetrofitClient;
import com.example.blogmusic.ui.components.Media;
import com.example.blogmusic.ui.components.ReviewAlbumDetail;
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

    private ImageView albumCoverImage;
    private TextView albumTitleText, albumArtistText, albumScoreText, detailSubtitleText;
    private TextView detailSummaryText, detailTracklistText, detailMainContentText, detailConclusionText;
    private ChipGroup tagsChipGroup;

    private LinearLayout summaryMediaContainer,
            tracklistMediaContainer, mainMediaContainer, conclusionMediaContainer, tagsMediaContainer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_review_detail, container, false);

        // Setup Toolbar
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);
        if (((AppCompatActivity) requireActivity()).getSupportActionBar() != null) {
            ((AppCompatActivity) requireActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            ((AppCompatActivity) requireActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        toolbar.setNavigationOnClickListener(v -> requireActivity().onBackPressed());

        // Map views
        albumCoverImage = view.findViewById(R.id.album_cover_image);
        albumTitleText = view.findViewById(R.id.album_title_text);
        albumArtistText = view.findViewById(R.id.album_artist_text);
        albumScoreText = view.findViewById(R.id.album_score_text);
        detailSubtitleText = view.findViewById(R.id.detail_subtitle_text);
        detailSummaryText = view.findViewById(R.id.detail_summary_text);
        detailTracklistText = view.findViewById(R.id.detail_tracklist_text);
        detailMainContentText = view.findViewById(R.id.detail_main_content_text);
        detailConclusionText = view.findViewById(R.id.detail_conclusion_text);
        tagsChipGroup = view.findViewById(R.id.tags_chip_group);

        // Media containers
        summaryMediaContainer = view.findViewById(R.id.summaryMediaContainer);
        tracklistMediaContainer = view.findViewById(R.id.tracklistMediaContainer);
        mainMediaContainer = view.findViewById(R.id.mainMediaContainer);
        conclusionMediaContainer = view.findViewById(R.id.conclusionMediaContainer);
        tagsMediaContainer = view.findViewById(R.id.tagsMediaContainer);

        // Fetch data
        Bundle args = getArguments();
        if (args != null && args.containsKey("review_id")) {
            int reviewId = args.getInt("review_id");
            increaseViews("review", reviewId);
            fetchReviewDetail(reviewId);
        }

        return view;
    }

    private void fetchReviewDetail(int reviewId) {
        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        apiService.getReviewDetail(reviewId).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ReviewAlbumDetail> call,
                                   @NonNull Response<ReviewAlbumDetail> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ReviewAlbumDetail detail = response.body();
                    populateUI(detail);
                } else {
                    Toast.makeText(getContext(), "Failed to load review details", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ReviewAlbumDetail> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "Network Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void populateUI(ReviewAlbumDetail detail) {
        // Header
        albumTitleText.setText(detail.getAlbumTitle());
        albumArtistText.setText(detail.getArtist());
        Glide.with(requireContext())
                .load(detail.getImageCover())
                .placeholder(R.drawable.placeholder)
                .into(albumCoverImage);

        // Body
        albumScoreText.setText(String.valueOf(detail.getScore()));
        detailSubtitleText.setText(detail.getSubtitle());
        detailSummaryText.setText(detail.getSummary());
        detailMainContentText.setText(detail.getMain_content());
        detailConclusionText.setText(detail.getConclusion());

        // Format and set Tracklist
        String[] tracks = detail.getTracklist().split(",");
        StringBuilder formattedTracklist = new StringBuilder();
        for (int i = 0; i < tracks.length; i++) {
            formattedTracklist.append(i + 1).append(". ").append(tracks[i].trim()).append("\n");
        }
        detailTracklistText.setText(formattedTracklist.toString().trim());

        // Add chips for tags
        String[] tags = detail.getTags().split(",");
        tagsChipGroup.removeAllViews();
        for (String tag : tags) {
            Chip chip = new Chip(requireContext());
            chip.setText(tag.trim());
            tagsChipGroup.addView(chip);
        }

        // Handle Media
        Map<String, List<Media>> mediaMap = detail.getMedia();
        if (mediaMap != null) {
            addMediaToSection(mediaMap.get("summary"), summaryMediaContainer);
            addMediaToSection(mediaMap.get("tracklist"), tracklistMediaContainer);
            addMediaToSection(mediaMap.get("main_content"), mainMediaContainer);
            addMediaToSection(mediaMap.get("conclusion"), conclusionMediaContainer);
            addMediaToSection(mediaMap.get("tags"), tagsMediaContainer);
        }
    }

    private void addMediaToSection(List<Media> mediaList, LinearLayout sectionLayout) {
        if (mediaList == null || mediaList.isEmpty()) return;

        LayoutInflater inflater = LayoutInflater.from(requireContext());

        for (Media item : mediaList) {
            String type = item.getType();
            String fileUrl = item.getFileUrl();

            if ("image".equalsIgnoreCase(type)) {
                View imageItem = inflater.inflate(R.layout.item_images, sectionLayout, false);
                ImageView imageView = imageItem.findViewById(R.id.imageView);
                Glide.with(requireContext()).load(Uri.parse(fileUrl)).into(imageView);
                sectionLayout.addView(imageItem);

            } else if ("video".equalsIgnoreCase(type) && isYouTubeUrl(fileUrl)) {
                View youtubeItem = inflater.inflate(R.layout.item_youtube_player, sectionLayout, false);
                YouTubePlayerView youtubePlayerView = youtubeItem.findViewById(R.id.youtubePlayerView);

                getLifecycle().addObserver(youtubePlayerView);
                youtubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                    @Override
                    public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                        youTubePlayer.loadVideo(extractYoutubeVideoId(fileUrl), 0);
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
        if (url.contains("v=")) {
            Uri uri = Uri.parse(url);
            return uri.getQueryParameter("v");
        } else if (url.contains("youtu.be/")) {
            return Uri.parse(url).getLastPathSegment();
        }
        return "";
    }

    public void increaseViews(String type, int id) {
        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        apiService.increaseViews(type, id).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d("Views", "Increased view for " + type + " id " + id);
                } else {
                    Log.e("Views", "Failed to increase view: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Log.e("Views", "Network error: " + t.getMessage());
            }
        });
    }
}

