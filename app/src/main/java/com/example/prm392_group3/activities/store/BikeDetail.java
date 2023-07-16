package com.example.prm392_group3.activities.store;

import static com.example.prm392_group3.activities.store.Store.loadingProgressBar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_group3.R;
import com.example.prm392_group3.activities.orders.Order;
import com.example.prm392_group3.adapters.RatingAdapter;
import com.example.prm392_group3.models.Bike;
import com.example.prm392_group3.models.Rating;
import com.example.prm392_group3.models.User;
import com.example.prm392_group3.utils.ObjectStorageUtil;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class BikeDetail extends AppCompatActivity {
    private static final int PAGE_SIZE = 2;
    private int countLoadMore = -1;
    private String lastRatingKey;
    DatabaseReference bikeRef;
    DatabaseReference ratingRef;
    DatabaseReference bookRef;
    User userDetails;
    private Bike bike;
    ImageView backButton;
    private TextView nameTextView;
    private ImageView imageView;
    private TextView priceTextView;
    private TextView quantityTextView;
    private TextView descriptionTextView;
    private AppCompatButton updateBtn;
    private AppCompatButton deleteBtn;
    private AppCompatButton bookOrCancelBtn;
    private RatingBar ratingBar;
    private TextInputEditText ratingDescription;
    private AppCompatButton submitRatingBtn;
    private AppCompatButton loadMoreBtn;
    private ProgressBar pbLoadMore;
    private ProgressBar pbRating;
    private Rating myRating;
    private TextView tvNotRating;
    private List<Rating> ratingList;
    private RatingAdapter ratingAdapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    boolean isFirstKey = false;
    private ImageView refreshBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bike_detail);

        userDetails = ObjectStorageUtil.loadObject(getApplicationContext(), "user_data.json", User.class);
        bikeRef = FirebaseDatabase.getInstance().getReference("Bike");
        ratingRef = FirebaseDatabase.getInstance().getReference("Rating");
        bookRef = FirebaseDatabase.getInstance().getReference("Book");

        ratingList = new ArrayList<>();
        ratingAdapter = new RatingAdapter(this, ratingList);
        layoutManager = new LinearLayoutManager(this);
        recyclerView = findViewById(R.id.bikedt_rcv_rating);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(ratingAdapter);

        // Ánh xạ các phần tử giao diện
        nameTextView = findViewById(R.id.bikedt_name);
        backButton = findViewById(R.id.bikedt_btn_back);
        imageView = findViewById(R.id.bikedt_image);
        priceTextView = findViewById(R.id.bikedt_price);
        quantityTextView = findViewById(R.id.bike_quantity);
        descriptionTextView = findViewById(R.id.bikedt_description);
        updateBtn = findViewById(R.id.bikedt_update_btn);
        deleteBtn = findViewById(R.id.bikedt_delete_btn);
        bookOrCancelBtn = findViewById(R.id.bikedt_book_btn);
        ratingBar = findViewById(R.id.bikedt_rating);
        ratingDescription = findViewById(R.id.bikedt_ratings_description);
        submitRatingBtn = findViewById(R.id.bikedt_submit_button);
        loadMoreBtn = findViewById(R.id.bikedt_loadmore_button);
        pbLoadMore = findViewById(R.id.bikedt_pb_loadmore);
        refreshBtn = findViewById(R.id.bikedt_btn_refresh);
        pbRating = findViewById(R.id.bikedt_pb_rating);
        tvNotRating = findViewById(R.id.bikedt_tv_no_one_ratings);

        // Nhận dữ liệu Bike từ Intent
        bike = (Bike) getIntent().getSerializableExtra("Bike");

        if (userDetails != null && !userDetails.isRole()) {
            updateBtn.setVisibility(View.GONE);
            deleteBtn.setVisibility(View.GONE);
            bookOrCancelBtn.setVisibility(View.VISIBLE);
        }

        Query query = bookRef.orderByChild("bikeID").equalTo(bike.getId());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Order order = snapshot.getValue(Order.class);
                        if (order.getUserID().equals(userDetails.getId())) {
                            bookOrCancelBtn.setText("CANCEL");
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Xử lý khi có lỗi xảy ra
            }
        });

        // Hiển thị dữ liệu Bike trên giao diện
        if (bike != null) {
            nameTextView.setText(bike.getName());
            priceTextView.setText("Price: " + String.valueOf(bike.getPricePerHour()));
            quantityTextView.setText("Quantity: " + String.valueOf(bike.getQuantity()));
            descriptionTextView.setText(bike.getDescription());

            // Sử dụng thư viện Picasso (hoặc Glide) để tải và hiển thị hình ảnh từ URL
            Picasso.get().load(bike.getImageUrl()).into(imageView);
        }

        Query queryOrder = ratingRef.orderByChild("bikeId").equalTo(bike.getId());

        queryOrder.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Rating rating = snapshot.getValue(Rating.class);
                        if (rating.getUserId().equals(userDetails.getId())) {
                            myRating = rating;

                            ratingDescription.setEnabled(false);
                            ratingBar.setIsIndicator(true);
                            submitRatingBtn.setText("Update");

                            ratingDescription.setText(myRating.getDescription());
                            ratingBar.setRating(myRating.getRating());
                        }
                    }
                } else {
                    // Không tìm thấy kết quả phù hợp
                }
                ratingDescription.setVisibility(View.VISIBLE);
                ratingBar.setVisibility(View.VISIBLE);
                submitRatingBtn.setVisibility(View.VISIBLE);
                pbRating.setVisibility(View.GONE);

                if (myRating == null) {
                    ratingDescription.setEnabled(true);
                    ratingBar.setIsIndicator(false);
                    submitRatingBtn.setText("Add");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Xử lý khi có lỗi xảy ra
            }
        });

        getInitialRatings();

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Chuyển sang trang mới và truyền mô hình Bike qua Intent
                Intent intent = new Intent(BikeDetail.this, AddOrUpddateBike.class);
                intent.putExtra("Bike", bike);
                startActivity(intent);
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Hiển thị hộp thoại xác nhận xóa
                showDeleteConfirmationDialog(bike);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        submitRatingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());


                if (submitRatingBtn.getText() == "Add") {
                    DatabaseReference newRatingRef = ratingRef.push(); // Tạo một nút con mới trong "Rating" và lấy tham chiếu đến nút đó
                    String ratingId = newRatingRef.getKey(); // Lấy ratingId từ key của nút con mới
                    String userId = userDetails.getId(); // Lấy userId từ userDetails hoặc thông tin người dùng hiện tại
                    String bikeId = bike.getId(); // Lấy bikeId từ bike hiện tại
                    String description = ratingDescription.getText().toString().trim(); // Lấy nội dung đánh giá từ EditText
                    float rating = ratingBar.getRating(); // Lấy giá trị rating từ RatingBar
                    String date = dateFormat.format(new Date()); // Lấy ngày và giờ hiện tại

                    // Tạo một đối tượng Rating
                    Rating ratingObj = new Rating(ratingId, userId, bikeId, description, date, rating);
                    myRating = ratingObj;

                    // Thực hiện lưu đối tượng Rating lên Firebase Realtime Database
                    newRatingRef.setValue(ratingObj)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    // Hiển thị thông báo thành công
                                    Toast.makeText(getApplicationContext(), "Rating submitted successfully", Toast.LENGTH_SHORT).show();
                                    // Thiết lập trạng thái của các phần tử
                                    ratingDescription.setEnabled(false);
                                    ratingBar.setIsIndicator(true);
                                    submitRatingBtn.setText("Update");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Hiển thị thông báo lỗi
                                    Toast.makeText(getApplicationContext(), "Failed to submit rating: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                } else if (submitRatingBtn.getText() == "Update") {
                    ratingDescription.setEnabled(true);
                    ratingBar.setIsIndicator(false);
                    submitRatingBtn.setText("Save");
                } else if (submitRatingBtn.getText() == "Save") {
                    String userId = userDetails.getId(); // Lấy userId từ userDetails hoặc thông tin người dùng hiện tại
                    String bikeId = bike.getId(); // Lấy bikeId từ bike hiện tại
                    String description = ratingDescription.getText().toString().trim(); // Lấy nội dung đánh giá từ EditText
                    float rating = ratingBar.getRating(); // Lấy giá trị rating từ RatingBar
                    String date = dateFormat.format(new Date()); // Lấy ngày và giờ hiện tại

                    // Tạo một đối tượng Rating
                    Rating ratingObj = new Rating(myRating.getRatingId(), userId, bikeId, description, date, rating);

                    // Thực hiện lưu đối tượng Rating lên Firebase Realtime Database
                    ratingRef.child(myRating.getRatingId()).setValue(ratingObj)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    // Hiển thị thông báo thành công
                                    Toast.makeText(getApplicationContext(), "Rating submitted successfully", Toast.LENGTH_SHORT).show();
                                    // Thiết lập trạng thái của các phần tử
                                    ratingDescription.setEnabled(false);
                                    ratingBar.setIsIndicator(true);
                                    submitRatingBtn.setText("Update");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Hiển thị thông báo lỗi
                                    Toast.makeText(getApplicationContext(), "Failed to submit rating: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });

        loadMoreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadMoreRatings(false);
            }
        });

        bookOrCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(bookOrCancelBtn.getText().equals("BOOK"))
                    handleBooking(bike);
                else if(bookOrCancelBtn.getText().equals("CANCEL"))
                    showDeleteOrderConfirmationDialog(bike.getId());
            }
        });

        refreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lastRatingKey = null;
                isFirstKey = false;
                countLoadMore =  -1;
                ratingList.clear();
                ratingAdapter.notifyDataSetChanged();
                getInitialRatings();
            }
        });
    }

    private void handleBooking(Bike bike) {
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
                            Toast.makeText(getApplicationContext(), "Booking successful!", Toast.LENGTH_SHORT).show();
                            bookOrCancelBtn.setText("CANCEL");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "Booking failed. Please try again.", Toast.LENGTH_SHORT).show();
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showDeleteConfirmationDialog(Bike bike) {
        AlertDialog.Builder builder = new AlertDialog.Builder(BikeDetail.this);
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

    private void showDeleteOrderConfirmationDialog(String bikeId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm Delete");
        builder.setMessage("Are you sure you want to delete this bike?");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Thực hiện xóa bike từ Firebase Realtime Database
                deleteBook(bikeId);
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

        deleteRatingByBikeId(bikeId);
        deleteBookByBikeId(bikeId);

        bikeRef.child(bikeId).removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                loadingProgressBar.setVisibility(View.GONE);

                if (databaseError == null) {
                    // Hiển thị thông báo thành công
                    Toast.makeText(getApplicationContext(), "Bike deleted successfully", Toast.LENGTH_SHORT).show();
                } else {
                    // Hiển thị thông báo lỗi
                    Toast.makeText(getApplicationContext(), "Failed to delete bike: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void deleteBook(String bikeId) {
        loadingProgressBar.setVisibility(View.VISIBLE);

        Query query = bookRef.orderByChild("bikeID").equalTo(bikeId);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Order order = snapshot.getValue(Order.class);
                    if (order != null && order.getUserID().equals(userDetails.getId())) {
                        snapshot.getRef().removeValue();

                        Toast.makeText(getApplicationContext(), "Order deleted successfully", Toast.LENGTH_SHORT).show();
                        bookOrCancelBtn.setText("BOOK");
                    }
                }
                loadingProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Failed to delete order: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteRatingByBikeId(String bikeId) {
        Query query = ratingRef.orderByChild("bikeId").equalTo(bikeId);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Rating rating = snapshot.getValue(Rating.class);
                    if (rating != null) {
                        snapshot.getRef().removeValue();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Failed to delete ratins: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void deleteBookByBikeId(String bikeId) {
        Query query = bookRef.orderByChild("bikeID").equalTo(bikeId);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Order order = snapshot.getValue(Order.class);
                    if (order != null) {
                        snapshot.getRef().removeValue();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Failed to delete book: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void processUserRating(Rating rating, String userId, boolean isOverload) {
        // Truy vấn Firebase Realtime Database để lấy thông tin User tương ứng
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("User").child(userId);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    User user = dataSnapshot.getValue(User.class);
                    rating.setUser(user); // Thiết lập thông tin User vào model Rating
                }

                ratingList.add(countLoadMore * PAGE_SIZE, rating);
                if (!isFirstKey) {
                    isFirstKey = true;
                    lastRatingKey = rating.getRatingId();
                }

                if(isOverload){
                    ratingList.remove(ratingList.size()-1);
                    lastRatingKey = ratingList.get(ratingList.size()-1).getRatingId();
                }
                ratingAdapter.notifyDataSetChanged();

//                recyclerView.scrollToPosition(ratingAdapter.getItemCount() - 1);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Xử lý khi có lỗi xảy ra trong việc lấy thông tin User
            }
        });
    }

    private void getInitialRatings() {
        loadMoreRatings(true);
    }

    private void loadMoreRatings(boolean isInit) {
        if (lastRatingKey == null && !isInit) {
            loadMoreBtn.setVisibility(View.GONE);
            return;
        }

        pbLoadMore.setVisibility(View.VISIBLE);

        Query query;
        if (isInit) {
            query = ratingRef.orderByChild("bikeId").equalTo(bike.getId()).limitToLast(PAGE_SIZE + 1);
        } else {
            query = ratingRef.orderByChild("bikeId")
                    .endBefore(bike.getId(), lastRatingKey)
                    .limitToLast(PAGE_SIZE + 1);

        }

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                pbLoadMore.setVisibility(View.VISIBLE);
                tvNotRating.setVisibility(View.GONE);
                if (dataSnapshot.exists()) {
                    int countSuitableData = 0;
                    isFirstKey = false;
                    countLoadMore++;

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Rating rating = snapshot.getValue(Rating.class);

                        // Kiểm tra bikeId và userId tương ứng
                        if (rating != null && rating.getBikeId().equals(bike.getId())) {
                            countSuitableData++;

                            if(countSuitableData == PAGE_SIZE + 1){
                                processUserRating(rating, userDetails.getId(), true);
                            }
                            else {
                                processUserRating(rating, userDetails.getId(), false);
                            }

                            if (countSuitableData == PAGE_SIZE + 1) {
                                loadMoreBtn.setVisibility(View.VISIBLE);
                            } else {
                                lastRatingKey = null;
                                loadMoreBtn.setVisibility(View.GONE);
                            }
                        }
                    }
                    
                    if(countSuitableData==0) tvNotRating.setVisibility(View.VISIBLE);
                } else {
                    lastRatingKey = null;
                    loadMoreBtn.setVisibility(View.GONE);
                    tvNotRating.setVisibility(View.VISIBLE);
                }

                pbLoadMore.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Xử lý khi có lỗi xảy ra
            }
        });
    }


}
