package com.example.blogmusic.ui.components;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.blogmusic.R;
import com.example.blogmusic.api.ApiService;
import com.example.blogmusic.network.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReviewAlbumAdapter extends RecyclerView.Adapter<ReviewAlbumAdapter.ReviewAlbumViewHolder> {

    public enum ReviewLayoutType {
        GRID,
        LIST
    }

    private List<ReviewAlbum> reviewAlbums = new ArrayList<>();
    private final OnReviewClickListener listener;
    private final ReviewLayoutType layoutType;

    public interface OnReviewClickListener {
        void onReviewClick(ReviewAlbum review);
    }

    public ReviewAlbumAdapter(OnReviewClickListener listener, ReviewLayoutType layoutType) {
        this.listener = listener;
        this.layoutType = layoutType;
    }

    @NonNull
    @Override
    public ReviewAlbumViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutRes = (layoutType == ReviewLayoutType.LIST)
                ? R.layout.item_review_all
                : R.layout.item_review_album;

        View view = LayoutInflater.from(parent.getContext()).inflate(layoutRes, parent, false);
        return new ReviewAlbumViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ReviewAlbumViewHolder holder, int position) {
        ReviewAlbum album = reviewAlbums.get(position);

        holder.albumTitleTextView.setText(album.getAlbumTitle());
        holder.artistTextView.setText(album.getArtist());

        if (holder.releaseDateTextView != null) {
            holder.releaseDateTextView.setText(album.getReleaseDate());
        }

        if (holder.reviewDateTextView != null) {
            holder.reviewDateTextView.setText(album.getReviewDate());
        }
        if (holder.ratingTextView != null) {
            holder.ratingTextView.setText(album.getRating());
        }
        if (holder.viewsTextView != null) {
            holder.viewsTextView.setText("👁 " + album.getViews());
        }
        if (holder.favoritesTextView != null) {
            holder.favoritesTextView.setText(String.valueOf(album.getFavorites()));
        }
        if (album.isFavorited()) {
            holder.btnFavorite.setImageResource(R.drawable.ic_heart_filled);
        } else {
            holder.btnFavorite.setImageResource(R.drawable.ic_heart_outline);
        }

        holder.btnFavorite.setOnClickListener(v -> {
            Context context = holder.itemView.getContext();
            SharedPreferences prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE);
            boolean isLoggedIn = prefs.getBoolean("isLoggedIn", false);
            if (!isLoggedIn) {
                Toast.makeText(context, "Vui lòng đăng nhập để thả tim", Toast.LENGTH_SHORT).show();
                return;
            }
            int userId = prefs.getInt("userId", -1);
            if (userId == -1) return;

            ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
            boolean newState = !album.isFavorited();

            Call<FavoriteResponse> call = newState
                    ? apiService.favorite(userId, null, album.getId())
                    : apiService.unfavorite(userId, null, album.getId());

            call.enqueue(new Callback<>() {
                @Override
                public void onResponse(@NonNull Call<FavoriteResponse> call, @NonNull Response<FavoriteResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        FavoriteResponse res = response.body();
                        album.setFavorited(newState);
                        album.setFavorites(res.getFavorites());

                        holder.btnFavorite.setImageResource(newState
                                ? R.drawable.ic_heart_filled
                                : R.drawable.ic_heart_outline);
                        holder.favoritesTextView.setText(String.valueOf(res.getFavorites()));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<FavoriteResponse> call, @NonNull Throwable t) {
                    Toast.makeText(context, "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
                }
            });

            apiService.countFavorites(album.getId()).enqueue(new Callback<>() {
                @Override
                public void onResponse(@NonNull Call<FavoriteResponse> call, @NonNull Response<FavoriteResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        int newCount = response.body().getFavorites();
                        album.setFavorites(newCount);
                        holder.favoritesTextView.setText(String.valueOf(newCount)); // ✅ trong countFavorites
                    }
                }

                @Override
                public void onFailure(@NonNull Call<FavoriteResponse> call, @NonNull Throwable t) {
                    Log.e("FAVORITE", "Count failed: " + t.getMessage());
                }
            });
        });



        Glide.with(holder.itemView.getContext())
                .load(album.getImageCover())
                .placeholder(R.drawable.placeholder)
                .transition(DrawableTransitionOptions.withCrossFade(500))
                .into(holder.imageView);
        holder.imageView.startAnimation(AnimationUtils.loadAnimation(
                holder.itemView.getContext(), R.anim.image_scale));


        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onReviewClick(album);
            }
        });
    }

    @Override
    public int getItemCount() {
        return reviewAlbums.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void submitList(List<ReviewAlbum> newList) {
        this.reviewAlbums = new ArrayList<>(newList);
        notifyDataSetChanged();
    }

    static class ReviewAlbumViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView albumTitleTextView, artistTextView, reviewDateTextView, releaseDateTextView;
        TextView ratingTextView, viewsTextView, favoritesTextView;
        ImageButton btnFavorite;
        ReviewAlbumViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            albumTitleTextView = itemView.findViewById(R.id.albumTitleTextView);
            artistTextView = itemView.findViewById(R.id.artistTextView);
            releaseDateTextView = itemView.findViewById(R.id.releaseDateTextView);
            reviewDateTextView = itemView.findViewById(R.id.reviewDateTextView);
            ratingTextView = itemView.findViewById(R.id.scoreTextView);
            viewsTextView = itemView.findViewById(R.id.viewsTextView);
            favoritesTextView = itemView.findViewById(R.id.favoritesTextView);
            btnFavorite = itemView.findViewById(R.id.btn_favorite);
        }
    }
}
