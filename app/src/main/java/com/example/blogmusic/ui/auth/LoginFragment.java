package com.example.blogmusic.ui.auth;

import android.app.Activity;
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

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.blogmusic.ui.components.AuthResponse.LoginResponse;


import com.example.blogmusic.MainActivity;
import com.example.blogmusic.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

public class LoginFragment extends Fragment {

    private EditText edtEmail, edtPassword;
    private Button btnLogin;
    private SignInButton btnGoogleSignIn;
    private LoginViewModel viewModel;
    private GoogleSignInClient mGoogleSignInClient;
    private ActivityResultLauncher<Intent> googleSignInLauncher;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        edtEmail = view.findViewById(R.id.edt_email);
        edtPassword = view.findViewById(R.id.edt_password);
        btnLogin = view.findViewById(R.id.btn_login);
        btnGoogleSignIn = view.findViewById(R.id.btn_google_signin); // Bổ sung nút Google

        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        // Xử lý đăng nhập thường
        btnLogin.setOnClickListener(v -> {
            String email = edtEmail.getText().toString().trim();
            String pass = edtPassword.getText().toString().trim();

            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(pass)) {
                Toast.makeText(getContext(), "Điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            viewModel.login(email, pass).observe(getViewLifecycleOwner(), response -> {
                handleLoginResponse(response);
            });
        });

        // Cấu hình Google Sign-In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("196151535992-kqtf2r7dk4me05n851jit9mmm2mu9ba3.apps.googleusercontent.com")
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(requireContext(), gso);

        // Launcher xử lý đăng nhập Google
        googleSignInLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                        try {
                            GoogleSignInAccount account = task.getResult(ApiException.class);
                            String email = account.getEmail();
                            String name = account.getDisplayName();
                            String idToken = account.getIdToken();

                            viewModel.loginWithGoogle(email, name, idToken).observe(getViewLifecycleOwner(), response -> {
                                handleLoginResponse(response);
                            });

                        } catch (ApiException e) {
                            Toast.makeText(getContext(), "Đăng nhập Google thất bại", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        // Khi người dùng nhấn nút Google Sign-In
        btnGoogleSignIn.setOnClickListener(v -> {
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            googleSignInLauncher.launch(signInIntent);
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
