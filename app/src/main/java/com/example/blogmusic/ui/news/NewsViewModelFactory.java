package com.example.blogmusic.ui.news;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModel;
public class NewsViewModelFactory implements ViewModelProvider.Factory {

    private final int userId;

    public NewsViewModelFactory(int userId) {
        this.userId = userId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(NewsViewModel.class)) {
            return (T) new NewsViewModel(userId);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
