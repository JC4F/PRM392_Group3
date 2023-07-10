package com.example.prm392_group3.activities.admin;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.prm392_group3.R;
import com.example.prm392_group3.models.Account;

import java.util.List;

public class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.AccountHolder> {
    private List<Account> acc;
    public AccountAdapter(List<Account> list){
        this.acc = list;
    }

    @NonNull
    @Override
    public AccountHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_admin_user, parent, false);
        return new AccountHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AccountHolder holder, int position) {
        holder.user_item_image.setImageResource(acc.get(position).getAvatar());
        holder.user_gmail.setText(acc.get(position).getEmail());
        holder.user_role.setText(acc.get(position).getRole());
        holder.user_phone.setText(acc.get(position).getPhone());
        holder.user_userName.setText(acc.get(position).getUsername());


    }

    @Override
    public int getItemCount() {
        return acc.size();
    }

    class AccountHolder extends RecyclerView.ViewHolder {//dai dien cho layout row_chapter
        ImageView user_item_image;
        TextView user_gmail;
        TextView user_phone;
        TextView user_status;
        TextView user_role;
        TextView user_userName;


        public AccountHolder(@NonNull View itemView) {
            super(itemView);
            user_item_image= itemView.findViewById(R.id.user_item_image);
            user_gmail = itemView.findViewById(R.id.user_gmail);
            user_phone = itemView.findViewById(R.id.user_phone);
            user_status = itemView.findViewById(R.id.user_status);
            user_role = itemView.findViewById(R.id.user_role);
            user_userName = itemView.findViewById(R.id.user_userName);


        }
    }
}
