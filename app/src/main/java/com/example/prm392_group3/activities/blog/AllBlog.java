package com.example.prm392_group3.activities.blog;

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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.prm392_group3.activities.MainActivity;
import com.example.prm392_group3.models.News;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.prm392_group3.adapters.NewsAdapter;

import java.util.List;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AllBlog#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AllBlog extends Fragment {
    private RecyclerView recyclerView;
    private NewsAdapter adapter;
    private List<News> newsList;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

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
    public static AllBlog newInstance(String param1, String param2) {
        AllBlog fragment = new AllBlog();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_all_blog, container, false);

        recyclerView = view.findViewById(R.id.recy_news);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Create a list of news items
        newsList = new ArrayList<>();
        // Add your news items to the list
        // Example:
        newsList.add(new News(1, "2023-06-20", "Exciting News 1", "https://example.com/news1", "This is the content of News 1", "News Source 1", "https://example.com/source1", "news1_image.jpg"));
        newsList.add(new News(2, "2023-06-19", "Breaking News 2", "https://example.com/news2", "This is the content of News 2", "News Source 2", "https://example.com/source2", "news2_image.jpg"));
        newsList.add(new News(3, "2023-06-18", "Latest News 3", "https://example.com/news3", "This is the content of News 3", "News Source 3", "https://example.com/source3", "news3_image.jpg"));
        // ...

        adapter = new NewsAdapter(newsList);
        Log.i("info",""+recyclerView);
        recyclerView.setAdapter(adapter);

        return view;
    }
}