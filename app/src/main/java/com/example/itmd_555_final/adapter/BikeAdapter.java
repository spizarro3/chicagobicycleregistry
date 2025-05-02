package com.example.itmd_555_final.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.view.*;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.itmd_555_final.R;
import com.example.itmd_555_final.activities.bike.IndividualStolenBikeActivity;
import com.example.itmd_555_final.data.repository.BikeRepository;
import com.example.itmd_555_final.models.Bicycle;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class BikeAdapter extends RecyclerView.Adapter<BikeAdapter.BikeViewHolder> implements Adapter {

    private final Context context;
    private List<Bicycle> bikeList;
    private boolean showDelete;
    private final BikeRepository repo;

    public BikeAdapter(@NonNull Context context, @NonNull List<Bicycle> bikeList, boolean showDelete) {
        this.context = context;
        this.bikeList = bikeList;
        this.showDelete = showDelete;
        this.repo = new BikeRepository(context);
    }

    @NonNull
    @Override
    public BikeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_bike, parent, false);
        return new BikeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BikeViewHolder holder, int position) {
        Bicycle bike = bikeList.get(position);

        holder.title.setText(bike.title != null ? bike.title : "No Title");
        holder.location.setText(bike.stolen_location != null ? bike.stolen_location : "Unknown Location");

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, IndividualStolenBikeActivity.class);
            intent.putExtra("bike_title", bike.getTitle() != null ? bike.getTitle() : "Unknown");
            intent.putExtra("bike_desc", bike.getDescription() != null ? bike.getDescription() : "No description");
            intent.putExtra("bike_location", bike.getStolen_location() != null ? bike.getStolen_location() : "Unknown");
            intent.putExtra("bike_serial", bike.getSerial() != null ? bike.getSerial() : "N/A");
            intent.putExtra("bike_date", bike.getDate_stolen());
            context.startActivity(intent);
        });

        if (showDelete && holder.btnDelete != null) {
            holder.btnDelete.setVisibility(View.VISIBLE);
            holder.btnDelete.setOnClickListener(v -> deleteBike(holder.getAdapterPosition(), holder));
        } else if (holder.btnDelete != null) {
            holder.btnDelete.setVisibility(View.GONE);
        }
    }

    private void deleteBike(int position, BikeViewHolder holder) {
        Bicycle deletedBike = bikeList.get(position);
        if (repo.deleteBicycleById(deletedBike.id)) {
            bikeList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, bikeList.size());

            Snackbar snackbar = Snackbar.make(holder.itemView, "Bike deleted", Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", v -> {
                repo.addBicycle(deletedBike);
                bikeList.add(position, deletedBike);
                notifyItemInserted(position);
                notifyItemRangeChanged(position, bikeList.size());
            });
            snackbar.show();
        } else {
            Snackbar.make(holder.itemView, "Failed to delete bike ❌", Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount() {
        return bikeList.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateList(List<Bicycle> newList) {
        this.bikeList = newList;
        notifyDataSetChanged();
    }

    public void setShowDelete(boolean showDelete) {
        this.showDelete = showDelete;
        notifyDataSetChanged();
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    public static class BikeViewHolder extends RecyclerView.ViewHolder {
        TextView title, location;
        Button btnDelete;

        public BikeViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.textBikeTitle);
            location = itemView.findViewById(R.id.textBikeLocation);
            btnDelete = itemView.findViewById(R.id.btnDeleteBike);
        }
    }
}
