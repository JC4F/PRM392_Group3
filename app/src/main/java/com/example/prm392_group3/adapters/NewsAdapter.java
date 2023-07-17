package com.example.prm392_group3.adapters;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.prm392_group3.R;
import com.example.prm392_group3.activities.MainActivity;
import com.example.prm392_group3.activities.blog.NewsDetail;
import com.example.prm392_group3.models.News;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.prm392_group3.models.News;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {
    private List<News> newsList;  // Assuming you have a list of NewsItem objects

    public NewsAdapter(List<News> newsList) {
        this.newsList = newsList;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news, parent, false);
        return new NewsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        News newsItem = newsList.get(position);

        // Bind the data to the views in the NewsViewHolder
        holder.newsTitle.setText(newsItem.getTitle());
        holder.newsDesc.setText(newsItem.getPostContent());
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM", Locale.getDefault());
        String formattedDate = dateFormat.format(newsItem.getPostDate());
        Picasso.get().load(newsItem.getImage()).into(holder.newsImage);
        holder.newsDate.setText(formattedDate);
        holder.newsSource.setText(newsItem.getCategory());
        holder.newsView.setText(newsItem.getHashtag());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open the detail page for the selected news item
                Context context = holder.itemView.getContext();
                Intent intent = new Intent(context, NewsDetail.class);
                intent.putExtra("newsId", newsItem.getPid()); // Pass any necessary data to the detail activity
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    // Inner class for the ViewHolder
    public class NewsViewHolder extends RecyclerView.ViewHolder {
        public ImageView newsImage;
        public TextView newsTitle;
        public TextView newsDesc;
        public TextView newsDate;
        public TextView newsSource;
        public TextView newsView;

        public NewsViewHolder(View itemView) {
            super(itemView);
            newsImage = itemView.findViewById(R.id.news_image);
            newsTitle = itemView.findViewById(R.id.news_title);
            newsDesc = itemView.findViewById(R.id.news_desc);
            newsDate = itemView.findViewById(R.id.news_date);
            newsSource = itemView.findViewById(R.id.news_source);
            newsView = itemView.findViewById(R.id.news_view);
        }
    }
}

