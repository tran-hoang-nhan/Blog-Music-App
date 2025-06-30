package com.example.blogmusic.ui.dashboard;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class DashboardViewModelFactory implements ViewModelProvider.Factory {

    private final int userId;

    public DashboardViewModelFactory(int userId) {
        this.userId = userId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(DashboardViewModel.class)) {
            return (T) new DashboardViewModel(userId);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
