package com.example.prm392_group3.activities.store;

import static com.example.prm392_group3.activities.store.Store.loadingProgressBar;

import com.example.prm392_group3.R;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.prm392_group3.models.Bike;
import com.example.prm392_group3.models.User;
import com.example.prm392_group3.utils.ObjectStorageUtil;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class BikeDetail extends AppCompatActivity {

    DatabaseReference myRef;
    User userDetails;
    private Bike bike;
    ImageView backButton;
    private TextView nameTextView;
    private ImageView imageView;
    private TextView priceTextView;
    private TextView abilityTextView;
    private TextView quantityTextView;
    private TextView descriptionTextView;
    private AppCompatButton updateBtn;
    private AppCompatButton deleteBtn;
    private AppCompatButton bookingBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bike_detail);

        userDetails = ObjectStorageUtil.loadObject(getApplicationContext(), "user_data.json", User.class);
        myRef = FirebaseDatabase.getInstance().getReference("Bike");

        // Ánh xạ các phần tử giao diện
        nameTextView = findViewById(R.id.bikedt_name);
        backButton = findViewById(R.id.bikedt_btn_back);
        imageView = findViewById(R.id.bikedt_image);
        priceTextView = findViewById(R.id.bikedt_price);
        abilityTextView = findViewById(R.id.bikedt_ability);
        quantityTextView = findViewById(R.id.bike_quantity);
        descriptionTextView = findViewById(R.id.bikedt_description);
        updateBtn = findViewById(R.id.bikedt_update_btn);
        deleteBtn = findViewById(R.id.bikedt_delete_btn);
        bookingBtn = findViewById(R.id.bikedt_book_btn);

        // Nhận dữ liệu Bike từ Intent
        bike = (Bike) getIntent().getSerializableExtra("Bike");

        if (userDetails!=null && !userDetails.isRole()){
            updateBtn.setVisibility(View.GONE);
            deleteBtn.setVisibility(View.GONE);
            bookingBtn.setVisibility(View.VISIBLE);
        }

        // Hiển thị dữ liệu Bike trên giao diện
        if (bike != null) {
            nameTextView.setText(bike.getName());
            priceTextView.setText(String.valueOf(bike.getPricePerHour()));
            abilityTextView.setText(bike.isAvailable() ? "Available" : "Not Available");
            quantityTextView.setText(String.valueOf(bike.getQuantity()));
            descriptionTextView.setText(bike.getDescription());

            // Sử dụng thư viện Picasso (hoặc Glide) để tải và hiển thị hình ảnh từ URL
            Picasso.get().load(bike.getImageUrl()).into(imageView);
        }

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

    private void deleteBike(Bike bike) {
        String bikeId = bike.getId();

        loadingProgressBar.setVisibility(View.VISIBLE);

        myRef.child(bikeId).removeValue(new DatabaseReference.CompletionListener() {
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
}
