package com.example.blogmusic.ui.news;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.blogmusic.api.ApiService;
import com.example.blogmusic.network.RetrofitClient;
import com.example.blogmusic.ui.components.Post;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsViewModel extends ViewModel {

    private final MutableLiveData<List<Post>> postsLiveData = new MutableLiveData<>();
    private final ApiService apiService;
    private final int userId;

    public NewsViewModel(int userId) {
        this.userId = userId;
        this.apiService = RetrofitClient.getInstance().create(ApiService.class);
    }

    public LiveData<List<Post>> getPosts() {
        return postsLiveData;
    }


    public void fetchPostsBySort(String sortType) {
        Call<List<Post>> call = apiService.getPostsBySort(sortType, userId);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<List<Post>> call, @NonNull Response<List<Post>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    postsLiveData.setValue(response.body());
                } else {
                    Log.e("NewsViewModel", "Lỗi lấy bài viết (" + sortType + ") - code: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Post>> call, @NonNull Throwable t) {
                Log.e("NewsViewModel", "Lỗi mạng (" + sortType + "): " + t.getMessage());
            }
        });
    }
}
