package com.example.prm392_group3.activities.orders;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_group3.R;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ordersManagement#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ordersManagement extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    ShimmerFrameLayout shimmerLayout;
    ConstraintLayout layout;
    private RecyclerView recyclerView;
    private OrderAdapter adapter;
    private DatabaseReference fbDatabase;
    private List<Order> orderList;
    private boolean isDataLoaded = false;
    private Fragment fragment;
    public ordersManagement() {
        // Required empty public constructor
    }

    public static ordersManagement newInstance(String param1, String param2) {
        ordersManagement fragment = new ordersManagement();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_orders_management, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        shimmerLayout = view.findViewById(R.id.shimmer_view);
        layout = view.findViewById(R.id.mainContent);

        layout.setVisibility(View.INVISIBLE);
        shimmerLayout.startShimmer();
        if (!isDataLoaded) {
            listAllData(new OrderCallback() {
                @Override
                public void onOrdersLoaded(List<Order> orders) {
                    processOrderList(orders);
                    isDataLoaded = true;
                    showOrderList();
                    shimmerLayout.stopShimmer();
                    shimmerLayout.setVisibility(View.INVISIBLE);
                    layout.setVisibility(View.VISIBLE);
                }
            });
        } else {
            showOrderList();
            shimmerLayout.stopShimmer();
            shimmerLayout.setVisibility(View.INVISIBLE);
            layout.setVisibility(View.VISIBLE);
        }

        return view;
    }

    private void showOrderList() {
        adapter = new OrderAdapter();
        adapter.setData(orderList);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(adapter);
    }

    private void processOrderList(List<Order> orders) {
        orderList = orders;
    }

    public interface OrderCallback {
        void onOrdersLoaded(List<Order> orders);
    }

    private void listAllData(OrderCallback callback) {
        List<Order> orders = new ArrayList<>();

        fbDatabase = FirebaseDatabase.getInstance().getReference("Book");
        fbDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<CompletableFuture<Void>> futures = new ArrayList<>();

                for (DataSnapshot bookSnapshot : dataSnapshot.getChildren()) {
                    String bookKey = bookSnapshot.getKey();
                    if (bookKey != null) {
                        Order order = new Order();
                        String bikeID = bookSnapshot.child("BikeID").getValue(String.class);
                        DatabaseReference bikeRef = FirebaseDatabase.getInstance().getReference("Bike")
                                .child(String.valueOf(bikeID));

                        CompletableFuture<Void> future = new CompletableFuture<>();
                        futures.add(future);

                        bikeRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                                    String keyChild = childSnapshot.getKey();
                                    if(keyChild.equals("name")){
                                        order.setBikeName(childSnapshot.getValue(String.class));
                                    }
                                }
                                future.complete(null);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                future.completeExceptionally(databaseError.toException());
                            }
                        });

                        //order.setResourceID(R.drawable.default_avatar);
                        order.setBookID(bookSnapshot.child("BookingID").getValue(String.class));
                        order.setBikeID(bikeID);
                        order.setBookingStatus(bookSnapshot.child("BookingStatus").getValue(String.class));
                        order.setEndDate(bookSnapshot.child("EndDateTime").getValue(String.class));
                        order.setStartDate(bookSnapshot.child("StartDateTime").getValue(String.class));
                        order.setTotalPrice(bookSnapshot.child("TotalPrice").getValue(Integer.class).intValue());
                        order.setUserID(bookSnapshot.child("UserID").getValue(Integer.class).intValue());
                        orders.add(order);
                    }
                }

                // Sử dụng CompletableFuture.allOf để đợi tất cả các CompletableFuture hoàn thành
                CompletableFuture<Void> allFutures = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));

                // Khi tất cả CompletableFuture hoàn thành, gọi callback với danh sách đơn hàng đã được cập nhật tên xe
                allFutures.thenAccept((Void) -> {
                    callback.onOrdersLoaded(orders);
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }
}