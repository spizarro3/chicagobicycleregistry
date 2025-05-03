package com.example.itmd_555_final.fragments;

import android.os.Bundle;
import android.view.*;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.itmd_555_final.R;
import com.example.itmd_555_final.data.dao.ClientDao;
import com.example.itmd_555_final.models.Client;
import com.example.itmd_555_final.util.SessionManager;
import com.google.android.material.snackbar.Snackbar;

public class ProfileFragment extends Fragment {

    private Client currentClient;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_profile_edit, container, false);

        currentClient = SessionManager.getCurrentClient();

        EditText editFirstName = view.findViewById(R.id.editFirstName);
        EditText editLastName = view.findViewById(R.id.editLastName);
        EditText editUsername = view.findViewById(R.id.editUsername);
        EditText editPassword = view.findViewById(R.id.editPassword);
        Button btnSaveProfile = view.findViewById(R.id.btnSaveProfile);

        if (currentClient != null) {
            editFirstName.setText(currentClient.firstName);
            editLastName.setText(currentClient.lastName);
            editUsername.setText(currentClient.username);
            editPassword.setText(currentClient.password);
        }

        btnSaveProfile.setOnClickListener(v -> {
            currentClient.firstName = editFirstName.getText().toString().trim();
            currentClient.lastName = editLastName.getText().toString().trim();
            currentClient.username = editUsername.getText().toString().trim();
            currentClient.password = editPassword.getText().toString().trim();

            boolean success = new ClientDao(requireContext()).updateClient(currentClient);
            if (success) {
                SessionManager.setCurrentClient(currentClient);
                Snackbar.make(view, "Profile updated!", Snackbar.LENGTH_SHORT).show();
            } else {
                Snackbar.make(view, "Update failed.", Snackbar.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}
