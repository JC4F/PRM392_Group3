package com.example.prm392_group3.activities.blog;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.prm392_group3.R;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.prm392_group3.activities.MainActivity;
import com.example.prm392_group3.activities.orders.Order;
import com.example.prm392_group3.models.Bike;
import com.example.prm392_group3.models.News;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.example.prm392_group3.adapters.NewsAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AllBlog#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AllBlog extends Fragment {
    private RecyclerView recyclerView;
    private NewsAdapter adapter;
    private List<News> newsList;
    private EditText etQuery;
    private Button btnSearch;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private DatabaseReference fbDatabase;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AllBlog() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AllBlog.
     */
    // TODO: Rename and change types and number of parameters
    @NonNull
    public static AllBlog newInstance(String param1, String param2) {
        AllBlog fragment = new AllBlog();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    private boolean isDataLoaded = false;
    DatabaseReference newsRef;
    private FloatingActionButton addNewsButton;

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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_all_blog, container, false);
        etQuery = view.findViewById(R.id.etQuery);
        btnSearch = view.findViewById(R.id.btnSearch);
        ImageView searchIcon = view.findViewById(R.id.searchIcon);
        GridLayout searchField = view.findViewById(R.id.searchField);
        searchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (searchField.getVisibility() == View.VISIBLE) {
                    searchField.setVisibility(View.GONE); // Hide searchField
                } else {
                    searchField.setVisibility(View.VISIBLE); // Show searchField
                }
            }
        });

        addNewsButton = view.findViewById(R.id.btnInsert);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = etQuery.getText().toString().trim();
                searchNews(query);
            }
        });

        addNewsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start CreateNewsActivity
                Intent intent = new Intent(getActivity(), CreateNewsActivity.class);
                startActivity(intent);
            }

        });


        recyclerView = view.findViewById(R.id.recy_news);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        newsRef = FirebaseDatabase.getInstance().getReference("News");

        loadNewsData();

        // Create a list of news items
        newsList = new ArrayList<>();
        // Add your news items to the list
        // Example:
        Calendar calendar = Calendar.getInstance();
        Date currentDate = calendar.getTime();

//        newsList.add(new News("1", currentDate, "Exciting News 1", "https://example.com/news1", "This is the content of News 1", "News Source 1", "https://example.com/source1", "news1_image.jpg"));
//        newsList.add(new News("2", currentDate, "Breaking News 2", "https://example.com/news2", "This is the content of News 2", "News Source 2", "https://example.com/source2", "news2_image.jpg"));
//        newsList.add(new News("3", currentDate, "Latest News 3", "https://example.com/news3", "This is the content of News 3", "News Source 3", "https://example.com/source3", "news3_image.jpg"));
        // ...

        adapter = new NewsAdapter(newsList, getContext());
        Log.i("info",""+recyclerView);
        recyclerView.setAdapter(adapter);

        return view;
    }

    private void searchNews(String query) {
        // Perform the search operation based on the query
        Query searchQuery = newsRef.orderByChild("title").startAt(query).endAt(query + "\uf8ff");

        searchQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                newsList.clear(); // Clear the existing newsList before adding new data

                for (DataSnapshot newsSnapshot : dataSnapshot.getChildren()) {
                    News news = newsSnapshot.getValue(News.class);
                    if (news != null) {
                        newsList.add(news);
                    }
                }

                adapter.notifyDataSetChanged(); // Notify the adapter that the data has changed
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any errors that occur during data retrieval
                Log.e("searchNews", "Failed to search news: " + databaseError.getMessage());
                Toast.makeText(getActivity(), "Failed to search news", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadNewsData() {
        Query query;
            query = newsRef;
        newsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                newsList.clear(); // Clear the existing newsList before adding new data

                for (DataSnapshot newsSnapshot : dataSnapshot.getChildren()) {
                    News news = newsSnapshot.getValue(News.class);
                    if (news != null) {
                        newsList.add(news);
                    }
                }

                adapter.notifyDataSetChanged(); // Notify the adapter that the data has changed
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any errors that occur during data retrieval
                Log.e("loadNewsData", "Failed to load news data: " + databaseError.getMessage());
                Toast.makeText(getActivity(), "Failed to load news data", Toast.LENGTH_SHORT).show();
            }
        });


    }


}