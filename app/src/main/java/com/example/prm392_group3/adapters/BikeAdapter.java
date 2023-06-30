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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_group3.R;
import com.example.prm392_group3.activities.orders.Order;
import com.example.prm392_group3.activities.store.AddOrUpddateBike;
import com.example.prm392_group3.activities.store.BikeDetail;
import com.example.prm392_group3.models.Bike;
import com.example.prm392_group3.models.User;
import com.example.prm392_group3.utils.ObjectStorageUtil;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class BikeAdapter extends RecyclerView.Adapter<BikeAdapter.ViewHolder> {

    private List<Bike> bikeList;
    private Context context;
    DatabaseReference bikeRef;
    DatabaseReference bookingRef;

    User userDetails;
    AppCompatButton updateBtn;
    AppCompatButton deleteBtn;
    AppCompatButton bookingBtn;
    ConstraintLayout bikeItemHolder;

    public BikeAdapter(Context context, List<Bike> bikeList) {
        this.context = context;
        this.bikeList = bikeList;
        this.userDetails = ObjectStorageUtil.loadObject(context, "user_data.json", User.class);
    }

    @NonNull
    @Override
    public BikeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate layout cho mỗi item trong danh sách
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.store_bike_item, parent, false);

        bikeRef = FirebaseDatabase.getInstance().getReference("Bike");
        bookingRef = FirebaseDatabase.getInstance().getReference("Book");

        updateBtn = view.findViewById(R.id.user_update_btn);
        deleteBtn = view.findViewById(R.id.user_delete_btn);
        bookingBtn = view.findViewById(R.id.user_book_btn);
        bikeItemHolder = view.findViewById(R.id.bike_item_holder);

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
        holder.ratingsCount.setText(String.format("%.1f (%d ratings)", calculateAverageRating(bike.getRatingList()), bike.getRatingList().size()));

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

        bookingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleBooking(bike);
            }
        });

        bikeItemHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, BikeDetail.class);
                intent.putExtra("Bike", bike);
                context.startActivity(intent);
            }
        });
    }

    private float calculateAverageRating(List<Float> ratingList) {
        int sum = 0;
        if(ratingList.size()==0) return 0;
        for (float rating : ratingList) {
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

        bikeRef.child(bikeId).removeValue(new DatabaseReference.CompletionListener() {
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

    private void handleBooking(Bike bike) {
        String bookingId = bookingRef.push().getKey();

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String startDateString = sdf.format(new Date()); // Ngày bắt đầu (dạng "dd/MM/yyyy HH:mm:ss")

        try {
            Date startDate = sdf.parse(startDateString);

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startDate);
            calendar.add(Calendar.DAY_OF_MONTH, 3);
            Date endDate = calendar.getTime();

            String endDateString = sdf.format(endDate);

            String userId = userDetails.getId();

            float pricePerHour = bike.getPricePerHour();
            long milliseconds = endDate.getTime() - startDate.getTime();
            long hours = milliseconds / (60 * 60 * 1000);
            float totalPrice =  (pricePerHour * hours);

            Order booking = new Order();
            booking.setBookID(bookingId);
            booking.setBikeID(bike.getId());
            booking.setBookingStatus("Pending");
            booking.setUserID(userId);
            booking.setBikeName(bike.getName());
            booking.setStartDate(startDateString);
            booking.setEndDate(endDateString);
            booking.setTotalPrice(totalPrice);

            // Lưu đối tượng Booking vào Firebase Realtime Database
            bookingRef.child(bookingId).setValue(booking)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(context, "Booking successful!", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context, "Booking failed. Please try again.", Toast.LENGTH_SHORT).show();
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
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

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            bikeImage = itemView.findViewById(R.id.user_item_image);
            bikeName = itemView.findViewById(R.id.user_userName);
            remainQuantity = itemView.findViewById(R.id.user_gmail);
            ratingsCount = itemView.findViewById(R.id.user_phone);
        }
    }
}

