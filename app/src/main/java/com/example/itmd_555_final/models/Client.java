package com.example.itmd_555_final.models;

import java.util.List;

public class Client {
    public int id;
    public String firstName;
    public String lastName;
    public String username;
    public String password;
    public List<Bicycle> bicycles;

    public Client(String firstName, String lastName, String username, String password, List<Bicycle> bicycles) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.bicycles = bicycles;
    }
}

