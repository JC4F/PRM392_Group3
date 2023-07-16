package com.example.prm392_group3.activities.blog;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.prm392_group3.R;
import com.example.prm392_group3.activities.store.AddOrUpddateBike;
import com.example.prm392_group3.models.News;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Date;

public class CreateNewsActivity extends AppCompatActivity {
    private EditText etTitle;
    private EditText etURL;
    private EditText etPostContent;
    private EditText etSource;
    private EditText etSourceURL;
    private EditText etImageURL;
    private Button btnSubmit;
    private ImageView backButton;
    DatabaseReference myRef;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_or_update_news);
        backButton = findViewById(R.id.backButton);
        Toast.makeText(CreateNewsActivity.this, "Book not found", Toast.LENGTH_SHORT).show();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("News");

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        etTitle = findViewById(R.id.etTitle);
        etURL = findViewById(R.id.etURL);
        etPostContent = findViewById(R.id.etPostContent);
        etSource = findViewById(R.id.etSource);
        etSourceURL = findViewById(R.id.etSourceURL);
        etImageURL = findViewById(R.id.etImageURL);
        btnSubmit = findViewById(R.id.btnSubmit);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve data from EditText fields
                String title = etTitle.getText().toString().trim();
                String url = etURL.getText().toString().trim();
                String postContent = etPostContent.getText().toString().trim();
                String source = etSource.getText().toString().trim();
                String sourceURL = etSourceURL.getText().toString().trim();
                String imageURL = etImageURL.getText().toString().trim();
                Calendar calendar = Calendar.getInstance();
                Date currentDate = calendar.getTime();

                // Create a News object with the retrieved data
                News news = new News("",currentDate, title, url, postContent, source, sourceURL, imageURL);
                // Add the news to Firebase database
                news.setPid(myRef.push().getKey());
                myRef.child(myRef.push().getKey()).setValue(news, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        // Ẩn thanh tiến trình

                        if (databaseError == null) {
                            // Hiển thị thông báo thành công
                            Toast.makeText(CreateNewsActivity.this, "Bike " + (news != null ? "updated" : "added") + " successfully", Toast.LENGTH_SHORT).show();
                            // Đóng Activity
                            finish();
                        } else {
                            // Hiển thị thông báo lỗi
                            Toast.makeText(CreateNewsActivity.this, "Failed to " + (news != null ? "update" : "add") + " bike: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
//                myRef.child(myRef.push().getKey()).setValue(news)
//                        .addOnSuccessListener(new OnSuccessListener<Void>() {
//                            @Override
//                            public void onSuccess(Void aVoid) {
//                                // Show a success message or perform any other operations
//                                Toast.makeText(CreateNewsActivity.this, "News added successfully", Toast.LENGTH_SHORT).show();
//                                finish(); // Close the activity after adding news
//                            }
//                        })
//                        .addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                // Show an error message or perform any other error handling
//                                Log.e("Error", e.getMessage());
//                                Toast.makeText(CreateNewsActivity.this, "Failed to add news: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//                            }
//                        });
            }
        });
    }
}

