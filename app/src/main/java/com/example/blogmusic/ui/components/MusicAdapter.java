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

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.MusicViewHolder> {

    private List<Music> musicList = new ArrayList<>();
    private final OnMusicClickListener listener;

    public interface OnMusicClickListener {
        void onMusicClick(Music music);
    }

    public MusicAdapter(OnMusicClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public MusicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_music, parent, false);
        return new MusicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MusicViewHolder holder, int position) {
        Music music = musicList.get(position);
        holder.imageView.setImageResource(music.getImageResourceId());
        holder.titleTextView.setText(music.getTitle());
        holder.artistTextView.setText(music.getArtist());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onMusicClick(music);
            }
        });
    }

    @Override
    public int getItemCount() {
        return musicList.size();
    }

    public void submitList(List<Music> newList) {
        this.musicList = newList;
        notifyDataSetChanged();
    }

    static class MusicViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView titleTextView;
        TextView artistTextView;

        MusicViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.musicImage);
            titleTextView = itemView.findViewById(R.id.musicTitle);
            artistTextView = itemView.findViewById(R.id.musicArtist);
        }
    }
} 