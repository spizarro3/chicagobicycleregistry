package com.example.itmd_555_final.fragments;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.itmd_555_final.R;
import com.example.itmd_555_final.adapter.BikeAdapter;
import com.example.itmd_555_final.data.database.DatabaseHelper;
import com.example.itmd_555_final.models.Bicycle;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;

import java.util.ArrayList;
import java.util.List;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private List<Bicycle> bikeList;
    private List<Bicycle> visibleBikes;
    private BikeAdapter adapter;
    private RecyclerView recyclerView;

    public interface OnMapBoundsChangedListener {
        void onBoundsChanged(List<Bicycle> visibleBikes);
    }

    private OnMapBoundsChangedListener boundsListener;

    public void setOnMapBoundsChangedListener(OnMapBoundsChangedListener listener) {
        this.boundsListener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        // ✅ Init RecyclerView
        recyclerView = view.findViewById(R.id.recyclerViewFilteredBikes);

        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        visibleBikes = new ArrayList<>();
        adapter = new BikeAdapter(requireContext(), visibleBikes, false);
        recyclerView.setAdapter(adapter);

        // ✅ Load bike data
        DatabaseHelper dbHelper = new DatabaseHelper(requireContext());
        bikeList = dbHelper.getAllBicycles();

        // ✅ Load map
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
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(chicago, 11f));
        mMap.getUiSettings().setZoomControlsEnabled(true);

        Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_bike_marker);
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(originalBitmap, 80, 80, false);
        originalBitmap.recycle();

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
                        .icon(BitmapDescriptorFactory.fromBitmap(resizedBitmap)));
            }
        }

        // ✅ Update visible bikes when map stops moving
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

            // Show RecyclerView and update it
            recyclerView.setVisibility(View.VISIBLE);
            adapter.notifyDataSetChanged();

            if (boundsListener != null) {
                boundsListener.onBoundsChanged(new ArrayList<>(visibleBikes));
            }
        });
    }
}
