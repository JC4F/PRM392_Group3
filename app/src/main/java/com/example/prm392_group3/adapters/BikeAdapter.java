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
import com.example.prm392_group3.models.Rating;
import com.example.prm392_group3.models.User;
import com.example.prm392_group3.utils.ObjectStorageUtil;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class BikeAdapter extends RecyclerView.Adapter<BikeAdapter.ViewHolder> {

    private List<Bike> bikeList;
    private Context context;
    DatabaseReference bikeRef;
    DatabaseReference bookRef;

    User userDetails;

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
        bookRef = FirebaseDatabase.getInstance().getReference("Book");
        bikeItemHolder = view.findViewById(R.id.bike_item_holder);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BikeAdapter.ViewHolder holder, int position) {
        // Lấy dữ liệu từ danh sách dựa trên vị trí position
        Bike bike = bikeList.get(position);


        if (userDetails!=null && !userDetails.isRole()){
            holder.updateBtn.setVisibility(View.GONE);
            holder.deleteBtn.setVisibility(View.GONE);
            holder.bookOrCancelBtn.setVisibility(View.VISIBLE);
        }
        Query query = bookRef.orderByChild("bikeID").equalTo(bike.getId());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Order order = snapshot.getValue(Order.class);
                        if (order.getUserID().equals(userDetails.getId())) {
                            holder.bookOrCancelBtn.setText("CANCEL");
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Xử lý khi có lỗi xảy ra
            }
        });

        // Load hình ảnh từ URL và hiển thị nó trong ImageView
        Picasso.get().load(bike.getImageUrl()).into(holder.bikeImage);
        holder.bikeName.setText(bike.getName());
        holder.remainQuantity.setText("Remain quantity: " + bike.getQuantity());
        holder.ratingsCount.setText(String.format("%.1f (%d ratings)", calculateAverageRating(bike.getRatingList()), bike.getRatingList().size()));

        holder.updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Chuyển sang trang mới và truyền mô hình Bike qua Intent
                Intent intent = new Intent(context, AddOrUpddateBike.class);
                intent.putExtra("Bike", bike);
                context.startActivity(intent);
            }
        });

        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Hiển thị hộp thoại xác nhận xóa
                showDeleteBikeConfirmationDialog(bike);
            }
        });

        holder.bookOrCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.bookOrCancelBtn.getText().equals("BOOK"))
                    handleBooking(bike, holder.bookOrCancelBtn);
                else if(holder.bookOrCancelBtn.getText().equals("CANCEL"))
                    showDeleteOrderConfirmationDialog(bike.getId(), holder.bookOrCancelBtn);
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

    private void showDeleteBikeConfirmationDialog(Bike bike) {
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

    private void showDeleteOrderConfirmationDialog(String bikeId, AppCompatButton bookOrCancelBtn) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Confirm Delete");
        builder.setMessage("Are you sure you want to delete this bike?");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Thực hiện xóa bike từ Firebase Realtime Database
                deleteBook(bikeId, bookOrCancelBtn);
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

    private void deleteBook(String bikeId, AppCompatButton bookOrCancelBtn) {
        loadingProgressBar.setVisibility(View.VISIBLE);

        Query query = bookRef.orderByChild("bikeID").equalTo(bikeId);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Order order = snapshot.getValue(Order.class);
                    if (order != null && order.getUserID().equals(userDetails.getId())) {
                        snapshot.getRef().removeValue();

                        Toast.makeText(context, "Order deleted successfully", Toast.LENGTH_SHORT).show();
                        bookOrCancelBtn.setText("BOOK");
                    }
                }
                loadingProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(context, "Failed to delete order: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleBooking(Bike bike, AppCompatButton bookOrCancelBtn) {
        String bookingId = bookRef.push().getKey();

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
            bookRef.child(bookingId).setValue(booking)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(context, "Booking successful!", Toast.LENGTH_SHORT).show();
                            bookOrCancelBtn.setText("CANCEL");
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
        AppCompatButton updateBtn;
        AppCompatButton deleteBtn;
        AppCompatButton bookOrCancelBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            bikeImage = itemView.findViewById(R.id.user_item_image);
            bikeName = itemView.findViewById(R.id.user_userName);
            remainQuantity = itemView.findViewById(R.id.user_gmail);
            ratingsCount = itemView.findViewById(R.id.user_phone);
            updateBtn = itemView.findViewById(R.id.user_update_btn);
            deleteBtn = itemView.findViewById(R.id.user_delete_btn);
            bookOrCancelBtn = itemView.findViewById(R.id.user_book_btn);
        }
    }
}

