package com.example.blogmusic.ui.components;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.blogmusic.api.ApiService;
import com.example.blogmusic.network.RetrofitClient;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Auth {

    private final ApiService api = RetrofitClient.getInstance().create(ApiService.class);

    public LiveData<AuthResponse.LoginResponse> login(String email, String password) {
        MutableLiveData<AuthResponse.LoginResponse>loginLiveData = new MutableLiveData<>();
        LoginRequest request = new LoginRequest(email, password);

        api.login(request).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<AuthResponse.LoginResponse> call, @NonNull Response<AuthResponse.LoginResponse> response) {
                loginLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<AuthResponse.LoginResponse> call, @NonNull Throwable t) {
                Log.e("Auth", "Lỗi kết nối: " + t.getMessage());
                loginLiveData.setValue(null);

            }
        });

        return loginLiveData;

    }
    public LiveData<AuthResponse.RegisterResponse> register(String name, String email, String password) {
        MutableLiveData<AuthResponse.RegisterResponse> result = new MutableLiveData<>();

        api.register(name, email, password).enqueue(new Callback<>() {
            // Không dùng @Override ở đây để hạn chế clutter, theo style bạn muốn
            public void onResponse(@NonNull Call<AuthResponse.RegisterResponse> call, @NonNull Response<AuthResponse.RegisterResponse> response) {
                result.setValue(response.body());
            }

            public void onFailure(@NonNull Call<AuthResponse.RegisterResponse> call, @NonNull Throwable t) {
                Log.e("RegisterAPI", "Lỗi kết nối: " + t.getMessage(), t);
                result.setValue(null);
            }
        });

        return result;
    }

}
