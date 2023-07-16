package com.example.prm392_group3.activities.orders;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_group3.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {
    private List<Order> orderList;

    public void setData(List<Order> list){
        this.orderList = list;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_orders, parent, false);

        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orderList.get(position);
        if(order == null){
            return;
        }
        Picasso.get()
                .load(order.getResourceID())
                .placeholder(R.drawable.inventory)
                .into(holder.imageView);
        holder.textViewName.setText(order.getBikeName());
        holder.textViewCustomer.setText("#Customer: " + order.getUserName());
        holder.textViewBookID.setText("#QTK0" + String.valueOf(order.getBookID()).substring(0, 6));
        holder.StartDate.setText("#Start Date: " + order.getStartDate());
        holder.EndDate.setText("#End Date: " + order.getEndDate());
        holder.Price.setText("#Price Per Hour: " + order.getTotalPrice() + "$");
        holder.bookingStatus.setText("#Status: " + order.getBookingStatus());
        holder.buttonDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = ((FragmentActivity) holder.itemView.getContext()).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                OrderDetail orderDetailFragment = OrderDetail.newInstance(order);
                fragmentTransaction.replace(R.id.fragment_container, orderDetailFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        if(orderList != null){
            return orderList.size();
        }
        return 0;
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder{
        private ImageView imageView;
        private TextView textViewName;
        private TextView textViewCustomer;
        private TextView textViewBookID;
        private TextView StartDate;
        private TextView EndDate;
        private TextView Price;

        private Button buttonDetail;
        private TextView bookingStatus;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageProduct);
            textViewCustomer = itemView.findViewById(R.id.customerName);
            textViewName = itemView.findViewById(R.id.bikeName);
            textViewBookID = itemView.findViewById(R.id.bookID);
            StartDate = itemView.findViewById(R.id.startDateTextView);
            EndDate = itemView.findViewById(R.id.endDateTextView);
            Price = itemView.findViewById(R.id.totalPriceTextView);
            buttonDetail = itemView.findViewById(R.id.btnDetailsOrder);
            bookingStatus = itemView.findViewById(R.id.bookingStatus);
        }
    }
}
