package com.example.blogmusic.ui.components;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blogmusic.R;

import java.util.ArrayList;
import java.util.List;

public class ReviewAlbumAdapter extends RecyclerView.Adapter<ReviewAlbumAdapter.ReviewAlbumViewHolder> {
    private List<ReviewAlbum> reviewAlbums = new ArrayList<>();
    private final OnReviewClickListener listener;

    public interface OnReviewClickListener {
        void onReviewClick(ReviewAlbum review);
    }

    public ReviewAlbumAdapter(OnReviewClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ReviewAlbumViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_review_album, parent, false);
        return new ReviewAlbumViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewAlbumViewHolder holder, int position) {
        ReviewAlbum album = reviewAlbums.get(position);
        holder.imageView.setImageResource(album.getImageResourceId());
        holder.titleTextView.setText(album.getTitle());
        holder.artistTextView.setText(album.getArtist());
        holder.ratingTextView.setText(album.getRating());
        holder.dateTextView.setText(album.getReviewDate());

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
        this.reviewAlbums = newList;
        notifyDataSetChanged();
    }

    static class ReviewAlbumViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView titleTextView;
        TextView artistTextView;
        TextView ratingTextView;
        TextView dateTextView;

        ReviewAlbumViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.reviewImage);
            titleTextView = itemView.findViewById(R.id.reviewTitle);
            artistTextView = itemView.findViewById(R.id.reviewArtist);
            ratingTextView = itemView.findViewById(R.id.reviewRating);
            dateTextView = itemView.findViewById(R.id.reviewDate);
        }
    }
} 