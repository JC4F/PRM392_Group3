package com.example.prm392_group3.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_group3.R;
import com.example.prm392_group3.activities.store.AddOrUpddateBike;
import com.example.prm392_group3.models.Bike;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;

public class BikeAdapter extends RecyclerView.Adapter<BikeAdapter.ViewHolder> {

    private List<Bike> bikeList;
    private int commentCount;
    private List<Integer> ratingList;
    private Context context;

    AppCompatButton updateBtn;

    public BikeAdapter(Context context, List<Bike> bikeList, int commentCount, List<Integer> ratingList) {
        this.context = context;
        this.bikeList = bikeList;
        this.commentCount = commentCount;
        this.ratingList = ratingList;
    }

    @NonNull
    @Override
    public BikeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate layout cho mỗi item trong danh sách
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.store_bike_item, parent, false);
        updateBtn = view.findViewById(R.id.store_update_btn);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BikeAdapter.ViewHolder holder, int position) {
        // Lấy dữ liệu từ danh sách dựa trên vị trí position
        Bike bike = bikeList.get(position);

        // Load hình ảnh từ URL và hiển thị nó trong ImageView
        Picasso.get().load(bike.getImageUrl()).into(holder.bikeImage);
        holder.bikeName.setText(bike.getName());
        holder.remainQuantity.setText("Remain quantity: " + bike.getQuantity());
        holder.ratingsCount.setText(String.format("%.1f (%d ratings)", calculateAverageRating(), ratingList.size()));
        holder.commentsCount.setText(String.valueOf(commentCount));
        holder.bikeStatus.setText(bike.isAvailable() ? "Available" : "Unavailable");

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Chuyển sang trang mới và truyền mô hình Bike qua Intent
                Intent intent = new Intent(context, AddOrUpddateBike.class);
                intent.putExtra("Bike", bike);
                context.startActivity(intent);
            }
        });
    }

    private float calculateAverageRating() {
        int sum = 0;
        if(ratingList.size()==0) return 0;
        for (int rating : ratingList) {
            sum += rating;
        }
        return (float) sum / ratingList.size();
    }

    @Override
    public int getItemCount() {
        return bikeList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView bikeImage;
        TextView bikeName;
        TextView remainQuantity;
        TextView ratingsCount;
        TextView commentsCount;
        TextView bikeStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            bikeImage = itemView.findViewById(R.id.store_item_image);
            bikeName = itemView.findViewById(R.id.store_bike_name);
            remainQuantity = itemView.findViewById(R.id.store_bike_remain_quantity);
            ratingsCount = itemView.findViewById(R.id.store_ratings_count);
            commentsCount = itemView.findViewById(R.id.store_comments_count);
            bikeStatus = itemView.findViewById(R.id.store_bike_status);
        }
    }
}

