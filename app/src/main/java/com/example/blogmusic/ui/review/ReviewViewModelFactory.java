package com.example.blogmusic.ui.review;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;


public class ReviewViewModelFactory implements ViewModelProvider.Factory {

    private final int userId;

    public ReviewViewModelFactory(int userId) {
        this.userId = userId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ReviewViewModel.class)) {
            return (T) new ReviewViewModel(userId);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
