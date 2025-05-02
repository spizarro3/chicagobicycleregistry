package com.example.itmd_555_final.fragments;

import android.os.Bundle;
import android.view.*;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.*;

import com.example.itmd_555_final.R;
import com.example.itmd_555_final.data.repository.BikeRepository;
import com.example.itmd_555_final.models.Bicycle;
import com.example.itmd_555_final.adapter.BikeAdapter;

import java.util.*;

public class BicycleListFragment extends Fragment {

    private BikeAdapter adapter;
    private List<Bicycle> allBikes;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bike_list, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewAllBikes);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        SearchView searchView = view.findViewById(R.id.searchBikes);
        searchView.setSuggestionsAdapter(null);

        allBikes = new BikeRepository(requireContext()).getAllBicycles();

        adapter = new BikeAdapter(requireContext(), allBikes, false);
        recyclerView.setAdapter(adapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterBikes(newText);
                return true;
            }
        });

        return view;
    }

    private void filterBikes(String query) {
        List<Bicycle> filtered = new ArrayList<>();
        for (Bicycle b : allBikes) {
            if (b.getFrame_model() != null &&
                    b.getFrame_model().toLowerCase().contains(query.toLowerCase())) {
                filtered.add(b);
            }
        }
        adapter.updateList(filtered);
    }
}
