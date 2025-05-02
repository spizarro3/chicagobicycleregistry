package com.example.itmd_555_final.util;

import com.example.itmd_555_final.models.Client;

public class SessionManager {
    private static Client currentClient;

    public static void setCurrentClient(Client client) {
        currentClient = client;
    }

    public static Client getCurrentClient() {
        return currentClient;
    }
}
