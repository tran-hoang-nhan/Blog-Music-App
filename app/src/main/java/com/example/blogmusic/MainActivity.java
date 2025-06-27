package com.example.blogmusic;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
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

        // Setup Navigation
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment_activity_main);
        navController = navHostFragment.getNavController();
        NavigationUI.setupWithNavController(binding.navView, navController);

        binding.navView.setOnItemSelectedListener(item -> {
            int destinationId = item.getItemId();
            if (navController.getCurrentDestination() != null &&
                    navController.getCurrentDestination().getId() == destinationId) {
                navController.popBackStack(destinationId, false);
            } else {
                navController.navigate(destinationId);
            }
            return true;
        });

        setupSearchView();
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if (destination.getId() == R.id.navigation_profile) {
                binding.searchBarContainer.setVisibility(View.GONE);
            } else {
                binding.searchBarContainer.setVisibility(View.VISIBLE);
            }
        });

    }

    private void setupSearchView() {
        SearchView searchView = binding.searchView;
        View searchPlate = searchView.findViewById(androidx.appcompat.R.id.search_plate);
        if (searchPlate != null) {
            searchPlate.setBackgroundColor(Color.TRANSPARENT);
        }

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Navigate to SearchFragment with query
                Bundle bundle = new Bundle();
                bundle.putString("query", query);
                navController.navigate(R.id.searchFragment, bundle);

                // Clear focus to hide keyboard
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }
}
