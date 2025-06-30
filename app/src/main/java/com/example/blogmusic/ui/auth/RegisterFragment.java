package com.example.blogmusic.ui.auth;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.blogmusic.R;

public class RegisterFragment extends Fragment {

    private EditText edtName, edtEmail, edtPassword;
    private Button btnRegister;
    private RegisterViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        edtName = view.findViewById(R.id.edt_name);
        edtEmail = view.findViewById(R.id.edt_email);
        edtPassword = view.findViewById(R.id.edt_password);
        btnRegister = view.findViewById(R.id.btn_register);

        viewModel = new ViewModelProvider(this).get(RegisterViewModel.class);

        btnRegister.setOnClickListener(v -> {
            String name = edtName.getText().toString().trim();
            String email = edtEmail.getText().toString().trim();
            String pass = edtPassword.getText().toString().trim();

            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(pass)) {
                Toast.makeText(getContext(), "Hãy nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            viewModel.register(name, email, pass).observe(getViewLifecycleOwner(), response -> {
                if (response != null && response.isStatus()) {
                    Toast.makeText(getContext(), response.getMessage(), Toast.LENGTH_SHORT).show();
                    NavController navController = Navigation.findNavController(requireView());
                    navController.navigate(R.id.action_register_to_login);  // Đảm bảo ID này đúng trong nav_graph
                } else if (response != null) {
                    Toast.makeText(getContext(), response.getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Lỗi kết nối", Toast.LENGTH_SHORT).show();
                }
            });
        });

        return view;
    }
}