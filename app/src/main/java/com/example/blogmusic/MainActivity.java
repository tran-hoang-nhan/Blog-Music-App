package com.example.blogmusic;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.blogmusic.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment_activity_main);
        navController = navHostFragment.getNavController();
        NavigationUI.setupWithNavController(binding.navView, navController);

        binding.navView.setOnItemSelectedListener(item -> {
            int destinationId = item.getItemId();
            if (navController.getCurrentDestination() != null &&
                    navController.getCurrentDestination().getId() == destinationId) {
                navController.popBackStack(destinationId, false);
                return true;
            } else {
                navController.navigate(destinationId);
                return true;
            }
        });
    }
}
