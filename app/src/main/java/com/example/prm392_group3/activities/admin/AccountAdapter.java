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
        holder.imv_ava.setImageResource(acc.get(position).getAvatar());
        holder.tv_title.setText(acc.get(position).getEmail());
        holder.tv_des.setText(acc.get(position).getRole());
        //holder.tv.phone.setText(acc.get(position).getRole());

    }

    @Override
    public int getItemCount() {
        return acc.size();
    }

    class AccountHolder extends RecyclerView.ViewHolder {//dai dien cho layout row_chapter
        ImageView imv_ava;
        TextView tv_title;
        TextView tv_des;
        public AccountHolder(@NonNull View itemView) {
            super(itemView);
            imv_ava= itemView.findViewById(R.id.imgAvatar);
            tv_title = itemView.findViewById(R.id.tvRec);
            tv_des = itemView.findViewById(R.id.tvDes);
        }
    }
}
