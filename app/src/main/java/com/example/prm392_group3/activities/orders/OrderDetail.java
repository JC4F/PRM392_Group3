package com.example.prm392_group3.activities.orders;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.prm392_group3.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;

public class OrderDetail extends Fragment {

    public OrderDetail() {
        // Required empty public constructor
    }

    public static OrderDetail newInstance(Order order) {
        OrderDetail fragment = new OrderDetail();
        Bundle bundle = new Bundle();
        bundle.putString("resourceID", order.getResourceID());
        bundle.putString("bikeName", order.getBikeName());
        bundle.putString("userName", order.getUserName());
        bundle.putString("bookID", order.getBookID());
        bundle.putString("startDate", order.getStartDate());
        bundle.putString("endDate", order.getEndDate());
        bundle.putFloat("totalPrice", order.getTotalPrice());
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.item_orderdetail, container, false);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Lấy dữ liệu từ Bundle
        Bundle arguments = getArguments();
        if (arguments != null) {
            String resourceID = arguments.getString("resourceID");
            String bikeName = arguments.getString("bikeName");
            Float totalPrice = arguments.getFloat("totalPrice");
            String bookID = arguments.getString("bookID");
            String startDate = arguments.getString("startDate");
            String endDate = arguments.getString("endDate");
            String userName = arguments.getString("userName");

            // Gán dữ liệu vào các thành phần của Fragment
            ImageView imgProduct = view.findViewById(R.id.imgProduct);
            Picasso.get()
                    .load(resourceID)
                    .placeholder(R.drawable.inventory)
                    .into(imgProduct);

            ImageView imageViewIcon = view.findViewById(R.id.imageViewIcon);
            Picasso.get()
                    .load(resourceID)
                    .placeholder(R.drawable.iconorder)
                    .into(imageViewIcon);

            TextView rightTextBikeName = view.findViewById(R.id.rightTextBikeName);
            rightTextBikeName.setText(bikeName);

            TextView rightTextPrice = view.findViewById(R.id.rightTextPrice);
            rightTextPrice.setText("Price to pay: $" + totalPrice.toString().substring(0,4));

            TextView rightOrderIDGet = view.findViewById(R.id.rightOrderIDGet);
            rightOrderIDGet.setText("#QTK0" + bookID.substring(1,6));

            TextView rightOrderEndDate = view.findViewById(R.id.rightOrderEndate);
            rightOrderEndDate.setText(endDate);

            TextView rightOrderStartDate = view.findViewById(R.id.rightOrderStartDate);
            rightOrderStartDate.setText(startDate);

            TextView leftTextName = view.findViewById(R.id.leftTextName);
            leftTextName.setText(userName);
            buttonEvent(view);
        }
    }

    private void buttonEvent(View view){
        Button btnEnterProcess = view.findViewById(R.id.btnEnterProcess);
        Button btnEnterCompleted = view.findViewById(R.id.btnEnterCompleted);
        Button btnEnterDenied = view.findViewById(R.id.btnEnterDenied);
        ImageView backButton = view.findViewById(R.id.backButton);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().popBackStack();
            }
        });
        btnEnterProcess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateBookingStatus("Process");
            }
        });

        btnEnterCompleted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateBookingStatus("Completed");
            }
        });

        btnEnterDenied.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateBookingStatus("Denied");
            }
        });

    }
    private void updateBookingStatus(String newStatus) {
        String orderId = getArguments().getString("bookID");
        DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference("Book").child(orderId);

        orderRef.child("bookingStatus").setValue(newStatus)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getActivity(), "Order had been change status success. Wait a bit to get back to list order.", Toast.LENGTH_SHORT).show();
                        FragmentManager fragmentManager = getParentFragmentManager();
                        FragmentTransaction transaction = fragmentManager.beginTransaction();
                        transaction.replace(R.id.container, new ordersManagement());
                        transaction.commit();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Cập nhật thất bại: Xử lý lỗi
                    }
                });
    }
}