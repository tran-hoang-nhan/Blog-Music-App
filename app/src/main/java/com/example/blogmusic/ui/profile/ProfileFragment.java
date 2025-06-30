package com.example.blogmusic.ui.profile;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.blogmusic.R;

public class ProfileFragment extends Fragment {

    private Button btnLogin, btnSignup, btnLogout, btnEdit;
    private TextView tvName, tvEmail;
    private ImageView avatar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        SharedPreferences prefs = requireContext().getSharedPreferences("auth", Context.MODE_PRIVATE);
        boolean isLoggedIn = prefs.getBoolean("isLoggedIn", false);

        // Ánh xạ View
        btnLogin   = view.findViewById(R.id.btn_login);
        btnSignup  = view.findViewById(R.id.btn_sign_up);
        btnLogout  = view.findViewById(R.id.btn_logout);
        btnEdit    = view.findViewById(R.id.btn_edit_profile);
        tvName     = view.findViewById(R.id.tv_user_name);
        tvEmail    = view.findViewById(R.id.tv_user_email);
        avatar     = view.findViewById(R.id.profile_avatar);

        NavController nav = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);

        if (isLoggedIn) {
            // Hiện thông tin người dùng
            String name  = prefs.getString("userName", "User");
            String email = prefs.getString("userEmail", "");

            tvName.setText(name);
            tvEmail.setText(email);

            btnLogin.setVisibility(View.GONE);
            btnSignup.setVisibility(View.GONE);

            avatar.setVisibility(View.VISIBLE);
            tvName.setVisibility(View.VISIBLE);
            tvEmail.setVisibility(View.VISIBLE);
            btnLogout.setVisibility(View.VISIBLE);
            btnEdit.setVisibility(View.VISIBLE);

            ViewGroup profileContainer = view.findViewById(R.id.profile_container);
            ViewGroup bottomContainer = view.findViewById(R.id.bottom_container);
            profileContainer.setVisibility(View.VISIBLE);
            bottomContainer.setVisibility(View.GONE);


            btnLogout.setOnClickListener(v -> {
                SharedPreferences.Editor editor = prefs.edit();
                editor.clear();
                editor.apply();

                Navigation.findNavController(view).navigate(R.id.action_profile_self);
            });


            btnEdit.setOnClickListener(v -> {
                Toast.makeText(getContext(), "Tính năng cập nhật hồ sơ đang được phát triển...", Toast.LENGTH_SHORT).show();
            });

        } else {
            // Chưa đăng nhập
            avatar.setVisibility(View.GONE);
            tvName.setVisibility(View.GONE);
            tvEmail.setVisibility(View.GONE);
            btnLogout.setVisibility(View.GONE);
            btnEdit.setVisibility(View.GONE);

            btnLogin.setVisibility(View.VISIBLE);
            btnSignup.setVisibility(View.VISIBLE);

            btnLogin.setOnClickListener(v -> nav.navigate(R.id.action_profile_to_login));
            btnSignup.setOnClickListener(v -> nav.navigate(R.id.action_profile_to_register));
        }

        return view;
    }
}