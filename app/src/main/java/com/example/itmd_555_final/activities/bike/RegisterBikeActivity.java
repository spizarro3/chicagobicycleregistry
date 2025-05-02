package com.example.itmd_555_final.activities.bike;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.itmd_555_final.R;
import com.example.itmd_555_final.data.database.DatabaseHelper;
import com.example.itmd_555_final.models.Bicycle;
import com.example.itmd_555_final.util.SessionManager;

import java.util.Arrays;
import java.util.List;

public class RegisterBikeActivity extends AppCompatActivity {

    private EditText inputFrameModel, inputDescription, inputSerial, inputZipCode, inputTitle, inputFrameColors;
    private Button btnSubmitBike;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_bike);

        initViews();
        btnSubmitBike.setOnClickListener(v -> submitBike());
    }

    private void initViews() {
        inputFrameModel = findViewById(R.id.inputFrameModel);
        inputDescription = findViewById(R.id.inputDescription);
        inputSerial = findViewById(R.id.inputSerial);
        inputZipCode = findViewById(R.id.inputZipCode);
        inputTitle = findViewById(R.id.inputTitle);
        inputFrameColors = findViewById(R.id.inputFrameColors);
        btnSubmitBike = findViewById(R.id.btnSubmitBike);
    }

    private void submitBike() {
        String frameModel = inputFrameModel.getText().toString().trim();
        String description = inputDescription.getText().toString().trim();
        String serial = inputSerial.getText().toString().trim();
        String zipCode = inputZipCode.getText().toString().trim();
        String title = inputTitle.getText().toString().trim();
        String frameColorsStr = inputFrameColors.getText().toString().trim();

        if (frameModel.isEmpty() || serial.isEmpty() || zipCode.isEmpty()) {
            Toast.makeText(this, "Please fill required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        double[] latLon = getLatLonFromZip(zipCode);
        List<String> frameColors = Arrays.asList(frameColorsStr.split(",\\s*"));

        Bicycle newBike = new Bicycle(
                frameModel,
                description,
                serial,
                System.currentTimeMillis(),
                zipCode,
                latLon,
                title,
                SessionManager.getCurrentClient().id,
                frameColors,
                SessionManager.getCurrentClient().id
        );

        boolean success = new DatabaseHelper(this).insertBicycle(newBike);

        if (success) {
            Toast.makeText(this, "Bike registered successfully!", Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK);
            finish();
        } else {
            Toast.makeText(this, "Failed to register bike.", Toast.LENGTH_SHORT).show();
        }
    }

    private double[] getLatLonFromZip(String zip) {
        switch (zip) {
            case "60616": return new double[]{41.8474, -87.6260};
            case "60601": return new double[]{41.8864, -87.6231};
            case "10001": return new double[]{40.7128, -74.0060};
            default: return new double[]{0.0, 0.0};
        }
    }
}
