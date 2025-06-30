package com.example.blogmusic.ui.news;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Build;
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
import com.example.blogmusic.ui.components.Post;
import com.example.blogmusic.ui.components.PostAdapter;
import com.example.blogmusic.ui.components.PostDetail;
import com.example.blogmusic.ui.components.PostResponse;
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

public class PostDetailFragment extends Fragment {
    private NavController navController;
    private TextView titleTextView, authorTextView, dateTextView;
    private TextView subtitleTextView, introTextView, mainContentTextView, conclusionTextView, relatedPostsLabel;
    private LinearLayout subtitleMediaContainer, introMediaContainer, mainMediaContainer, conclusionMediaContainer;
    private ChipGroup tagsChipGroup;
    private PostAdapter postAdapter;
    private ImageView postCoverImage;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_post_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

        // Khởi tạo view
        postCoverImage = view.findViewById(R.id.post_cover_image);
        titleTextView = view.findViewById(R.id.titleTextView);
        authorTextView = view.findViewById(R.id.authorTextView);
        dateTextView = view.findViewById(R.id.dateTextView);
        subtitleTextView = view.findViewById(R.id.postSubtitle);
        introTextView = view.findViewById(R.id.postIntro);
        mainContentTextView = view.findViewById(R.id.postMainContent);
        conclusionTextView = view.findViewById(R.id.postConclusion);
        relatedPostsLabel = view.findViewById(R.id.relatedPostsLabel);

        tagsChipGroup = view.findViewById(R.id.tagsChipGroup);

        subtitleMediaContainer = view.findViewById(R.id.subtitleMediaContainer);
        introMediaContainer = view.findViewById(R.id.introMediaContainer);
        mainMediaContainer = view.findViewById(R.id.mainMediaContainer);
        conclusionMediaContainer = view.findViewById(R.id.conclusionMediaContainer);

        RecyclerView relatedPostsRecyclerView = view.findViewById(R.id.relatedPostsRecyclerView);
        relatedPostsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        postAdapter = new PostAdapter(post -> {
            Bundle args = new Bundle();
            args.putInt("id", post.getId());
            navController.navigate(R.id.postDetailFragment, args);
        }, PostAdapter.PostLayoutType.GRID);
        relatedPostsRecyclerView.setAdapter(postAdapter);

        Bundle args = getArguments();
        if (args != null && args.containsKey("post_id")) {
            int postId = args.getInt("post_id");
            increaseViews("post", postId);
            fetchPostDetail(postId);
        }

        // Xử lý YouTubePlayerView bị che trên Android API < 29
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            view.setOnApplyWindowInsetsListener((v, insets) -> {
                int bottomInset = insets.getSystemWindowInsetBottom();
                v.setPadding(0, 0, 0, bottomInset + 64);
                return insets;
            });
        }
    }

    private void fetchPostDetail(int postId) {
        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        apiService.getPostDetail(postId).enqueue(new Callback<>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<PostDetail> call, @NonNull Response<PostDetail> response) {
                if (response.isSuccessful() && response.body() != null) {
                    PostDetail detail = response.body();


                    Glide.with(requireContext())
                            .load(detail.getImageCover())
                            .placeholder(R.drawable.placeholder) // nếu có ảnh mặc định
                            .into(postCoverImage);


                    titleTextView.setText(detail.getTitle());
                    authorTextView.setText("By : " + detail.getAuthor());
                    dateTextView.setText(detail.getDate());
                    subtitleTextView.setText(detail.getSubtitle());
                    introTextView.setText(detail.getIntroduction());
                    mainContentTextView.setText(detail.getMainContent());
                    conclusionTextView.setText(detail.getConclusion());

                    String[] tags = detail.getTags().split(",");
                    tagsChipGroup.removeAllViews();
                    for (String tag : tags) {
                        Chip chip = new Chip(requireContext());
                        chip.setText(tag.trim());
                        tagsChipGroup.addView(chip);
                    }

                    fetchRelatedPosts(detail.getAuthor(), postId);

                    Map<String, List<Media>> mediaMap = detail.getMedia();
                    if (mediaMap != null) {
                        addMediaToSection(mediaMap.get("subtitle"), subtitleMediaContainer);
                        addMediaToSection(mediaMap.get("introduction"), introMediaContainer);
                        addMediaToSection(mediaMap.get("main_content"), mainMediaContainer);
                        addMediaToSection(mediaMap.get("conclusion"), conclusionMediaContainer);
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

    private void fetchRelatedPosts(String author, int postId) {
        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        apiService.getRelatedPosts(author, postId).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<PostResponse> call, @NonNull Response<PostResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Post> relatedPosts = response.body().getPosts();
                    if (!relatedPosts.isEmpty()) {
                        relatedPostsLabel.setVisibility(View.VISIBLE);
                        postAdapter.submitList(relatedPosts);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<PostResponse> call, @NonNull Throwable t) {
                Log.e("RelatedPosts", "Error: " + t.getMessage());
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

                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                    youtubeItem.setPadding(0, 0, 0, 128);
                }

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
