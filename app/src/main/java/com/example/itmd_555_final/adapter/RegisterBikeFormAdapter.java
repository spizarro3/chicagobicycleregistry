package com.example.itmd_555_final.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.itmd_555_final.R;
import com.example.itmd_555_final.data.database.DatabaseHelper;
import com.example.itmd_555_final.models.Bicycle;
import com.example.itmd_555_final.util.SessionManager;

import java.util.Arrays;
import java.util.List;

public class RegisterBikeFormAdapter extends RecyclerView.Adapter<RegisterBikeFormAdapter.FormViewHolder> {

    private final Context context;

    public RegisterBikeFormAdapter(Context context) {
        this.context = context;
    }

    @Override
    public FormViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_register_bike, parent, false);
        return new FormViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FormViewHolder holder, int position) {
        holder.btnSubmitBike.setOnClickListener(v -> {
            String frameModel = holder.inputFrameModel.getText().toString().trim();
            String description = holder.inputDescription.getText().toString().trim();
            String serial = holder.inputSerial.getText().toString().trim();
            String zipCode = holder.inputZipCode.getText().toString().trim();
            String title = holder.inputTitle.getText().toString().trim();
            String frameColorsStr = holder.inputFrameColors.getText().toString().trim();

            if (frameModel.isEmpty() || serial.isEmpty() || zipCode.isEmpty()) {
                Toast.makeText(context, "Please fill required fields", Toast.LENGTH_SHORT).show();
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

            DatabaseHelper db = new DatabaseHelper(context);
            boolean success = db.insertBicycle(newBike);

            if (success) {
                Toast.makeText(context, "Bike registered successfully!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Failed to register bike.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return 1; // Only one form
    }

    public static class FormViewHolder extends RecyclerView.ViewHolder {
        EditText inputFrameModel, inputDescription, inputSerial, inputZipCode, inputTitle, inputFrameColors;
        Button btnSubmitBike;

        public FormViewHolder(View itemView) {
            super(itemView);
            inputFrameModel = itemView.findViewById(R.id.inputFrameModel);
            inputDescription = itemView.findViewById(R.id.inputDescription);
            inputSerial = itemView.findViewById(R.id.inputSerial);
            inputZipCode = itemView.findViewById(R.id.inputZipCode);
            inputTitle = itemView.findViewById(R.id.inputTitle);
            inputFrameColors = itemView.findViewById(R.id.inputFrameColors);
            btnSubmitBike = itemView.findViewById(R.id.btnSubmitBike);
        }
    }

    // Dummy zip lookup
    private double[] getLatLonFromZip(String zip) {
        switch (zip) {
            case "60616": return new double[]{41.8474, -87.6260};
            case "60601": return new double[]{41.8864, -87.6231};
            case "10001": return new double[]{40.7128, -74.0060};
            default: return new double[]{0.0, 0.0};
        }
    }
}
