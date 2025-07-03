package com.example.blogmusic.ui.chatbot;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.blogmusic.api.ApiService;
import com.example.blogmusic.network.RetrofitClient;
import com.example.blogmusic.ui.components.BotResponse;
import com.example.blogmusic.ui.components.ChatMessage;
import com.example.blogmusic.ui.components.ChatRequest;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatbotViewModel extends ViewModel {

    private final MutableLiveData<List<ChatMessage>> messages = new MutableLiveData<>(new ArrayList<>());

    public LiveData<List<ChatMessage>> getMessages() {
        return messages;
    }

    public void sendMessage(String text) {
        List<ChatMessage> current = new ArrayList<>(messages.getValue());
        current.add(new ChatMessage("user", text));
        messages.setValue(current);

        // Gọi API thông qua Retrofit
        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        apiService.sendMessage(new ChatRequest(text)).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<BotResponse> call, @NonNull Response<BotResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    current.add(new ChatMessage("bot", response.body().getMessage()));
                } else {
                    current.add(new ChatMessage("bot", "Xin lỗi, tôi không thể trả lời."));
                }
                messages.setValue(current);
            }

            @Override
            public void onFailure(@NonNull Call<BotResponse> call, @NonNull Throwable t) {
                current.add(new ChatMessage("bot", "Lỗi máy chủ."));
                messages.setValue(current);
            }
        });
    }
}

