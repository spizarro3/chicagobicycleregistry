// MainActivity.java
package com.example.itmd_555_final.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.itmd_555_final.R;
import com.example.itmd_555_final.activities.client.LoginActivity;
import com.example.itmd_555_final.data.repository.BikeRepository;
import com.example.itmd_555_final.fragments.BicycleListFragment;
import com.example.itmd_555_final.fragments.MapFragment;
import com.example.itmd_555_final.fragments.RegisterBikeFragment;
import com.example.itmd_555_final.models.Client;
import com.example.itmd_555_final.util.SessionManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private MainActivityHandler handler;
    private View fragmentContainer;

    @SuppressLint({"NonConstantResourceId", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Fetch Chicago bikes at launch
        new BikeRepository(this).fetchChicagoBikes();

        // Login check
        Client currentClient = SessionManager.getCurrentClient();
        if (currentClient == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        fragmentContainer = findViewById(R.id.fragmentContainer);

        handler = new MainActivityHandler(this, null, currentClient);

        BottomNavigationView nav = findViewById(R.id.bottomNavigationView);
        nav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_map) {
                MapFragment mapFragment = new MapFragment();
                showFragment(mapFragment);
                return true;

            } else if (itemId == R.id.nav_list) {
                showFragment(new BicycleListFragment());
                return true;

            } else if (itemId == R.id.nav_add) {
                showFragment(new RegisterBikeFragment());

                return true;

            } else if (itemId == R.id.nav_profile) {
                handler.showProfileOptions();
                return true;
            }

            return false;
        });

        // Default view
//        nav.setSelectedItemId(R.id.nav_map);
    }

    private void showFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit();

        fragmentContainer.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @androidx.annotation.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            handler.showAllStolenBikes(); // Refresh list if a bike was added
        }
    }
}