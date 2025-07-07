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
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.EditText;
import com.example.blogmusic.ui.components.OrderResponse;
import android.widget.Button;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.MotionEvent;

public class ReviewAlbumDetailFragment extends Fragment {

    private NavController navController;
    private ImageView albumCoverImage;
    private TextView albumTitleText, albumArtistText, albumScoreText, detailSubtitleText;
    private TextView detailSummaryText, detailTracklistText, detailMainContentText, detailConclusionText, relatedReviewsLabel;
    private ChipGroup tagsChipGroup;
    private LinearLayout summaryMediaContainer, tracklistMediaContainer, mainMediaContainer, conclusionMediaContainer, tagsMediaContainer;
    private ReviewAlbumAdapter reviewAdapter;

    private int reviewId;
    private String albumImageUrl = null;
    private int albumPrice = 0;
    private boolean isOrdering = false;

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

        RecyclerView relatedReviewsRecyclerView = view.findViewById(R.id.relatedReviewRecyclerView);
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
        // Thêm logic cho nút thanh toán album
        View btnOrder = view.findViewById(R.id.btn_order_album);
        btnOrder.setOnClickListener(v -> showOrderDialog(albumImageUrl)); // albumImageUrl sẽ được set khi fetchReviewDetail
    }

    public void fetchReviewDetail(int id) {
        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        apiService.getReviewDetail(id).enqueue(new Callback<>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<ReviewAlbumDetail> call, @NonNull Response<ReviewAlbumDetail> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ReviewAlbumDetail detail = response.body();
                    albumImageUrl = detail.getImageCover();
                    albumPrice = detail.getPrice(); // Lưu giá album

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
                playerView.setEnableAutomaticInitialization(false);
                getLifecycle().addObserver(playerView);

                playerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                    @Override
                    public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                        String videoId = extractYoutubeVideoId(url);
                        if (!videoId.isEmpty()) {
                            youTubePlayer.cueVideo(videoId, 0);
                        }
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

    private void showOrderDialog(String albumImageUrl) {
        Context context = requireContext();
        SharedPreferences prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE);
        int userId = prefs.getInt("userId", -1);
        if (userId == -1) {
            Toast.makeText(context, "Bạn cần đăng nhập để thanh toán!", Toast.LENGTH_SHORT).show();
            return;
        }
        // Tạo dialog nhập thông tin thanh toán
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_order_album, null);
        ImageView imgAlbum = dialogView.findViewById(R.id.img_album_cover);
        if (albumImageUrl != null && !albumImageUrl.isEmpty()) {
            Glide.with(context)
                .load(albumImageUrl)
                .placeholder(R.drawable.placeholder)
                .into(imgAlbum);
        }
        TextView tvAlbumPrice = dialogView.findViewById(R.id.tv_album_price);
        tvAlbumPrice.setText("Giá: " + albumPrice + " VNĐ");
        EditText edtName = dialogView.findViewById(R.id.edt_order_name);
        EditText edtAddress = dialogView.findViewById(R.id.edt_order_address);
        EditText edtPhone = dialogView.findViewById(R.id.edt_order_phone);
        EditText edtQuantity = dialogView.findViewById(R.id.edt_order_quantity);
        View btnPay = dialogView.findViewById(R.id.btn_confirm_order);
        // Gợi ý tên từ profile
        edtName.setText(prefs.getString("userName", ""));
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle("Thanh toán album")
                .setView(dialogView)
                .setNegativeButton("Hủy", null)
                .create();

        btnPay.setOnClickListener(v -> {
            String name = edtName.getText().toString().trim();
            String address = edtAddress.getText().toString().trim();
            String phone = edtPhone.getText().toString().trim();
            String quantityStr = edtQuantity.getText().toString().trim();
            int quantity = 1;
            try { quantity = Integer.parseInt(quantityStr); } catch (Exception ignored) {}

            if (name.isEmpty() || address.isEmpty() || phone.isEmpty()) {
                Toast.makeText(context, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (isOrdering) return;

            final int fUserId = userId;
            final int fReviewId = reviewId;
            final String fName = name;
            final String fAddress = address;
            final String fPhone = phone;
            final int fQuantity = quantity;

            showPaymentMethodDialog(() -> {
                if (isOrdering) return;
                isOrdering = true;
                btnPay.setEnabled(false);
                orderAlbum(fUserId, fReviewId, fName, fAddress, fPhone, fQuantity);
                dialog.dismiss();
            });
        });

        dialog.show();
    }

    private void showPaymentMethodDialog(Runnable onConfirm) {
        Context context = requireContext();
        View paymentView = LayoutInflater.from(context).inflate(R.layout.dialog_payment_method, null);
        AlertDialog paymentDialog = new AlertDialog.Builder(context)
                .setView(paymentView)
                .create();
        View btnConfirm = paymentView.findViewById(R.id.btn_confirm_payment);
        
        // Thêm animation cho nút xác nhận thanh toán
        btnConfirm.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                Animation scaleDown = AnimationUtils.loadAnimation(context, R.anim.button_scale);
                v.startAnimation(scaleDown);
            } else if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
                Animation scaleUp = AnimationUtils.loadAnimation(context, R.anim.button_scale_reverse);
                v.startAnimation(scaleUp);
                v.performClick();
            }
            return false;
        });
        
        btnConfirm.setOnClickListener(v -> {
            // Có thể lấy phương thức đã chọn ở đây nếu muốn xử lý riêng
            paymentDialog.dismiss();
            onConfirm.run();
        });
        paymentDialog.show();
    }

    private void orderAlbum(int userId, int albumId, String name, String address, String phone, int quantity) {
        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        apiService.orderAlbum(userId, albumId, name, address, phone, quantity).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<OrderResponse> call, @NonNull Response<OrderResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isStatus()) {
                    showOrderSuccessDialog();
                    isOrdering = false;
                } else {
                    Toast.makeText(getContext(), "Thanh toán thất bại!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<OrderResponse> call, @NonNull Throwable t) {
                isOrdering = false;
                Toast.makeText(getContext(), "Lỗi kết nối khi thanh toán!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showOrderSuccessDialog() {
        Context context = requireContext();
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_order_success, null);
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setView(dialogView)
                .create();
        Button btnClose = dialogView.findViewById(R.id.btn_close_success);
        btnClose.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }
}
