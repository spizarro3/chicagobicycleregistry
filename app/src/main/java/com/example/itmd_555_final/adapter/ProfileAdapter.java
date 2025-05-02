package com.example.itmd_555_final.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.itmd_555_final.R;

import java.util.List;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.OptionViewHolder> {

    public interface OnOptionClickListener {
        void onOptionClick(String option);
    }

    private final List<String> options;
    private final Context context;
    private final OnOptionClickListener listener;

    public ProfileAdapter(Context context, List<String> options, OnOptionClickListener listener) {
        this.context = context;
        this.options = options;
        this.listener = listener;
    }

    @NonNull
    @Override
    public OptionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_profile_option, parent, false);
        return new OptionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OptionViewHolder holder, int position) {
        String option = options.get(position);
        holder.textOption.setText(option);
        holder.itemView.setOnClickListener(v -> listener.onOptionClick(option));
    }

    @Override
    public int getItemCount() {
        return options.size();
    }

    public static class OptionViewHolder extends RecyclerView.ViewHolder {
        TextView textOption;

        public OptionViewHolder(@NonNull View itemView) {
            super(itemView);
            textOption = itemView.findViewById(R.id.textProfileOption);
        }
    }
}
