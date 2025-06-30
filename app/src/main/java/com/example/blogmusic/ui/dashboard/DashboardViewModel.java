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
    private final int userId;

    public DashboardViewModel(int userId) {
        this.userId = userId;
        apiService = RetrofitClient.getInstance().create(ApiService.class);
        loadPosts();
        loadReviews();
    }

    public LiveData<List<Post>> getPosts() { return postsLiveData; }
    public LiveData<List<ReviewAlbum>> getReviews() { return reviewsLiveData; }
    private void loadPosts() {
        Call<List<Post>> call = apiService.getPostsBySort("recent", userId);

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<List<Post>> call, @NonNull Response<List<Post>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Post> allPosts = response.body();
                    List<Post> limitedPosts = allPosts.subList(0, Math.min(5, allPosts.size()));
                    postsLiveData.setValue(limitedPosts);
                } else {
                    Log.e("DashboardViewModel", "Lỗi lấy dữ liệu bài viết (status: " + response.code() + ")");
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Post>> call, @NonNull Throwable t) {
                Log.e("DashboardViewModel", "Lỗi mạng hoặc server: " + t.getMessage());
            }
        });
    }
    private void loadReviews() {
        Call<List<ReviewAlbum>> call = apiService.getReviewsBySort("recent", userId);

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<List<ReviewAlbum>> call, @NonNull Response<List<ReviewAlbum>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<ReviewAlbum> allReviews = response.body();
                    List<ReviewAlbum> limitedReviews = allReviews.subList(0, Math.min(5, allReviews.size()));
                    reviewsLiveData.setValue(limitedReviews);
                } else {
                    Log.e("DashboardViewModel", "Lỗi lấy dữ liệu review (status: " + response.code() + ")");
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<ReviewAlbum>> call, @NonNull Throwable t) {
                Log.e("DashboardViewModel", "Lỗi mạng hoặc server: " + t.getMessage());
            }
        });
    }
}
