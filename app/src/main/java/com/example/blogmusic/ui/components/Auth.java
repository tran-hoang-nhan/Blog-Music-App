package com.example.blogmusic.ui.components;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.blogmusic.api.ApiService;
import com.example.blogmusic.network.RetrofitClient;
import com.example.blogmusic.ui.model.AuthResponse;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Auth {

    private final ApiService api = RetrofitClient.getInstance().create(ApiService.class);

    public LiveData<AuthResponse.LoginResponse> login(String email, String password) {
        MutableLiveData<AuthResponse.LoginResponse>loginLiveData = new MutableLiveData<>();

        api.login(email, password).enqueue(new Callback<AuthResponse.LoginResponse>() {
            @Override
            public void onResponse(Call<AuthResponse.LoginResponse> call, Response<AuthResponse.LoginResponse> response) {
                loginLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<AuthResponse.LoginResponse> call, Throwable t) {
                Log.e("Auth", "Lỗi kết nối: " + t.getMessage());
                loginLiveData.setValue(null);

            }
        });

        return loginLiveData;

    }

    public LiveData<AuthResponse.RegisterResponse> register(String name, String email, String password) {
        MutableLiveData<AuthResponse.RegisterResponse> result = new MutableLiveData<>();

        api.register(name, email, password).enqueue(new Callback<AuthResponse.RegisterResponse>() {
            // Không dùng @Override ở đây để hạn chế clutter, theo style bạn muốn
            public void onResponse(Call<AuthResponse.RegisterResponse> call, Response<AuthResponse.RegisterResponse> response) {
                result.setValue(response.body());
            }

            public void onFailure(Call<AuthResponse.RegisterResponse> call, Throwable t) {
                Log.e("RegisterAPI", "Lỗi kết nối: " + t.getMessage(), t);
                result.setValue(null);
            }
        });

        return result;
    }

}
