package com.example.prm392_group3.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.prm392_group3.R;
import com.example.prm392_group3.activities.authen.Login;
import com.example.prm392_group3.activities.blog.AllBlog;
import com.example.prm392_group3.activities.home.Home;
import com.example.prm392_group3.activities.orders.ordersManagement;
import com.example.prm392_group3.activities.profile.Profile;
import com.example.prm392_group3.activities.store.Store;
import com.example.prm392_group3.models.User;
import com.example.prm392_group3.utils.ObjectStorageUtil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private ChipNavigationBar chipNavigationBar;
    private Fragment fragment = null;
    User userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Khởi tạo FirebaseAuth
        firebaseAuth = FirebaseAuth.getInstance();

        // Lấy thông tin người dùng đã đăng nhập
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user == null) {
            ObjectStorageUtil.deleteObject(this, "user_data.json");
            Intent intent = new Intent(MainActivity.this, Login.class);
            startActivity(intent);
            finish();
        }

        chipNavigationBar = findViewById(R.id.chipNavigation);
        chipNavigationBar.setItemSelected(R.id.home, true);
        getSupportFragmentManager().beginTransaction().replace(R.id.container, new Home()).commit();

        chipNavigationBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                if (i == R.id.home) {
                    fragment = new Home();
                } else if (i == R.id.profile) {
                    fragment = new Profile();
                } else if (i == R.id.store) {
                    fragment = new Store();
                } else if (i == R.id.blog) {
                    fragment = new AllBlog();
                } else if (i == R.id.ordersManagement) {
                    fragment = new ordersManagement();
                }

                if (fragment != null) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
                }
            }
        });
    }

}