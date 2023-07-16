package com.example.prm392_group3.adapters;
import com.example.prm392_group3.R;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_group3.activities.authen.Login;
import com.example.prm392_group3.activities.profile.ChangePassword;
import com.example.prm392_group3.models.OptionItem;
import com.example.prm392_group3.utils.ObjectStorageUtil;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ViewHolder> {
    private FirebaseAuth firebaseAuth;
    private List<OptionItem> dataList;
    private Context context;

    public ProfileAdapter(Context context, List<OptionItem> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Khởi tạo FirebaseAuth
        firebaseAuth = FirebaseAuth.getInstance();
        View view = LayoutInflater.from(context).inflate(R.layout.share_option_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OptionItem data = dataList.get(position);
        holder.startIcon.setImageResource(data.getStartIconResId());
        holder.descriptionText.setText(data.getDescription());

        // Lắng nghe sự kiện nhấp vào item
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Kiểm tra nội dung của TextView
                if (holder.descriptionText.getText().toString().equals("Logout")) {
                    // Delete User


                    ObjectStorageUtil.deleteObject(context, "user_data.json");

                    // Thực hiện hành động Logout
                    performLogoutAction();
                }
                if(holder.descriptionText.getText().toString().equals("Change Password")) {
                    performChangePassword();
                }
                // Thêm phần
            }
        });

        if (data.getEndIconResId() != null) {
            holder.endIcon.setVisibility(View.VISIBLE);
            holder.endIcon.setImageResource(data.getEndIconResId());
        } else {
            holder.endIcon.setVisibility(View.GONE);
        }
    }

    private void performLogoutAction() {
        firebaseAuth.signOut();

        Intent intent = new Intent(context, Login.class); //sẽ xóa tất cả các Activity khác trong stack và chỉ hiển thị trang đăng nhập
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

    private void performChangePassword() {
        Intent intent = new Intent(context, ChangePassword.class);
        context.startActivity(intent);
    }


    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView startIcon, endIcon;
        TextView descriptionText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            startIcon = itemView.findViewById(R.id.startIcon);
            endIcon = itemView.findViewById(R.id.endIcon);
            descriptionText = itemView.findViewById(R.id.descriptionText);
        }
    }
}

