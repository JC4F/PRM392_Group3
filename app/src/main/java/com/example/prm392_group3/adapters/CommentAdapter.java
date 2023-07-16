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
import com.example.prm392_group3.models.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {
    private List<Comment> commentList;  // Assuming you have a list of CommentItem objects
    private  Context context;
    DatabaseReference userRef;
    FirebaseDatabase database;

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
        database = FirebaseDatabase.getInstance();
        userRef = database.getReference("User");
        Query userQuery = userRef.orderByChild("id").equalTo(commentItem.getUserId());
        userQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    User user = userSnapshot.getValue(User.class);
                    if (user != null) {
                        // Here, you have access to the user with the ID news.getCreatedBy()
                        // You can do something with the user object
                        // For example, set the source TextView to the user's name
                        holder.commentUserName.setText(user.getUsername().length()<=30?"By: "+user.getUsername():"By: "+user.getUsername().substring(0, 30)+"...");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(context, "Failed to load user details", Toast.LENGTH_SHORT).show();
                holder.commentUserName.setText("By "+"?");

            }
        });
        // Bind the data to the views in the CommentViewHolder
        holder.commentDes.setText(commentItem.getCommentContent().length()<=30?commentItem.getCommentContent():commentItem.getCommentContent().substring(0, 30)+"...");
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

