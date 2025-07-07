package com.example.blogmusic.ui.auth;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.blogmusic.ui.components.AuthResponse.LoginResponse;


import com.example.blogmusic.MainActivity;
import com.example.blogmusic.R;


public class LoginFragment extends Fragment {

    private EditText edtEmail, edtPassword;
    private LoginViewModel viewModel;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        edtEmail = view.findViewById(R.id.edt_email);
        edtPassword = view.findViewById(R.id.edt_password);
        Button btnLogin = view.findViewById(R.id.btn_login);

        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        // Xử lý đăng nhập thường
        btnLogin.setOnClickListener(v -> {
            String email = edtEmail.getText().toString().trim();
            String pass = edtPassword.getText().toString().trim();

            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(pass)) {
                Toast.makeText(getContext(), "Điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            viewModel.login(email, pass).observe(getViewLifecycleOwner(), this::handleLoginResponse);
        });

        return view;
    }

    private void handleLoginResponse(LoginResponse response) {
        if (response != null && response.isStatus()) {
            Toast.makeText(getContext(), "Xin chào " + response.getName(), Toast.LENGTH_SHORT).show();

            SharedPreferences prefs = requireContext().getSharedPreferences("auth", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("isLoggedIn", true);
            editor.putInt("userId", response.getUserId());
            editor.putString("userName", response.getName());
            editor.putString("userEmail", response.getEmail());
            editor.putString("role", response.getRole());
            editor.apply();

            Intent intent = new Intent(getContext(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            requireActivity().finish();
        } else {
            Toast.makeText(getContext(), "Sai tài khoản hoặc lỗi kết nối", Toast.LENGTH_SHORT).show();
        }
    }
}
