package com.example.itmd_555_final.activities.map;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.example.itmd_555_final.R;
import com.example.itmd_555_final.data.database.DatabaseHelper;
import com.example.itmd_555_final.models.Bicycle;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class MapViewActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private List<Bicycle> bikeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_view);

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        bikeList = dbHelper.getAllBicycles();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
        Button btnBackHome = findViewById(R.id.btnBackHome);
        btnBackHome.setOnClickListener(v -> {
            finish(); // Close map and return to MainActivity
        });

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        // Resize bike icon
        Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_bike_marker);
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(originalBitmap, 80, 80, false);

        // Move camera to Chicago
        LatLng chicago = new LatLng(41.8781, -87.6298);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(chicago, 11f));
        mMap.getUiSettings().setZoomControlsEnabled(true);

        // Add markers for all stolen bikes
        for (Bicycle bike : bikeList) {
            if (bike.stolen_coordinates != null && bike.stolen_coordinates.length == 2) {
                LatLng position = new LatLng(
                        bike.stolen_coordinates[0],
                        bike.stolen_coordinates[1]
                );
                mMap.addMarker(new MarkerOptions()
                        .position(position)
                        .title(bike.title != null ? bike.title : "Unknown Bike")
                        .snippet(bike.frame_model != null ? bike.frame_model : "No details")
                        .icon(BitmapDescriptorFactory.fromBitmap(resizedBitmap))
                );
            }
        }
    }
}
