package com.example.prm392_group3.activities.store;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.prm392_group3.R;
import com.example.prm392_group3.adapters.BikeAdapter;
import com.example.prm392_group3.models.Bike;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Store#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Store extends Fragment {

    DatabaseReference myRef;
    private List<Bike> bikeList;
    private int commentCount;
    private List<Integer> ratingList;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Store() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Store.
     */
    // TODO: Rename and change types and number of parameters
    public static Store newInstance(String param1, String param2) {
        Store fragment = new Store();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        commentCount=0;
        bikeList = new ArrayList<>();
        ratingList = new ArrayList<>();

        View view = inflater.inflate(R.layout.fragment_store, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.store_recycle_view);
        ProgressBar loadingProgressBar = view.findViewById(R.id.store_loading_progress);
        FloatingActionButton addStoreFab = view.findViewById(R.id.add_store_fab);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        BikeAdapter bikeAdapter = new BikeAdapter(getContext(), bikeList, commentCount, ratingList);
        recyclerView.setAdapter(bikeAdapter);

        myRef = FirebaseDatabase.getInstance().getReference("Bike");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                loadingProgressBar.setVisibility(View.VISIBLE);

                bikeList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Bike bike = snapshot.getValue(Bike.class);
                    bikeList.add(bike);
                }
                bikeAdapter.notifyDataSetChanged();

                loadingProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });


        addStoreFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddOrUpddateBike.class);
                startActivity(intent);
            }
        });

        return view;
    }
}