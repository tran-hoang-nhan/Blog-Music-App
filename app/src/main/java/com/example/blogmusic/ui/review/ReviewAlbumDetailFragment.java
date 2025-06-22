package com.example.blogmusic.ui.review;

import android.net.Uri;
import android.os.Bundle;
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

import com.bumptech.glide.Glide;
import com.example.blogmusic.R;
import com.example.blogmusic.api.ApiService;
import com.example.blogmusic.network.RetrofitClient;
import com.example.blogmusic.ui.components.Media;
import com.example.blogmusic.ui.components.ReviewAlbumDetail;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReviewAlbumDetailFragment extends Fragment {

    private TextView subtitleTextView, summaryTextView, tracklistTextView,
            main_contentTextView, scoreTextView, conclusionTextView, tagsTextView;

    private LinearLayout subtitleMediaContainer, summaryMediaContainer,
            tracklistMediaContainer, mainMediaContainer, conclusionMediaContainer, tagsMediaContainer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_review_detail, container, false);

        // Ánh xạ TextView
        subtitleTextView = view.findViewById(R.id.detailSubtitle);
        summaryTextView = view.findViewById(R.id.detailSummary);
        tracklistTextView = view.findViewById(R.id.detailTracklist);
        main_contentTextView = view.findViewById(R.id.detailMaincontent);
        scoreTextView = view.findViewById(R.id.detailScore);
        conclusionTextView = view.findViewById(R.id.detailConclusion);
        tagsTextView = view.findViewById(R.id.detailTags);

        // Ánh xạ LinearLayout chứa media
        subtitleMediaContainer = view.findViewById(R.id.subtitleMediaContainer);
        summaryMediaContainer = view.findViewById(R.id.summaryMediaContainer);
        tracklistMediaContainer = view.findViewById(R.id.tracklistMediaContainer);
        mainMediaContainer = view.findViewById(R.id.mainMediaContainer);
        conclusionMediaContainer = view.findViewById(R.id.conclusionMediaContainer);
        tagsMediaContainer = view.findViewById(R.id.tagsMediaContainer);

        Bundle args = getArguments();
        if (args != null && args.containsKey("review_id")) {
            int reviewId = args.getInt("review_id");
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

                    subtitleTextView.setText(detail.getSubtitle());
                    summaryTextView.setText(detail.getSummary());

                    String[] tracks = detail.getTracklist().split(",");
                    StringBuilder formatted = new StringBuilder();
                    for (int i = 0; i < tracks.length; i++) {
                        formatted.append((i + 1)).append(". ").append(tracks[i].trim()).append("\n");
                    }
                    tracklistTextView.setText(formatted.toString().trim());

                    main_contentTextView.setText(detail.getMain_content());
                    scoreTextView.setText(String.valueOf(detail.getScore()));
                    conclusionTextView.setText(detail.getConclusion());
                    tagsTextView.setText("Tags: " + detail.getTags());

                    Map<String, List<Media>> mediaMap = detail.getMedia();
                    if (mediaMap != null) {
                        addMediaToSection(mediaMap.get("subtitle"), subtitleMediaContainer);
                        addMediaToSection(mediaMap.get("summary"), summaryMediaContainer);
                        addMediaToSection(mediaMap.get("tracklist"), tracklistMediaContainer);
                        addMediaToSection(mediaMap.get("main_content"), mainMediaContainer);
                        addMediaToSection(mediaMap.get("conclusion"), conclusionMediaContainer);
                        addMediaToSection(mediaMap.get("tags"), tagsMediaContainer);
                    }

                } else {
                    Toast.makeText(getContext(), "Không tìm thấy chi tiết review", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ReviewAlbumDetail> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "Lỗi kết nối server", Toast.LENGTH_SHORT).show();
            }
        });
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
}
