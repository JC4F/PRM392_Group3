package com.example.prm392_group3.adapters;

import static com.example.prm392_group3.activities.store.Store.loadingProgressBar;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_group3.R;
import com.example.prm392_group3.activities.store.AddOrUpddateBike;
import com.example.prm392_group3.models.Bike;
import com.example.prm392_group3.models.User;
import com.example.prm392_group3.utils.ObjectStorageUtil;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;

public class BikeAdapter extends RecyclerView.Adapter<BikeAdapter.ViewHolder> {

    private List<Bike> bikeList;
    private int commentCount;
    private List<Integer> ratingList;
    private Context context;
    DatabaseReference myRef;

    User userDetails;
    AppCompatButton updateBtn;
    AppCompatButton deleteBtn;
    AppCompatButton bookingBtn;

    public BikeAdapter(Context context, List<Bike> bikeList, int commentCount, List<Integer> ratingList) {
        this.context = context;
        this.bikeList = bikeList;
        this.commentCount = commentCount;
        this.ratingList = ratingList;
        this.userDetails = ObjectStorageUtil.loadObject(context, "user_data.json", User.class);
    }

    @NonNull
    @Override
    public BikeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate layout cho mỗi item trong danh sách
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.store_bike_item, parent, false);

        myRef = FirebaseDatabase.getInstance().getReference("Bike");

        updateBtn = view.findViewById(R.id.store_update_btn);
        deleteBtn = view.findViewById(R.id.store_delete_btn);
        bookingBtn = view.findViewById(R.id.store_book_btn);

        if (userDetails!=null && !userDetails.isRole()){
            updateBtn.setVisibility(View.GONE);
            deleteBtn.setVisibility(View.GONE);
            bookingBtn.setVisibility(View.VISIBLE);
        }

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

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Hiển thị hộp thoại xác nhận xóa
                showDeleteConfirmationDialog(bike);
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

    private void showDeleteConfirmationDialog(Bike bike) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Confirm Delete");
        builder.setMessage("Are you sure you want to delete this bike?");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Thực hiện xóa bike từ Firebase Realtime Database
                deleteBike(bike);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Đóng dialog
                dialogInterface.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void deleteBike(Bike bike) {
        String bikeId = bike.getId();

        loadingProgressBar.setVisibility(View.VISIBLE);

        myRef.child(bikeId).removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                loadingProgressBar.setVisibility(View.GONE);

                if (databaseError == null) {
                    // Hiển thị thông báo thành công
                    Toast.makeText(context, "Bike deleted successfully", Toast.LENGTH_SHORT).show();
                } else {
                    // Hiển thị thông báo lỗi
                    Toast.makeText(context, "Failed to delete bike: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
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

