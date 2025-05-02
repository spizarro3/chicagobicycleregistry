package com.example.itmd_555_final.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.recyclerview.widget.RecyclerView;

import com.example.itmd_555_final.R;
import com.example.itmd_555_final.data.dao.ClientDao;
import com.example.itmd_555_final.models.Client;
import com.google.android.material.snackbar.Snackbar;

public class ShowProfileHelper {

    private final Context context;
    private final RecyclerView recyclerViewMain;
    private final Client currentClient;

    public ShowProfileHelper(Context context, RecyclerView recyclerViewMain, Client currentClient) {
        this.context = context;
        this.recyclerViewMain = recyclerViewMain;
        this.currentClient = currentClient;
    }

    public void showProfileOptions() {
        recyclerViewMain.setVisibility(View.GONE);

        View formView = LayoutInflater.from(context).inflate(R.layout.layout_profile_edit, (ViewGroup) recyclerViewMain.getParent(), false);
        ((ViewGroup) recyclerViewMain.getParent()).addView(formView);

        EditText editFirstName = formView.findViewById(R.id.editFirstName);
        EditText editLastName = formView.findViewById(R.id.editLastName);
        EditText editUsername = formView.findViewById(R.id.editUsername);
        EditText editPassword = formView.findViewById(R.id.editPassword);
        Button btnSaveProfile = formView.findViewById(R.id.btnSaveProfile);

        // Check if client is null
        if (currentClient == null) {
            // You can either show a message or just leave fields blank
            editFirstName.setText("");
            editLastName.setText("");
            editUsername.setText("");
            editPassword.setText("");
        } else {
            // Pre-fill client info
            editFirstName.setText(currentClient.firstName != null ? currentClient.firstName : "");
            editLastName.setText(currentClient.lastName != null ? currentClient.lastName : "");
            editUsername.setText(currentClient.username != null ? currentClient.username : "");
            editPassword.setText(currentClient.password != null ? currentClient.password : "");
        }

        btnSaveProfile.setOnClickListener(v -> saveProfile(editFirstName, editLastName, editUsername, editPassword, formView));
    }


    private void saveProfile(EditText firstNameField, EditText lastNameField, EditText usernameField, EditText passwordField, View formView) {
        String newFirstName = firstNameField.getText().toString().trim();
        String newLastName = lastNameField.getText().toString().trim();
        String newUsername = usernameField.getText().toString().trim();
        String newPassword = passwordField.getText().toString().trim();

        currentClient.firstName = newFirstName;
        currentClient.lastName = newLastName;
        currentClient.username = newUsername;
        currentClient.password = newPassword;

        ClientDao clientDao = new ClientDao(context);
        boolean success = clientDao.updateClient(currentClient);

        if (success) {
            SessionManager.setCurrentClient(currentClient);

            Snackbar.make(formView, "Profile updated successfully!", Snackbar.LENGTH_SHORT).show();

            // After saving, remove the form view and show RecyclerViewMain again
            ((ViewGroup) formView.getParent()).removeView(formView); // Remove the form
            recyclerViewMain.setVisibility(View.VISIBLE); // Show RecyclerView again


        } else {
            Snackbar.make(formView, "Failed to update profile.", Snackbar.LENGTH_SHORT).show();
        }
    }

}
