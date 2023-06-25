package com.example.prm392_group3.utils;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.example.prm392_group3.activities.authen.Login;
import com.example.prm392_group3.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserUtils {
    private FirebaseAuth firebaseAuth;
    private DatabaseReference userRef;
    private FirebaseUser userAuthenFB;
    private User userData;
    private Context context;

    public UserUtils(Context context) {
        this.context = context;
        initializeUserData();
    }

    private void initializeUserData() {
        // Get the authenticated user
        firebaseAuth = FirebaseAuth.getInstance();
        userAuthenFB = firebaseAuth.getCurrentUser();
        if (userAuthenFB == null) {
            // User not authenticated, redirect to login activity
            Intent intent = new Intent(context, Login.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            context.startActivity(intent);
        } else {
            String userId = userAuthenFB.getUid();
            userRef = FirebaseDatabase.getInstance().getReference("User").child(userId);

            // Read user data from Firebase Realtime Database
            userRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        userData = dataSnapshot.getValue(User.class);
                        if (userData != null) {
                            // Process user data
                            // ...
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle error
                    // ...
                }
            });
        }
    }

    // Getter and setter methods for fields
    public FirebaseAuth getFirebaseAuth() {
        return firebaseAuth;
    }

    public void setFirebaseAuth(FirebaseAuth firebaseAuth) {
        this.firebaseAuth = firebaseAuth;
    }

    public DatabaseReference getUserRef() {
        return userRef;
    }

    public void setUserRef(DatabaseReference userRef) {
        this.userRef = userRef;
    }

    public FirebaseUser getUserAuthenFB() {
        return userAuthenFB;
    }

    public void setUserAuthenFB(FirebaseUser userAuthenFB) {
        this.userAuthenFB = userAuthenFB;
    }

    public User getUserData() {
        return userData;
    }

    public void setUserData(User userData) {
        this.userData = userData;
    }
}
