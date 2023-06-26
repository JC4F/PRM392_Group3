package com.example.prm392_group3.activities.store;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.prm392_group3.R;
import com.example.prm392_group3.adapters.BikeAdapter;
import com.example.prm392_group3.models.Bike;
import com.example.prm392_group3.models.User;
import com.example.prm392_group3.utils.ObjectStorageUtil;
import com.example.prm392_group3.utils.UserUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Store#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Store extends Fragment {
    User userDetails;
    DatabaseReference myRef;
    private List<Bike> bikeList;
    private int commentCount;
    private List<Integer> ratingList;
    BikeAdapter bikeAdapter;
    EditText editTextSearch;
    private FloatingActionButton addStoreFab;
    private TextView tvNotFound;
    public static ProgressBar loadingProgressBar;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    public Store() {
        // Required empty public constructor
    }

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
        userDetails = ObjectStorageUtil.loadObject(getContext(), "user_data.json", User.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        commentCount = 0;
        bikeList = new ArrayList<>();
        ratingList = new ArrayList<>();

        View view = inflater.inflate(R.layout.fragment_store, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.store_recycle_view);
        loadingProgressBar = view.findViewById(R.id.store_loading_progress);
        addStoreFab = view.findViewById(R.id.add_store_fab);
        editTextSearch = view.findViewById(R.id.editTextSearch);
        tvNotFound = view.findViewById(R.id.store_not_found);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        bikeAdapter = new BikeAdapter(getContext(), bikeList, commentCount, ratingList);
        recyclerView.setAdapter(bikeAdapter);

        myRef = FirebaseDatabase.getInstance().getReference("Bike");

        if (userDetails != null && !userDetails.isRole()) {
            addStoreFab.setVisibility(View.GONE);
        }

        searchBikeFromFirebase("");

        editTextSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE ||
                        (event != null && event.getAction() == KeyEvent.ACTION_DOWN &&
                                event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    // Xử lý sự kiện khi người dùng nhấn phím Enter
                    String searchText = editTextSearch.getText().toString().trim();
                    if (searchText.isEmpty()) {
                        // Tìm kiếm tất cả
                        searchBikeFromFirebase("");
                    } else {
                        // Tìm kiếm theo tên hoặc mô tả
                        searchBikeFromFirebase(searchText);
                    }
                    return true;
                }
                return false;
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

    private void searchBikeFromFirebase(String searchText) {
        tvNotFound.setVisibility(View.GONE);
        loadingProgressBar.setVisibility(View.VISIBLE);

        Query query;
        if (searchText.isEmpty()) {
            query = myRef;
        } else {
            query = myRef.orderByChild("name").startAt(searchText).endAt(searchText + "\uf8ff");
        }

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                bikeList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Bike bike = snapshot.getValue(Bike.class);
                    if (bike.getDescription().toLowerCase().contains(searchText.toLowerCase()) ||
                            bike.getName().toLowerCase().contains(searchText.toLowerCase())) {
                        bikeList.add(bike);
                    }
                }
                bikeAdapter.notifyDataSetChanged();

                loadingProgressBar.setVisibility(View.GONE);
                if(bikeList.size()==0) tvNotFound.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });
    }
}