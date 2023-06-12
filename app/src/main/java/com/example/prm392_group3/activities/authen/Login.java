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

public class Login extends AppCompatActivity {

    private EditText editTextUsername;
    private EditText editTextPassword;
    private Button buttonLogin;
    private ProgressBar progressBar;
    private TextView tvLoginErrorMessage;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Khởi tạo FirebaseAuth
        firebaseAuth = FirebaseAuth.getInstance();

        // Ánh xạ các view
        editTextUsername = findViewById(R.id.login_email);
        editTextPassword = findViewById(R.id.login_password);
        buttonLogin = findViewById(R.id.buttonLogin);
        progressBar = findViewById(R.id.progressBar);
        tvLoginErrorMessage = findViewById(R.id.tvLoginErrorMessage);

        // Xử lý sự kiện click của nút Login
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy thông tin đăng nhập từ EditText
                String email = editTextUsername.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                // Kiểm tra xem email và password có hợp lệ hay không
                if (TextUtils.isEmpty(email)) {
                    editTextUsername.setError("Please enter your email");
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    editTextPassword.setError("Please enter your password");
                    return;
                }

                // Hiển thị ProgressBar
                progressBar.setVisibility(View.VISIBLE);

                // Đăng nhập bằng email và password
                firebaseAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                // Ẩn ProgressBar
                                progressBar.setVisibility(View.GONE);

                                if (task.isSuccessful()) {
                                    // Đăng nhập thành công
                                    FirebaseUser user = firebaseAuth.getCurrentUser();
                                    if (user != null) {
                                        // Chuyển sang MainActivity
                                        Intent intent = new Intent(Login.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                } else {
                                    // Đăng nhập thất bại
                                    tvLoginErrorMessage.setVisibility(View.VISIBLE);
                                    tvLoginErrorMessage.setText("Authentication failed");
                                }
                            }
                        });
            }
        });

        // Xử lý sự kiện click của TextView Register
        TextView textViewRegister = findViewById(R.id.textViewRegister);
        textViewRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển sang trang đăng ký (RegisterActivity)
                Intent intent = new Intent(getApplicationContext(), Register.class);
                startActivity(intent);
                finish();
            }
        });
    }
}