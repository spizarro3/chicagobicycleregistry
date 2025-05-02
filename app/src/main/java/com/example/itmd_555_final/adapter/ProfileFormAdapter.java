package com.example.itmd_555_final.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.itmd_555_final.R;
import com.example.itmd_555_final.data.dao.ClientDao;
import com.example.itmd_555_final.models.Client;
import com.example.itmd_555_final.util.SessionManager;

public class ProfileFormAdapter extends RecyclerView.Adapter<ProfileFormAdapter.ProfileFormViewHolder> {

    private final Context context;
    private final Client currentClient;

    public ProfileFormAdapter(Context context, Client client) {
        this.context = context;
        this.currentClient = client;
    }

    @NonNull
    @Override
    public ProfileFormViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_profile_edit, parent, false);
        return new ProfileFormViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileFormViewHolder holder, int position) {
        holder.editFirstName.setText(currentClient.firstName);
        holder.editLastName.setText(currentClient.lastName);
        holder.editUsername.setText(currentClient.username);
        holder.editPassword.setText(currentClient.password);

        holder.btnSaveProfile.setOnClickListener(v -> {
            // Save updated info
            String newFirstName = holder.editFirstName.getText().toString().trim();
            String newLastName = holder.editLastName.getText().toString().trim();
            String newUsername = holder.editUsername.getText().toString().trim();
            String newPassword = holder.editPassword.getText().toString().trim();

            currentClient.firstName = newFirstName;
            currentClient.lastName = newLastName;
            currentClient.username = newUsername;
            currentClient.password = newPassword;

            ClientDao clientDao = new ClientDao(context);
            boolean success = clientDao.updateClient(currentClient);

            if (success) {
                SessionManager.setCurrentClient(currentClient);
                Toast.makeText(context, "Profile updated successfully!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Failed to update profile.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return 1; // Only one profile form
    }

    static class ProfileFormViewHolder extends RecyclerView.ViewHolder {
        EditText editFirstName, editLastName, editUsername, editPassword;
        Button btnSaveProfile;

        public ProfileFormViewHolder(@NonNull View itemView) {
            super(itemView);
            editFirstName = itemView.findViewById(R.id.editFirstName);
            editLastName = itemView.findViewById(R.id.editLastName);
            editUsername = itemView.findViewById(R.id.editUsername);
            editPassword = itemView.findViewById(R.id.editPassword);
            btnSaveProfile = itemView.findViewById(R.id.btnSaveProfile);
        }
    }
}
