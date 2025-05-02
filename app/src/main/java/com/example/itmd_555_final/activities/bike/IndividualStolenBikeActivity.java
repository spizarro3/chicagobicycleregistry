package com.example.itmd_555_final.activities.bike;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.itmd_555_final.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class IndividualStolenBikeActivity extends AppCompatActivity {

    TextView bikeTitle, bikeDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individualstolenbikes);

        bikeTitle = findViewById(R.id.bikeTitle);
        bikeDetails = findViewById(R.id.bikeDetails);

        // Safely get extras from Intent
        String title = getIntent().getStringExtra("bike_title");
        String desc = getIntent().getStringExtra("bike_desc");
        String location = getIntent().getStringExtra("bike_location");
        String serial = getIntent().getStringExtra("bike_serial");
        long dateStolen = getIntent().getLongExtra("bike_date", 0);

        // Convert epoch to readable date
        String formattedDate = "Unknown";
        if (dateStolen > 0) {
            Date date = new Date(dateStolen * 1000); // if it's epoch seconds
            formattedDate = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(date);
        }

        String details = "Description: " + (desc != null ? desc : "N/A") + "\n"
                + "Location: " + (location != null ? location : "N/A") + "\n"
                + "Serial: " + (serial != null ? serial : "N/A") + "\n"
                + "Date Stolen: " + formattedDate;

        bikeTitle.setText(title != null ? title : "Untitled Bike");
        bikeDetails.setText(details);
    }
}
