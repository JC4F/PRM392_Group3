package com.example.prm392_group3.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.prm392_group3.R;
import com.example.prm392_group3.activities.authen.Login;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private TextView textViewWelcome;
    private Button buttonLogout;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Khởi tạo FirebaseAuth
        firebaseAuth = FirebaseAuth.getInstance();

        // Ánh xạ các view
        textViewWelcome = findViewById(R.id.textViewWelcome);
        buttonLogout = findViewById(R.id.buttonLogout);

        // Lấy thông tin người dùng đã đăng nhập
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user == null) {
            // Chuyển về trang đăng nhập (LoginActivity)
            Intent intent = new Intent(MainActivity.this, Login.class);
            startActivity(intent);
            finish();
        } else {
            String username = user.getEmail(); // Lấy email của người dùng
            textViewWelcome.setText("Welcome, " + username);

            // Xử lý sự kiện click của nút Logout
            buttonLogout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Đăng xuất
                    firebaseAuth.signOut();

                    // Chuyển về trang đăng nhập (LoginActivity)
                    Intent intent = new Intent(MainActivity.this, Login.class);
                    startActivity(intent);
                    finish();
                }
            });
        }
    }
}