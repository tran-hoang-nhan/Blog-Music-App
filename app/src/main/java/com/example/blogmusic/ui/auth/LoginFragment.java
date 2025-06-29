package com.example.blogmusic.ui.auth;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.blogmusic.R;
import com.example.blogmusic.ui.auth.LoginViewModel;
import com.example.blogmusic.ui.model.AuthResponse.LoginResponse;

public class LoginFragment extends Fragment {

    private EditText edtEmail, edtPassword;
    private Button btnLogin;
    private LoginViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        edtEmail = view.findViewById(R.id.edt_email);
        edtPassword = view.findViewById(R.id.edt_password);
        btnLogin = view.findViewById(R.id.btn_login);

        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        btnLogin.setOnClickListener(v -> {
            String email = edtEmail.getText().toString().trim();
            String pass = edtPassword.getText().toString().trim();

            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(pass)) {
                Toast.makeText(getContext(), "Điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            viewModel.login(email, pass).observe(getViewLifecycleOwner(), response -> {
                if (response != null && response.isStatus()) {
                    Toast.makeText(getContext(), "Xin chào " + response.getName(), Toast.LENGTH_SHORT).show();
                    // ➕ Lưu thông tin nếu cần
                    SharedPreferences prefs = requireContext().getSharedPreferences("auth", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putBoolean("isLoggedIn", true);
                    editor.putString("userName", response.getName());
                    editor.putString("userEmail", response.getEmail());
                    editor.apply();

                } else {
                    Toast.makeText(getContext(), "Sai tài khoản hoặc lỗi kết nối", Toast.LENGTH_SHORT).show();
                }
            });

        });

        return view;
    }
}