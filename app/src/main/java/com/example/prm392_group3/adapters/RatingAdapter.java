package com.example.prm392_group3.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_group3.R;
import com.example.prm392_group3.models.Rating;
import com.google.android.material.imageview.ShapeableImageView;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class RatingAdapter extends RecyclerView.Adapter<RatingAdapter.ViewHolder> {
    private Context context;
    private List<Rating> ratingList;

    public RatingAdapter(Context context, List<Rating> ratingList){
        this.context = context;
        this.ratingList = ratingList;
    }

    @NonNull
    @Override
    public RatingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate layout cho mỗi item trong danh sách
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bike_rating_item, parent, false);
        return new RatingAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RatingAdapter.ViewHolder holder, int position) {
        Rating rating = ratingList.get(position);

        // Set data for the views in the ViewHolder
        Picasso.get().load(rating.getUser().getAvatarURL().equals("")?"https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRCO2sR3EtGqpIpIa-GTVnvdrDHu0WxuzpA8g&usqp=CAU":rating.getUser().getAvatarURL()).into(holder.ratingAccImage);
        // Set data for the views in the ViewHolder
        holder.ratingAccName.setText(rating.getUser().getUsername().equals("")?rating.getUser().getEmail():rating.getUser().getUsername());
        holder.ratingBar.setRating(rating.getRating());
        holder.tvNumRating.setText(String.valueOf(rating.getRating()));
        holder.description.setText(rating.getDescription());

        // Calculate the time duration since the rating date
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
        try {
            Date ratingDate = dateFormat.parse(rating.getDate());
            Date currentDate = new Date();

            long timeDiff = currentDate.getTime() - ratingDate.getTime();
            long secondsPassed = TimeUnit.MILLISECONDS.toSeconds(timeDiff);
            long minutesPassed = TimeUnit.MILLISECONDS.toMinutes(timeDiff);
            long hoursPassed = TimeUnit.MILLISECONDS.toHours(timeDiff);

            String timeAgo;
            if (secondsPassed < 60) {
                timeAgo = secondsPassed + " seconds ago";
            } else if (minutesPassed < 60) {
                timeAgo = minutesPassed + " minutes ago";
            } else if (hoursPassed < 24) {
                timeAgo = hoursPassed + " hours ago";
            } else {
                long daysPassed = TimeUnit.MILLISECONDS.toDays(timeDiff);
                timeAgo = daysPassed + " days ago";
            }

            holder.tvDayLeft.setText(timeAgo);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return ratingList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ShapeableImageView ratingAccImage;
        TextView ratingAccName;
        RatingBar ratingBar;
        TextView tvNumRating;
        TextView tvDayLeft;
        TextView description;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ratingAccImage = itemView.findViewById(R.id.rating_item_acc_image);
            ratingAccName = itemView.findViewById(R.id.rating_item_acc_name);
            ratingBar = itemView.findViewById(R.id.rating_item_rating_bar);
            tvNumRating = itemView.findViewById(R.id.rating_item_tv_rating);
            tvDayLeft = itemView.findViewById(R.id.rating_item_tv_dayleft);
            description = itemView.findViewById(R.id.rating_item_tv_description);
        }
    }

}
