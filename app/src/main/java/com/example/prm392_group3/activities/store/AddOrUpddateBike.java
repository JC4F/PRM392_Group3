package com.example.prm392_group3.activities.store;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prm392_group3.R;
import com.example.prm392_group3.models.Bike;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddOrUpddateBike extends AppCompatActivity {
    FirebaseDatabase database;
    DatabaseReference myRef;

    ImageView backButton;
    TextView toolbarTitle;
    TextInputEditText bikeNameEditText;
    TextInputEditText bikeDescriptionEditText;
    TextInputEditText imageUrlEditText;
    TextInputEditText pricePerHourEditText;
    TextInputEditText quantityEditText;
    CheckBox availableCheckBox;
    Button addOrUpateButton;
    ProgressBar progressBar;
    Bike bike;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_or_update_bike);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Bike");

        bike = (Bike) getIntent().getSerializableExtra("Bike");
        toolbarTitle = findViewById(R.id.au_toolbar_title);
        addOrUpateButton = findViewById(R.id.au_bike_btn_add_bike);
        backButton = findViewById(R.id.au_bike_btn_back);
        bikeNameEditText = findViewById(R.id.au_bike_bike_name);
        bikeDescriptionEditText = findViewById(R.id.au_bike_description);
        imageUrlEditText = findViewById(R.id.au_bike_image_url);
        pricePerHourEditText = findViewById(R.id.au_bike_pricePerHour);
        quantityEditText = findViewById(R.id.au_bike_quantity);
        availableCheckBox = findViewById(R.id.au_bike_checkbox_available);

        if (bike != null) {
            toolbarTitle.setText("Update Bike");
            bikeNameEditText.setText(bike.getName());
            bikeDescriptionEditText.setText(bike.getDescription());
            imageUrlEditText.setText(bike.getImageUrl());
            pricePerHourEditText.setText(String.valueOf(bike.getPricePerHour()));
            quantityEditText.setText(String.valueOf(bike.getQuantity()));
            availableCheckBox.setChecked(bike.isAvailable());
            addOrUpateButton.setText("Save Bike");
        }

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        addOrUpateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy giá trị từ các trường thông tin
                String bikeName = bikeNameEditText.getText().toString();
                String bikeDescription = bikeDescriptionEditText.getText().toString();
                String imageUrl = imageUrlEditText.getText().toString();
                String pricePerHourString = pricePerHourEditText.getText().toString();
                String quantityString = quantityEditText.getText().toString();
                boolean isAvailable = availableCheckBox.isChecked();

                // Kiểm tra dữ liệu không được trống
                if (TextUtils.isEmpty(bikeName) || TextUtils.isEmpty(bikeDescription) ||
                        TextUtils.isEmpty(imageUrl) || TextUtils.isEmpty(pricePerHourString) ||
                        TextUtils.isEmpty(quantityString)) {
                    Toast.makeText(AddOrUpddateBike.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Chuyển đổi giá trị số sang kiểu dữ liệu tương ứng
                float pricePerHour = Float.parseFloat(pricePerHourString);
                int quantity = Integer.parseInt(quantityString);

                // Hiển thị thanh tiến trình
                showProgressBar();

                // Tạo đối tượng Bike mới
                Bike newBike;
                String bikeId;

                if (bike != null) {
                    // Đây là bước cập nhật (update)
                    newBike = new Bike(bike.getId(), bikeName, bikeDescription, imageUrl, pricePerHour, isAvailable, quantity);
                    bikeId = bike.getId();
                } else {
                    // Đây là bước thêm mới (add)
                    newBike = new Bike("", bikeName, bikeDescription, imageUrl, pricePerHour, isAvailable, quantity);
                    bikeId = myRef.push().getKey();
                    newBike.setId(bikeId);
                }

                // Cập nhật đối tượng Bike vào Firebase Realtime Database
                myRef.child(bikeId).setValue(newBike, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        // Ẩn thanh tiến trình
                        hideProgressBar();

                        if (databaseError == null) {
                            // Hiển thị thông báo thành công
                            Toast.makeText(AddOrUpddateBike.this, "Bike " + (bike != null ? "updated" : "added") + " successfully", Toast.LENGTH_SHORT).show();
                            // Đóng Activity
                            finish();
                        } else {
                            // Hiển thị thông báo lỗi
                            Toast.makeText(AddOrUpddateBike.this, "Failed to " + (bike != null ? "update" : "add") + " bike: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }

    private void showProgressBar() {
        if (progressBar == null) {
            progressBar = findViewById(R.id.au_bike_pb_loading);
        }
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        if (progressBar == null) {
            progressBar = findViewById(R.id.au_bike_pb_loading);
        }
        progressBar.setVisibility(View.GONE);
    }
}
