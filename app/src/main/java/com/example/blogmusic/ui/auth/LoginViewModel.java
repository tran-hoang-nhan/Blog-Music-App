package com.example.blogmusic.ui.auth;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.blogmusic.ui.components.Auth;
import com.example.blogmusic.ui.model.AuthResponse.LoginResponse;
import com.example.blogmusic.ui.model.AuthResponse;

public class LoginViewModel extends ViewModel {
    private final Auth auth = new Auth();

    public LiveData<AuthResponse.LoginResponse> login(String email, String password) {
        return auth.login(email, password);
    }
    }
