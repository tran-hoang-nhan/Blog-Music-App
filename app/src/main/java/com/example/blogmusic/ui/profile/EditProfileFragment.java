package com.example.blogmusic.ui.profile;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.blogmusic.R;
import com.example.blogmusic.api.ApiService;
import com.example.blogmusic.network.RetrofitClient;
import com.example.blogmusic.ui.components.EditProfileResponse;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileFragment extends Fragment {

    private EditText editName, editEmail;
    private SharedPreferences prefs;
    private ApiService apiService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        editName = view.findViewById(R.id.edit_name);
        editEmail = view.findViewById(R.id.edit_email);
        Button btnSave = view.findViewById(R.id.btn_save);

        prefs = requireContext().getSharedPreferences("auth", Context.MODE_PRIVATE);
        apiService = RetrofitClient.getInstance().create(ApiService.class);

        loadUserData();

        btnSave.setOnClickListener(v -> saveChanges());

        return view;
    }

    private void loadUserData() {
        String name = prefs.getString("userName", "");
        String email = prefs.getString("userEmail", "");

        editName.setText(name);
        editEmail.setText(email);
    }

    private void saveChanges() {
        int userId = prefs.getInt("userId", -1);
        String name = editName.getText().toString().trim();
        String email = editEmail.getText().toString().trim();

        if (name.isEmpty() || email.isEmpty()) {
            Toast.makeText(getContext(), "Tên và email không được để trống", Toast.LENGTH_SHORT).show();
            return;
        }

        apiService.updateProfile(userId, name, email)
                .enqueue(new Callback<>() {
                    @Override
                    public void onResponse(@NonNull Call<EditProfileResponse> call, @NonNull Response<EditProfileResponse> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().isStatus()) {
                            Log.d("API", "JSON OK: " + new Gson().toJson(response.body()));
                            // Cập nhật lại SharedPreferences
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putString("userName", name);
                            editor.putString("userEmail", email);
                            editor.apply();

                            Toast.makeText(getContext(), "Cập nhật thành công", Toast.LENGTH_SHORT).show();

                            // Quay lại ProfileFragment
                            NavController nav = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
                            nav.navigate(R.id.action_edit_to_profile);
                        } else {
                            Toast.makeText(getContext(), "Cập nhật thất bại: " +
                                    (response.body() != null ? response.body().getMessage() : "Lỗi không xác định"), Toast.LENGTH_SHORT).show();
                            try {
                                String errorBody = response.errorBody().string();
                                Log.e("API", "LỖI JSON: " + errorBody);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            Toast.makeText(getContext(), "Lỗi phản hồi JSON", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<EditProfileResponse> call, @NonNull Throwable t) {
                        t.printStackTrace();
                        Toast.makeText(getContext(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
