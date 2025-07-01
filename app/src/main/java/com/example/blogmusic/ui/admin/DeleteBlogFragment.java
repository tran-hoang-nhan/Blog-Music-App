package com.example.blogmusic.ui.admin;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.blogmusic.R;
import com.example.blogmusic.ui.components.AdminResponse;

public class DeleteBlogFragment extends Fragment {

    private AdminViewModel adminViewModel;

    private Spinner spinnerType;
    private EditText edtDeleteIdOrTitle;

    private final String[] contentTypes = {"post", "review"};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_delete_blog, container, false);

        adminViewModel = new ViewModelProvider(this).get(AdminViewModel.class);

        spinnerType = view.findViewById(R.id.spinner_type);
        edtDeleteIdOrTitle = view.findViewById(R.id.edt_delete_id_or_title);
        Button btnDelete = view.findViewById(R.id.btn_delete);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_item, contentTypes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(adapter);

        btnDelete.setOnClickListener(v -> handleDelete());

        adminViewModel.getDeleteBlogResult().observe(getViewLifecycleOwner(), this::handleDeleteResult);

        return view;
    }

    private void handleDelete() {
        String type = spinnerType.getSelectedItem().toString();
        String idOrTitle = edtDeleteIdOrTitle.getText().toString().trim();

        if (idOrTitle.isEmpty()) {
            Toast.makeText(getContext(), "Vui lòng nhập ID hoặc tiêu đề", Toast.LENGTH_SHORT).show();
            return;
        }

        adminViewModel.deleteBlog(type, idOrTitle);
    }

    private void handleDeleteResult(AdminResponse response) {
        if (response != null) {
            Log.d("DELETE_BLOG_RESPONSE", "status=" + response.isStatus() + ", message=" + response.getMessage());
            if (response.isStatus()) {
                Toast.makeText(getContext(), response.getMessage(), Toast.LENGTH_SHORT).show();
                edtDeleteIdOrTitle.setText("");
            } else {
                Toast.makeText(getContext(), "Xóa thất bại: " + response.getMessage(), Toast.LENGTH_SHORT).show();
            }
        } else {
            Log.e("DELETE_BLOG_RESPONSE", "Response null");
            Toast.makeText(getContext(), "Không nhận được phản hồi từ server", Toast.LENGTH_SHORT).show();
        }
    }
}
