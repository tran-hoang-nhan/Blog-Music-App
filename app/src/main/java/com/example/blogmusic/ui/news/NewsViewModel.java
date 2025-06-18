package com.example.blogmusic.ui.news;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.blogmusic.network.RetrofitClient;
import com.example.blogmusic.api.ApiService;
import com.example.blogmusic.ui.components.Post;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsViewModel extends ViewModel {

    private final MutableLiveData<List<Post>> postsLiveData = new MutableLiveData<>();

    public NewsViewModel() {
        loadPosts();
    }

    public LiveData<List<Post>> getPosts() {
        return postsLiveData;
    }

    private void loadPosts() {
        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        Call<List<Post>> call = apiService.getAllPosts(); // GET từ PHP API trả về List<Post>

        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    postsLiveData.setValue(response.body());
                } else {
                    Log.e("NewsViewModel", "Lỗi lấy dữ liệu bài viết (status: " + response.code() + ")");
                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                Log.e("NewsViewModel", "Lỗi mạng hoặc server: " + t.getMessage());
            }
        });
    }
}
