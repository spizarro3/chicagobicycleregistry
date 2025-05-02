package com.example.itmd_555_final.data.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.itmd_555_final.models.Bicycle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "BikeApp.db";
    private static final String BIKE_TABLE = "bicycle_table";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 2); // Bump version to 2
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE client_table (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "FIRSTNAME TEXT, " +
                "LASTNAME TEXT, " +
                "USERNAME TEXT, " +
                "PASSWORD TEXT)");

        db.execSQL("CREATE TABLE " + BIKE_TABLE + " (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "CLIENT_ID INTEGER, " + // <-- add CLIENT_ID
                "BIKE_ID INTEGER, " +
                "TITLE TEXT, " +
                "FRAME_MODEL TEXT, " +
                "DESCRIPTION TEXT, " +
                "SERIAL TEXT, " +
                "DATE_STOLEN INTEGER, " +
                "STOLEN_LOCATION TEXT, " +
                "LATITUDE REAL, " +
                "LONGITUDE REAL, " +
                "FRAME_COLORS TEXT" + // Stored as comma-separated string
                ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS client_table");
        db.execSQL("DROP TABLE IF EXISTS " + BIKE_TABLE);
        onCreate(db);
    }

    // Insert a bicycle into the database
    public boolean insertBicycle(Bicycle bike) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("CLIENT_ID", bike.clientId); // <-- Save client id
        values.put("BIKE_ID", bike.id);
        values.put("TITLE", bike.title != null ? bike.title : "Unknown");
        values.put("FRAME_MODEL", bike.frame_model != null ? bike.frame_model : "Unknown");
        values.put("DESCRIPTION", bike.description != null ? bike.description : "");
        values.put("SERIAL", bike.serial != null ? bike.serial : "");
        values.put("DATE_STOLEN", bike.date_stolen);
        values.put("STOLEN_LOCATION", bike.stolen_location != null ? bike.stolen_location : "");

        if (bike.stolen_coordinates != null && bike.stolen_coordinates.length == 2) {
            values.put("LATITUDE", bike.stolen_coordinates[0]);
            values.put("LONGITUDE", bike.stolen_coordinates[1]);
        } else {
            values.put("LATITUDE", 0.0);
            values.put("LONGITUDE", 0.0);
        }

        values.put("FRAME_COLORS", bike.frame_colors != null ? String.join(", ", bike.frame_colors) : "");

        long result = db.insert(BIKE_TABLE, null, values);
        return result != -1;
    }

    // Get all bicycles from the database
    public List<Bicycle> getAllBicycles() {
        List<Bicycle> bicycleList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + BIKE_TABLE, null);

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
                int clientId = cursor.getInt(cursor.getColumnIndexOrThrow("CLIENT_ID")); // <-- Read client id too

                List<String> frame_colors = Arrays.asList(frame_colors_string.split(", "));

                Bicycle bike = new Bicycle(frame_model, description, serial, date_stolen,
                        stolen_location, new double[]{lat, lon}, title, id, frame_colors, clientId); // <-- set client id

                bicycleList.add(bike);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return bicycleList;
    }
}
