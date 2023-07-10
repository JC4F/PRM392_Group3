package com.example.prm392_group3.activities.admin;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_group3.R;
import com.example.prm392_group3.models.Account;

import java.util.ArrayList;
import java.util.List;

public class AccountManagement extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_account);
        List<Account> accounts = new ArrayList<>();
        Account c1 = new Account(R.drawable.default_avatar,"yasua@gmail.com","Admin","0912851956","Ba Quan");
        Account c2 = new Account(R.drawable.default_avatar,"dungssj@gmail.com","User","0912851953","Quang Dung");
        Account c3 = new Account(R.drawable.default_avatar,"dunng@gmail.com","User","0912851954","Quang Dung");
        Account c4 = new Account(R.drawable.default_avatar,"truong@gmail.com","User","0912851958","Hong Truong");
        Account c5 = new Account(R.drawable.default_avatar,"phong@gmail.com","User","0912851957","Hung Phong");
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
