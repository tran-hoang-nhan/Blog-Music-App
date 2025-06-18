package com.example.blogmusic.ui.dashboard;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.blogmusic.api.ApiService;
import com.example.blogmusic.network.RetrofitClient;
import com.example.blogmusic.ui.components.Post;
import com.example.blogmusic.ui.components.ReviewAlbum;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardViewModel extends ViewModel {

    private final MutableLiveData<List<Post>> posts = new MutableLiveData<>();
    private final MutableLiveData<List<ReviewAlbum>> reviewList = new MutableLiveData<>();

    public DashboardViewModel() {
        loadPosts();
        loadReviews();
    }

    public LiveData<List<Post>> getPosts() {
        return posts;
    }

    public LiveData<List<ReviewAlbum>> getReviews() {
        return reviewList;
    }
    private void loadPosts() {
        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        Call<List<Post>> call = apiService.getAllPosts(); // GET từ PHP API trả về List<Post>

        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    posts.setValue(response.body());
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

    private void loadReviews() {
        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        Call<List<ReviewAlbum>> call = apiService.getReviews();

        call.enqueue(new Callback<List<ReviewAlbum>>() {
            @Override
            public void onResponse(Call<List<ReviewAlbum>> call, Response<List<ReviewAlbum>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<ReviewAlbum> data = response.body();
                    reviewList.setValue(data);
                    // ✅ In log số lượng review để kiểm tra
                    Log.d("DEBUG", "Số lượng review: " + data.size());
                } else {
                    Log.e("DEBUG", "Phản hồi thất bại - code: " + response.code());
                    reviewList.setValue(new ArrayList<>());
                }
            }

            @Override
            public void onFailure(Call<List<ReviewAlbum>> call, Throwable t) {
                Log.e("DEBUG", "Lỗi khi gọi API: " + t.getMessage());
                reviewList.setValue(new ArrayList<>());
            }
        });
    }
}
