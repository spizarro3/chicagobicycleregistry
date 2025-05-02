package com.example.itmd_555_final.data.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.itmd_555_final.data.database.DatabaseHelper;
import com.example.itmd_555_final.models.Bicycle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BicycleDao {
    private final DatabaseHelper dbHelper;

    public BicycleDao(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    // Insert a bicycle
    public boolean insertBicycle(Bicycle bike) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("CLIENT_ID", bike.clientId);
        values.put("BIKE_ID", bike.id);
        values.put("TITLE", bike.title != null ? bike.title : "Unknown");
        values.put("FRAME_MODEL", bike.frame_model != null ? bike.frame_model : "Unknown");
        values.put("DESCRIPTION", bike.description != null ? bike.description : "");
        values.put("SERIAL", bike.serial != null ? bike.serial : "");
        values.put("STOLEN_LOCATION", bike.stolen_location != null ? bike.stolen_location : "");

        if (bike.stolen_coordinates != null && bike.stolen_coordinates.length == 2) {
            values.put("LATITUDE", bike.stolen_coordinates[0]);
            values.put("LONGITUDE", bike.stolen_coordinates[1]);
        } else {
            values.put("LATITUDE", 0.0);
            values.put("LONGITUDE", 0.0);
        }

        values.put("FRAME_COLORS", bike.frame_colors != null ? String.join(", ", bike.frame_colors) : "");

        long result = db.insert("bicycle_table", null, values);
        return result != -1;
    }

    // Get all bicycles
    public List<Bicycle> getAllBicycles() {
        List<Bicycle> bicycleList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM bicycle_table", null);

        if (cursor.moveToFirst()) {
            do {
                String frame_model = cursor.getString(cursor.getColumnIndexOrThrow("FRAME_MODEL"));
                String description = cursor.getString(cursor.getColumnIndexOrThrow("DESCRIPTION"));
                String serial = cursor.getString(cursor.getColumnIndexOrThrow("SERIAL"));
                long date_stolen = cursor.getLong(cursor.getColumnIndexOrThrow("DATE_STOLEN"));
                String stolen_location = cursor.getString(cursor.getColumnIndexOrThrow("STOLEN_LOCATION"));
                double lat = cursor.getDouble(cursor.getColumnIndexOrThrow("LATITUDE"));
                double lon = cursor.getDouble(cursor.getColumnIndexOrThrow("LONGITUDE"));
                String title = cursor.getString(cursor.getColumnIndexOrThrow("TITLE"));
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("BIKE_ID"));
                String frame_colors_string = cursor.getString(cursor.getColumnIndexOrThrow("FRAME_COLORS"));
                int clientId = cursor.getInt(cursor.getColumnIndexOrThrow("CLIENT_ID"));

                List<String> frame_colors = Arrays.asList(frame_colors_string.split(", "));

                Bicycle bike = new Bicycle(frame_model, description, serial, date_stolen,
                        stolen_location, new double[]{lat, lon}, title, id, frame_colors, clientId);

                bicycleList.add(bike);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return bicycleList;
    }

    // Delete a bicycle
    public boolean deleteBicycle(int bikeId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int deletedRows = db.delete("bicycle_table", "BIKE_ID = ?", new String[]{String.valueOf(bikeId)});
        return deletedRows > 0;
    }
}
