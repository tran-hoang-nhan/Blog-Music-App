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
            holder.favoritesTextView.setText("❤️ " + album.getFavorites());
        }


        Glide.with(holder.itemView.getContext())
                .load(album.getImageCover())
                .into(holder.imageView);

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

    public void submitList(List<ReviewAlbum> newList) {
        this.reviewAlbums = new ArrayList<>(newList);
        notifyDataSetChanged();
    }

    static class ReviewAlbumViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView albumTitleTextView, artistTextView, reviewDateTextView, releaseDateTextView;
        TextView ratingTextView, viewsTextView, favoritesTextView;

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
        }
    }
}
