package com.example.prm392_group3.activities.profile;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.prm392_group3.R;
import com.example.prm392_group3.activities.MainActivity;
import com.example.prm392_group3.activities.authen.Login;
import com.example.prm392_group3.activities.store.AddOrUpddateBike;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import org.jetbrains.annotations.NotNull;

public class UpdateProfile extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    DatabaseReference myRef;

    private FirebaseDatabase database;

    EditText updateUsername;
    EditText updateEmail;
    EditText updatePhonenumber;

    Button btnUpdateProfile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);


        database = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        myRef = database.getReference("User").child(firebaseAuth.getCurrentUser().getUid());

        updateUsername = findViewById(R.id.updateUsername);
        updateEmail = findViewById(R.id.updateEmail);
        updatePhonenumber = findViewById(R.id.updatePhonenumber);
        btnUpdateProfile = findViewById(R.id.btnUpdateProfile);

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    String usernameDB= snapshot.child("username").getValue(String.class);
                    String emailDB = snapshot.child("email").getValue(String.class);
                    String phoneDB = snapshot.child("phoneNumber").getValue(String.class);
                    Log.i("Username", usernameDB);
                    Log.i("email", emailDB);
                    Log.i("phoneNumber", phoneDB);
                    updateUsername.setText(usernameDB);
                    updateEmail.setText(emailDB);
                    updatePhonenumber.setText(phoneDB);
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });


        btnUpdateProfile.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String username = updateUsername.getText().toString();
                String email = updateEmail.getText().toString();
                String phone = updatePhonenumber.getText().toString();
                myRef.child("username").setValue(username);
                myRef.child("email").setValue(email);
                myRef.child("phoneNumber").setValue(phone);
                Toast.makeText(UpdateProfile.this, "Update successfully", Toast.LENGTH_SHORT).show();
                // Chuyển sang MainActivity
                Intent intent = new Intent(UpdateProfile.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}