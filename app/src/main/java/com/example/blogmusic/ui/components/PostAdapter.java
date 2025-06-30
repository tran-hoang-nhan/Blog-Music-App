package com.example.blogmusic.ui.components;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.blogmusic.R;
import com.example.blogmusic.api.ApiService;
import com.example.blogmusic.network.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
    public enum PostLayoutType {
        GRID,
        LIST
    }
    private List<Post> posts = new ArrayList<>();
    private final OnPostClickListener listener;
    private final PostLayoutType layoutType;

    public interface OnPostClickListener {
        void onPostClick(Post post);
    }
    public PostAdapter(OnPostClickListener listener, PostLayoutType layoutType) {
        this.listener = listener;
        this.layoutType = layoutType;
    }
    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutRes = (layoutType == PostLayoutType.LIST)
                ? R.layout.item_post_all
                : R.layout.item_post;
        View view = LayoutInflater.from(parent.getContext()).inflate(layoutRes, parent, false);
        return new PostViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post post = posts.get(position);


        holder.titleTextView.setText(post.getTitle());
        holder.authorTextView.setText("By: " + post.getAuthor());
        holder.dateTextView.setText(post.getDate());


        if (holder.viewsTextView != null) {
            holder.viewsTextView.setText("👁 " + post.getViews());
        }
        if (holder.favoritesTextView != null) {
            holder.favoritesTextView.setText(String.valueOf(post.getFavorites()));
        }
        if (post.isFavorited()) {
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
            boolean newState = !post.isFavorited();

            Call<FavoriteResponse> call = newState
                    ? apiService.favorite(userId, post.getId(), null)
                    : apiService.unfavorite(userId, post.getId(), null);
            call.enqueue(new Callback<>() {
                @Override
                public void onResponse(@NonNull Call<FavoriteResponse> call, @NonNull Response<FavoriteResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        FavoriteResponse res = response.body();

                        post.setFavorited(newState);
                        post.setFavorites(res.getFavorites());

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
            apiService.countFavorites(post.getId()).enqueue(new Callback<>() {
                @Override
                public void onResponse(@NonNull Call<FavoriteResponse> call, @NonNull Response<FavoriteResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        int newCount = response.body().getFavorites();
                        post.setFavorites(newCount);
                        holder.favoritesTextView.setText(String.valueOf(newCount)); // ✅ trong countFavorites
                    }
                }

                @Override
                public void onFailure(@NonNull Call<FavoriteResponse> call, @NonNull Throwable t) {
                    Log.e("FAVORITE", "Count failed: " + t.getMessage());
                }
            });

        });


        if (post.getImageCover() != null && !post.getImageCover().isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load(post.getImageCover())
                    .placeholder(R.drawable.placeholder)
                    .transition(com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade(500))
                    .into(holder.imageView);
        } else {
            holder.imageView.setImageResource(R.drawable.placeholder);
        }
        holder.imageView.startAnimation(android.view.animation.AnimationUtils.loadAnimation(
                holder.itemView.getContext(), R.anim.image_scale));


        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onPostClick(post);
            }
        });
    }
    @Override
    public int getItemCount() {
        return posts.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void submitList(List<Post> newPosts) {
        this.posts = newPosts;
        notifyDataSetChanged();
    }

    static class PostViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView titleTextView, authorTextView, dateTextView;
        TextView viewsTextView, favoritesTextView;
        ImageButton btnFavorite;

        PostViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            authorTextView = itemView.findViewById(R.id.authorTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);

            // Chỉ có ở item_post_all.xml
            viewsTextView = itemView.findViewById(R.id.viewsTextView);
            favoritesTextView = itemView.findViewById(R.id.favoritesTextView);
            btnFavorite = itemView.findViewById(R.id.btn_favorite);

        }
    }
}
