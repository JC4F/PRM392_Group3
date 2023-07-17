package com.example.prm392_group3.activities.profile;

import android.util.Log;
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
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.core.Tag;

public class ChangePassword extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        EditText txtPassword = findViewById(R.id.txtChangePassword);
        EditText currentPassword = findViewById(R.id.txtOldPassword);

        Button btn = findViewById(R.id.btnChangePassword);
        /*btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user.updatePassword(txtPassword.toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("Password Update", "User password updated.");
                            Toast.makeText(ChangePassword.this, "Update success", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });*/

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = user.getEmail().toString();
                AuthCredential credential = EmailAuthProvider
                        .getCredential(email, txtPassword.getText().toString());

                user.reauthenticate(credential)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Log.d("Authen", "User re-authenticated.");
                            }
                        });
                String newPassword = txtPassword.getText().toString();
                user.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("Change password", "User password updated.");
                        } else {
                            Log.d("Change password", "Error password not updated");
                        }
                    }
                });
            }
        });


    }
}