package com.example.itmd_555_final.activities;

import android.content.Context;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.itmd_555_final.R;
import com.example.itmd_555_final.fragments.ProfileFragment;
import com.example.itmd_555_final.util.ShowProfileHelper;
import com.example.itmd_555_final.data.repository.BikeRepository;
import com.example.itmd_555_final.models.Bicycle;
import com.example.itmd_555_final.models.Client;
import com.example.itmd_555_final.adapter.BikeAdapter;
import com.example.itmd_555_final.adapter.ProfileFormAdapter;
import com.example.itmd_555_final.adapter.RegisterBikeFormAdapter;

import java.util.List;

public class MainActivityHandler {

    private final Context context;
    private final RecyclerView recyclerViewMain;
    private final Client currentClient;
    private final BikeRepository bikeRepository;
    private final ShowProfileHelper profileHelper;

    public MainActivityHandler(Context context, RecyclerView recyclerViewMain, Client currentClient) {
        this.context = context;
        this.recyclerViewMain = recyclerViewMain;
        this.currentClient = currentClient;
        this.bikeRepository = new BikeRepository(context);
        this.profileHelper = new ShowProfileHelper(context, recyclerViewMain, currentClient);
    }

    // Profile form
    public void showProfileOptions() {
        Fragment profileFragment = new ProfileFragment();
        ((AppCompatActivity) context).getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, profileFragment)
                .commit();
    }


    // All stolen bikes (no delete option)
    public void showAllStolenBikes() {
        List<Bicycle> bikeList = bikeRepository.getAllBicycles();
        setRecyclerViewAdapter(new BikeAdapter(context, bikeList, false));
    }

    // New bike registration form
    public void registerNewBike() {
        setRecyclerViewAdapter(new RegisterBikeFormAdapter(context));
    }

    // Only current user's bikes (with delete option)
    public void showMyStolenBikes() {
        List<Bicycle> myBikes = bikeRepository.getBicyclesByClientId(currentClient.id);
        setRecyclerViewAdapter(new BikeAdapter(context, myBikes, true));
    }

    // Helper method to clean up adapter setting
    private void setRecyclerViewAdapter(RecyclerView.Adapter<?> adapter) {
        recyclerViewMain.setAdapter(adapter);
        recyclerViewMain.setVisibility(View.VISIBLE);
    }
}
