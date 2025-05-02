package com.example.itmd_555_final.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.itmd_555_final.R;
import com.example.itmd_555_final.activities.client.LoginActivity;
import com.example.itmd_555_final.adapter.BikeAdapter;
import com.example.itmd_555_final.controllers.BottomSheetController;
import com.example.itmd_555_final.data.repository.BikeRepository;
import com.example.itmd_555_final.fragments.BicycleListFragment;
import com.example.itmd_555_final.fragments.MapFragment;
import com.example.itmd_555_final.models.Bicycle;
import com.example.itmd_555_final.models.Client;
import com.example.itmd_555_final.util.SessionManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private MainActivityHandler handler;
    private BottomSheetController bottomSheetController;
    private BottomSheetBehavior<View> bottomSheetBehavior;
    private View bottomSheet;
    private RecyclerView recyclerView;
    private View fragmentContainer;

    @SuppressLint("NonConstantResourceId")
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

        // Setup views
        bottomSheet = findViewById(R.id.bottomSheet);
        recyclerView = findViewById(R.id.recyclerViewMapBikes);
        fragmentContainer = findViewById(R.id.fragmentContainer);

        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        bottomSheetBehavior.setHideable(false); // Optional: prevent full hiding
        bottomSheetBehavior.setPeekHeight(400); // Starting visible height
        bottomSheetBehavior.setDraggable(true); // Allow drag to expand/collapse
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

        bottomSheetController = new BottomSheetController(bottomSheet, recyclerView);
        bottomSheetController.updateRecyclerView(new BikeAdapter(this, new ArrayList<>(), false));
        bottomSheetController.collapse();

        handler = new MainActivityHandler(this, recyclerView, currentClient);

        BottomNavigationView nav = findViewById(R.id.bottomNavigationView);
        nav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_map) {
                MapFragment mapFragment = new MapFragment();

                mapFragment.setOnMapBoundsChangedListener(filteredBikes -> {
                    runOnUiThread(() -> {
                        bottomSheetController.updateRecyclerView(new BikeAdapter(this, filteredBikes, false));
                        bottomSheetController.collapse();
                    });
                });

                showFragment(mapFragment);
                return true;

            } else if (itemId == R.id.nav_list) {
                bottomSheetController.hide();
                showFragment(new BicycleListFragment());
                return true;

            } else if (itemId == R.id.nav_add) {
                handler.registerNewBike();
                fragmentContainer.setVisibility(View.GONE);
                bottomSheetController.hide();
                return true;

            } else if (itemId == R.id.nav_profile) {
                handler.showProfileOptions();
                fragmentContainer.setVisibility(View.GONE);
                bottomSheetController.hide();
                return true;
            }

            return false;
        });

        // Default view
        nav.setSelectedItemId(R.id.nav_map);
    }

    private void showFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit();

        fragmentContainer.setVisibility(View.VISIBLE);
        bottomSheetController.collapse();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @androidx.annotation.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            handler.showAllStolenBikes(); // Refresh list if a bike was added
        }
    }
}
