package com.example.prm392_group3.activities.authen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.prm392_group3.R;
import com.example.prm392_group3.activities.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Register extends AppCompatActivity {
    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonRegister;
    private ProgressBar progressBar;
    private TextView tvRegisterErrorMessage;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Khởi tạo FirebaseAuth
        firebaseAuth = FirebaseAuth.getInstance();

        // Ánh xạ các view
        editTextEmail = findViewById(R.id.register_email);
        editTextPassword = findViewById(R.id.register_password);
        buttonRegister = findViewById(R.id.buttonRegister);
        progressBar = findViewById(R.id.progressBar);
        tvRegisterErrorMessage = findViewById(R.id.tvRegisterErrorMessage);

        // Xử lý sự kiện click của nút Register
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy thông tin đăng ký từ EditText
                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                // Kiểm tra xem email và password có hợp lệ hay không
                if (TextUtils.isEmpty(email)) {
                    editTextEmail.setError("Please enter your email");
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    editTextPassword.setError("Please enter your password");
                    return;
                }

                // Hiển thị ProgressBar
                progressBar.setVisibility(View.VISIBLE);

                // Đăng ký bằng email và password
                firebaseAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                // Ẩn ProgressBar
                                progressBar.setVisibility(View.GONE);

                                if (task.isSuccessful()) {
                                    // Đăng ký thành công
                                    FirebaseUser user = firebaseAuth.getCurrentUser();
                                    if (user != null) {
                                        // Chuyển sang MainActivity
                                        Intent intent = new Intent(Register.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                } else {
                                    // Đăng ký thất bại
                                    tvRegisterErrorMessage.setVisibility(View.VISIBLE);
                                    tvRegisterErrorMessage.setText("Registration failed");
                                }
                            }
                        });
            }
        });

        // Xử lý sự kiện click của TextView Login
        TextView textViewLogin = findViewById(R.id.textViewLogin);
        textViewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển sang trang đăng nhập (LoginActivity)
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });
    }
}