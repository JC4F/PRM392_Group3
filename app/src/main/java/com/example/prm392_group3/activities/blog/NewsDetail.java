package com.example.prm392_group3.activities.blog;

import static com.example.prm392_group3.activities.store.Store.loadingProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prm392_group3.R;
import com.example.prm392_group3.activities.orders.Order;
import com.example.prm392_group3.activities.store.AddOrUpddateBike;
import com.example.prm392_group3.activities.store.BikeDetail;
import com.example.prm392_group3.models.Bike;
import com.example.prm392_group3.models.News;
import com.example.prm392_group3.models.User;
import com.example.prm392_group3.utils.ObjectStorageUtil;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class NewsDetail extends AppCompatActivity {
    DatabaseReference myRef;
    DatabaseReference userRef;

    FirebaseDatabase database;
    private ImageView backButton;
    User userDetails;
    TextView newsTitle;
    TextView category;
    TextView date;


    TextView source;
    ImageView newsImage;
    TextView newsContent;
    private AppCompatButton updateBtn;
    private AppCompatButton deleteBtn;
News cNews;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        // Get the news ID passed from the previous activity
        Intent intent = getIntent();
        String newsId = intent.getStringExtra("newsId");
        backButton = findViewById(R.id.backButton);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("News");
        userRef = database.getReference("User");

        userDetails = ObjectStorageUtil.loadObject(NewsDetail.this, "user_data.json", User.class);
        Query query = myRef.orderByChild("pid").equalTo(newsId);
        newsTitle = findViewById(R.id.newsTitle);
        source = findViewById(R.id.source);
        newsImage = findViewById(R.id.newsImage);
        newsContent = findViewById(R.id.newsContent);
        category = findViewById(R.id.etCategory);
        date = findViewById(R.id.date);

        updateBtn = findViewById(R.id.updatebtn);
        deleteBtn = findViewById(R.id.deletebtn);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    News news = snapshot.getValue(News.class);
                    if(news.getCreatedBy()!=null){
                        if (news.getCreatedBy().equals(userDetails.getId())) {
                            deleteBtn.setVisibility(View.VISIBLE);
                            updateBtn.setVisibility(View.VISIBLE);
                        }
                    }


                    cNews = news;
                    if (news != null) {
                        Query userQuery = userRef.orderByChild("id").equalTo(news.getCreatedBy());
                        userQuery.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                                    User user = userSnapshot.getValue(User.class);
                                    if (user != null) {
                                        // Here, you have access to the user with the ID news.getCreatedBy()
                                        // You can do something with the user object
                                        // For example, set the source TextView to the user's name
                                        source.setText("By "+user.getUsername());
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Toast.makeText(NewsDetail.this, "Failed to load user details", Toast.LENGTH_SHORT).show();
                                source.setText("By "+"?");

                            }
                        });
                        newsTitle.setText(news.getTitle());
                        category.setText(news.getCategory());
                        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a, EEEE, dd MMM yyyy", Locale.ENGLISH);
                        String formattedDate = dateFormat.format(news.getPostDate());

                        date.setText("Created at "+formattedDate);
                        // Load the news image using a library like Picasso or Glide
                        // Example with Picasso:
                        Picasso.get().load(news.getImage()).into(newsImage);
                        newsContent.setText(news.getPostContent());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(NewsDetail.this, "Failed to load news details", Toast.LENGTH_SHORT).show();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Chuyển sang trang mới và truyền mô hình Bike qua Intent
                Intent intent = new Intent(NewsDetail.this, CreateNewsActivity.class);
                intent.putExtra("News",  cNews);
                startActivity(intent);
            }
        });
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Hiển thị hộp thoại xác nhận xóa
                showDeleteConfirmationDialog(cNews);
            }
        });
        // Do something with the news ID (e.g., display it in a TextView)

    }
    private void showDeleteConfirmationDialog(News news) {
        AlertDialog.Builder builder = new AlertDialog.Builder(NewsDetail.this);
        builder.setTitle("Confirm Delete");
        builder.setMessage("Are you sure you want to delete this news?");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Thực hiện xóa bike từ Firebase Realtime Database
                deleteNews(news);
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
    private void deleteNews(News news) {
        String pid = news.getPid();

        deleteNewsByBikeId(pid);

        myRef.child(pid).removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                if (databaseError == null) {
                    // Hiển thị thông báo thành công
                    Toast.makeText(getApplicationContext(), "News deleted successfully", Toast.LENGTH_SHORT).show();
                } else {
                    // Hiển thị thông báo lỗi
                    Toast.makeText(getApplicationContext(), "Failed to delete news: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void deleteNewsByBikeId(String newsId) {
        Query query = myRef.orderByChild("pid").equalTo(newsId);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    News news = snapshot.getValue(News.class);
                    if (news != null) {
                        snapshot.getRef().removeValue();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Failed to delete News: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}