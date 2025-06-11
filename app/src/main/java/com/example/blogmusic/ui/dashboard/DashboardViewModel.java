package com.example.blogmusic.ui.dashboard;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.blogmusic.R;
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
        loadPosts();     // Nếu vẫn dùng dummy posts
        loadReviews();   // Gọi dữ liệu từ API
    }

    public LiveData<List<Post>> getPosts() {
        return posts;
    }

    public LiveData<List<ReviewAlbum>> getReviews() {
        return reviewList;
    }

    private void loadPosts() {
        List<Post> dummyPosts = new ArrayList<>();
        dummyPosts.add(new Post(R.drawable.ic_dashboard_black_24dp, "Post 1", "Author 1", "2023-10-01", "a"));
        dummyPosts.add(new Post(R.drawable.ic_dashboard_black_24dp, "Post 2", "Author 2", "2023-10-02", "a"));
        dummyPosts.add(new Post(R.drawable.ic_dashboard_black_24dp, "Post 3", "Author 3", "2023-10-03", "a"));
        posts.setValue(dummyPosts);
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
