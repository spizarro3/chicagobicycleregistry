package com.example.itmd_555_final.data.repository;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.itmd_555_final.api.ApiClient;
import com.example.itmd_555_final.api.BikeIndexApi;
import com.example.itmd_555_final.data.dao.BicycleDao;
import com.example.itmd_555_final.models.Bicycle;
import com.example.itmd_555_final.models.BicycleCollection;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BikeRepository {

    private final BicycleDao bicycleDao;
    private final BikeIndexApi api;
    private List<Bicycle> cachedBicycles = null;

    public BikeRepository(Context context) {
        bicycleDao = new BicycleDao(context);
        api = ApiClient.getClient().create(BikeIndexApi.class);
    }

    // API Fetch
    public void fetchChicagoBikes() {
        for (int page = 1; page <= 5; page++) {
            Call<BicycleCollection> call = api.getStolenBikes("Chicago", "proximity", 100, page);

            call.enqueue(new Callback<BicycleCollection>() {
                @Override
                public void onResponse(@NonNull Call<BicycleCollection> call, @NonNull Response<BicycleCollection> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        for (Bicycle bike : response.body().bikes) {
                            bicycleDao.insertBicycle(bike);
                        }
                        Log.d("BikeRepo", "Bicycles inserted into database");
                        cachedBicycles = null; // Invalidate cache after new inserts
                    }
                }

                @Override
                public void onFailure(@NonNull Call<BicycleCollection> call, @NonNull Throwable t) {
                    Log.e("BikeRepo", "Error fetching bikes: " + t.getMessage());
                }
            });
        }
    }

    // Get all bicycles
    public List<Bicycle> getAllBicycles() {
        if (cachedBicycles == null) {
            cachedBicycles = bicycleDao.getAllBicycles();
        }
        return cachedBicycles;
    }

    // Get bicycles by clientId
    public List<Bicycle> getBicyclesByClientId(int clientId) {
        cachedBicycles = bicycleDao.getAllBicycles(); // <--- Always refresh
        List<Bicycle> clientBikes = new ArrayList<>();
        for (Bicycle bike : cachedBicycles) {
            if (bike.clientId == clientId) {
                clientBikes.add(bike);
            }
        }
        return clientBikes;
    }

    // Add new bicycle
    public boolean addBicycle(Bicycle bike) {
        boolean success = bicycleDao.insertBicycle(bike);
        if (success && cachedBicycles != null) {
            cachedBicycles.add(bike);
        }
        return success;
    }

    // Delete bicycle
    public boolean deleteBicycleById(int bikeId) {
        boolean success = bicycleDao.deleteBicycle(bikeId);
        if (success && cachedBicycles != null) {
            cachedBicycles.removeIf(bike -> bike.id == bikeId);
        }
        return success;
    }
    public List<Bicycle> getBikesInBounds(LatLngBounds bounds) {
        List<Bicycle> inBounds = new ArrayList<>();
        List<Bicycle> allBikes = getAllBicycles(); // from cache or DB

        for (Bicycle bike : allBikes) {
            if (bike.stolen_coordinates != null && bike.stolen_coordinates.length == 2) {
                double lat = bike.stolen_coordinates[0];
                double lng = bike.stolen_coordinates[1];
                if (bounds.contains(new com.google.android.gms.maps.model.LatLng(lat, lng))) {
                    inBounds.add(bike);
                }
            }
        }

        return inBounds;
    }
}
