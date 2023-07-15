package com.example.prm392_group3.activities.authen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.prm392_group3.R;
import com.example.prm392_group3.activities.MainActivity;
import com.example.prm392_group3.models.User;
import com.example.prm392_group3.utils.ObjectStorageUtil;
import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

public class Login extends AppCompatActivity {

    private EditText editTextUsername;
    private EditText editTextPassword;
    private Button buttonLogin;
    private ProgressBar progressBar;
    private TextView tvLoginErrorMessage;

    private FirebaseAuth firebaseAuth;
    DatabaseReference myRef;

    private Button btnGG;

    private SignInClient oneTapClient;
    private BeginSignInRequest signInRequest;

    private TextView forgotPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Khởi tạo FirebaseAuth
        firebaseAuth = FirebaseAuth.getInstance();
        myRef = FirebaseDatabase.getInstance().getReference("User");

        // Ánh xạ các view
        editTextUsername = findViewById(R.id.login_email);
        editTextPassword = findViewById(R.id.login_password);
        buttonLogin = findViewById(R.id.buttonLogin);
        progressBar = findViewById(R.id.progressBar);
        tvLoginErrorMessage = findViewById(R.id.tvLoginErrorMessage);
        forgotPass = findViewById(R.id.txtForgotPass);


        // Xử lý đăng nhập với google
        oneTapClient = Identity.getSignInClient(this);
        signInRequest = BeginSignInRequest.builder()
                .setPasswordRequestOptions(BeginSignInRequest.PasswordRequestOptions.builder()
                        .setSupported(true)
                        .build())
                .setGoogleIdTokenRequestOptions(BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                        .setSupported(true)
                        // Your server's client ID, not your Android client ID.
                        .setServerClientId(getString(R.string.default_web_client_id))
                        // Only show accounts previously used to sign in.
                        .setFilterByAuthorizedAccounts(true)
                        .build())
                // Automatically sign in when exactly one credential is retrieved.
                .setAutoSelectEnabled(true)
                .build();

        // Xử lý sự kiện click của nút Login
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy thông tin đăng nhập từ EditText
                String email = editTextUsername.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();
f
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
                                    myRef.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.exists()) {
                                                User userData = dataSnapshot.getValue(User.class);

                                                if (userData != null) {
                                                    ObjectStorageUtil.saveObject(getApplicationContext(), "user_data.json", userData);

                                                    // Chuyển sang MainActivity
                                                    Intent intent = new Intent(Login.this, MainActivity.class);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {
                                            Log.e("ObjectStorageUtil", "Error reading user data: " + databaseError.getMessage());
                                            firebaseAuth.signOut();
                                            tvLoginErrorMessage.setVisibility(View.VISIBLE);
                                            tvLoginErrorMessage.setText("Can not get user data!");
                                        }
                                    });

                                } else {
                                    // Đăng nhập thất bại
                                    tvLoginErrorMessage.setVisibility(View.VISIBLE);
                                    tvLoginErrorMessage.setText("Authentication failed");
                                }
                            }
                        });
            }
        });


        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Chuyển sang Forgot Pass Activity
                Intent intent = new Intent(Login.this, Forgotpass.class);
                startActivity(intent);
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