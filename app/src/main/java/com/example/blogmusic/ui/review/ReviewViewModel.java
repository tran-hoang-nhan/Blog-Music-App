package com.example.blogmusic.ui.review;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.blogmusic.api.ApiService;
import com.example.blogmusic.network.RetrofitClient;
import com.example.blogmusic.ui.components.ReviewAlbum;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReviewViewModel extends ViewModel {
    private final MutableLiveData<List<ReviewAlbum>> reviewsLiveData = new MutableLiveData<>();
    private final ApiService apiService;

    public ReviewViewModel() {
        apiService = RetrofitClient.getInstance().create(ApiService.class);
    }

    public LiveData<List<ReviewAlbum>> getReviews() {
        return reviewsLiveData;
    }


    public void fetchReviewsBySort(String sortType) {
        Call<List<ReviewAlbum>> call = apiService.getReviewsBySort(sortType);

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<List<ReviewAlbum>> call, @NonNull Response<List<ReviewAlbum>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    reviewsLiveData.setValue(response.body());
                } else {
                    Log.e("ReviewViewModel", "Lỗi lấy bài viết (" + sortType + ") - code: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<ReviewAlbum>> call, @NonNull Throwable t) {
                Log.e("ReviewViewModel", "Lỗi mạng (" + sortType + "): " + t.getMessage());
            }
        });
    }
}
