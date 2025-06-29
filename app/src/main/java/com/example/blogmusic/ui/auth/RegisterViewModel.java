package com.example.blogmusic.ui.auth;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.blogmusic.ui.components.Auth;
import com.example.blogmusic.ui.model.AuthResponse.RegisterResponse;

public class RegisterViewModel extends ViewModel {
    private final Auth auth = new Auth();

    public LiveData<RegisterResponse> register(String name, String email, String password) {
        return auth.register(name, email, password);
    }
}