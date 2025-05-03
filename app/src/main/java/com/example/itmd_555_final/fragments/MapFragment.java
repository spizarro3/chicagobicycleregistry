package com.example.itmd_555_final.fragments;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.itmd_555_final.R;
import com.example.itmd_555_final.adapter.BikeAdapter;
import com.example.itmd_555_final.data.database.DatabaseHelper;
import com.example.itmd_555_final.models.Bicycle;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.ArrayList;
import java.util.List;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private List<Bicycle> bikeList;
    private List<Bicycle> visibleBikes;
    private BikeAdapter adapter;
    private BottomSheetBehavior<View> bottomSheetBehavior;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        View bottomSheet = view.findViewById(R.id.bottomSheet);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewMapBikes);

        if (bottomSheet != null) {
            bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
            bottomSheetBehavior.setPeekHeight(900);
            bottomSheetBehavior.setHideable(false);
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        visibleBikes = new ArrayList<>();
        adapter = new BikeAdapter(requireContext(), visibleBikes, false);
        recyclerView.setAdapter(adapter);

        DatabaseHelper dbHelper = new DatabaseHelper(requireContext());
        bikeList = dbHelper.getAllBicycles();

        SupportMapFragment mapFragment = (SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        return view;
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        LatLng chicago = new LatLng(41.8781, -87.6298);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(chicago, 13f));
        mMap.getUiSettings().setZoomControlsEnabled(true);

        // Load vector drawable and render to bitmap
        Drawable drawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_bike_marker);
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        // Resize bitmap to a smaller icon (e.g., 36x36 px)
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, 42, 42, false);
        BitmapDescriptor icon = BitmapDescriptorFactory.fromBitmap(scaledBitmap);

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
                        .icon(icon));
            }
        }

        mMap.setOnCameraIdleListener(() -> {
            LatLngBounds bounds = mMap.getProjection().getVisibleRegion().latLngBounds;
            visibleBikes.clear();

            for (Bicycle bike : bikeList) {
                if (bike.stolen_coordinates != null && bike.stolen_coordinates.length == 2) {
                    LatLng pos = new LatLng(bike.stolen_coordinates[0], bike.stolen_coordinates[1]);
                    if (bounds.contains(pos)) {
                        visibleBikes.add(bike);
                    }
                }
            }

            adapter.notifyDataSetChanged();

            if (!visibleBikes.isEmpty() && bottomSheetBehavior != null) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });
    }

}
