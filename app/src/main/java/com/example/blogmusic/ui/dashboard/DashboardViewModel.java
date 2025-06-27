package com.example.blogmusic.ui.dashboard;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.blogmusic.api.ApiService;
import com.example.blogmusic.network.RetrofitClient;
import com.example.blogmusic.ui.components.Post;
import com.example.blogmusic.ui.components.ReviewAlbum;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardViewModel extends ViewModel {

    private final MutableLiveData<List<Post>> postsLiveData = new MutableLiveData<>();
    private final ApiService apiService;
    private final MutableLiveData<List<ReviewAlbum>> reviewsLiveData = new MutableLiveData<>();

    public DashboardViewModel() {
        apiService = RetrofitClient.getInstance().create(ApiService.class);
        loadPosts();
        loadReviews();
    }

    public LiveData<List<Post>> getPosts() { return postsLiveData; }
    public LiveData<List<ReviewAlbum>> getReviews() { return reviewsLiveData; }
    private void loadPosts() {
        Call<List<Post>> call = apiService.getPostsBySort("recent");

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<List<Post>> call, @NonNull Response<List<Post>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    postsLiveData.setValue(response.body());
                } else {
                    Log.e("NewsViewModel", "Lỗi lấy dữ liệu bài viết (status: " + response.code() + ")");
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Post>> call, @NonNull Throwable t) {
                Log.e("NewsViewModel", "Lỗi mạng hoặc server: " + t.getMessage());
            }
        });
    }

    private void loadReviews() {
        Call<List<ReviewAlbum>> call = apiService.getReviewsBySort("recent");

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<List<ReviewAlbum>> call, @NonNull Response<List<ReviewAlbum>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    reviewsLiveData.setValue(response.body());
                } else {
                    Log.e("ReviewViewModel", "Lỗi lấy dữ liệu bài viết (status: " + response.code() + ")");
                }
            }
            @Override
            public void onFailure(@NonNull Call<List<ReviewAlbum>> call, @NonNull Throwable t) {
                Log.e("ReviewViewModel", "Lỗi mạng hoặc server: " + t.getMessage());
            }
        });
    }
}
