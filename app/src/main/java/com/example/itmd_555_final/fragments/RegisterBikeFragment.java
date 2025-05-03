package com.example.itmd_555_final.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.itmd_555_final.R;

import org.jetbrains.annotations.Nullable;

public class RegisterBikeFragment extends Fragment {

    public RegisterBikeFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_register_bike, container, false);
    }
}

