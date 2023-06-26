package com.example.prm392_group3.activities.admin;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_group3.R;
import com.example.prm392_group3.models.Account;

import static com.example.prm392_group3.activities.store.Store.loadingProgressBar;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import com.example.prm392_group3.models.User;
import com.example.prm392_group3.utils.ObjectStorageUtil;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class AccountManagement extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_account);
        List<Account> accounts = new ArrayList<>();
        Account c1 = new Account(R.drawable.default_avatar,"yasua@gmail.com","0","0912851956");
        Account c2 = new Account(R.drawable.default_avatar,"dungssj@gmail.com","0","0912851953");
        Account c3 = new Account(R.drawable.default_avatar,"dunng@gmail.com","0","0912851954");
        Account c4 = new Account(R.drawable.default_avatar,"truong@gmail.com","0","0912851958");
        Account c5 = new Account(R.drawable.default_avatar,"phong@gmail.com","0","0912851957");
        accounts.add(c1);
        accounts.add(c2);
        accounts.add(c3);
        accounts.add(c4);
        accounts.add(c5);
        AccountAdapter adapter = new AccountAdapter(accounts);
        RecyclerView rec = findViewById(R.id.rec_Chapter);
        RecyclerView.LayoutManager layout_mag = new LinearLayoutManager(this);
        rec.setLayoutManager(layout_mag);
        rec.setAdapter(adapter);
    }
}
