package com.example.blogmusic.ui.review;

import android.util.Log;
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

    private final MutableLiveData<List<ReviewAlbum>> reviewLiveData = new MutableLiveData<>();
    private final ApiService apiService;

    public ReviewViewModel() {
        apiService = RetrofitClient.getInstance().create(ApiService.class);
        fetchAllReviews(); // mặc định gọi "All"
    }

    public LiveData<List<ReviewAlbum>> getReviews() {
        return reviewLiveData;
    }

    public void fetchAllReviews() {
        Call<List<ReviewAlbum>> call = apiService.getReviews();
        call.enqueue(new Callback<List<ReviewAlbum>>() {
            @Override
            public void onResponse(Call<List<ReviewAlbum>> call, Response<List<ReviewAlbum>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    reviewLiveData.setValue(response.body());
                } else {
                    Log.e("ReviewViewModel", "Lỗi lấy dữ liệu All Reviews (status: " + response.code() + ")");
                }
            }

            @Override
            public void onFailure(Call<List<ReviewAlbum>> call, Throwable t) {
                Log.e("ReviewViewModel", "Lỗi mạng khi gọi All Reviews: " + t.getMessage());
            }
        });
    }

    public void fetchRecentReviews() {
        Call<List<ReviewAlbum>> call = apiService.getRecentReviews();
        call.enqueue(new Callback<List<ReviewAlbum>>() {
            @Override
            public void onResponse(Call<List<ReviewAlbum>> call, Response<List<ReviewAlbum>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    reviewLiveData.setValue(response.body());
                } else {
                    Log.e("ReviewViewModel", "Lỗi lấy dữ liệu Recent Reviews (status: " + response.code() + ")");
                }
            }

            @Override
            public void onFailure(Call<List<ReviewAlbum>> call, Throwable t) {
                Log.e("ReviewViewModel", "Lỗi mạng khi gọi Recent Reviews: " + t.getMessage());
            }
        });
    }
}
