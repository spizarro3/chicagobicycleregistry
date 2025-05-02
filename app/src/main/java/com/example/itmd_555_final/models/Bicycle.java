package com.example.itmd_555_final.models;

import java.util.List;

public class Bicycle {
    public String frame_model;
    public String description;
    public String serial;
    public long date_stolen;
    public String stolen_location;
    public double[] stolen_coordinates;
    public String title;
    public int id;
    public List<String> frame_colors;
    public int clientId;

    // Required empty constructor (good for Gson, Firebase, etc.)
    public Bicycle() {
    }

    // Full constructor
    public Bicycle(String frame_model, String description, String serial, long date_stolen,
                   String stolen_location, double[] stolen_coordinates, String title, int id,
                   List<String> frame_colors, int clientId) {
        this.frame_model = frame_model;
        this.description = description;
        this.serial = serial;
        this.date_stolen = date_stolen;
        this.stolen_location = stolen_location;
        this.stolen_coordinates = stolen_coordinates;
        this.title = title;
        this.id = id;
        this.frame_colors = frame_colors;
        this.clientId = clientId;
    }

    // Getters
    public String getFrame_model() {
        return frame_model;
    }

    public String getDescription() {
        return description != null ? description : "";
    }

    public String getSerial() {
        return serial;
    }

    public long getDate_stolen() {
        return date_stolen;
    }

    public String getStolen_location() {
        return stolen_location;
    }

    public double[] getStolen_coordinates() {
        return stolen_coordinates;
    }

    public String getTitle() {
        return title;
    }

    public int getId() {
        return id;
    }

    public List<String> getFrame_colors() {
        return frame_colors;
    }

    public int getClientId() {
        return clientId;
    }
}
