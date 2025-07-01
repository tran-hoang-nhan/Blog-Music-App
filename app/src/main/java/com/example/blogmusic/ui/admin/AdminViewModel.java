package com.example.blogmusic.ui.admin;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.blogmusic.api.ApiService;
import com.example.blogmusic.network.RetrofitClient;
import com.example.blogmusic.ui.components.AdminResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminViewModel extends ViewModel {

    private final MutableLiveData<AdminResponse> addBlogResult = new MutableLiveData<>();
    private final MutableLiveData<AdminResponse> deleteBlogResult = new MutableLiveData<>();

    public LiveData<AdminResponse> getAddBlogResult() {
        return addBlogResult;
    }

    public LiveData<AdminResponse> getDeleteBlogResult() {
        return deleteBlogResult;
    }

    public void addBlog( Map<String, String> data) {
        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        Call<AdminResponse> call = apiService.addBlog(data);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<AdminResponse> call, @NonNull Response<AdminResponse> response) {
                addBlogResult.postValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<AdminResponse> call, @NonNull Throwable t) {
                addBlogResult.postValue(new AdminResponse(false, "Lỗi kết nối"));
            }
        });
    }

    public void deleteBlog(String type, String idOrTitle) {
        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        Call<AdminResponse> call = apiService.deleteBlog(type, idOrTitle);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<AdminResponse> call, @NonNull Response<AdminResponse> response) {
                deleteBlogResult.postValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<AdminResponse> call, @NonNull Throwable t) {
                deleteBlogResult.postValue(new AdminResponse(false, "Lỗi kết nối"));
            }
        });
    }
}

