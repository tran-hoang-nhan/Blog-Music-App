package com.example.blogmusic.ui.admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blogmusic.R;
import com.example.blogmusic.ui.components.UserAdapter;

public class UserFragment extends Fragment {

    private AdminViewModel adminViewModel;
    private UserAdapter userAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        adminViewModel = new ViewModelProvider(this).get(AdminViewModel.class);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_user);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        userAdapter = new UserAdapter(user -> adminViewModel.deleteUser(user.getUser_id()));
        recyclerView.setAdapter(userAdapter);

        adminViewModel.getAllUsers();
        adminViewModel.getUserList().observe(getViewLifecycleOwner(), users -> {
            if (users != null) {
                userAdapter.setUserList(users);
            }
        });

        adminViewModel.getDeleteUserResult().observe(getViewLifecycleOwner(), response -> {
            if (response != null && response.isStatus()) {
                Toast.makeText(getContext(), "Xóa thành công", Toast.LENGTH_SHORT).show();
                adminViewModel.getAllUsers(); // reload
            } else {
                Toast.makeText(getContext(), "Xóa thất bại", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}

