package com.example.blogmusic.ui.search;

import android.content.Context;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import java.util.List;

import com.example.blogmusic.api.ApiService;
import com.example.blogmusic.network.RetrofitClient;
import com.example.blogmusic.ui.components.Post;
import com.example.blogmusic.ui.components.ReviewAlbum;
import com.example.blogmusic.ui.components.SearchResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class SearchViewModel extends ViewModel {
    private final MutableLiveData<List<Post>> _posts = new MutableLiveData<>();
    private final MutableLiveData<List<ReviewAlbum>> _reviews = new MutableLiveData<>();
    public LiveData<List<Post>> posts = _posts;
    public LiveData<List<ReviewAlbum>> reviews = _reviews;

    public MutableLiveData<Integer> postCount = new MutableLiveData<>(0);
    public MutableLiveData<Integer> reviewCount = new MutableLiveData<>(0);
    private Context context;

    public void setContext(Context context) {
        this.context = context.getApplicationContext();
    }

    public void search(String query) {
        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        apiService.search(query).enqueue(new Callback<SearchResponse>() {
            @Override
            public void onResponse(@NonNull Call<SearchResponse> call, @NonNull Response<SearchResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Post> postList = response.body().getPosts();
                    List<ReviewAlbum> reviewList = response.body().getReviews();

                    _posts.setValue(postList);
                    _reviews.setValue(reviewList);
                    postCount.setValue(postList != null ? postList.size() : 0);
                    reviewCount.setValue(reviewList != null ? reviewList.size() : 0);
                } else {
                    Log.e("Search", "Response error: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<SearchResponse> call, @NonNull Throwable t) {
                Log.e("Search", "Network error: " + t.getMessage());
            }
        });
    }
}
