package com.example.prm392_group3.activities.blog;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.prm392_group3.R;

public class NewsDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        // Get the news ID passed from the previous activity
        Intent intent = getIntent();
        String newsId = intent.getStringExtra("newsId");

        // Do something with the news ID (e.g., display it in a TextView)
        Toast.makeText(this, "News ID: " + newsId, Toast.LENGTH_SHORT).show();

    }
}