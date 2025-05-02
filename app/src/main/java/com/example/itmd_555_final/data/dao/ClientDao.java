package com.example.itmd_555_final.data.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.itmd_555_final.data.database.DatabaseHelper;
import com.example.itmd_555_final.models.Client;

public class ClientDao {
    private final DatabaseHelper dbHelper;

    public ClientDao(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public boolean insertClient(Client client) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("FIRSTNAME", client.firstName);
        values.put("LASTNAME", client.lastName);
        values.put("USERNAME", client.username);
        values.put("PASSWORD", client.password);

        long result = db.insert("client_table", null, values);
        return result != -1;
    }
    public boolean validateLogin(String username, String password) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM client_table WHERE USERNAME = ? AND PASSWORD = ?",
                new String[]{username, password});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    public Client getClientByUsername(String username) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM client_table WHERE USERNAME = ?", new String[]{username});

        if (cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("ID"));
            String firstName = cursor.getString(cursor.getColumnIndexOrThrow("FIRSTNAME"));
            String lastName = cursor.getString(cursor.getColumnIndexOrThrow("LASTNAME"));
            String password = cursor.getString(cursor.getColumnIndexOrThrow("PASSWORD"));

            cursor.close();

            Client client = new Client(firstName, lastName, username, password, null);
            client.id = id; // Don't forget to set ID!
            return client;
        } else {
            cursor.close();
            return null;
        }
    }
    public boolean updateClient(Client client) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("FIRSTNAME", client.firstName);
        values.put("LASTNAME", client.lastName);
        values.put("USERNAME", client.username);
        values.put("PASSWORD", client.password);

        int rows = db.update("client_table", values, "ID = ?", new String[]{String.valueOf(client.id)});
        return rows > 0;
    }

}
