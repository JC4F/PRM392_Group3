package com.example.prm392_group3.activities.authen;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.prm392_group3.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class Forgotpass extends AppCompatActivity {
    private Button btnForgotPass;
    private EditText editTextEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpass);

        btnForgotPass = findViewById(R.id.buttonForgotPassword);
        editTextEmail = findViewById(R.id.editTextEmail);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        btnForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = editTextEmail.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    // Hiển thị thông báo lỗi nếu email trống
                    Toast.makeText(getApplicationContext(), "Vui lòng nhập email", Toast.LENGTH_SHORT).show();
                } else {
                    // Gửi email đặt lại mật khẩu
                    firebaseAuth.sendPasswordResetEmail(email)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        // Hiển thị thông báo thành công nếu gửi email thành công
                                        Toast.makeText(getApplicationContext(), "Đã gửi email đặt lại mật khẩu", Toast.LENGTH_SHORT).show();
                                    } else {
                                        // Hiển thị thông báo lỗi nếu gửi email không thành công
                                        Toast.makeText(getApplicationContext(), "Gửi email đặt lại mật khẩu thất bại", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });
    }
}