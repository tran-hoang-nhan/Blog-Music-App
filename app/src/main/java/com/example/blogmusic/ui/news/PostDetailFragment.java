package com.example.blogmusic.ui.news;

import android.util.Log;
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
import com.example.blogmusic.ui.components.PostDetail;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostDetailFragment extends Fragment {

    private TextView titleTextView, authorTextView, dateTextView;
    private TextView subtitleTextView, introTextView, mainContentTextView, conclusionTextView, tagsTextView;
    private LinearLayout subtitleMediaContainer, introMediaContainer, mainMediaContainer, conclusionMediaContainer, tagsMediaContainer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post_detail, container, false);

        titleTextView = view.findViewById(R.id.titleTextView);
        authorTextView = view.findViewById(R.id.authorTextView);
        dateTextView = view.findViewById(R.id.dateTextView);

        subtitleTextView = view.findViewById(R.id.postSubtitle);
        introTextView = view.findViewById(R.id.postIntro);
        mainContentTextView = view.findViewById(R.id.postMainContent);
        conclusionTextView = view.findViewById(R.id.postConclusion);
        tagsTextView = view.findViewById(R.id.postTags);

        subtitleMediaContainer = view.findViewById(R.id.subtitleMediaContainer);
        introMediaContainer = view.findViewById(R.id.introMediaContainer);
        mainMediaContainer = view.findViewById(R.id.mainMediaContainer);
        conclusionMediaContainer = view.findViewById(R.id.conclusionMediaContainer);
        tagsMediaContainer = view.findViewById(R.id.tagsMediaContainer);

        Bundle args = getArguments();
        if (args != null && args.containsKey("post_id")) {
            int postId = args.getInt("post_id");
            fetchPostDetail(postId);
        }

        return view;
    }

    private void fetchPostDetail(int postId) {
        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        apiService.getPostDetail(postId).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<PostDetail> call, @NonNull Response<PostDetail> response) {
                if (response.isSuccessful() && response.body() != null) {
                    PostDetail detail = response.body();

                    titleTextView.setText(detail.getTitle());
                    authorTextView.setText("By : " + detail.getAuthor());
                    dateTextView.setText(detail.getDate());

                    subtitleTextView.setText(detail.getSubtitle());
                    introTextView.setText(detail.getIntroduction());
                    mainContentTextView.setText(detail.getMainContent());
                    conclusionTextView.setText(detail.getConclusion());
                    tagsTextView.setText(detail.getTags());

                    Map<String, List<Media>> mediaMap = detail.getMedia();
                    if (mediaMap != null) {
                        addMediaToSection(mediaMap.get("subtitle"), subtitleMediaContainer);
                        addMediaToSection(mediaMap.get("introduction"), introMediaContainer);
                        addMediaToSection(mediaMap.get("main_content"), mainMediaContainer);
                        addMediaToSection(mediaMap.get("conclusion"), conclusionMediaContainer);
                        addMediaToSection(mediaMap.get("tags"), tagsMediaContainer);
                    }

                } else {
                    Toast.makeText(getContext(), "Không tìm thấy chi tiết bài viết", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<PostDetail> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "Lỗi khi tải dữ liệu", Toast.LENGTH_SHORT).show();
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
                Log.d("MediaDebug", "Image URL: " + fileUrl);
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
