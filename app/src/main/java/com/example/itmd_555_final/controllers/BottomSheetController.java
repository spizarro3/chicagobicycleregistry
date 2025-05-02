package com.example.itmd_555_final.controllers;

import android.view.View;
import android.widget.Adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

public class BottomSheetController {
    private final View bottomSheet;
    private final RecyclerView recyclerView;
    private final BottomSheetBehavior<View> behavior;

    public BottomSheetController(View bottomSheet, RecyclerView recyclerView) {
        this.bottomSheet = bottomSheet;
        this.recyclerView = recyclerView;
        this.behavior = BottomSheetBehavior.from(bottomSheet);
    }

    public void updateRecyclerView(RecyclerView.Adapter<?> adapter) {
        recyclerView.setAdapter(adapter);
    }

    public void expand() {
        bottomSheet.setVisibility(View.VISIBLE);
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    public void collapse() {
        bottomSheet.setVisibility(View.VISIBLE);
        behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    public void hide() {
        bottomSheet.setVisibility(View.GONE);
    }
}

