package com.example.prm392_group3.adapters;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.prm392_group3.R;
import com.example.prm392_group3.activities.MainActivity;
import com.example.prm392_group3.activities.blog.NewsDetail;
import com.example.prm392_group3.models.Comment;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.prm392_group3.models.Comment;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {
    private List<Comment> commentList;  // Assuming you have a list of CommentItem objects
    private  Context context;

    public CommentAdapter(List<Comment> commentList, Context context) {
        this.commentList = commentList;
        this.context = context;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
        return new CommentViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        Comment commentItem = commentList.get(position);

        // Bind the data to the views in the CommentViewHolder
        holder.commentDes.setText(commentItem.getCommentContent().length()<=30?commentItem.getCommentContent():commentItem.getCommentContent().substring(0, 30)+"...");
        holder.commentUserName.setText(commentItem.getUserName().length()<=30?"User: "+commentItem.getUserName():"User: "+commentItem.getUserName().substring(0, 30)+"...");
        SimpleDateFormat inputDateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
        SimpleDateFormat outputDateFormat = new SimpleDateFormat("MM-dd-yyyy", Locale.ENGLISH);

        try {
            Date date = inputDateFormat.parse(commentItem.getCreatedTime());
            String formattedDate = outputDateFormat.format(date);
            holder.commentDate.setText("Created at: "+formattedDate);
            // Use the formatted date as needed
        } catch (ParseException e) {
            // Handle the exception (e.g., display an error message)
            holder.commentDate.setText("Created at: "+commentItem.getCreatedTime());
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    // Inner class for the ViewHolder
    public class CommentViewHolder extends RecyclerView.ViewHolder {
        public TextView commentDes;
        public TextView commentUserName;
        public TextView commentDate;

        public CommentViewHolder(View itemView) {
            super(itemView);
            commentUserName = itemView.findViewById(R.id.comment_title);
            commentDes = itemView.findViewById(R.id.comment_desc);
            commentDate = itemView.findViewById(R.id.comment_date);

        }
    }
}

