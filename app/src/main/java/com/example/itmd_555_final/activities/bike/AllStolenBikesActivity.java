package com.example.itmd_555_final.activities.bike;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.itmd_555_final.R;
import com.example.itmd_555_final.adapter.BikeAdapter;
import com.example.itmd_555_final.data.database.DatabaseHelper;
import com.example.itmd_555_final.models.Bicycle;

import java.util.List;

public class AllStolenBikesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private BikeAdapter adapter;
    private List<Bicycle> bikeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allstolenbikes);

        recyclerView = findViewById(R.id.recyclerViewBikes);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        bikeList = dbHelper.getAllBicycles();

        // Create adapter with delete buttons visible
        adapter = new BikeAdapter(this, bikeList, true);
        recyclerView.setAdapter(adapter);
    }
}
