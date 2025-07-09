package com.example.blogmusic.ui.chatbot;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blogmusic.R;
import com.example.blogmusic.ui.components.ChatAdapter;

import java.util.ArrayList;

public class ChatbotFragment extends DialogFragment {

    private ChatbotViewModel viewModel;
    private ChatAdapter adapter;
    private RecyclerView recyclerView;
    private EditText editInput;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chatbot, container, false);
    }
    public ChatbotFragment() {
        setStyle(STYLE_NORMAL, R.style.ChatbotDialogStyle);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        recyclerView = view.findViewById(R.id.recycler_chat);
        editInput = view.findViewById(R.id.edit_input);
        ImageButton btnSend = view.findViewById(R.id.btn_send);
        ImageButton btnClose = view.findViewById(R.id.btn_close);


        viewModel = new ViewModelProvider(this).get(ChatbotViewModel.class);
        adapter = new ChatAdapter(new ArrayList<>());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        viewModel.getMessages().observe(getViewLifecycleOwner(), messages -> {
            adapter.setMessages(messages); // Cập nhật dữ liệu, KHÔNG tạo lại adapter
            recyclerView.scrollToPosition(messages.size() - 1);
        });


        btnSend.setOnClickListener(v -> {
            String input = editInput.getText().toString().trim();
            if (!input.isEmpty()) {
                viewModel.sendMessage(input);
                editInput.setText("");
            }
        });


        btnClose.setOnClickListener(v -> dismiss());

    }
    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setLayout(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.WRAP_CONTENT
            );
        }
    }

}

