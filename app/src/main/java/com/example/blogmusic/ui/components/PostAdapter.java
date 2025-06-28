package com.example.blogmusic.ui.components;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.blogmusic.R;

import java.util.ArrayList;
import java.util.List;

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
            holder.favoritesTextView.setText("❤️ " + post.getFavorites());
        }


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

    public void submitList(List<Post> newPosts) {
        this.posts = newPosts;
        notifyDataSetChanged();
    }

    static class PostViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView titleTextView, authorTextView, dateTextView;
        TextView viewsTextView, favoritesTextView; // optional

        PostViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            authorTextView = itemView.findViewById(R.id.authorTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);

            // Chỉ có ở item_post_all.xml
            viewsTextView = itemView.findViewById(R.id.viewsTextView);
            favoritesTextView = itemView.findViewById(R.id.favoritesTextView);
        }
    }
}
