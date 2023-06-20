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
import android.widget.Toast;

import com.example.prm392_group3.R;
import com.example.prm392_group3.models.Bike;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddBike extends AppCompatActivity {
    FirebaseDatabase database;
    DatabaseReference myRef;

    ImageView backButton;
    Button addButton;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bike);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Bike");

        backButton = findViewById(R.id.add_bike_btn_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        addButton = findViewById(R.id.add_bike_btn_add_bike);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy giá trị từ các trường thông tin
                EditText bikeNameEditText = findViewById(R.id.add_bike_bike_name);
                String bikeName = bikeNameEditText.getText().toString();

                EditText bikeDescriptionEditText = findViewById(R.id.bike_description);
                String bikeDescription = bikeDescriptionEditText.getText().toString();

                EditText imageUrlEditText = findViewById(R.id.add_bike_image_url);
                String imageUrl = imageUrlEditText.getText().toString();

                EditText pricePerHourEditText = findViewById(R.id.add_bike_pricePerHour);
                String pricePerHourString = pricePerHourEditText.getText().toString();

                EditText quantityEditText = findViewById(R.id.add_bike_quantity);
                String quantityString = quantityEditText.getText().toString();

                CheckBox availableCheckBox = findViewById(R.id.add_bike_checkbox_available);
                boolean isAvailable = availableCheckBox.isChecked();

                // Kiểm tra dữ liệu không được trống
                if (TextUtils.isEmpty(bikeName) || TextUtils.isEmpty(bikeDescription) ||
                        TextUtils.isEmpty(imageUrl) || TextUtils.isEmpty(pricePerHourString) ||
                        TextUtils.isEmpty(quantityString)) {
                    Toast.makeText(AddBike.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Chuyển đổi giá trị số sang kiểu dữ liệu tương ứng
                float pricePerHour = Float.parseFloat(pricePerHourString);
                int quantity = Integer.parseInt(quantityString);

                // Hiển thị thanh tiến trình
                showProgressBar();

                // Tạo đối tượng Bike mới
                Bike newBike = new Bike("", bikeName, bikeDescription, imageUrl, pricePerHour, isAvailable, quantity);

                // Ghi đối tượng Bike vào Firebase Realtime Database
                String bikeId = myRef.push().getKey();
                newBike.setId(bikeId);
                myRef.child(bikeId).setValue(newBike, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        // Ẩn thanh tiến trình
                        hideProgressBar();

                        if (databaseError == null) {
                            // Hiển thị thông báo thành công
                            Toast.makeText(AddBike.this, "Bike added successfully", Toast.LENGTH_SHORT).show();
                            // Đóng Activity
                            finish();
                        } else {
                            // Hiển thị thông báo lỗi
                            Toast.makeText(AddBike.this, "Failed to add bike: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    private void showProgressBar() {
        if (progressBar == null) {
            progressBar = findViewById(R.id.add_bike_pb_loading);
        }
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        if (progressBar == null) {
            progressBar = findViewById(R.id.add_bike_pb_loading);
        }
        progressBar.setVisibility(View.GONE);
    }
}
