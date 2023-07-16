package com.example.prm392_group3.activities.orders;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

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
    public ordersManagement() {}

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
        loadDataView();
        buttonEvent(view);
        ImageView searchIcon = view.findViewById(R.id.SearchIcon);
        searchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSearchDialog(Gravity.TOP);
            }
        });
        return view;
    }

    private void showSearchDialog(int gravity) {
        Dialog dialog = new Dialog(requireContext());
        dialog.setContentView(R.layout.item_searchbar);

        EditText searchEditText = dialog.findViewById(R.id.searchEditText);
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String searchText = s.toString();
                searchData(searchText, new OrderCallback() {
                    @Override
                    public void onOrdersLoaded(List<Order> orders) {
                    }
                });
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = gravity;
        window.setAttributes(windowAttributes);
        dialog.show();
    }

    private void loadDataView(){
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
    }

    private void buttonEvent(View view){
        Button btnAll = view.findViewById(R.id.btnAll);
        Button btnPending = view.findViewById(R.id.btnPending);
        Button btnProcess = view.findViewById(R.id.btnProcess);
        Button btnCompleted = view.findViewById(R.id.btnCompleted);
        Button btnDenied = view.findViewById(R.id.btnDenied);

        btnAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchData("", new OrderCallback() {
                    @Override
                    public void onOrdersLoaded(List<Order> orders) {
                    }
                });
            }
        });

        btnPending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchData("", new OrderCallback() {
                    @Override
                    public void onOrdersLoaded(List<Order> orders) {
                        showOrderList();
                    }
                });
            }
        });

        btnProcess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchData("Process", new OrderCallback() {
                    @Override
                    public void onOrdersLoaded(List<Order> orders) {
                    }
                });
            }
        });

        btnCompleted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchData("Completed", new OrderCallback() {
                    @Override
                    public void onOrdersLoaded(List<Order> orders) {
                    }
                });
            }
        });

        btnDenied.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchData("Denied", new OrderCallback() {
                    @Override
                    public void onOrdersLoaded(List<Order> orders) {
                    }
                });
            }
        });
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

    private void searchData(String keyword, OrderCallback callback) {
        if (keyword.isEmpty() || keyword == "") {
            listAllData(new OrderCallback() {
                @Override
                public void onOrdersLoaded(List<Order> orders) {
                    adapter.setData(orders);
                    adapter.notifyDataSetChanged();
                }
            });
            return;
        }

        List<Order> searchResults = new ArrayList<>();

        for (Order order : orderList) {
            if (order.getBikeName() != null && order.getBikeName().toLowerCase().contains(keyword.toLowerCase())
                    || order.getBookID() != null && order.getBookID().toLowerCase().contains(keyword.toLowerCase())
                    || order.getBookingStatus() != null && order.getBookingStatus().toLowerCase().contains(keyword.toLowerCase())) {
                searchResults.add(order);
            }
        }

        callback.onOrdersLoaded(searchResults);
        adapter.setData(searchResults);
        adapter.notifyDataSetChanged();
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
                        String bikeID = bookSnapshot.child("bikeID").getValue(String.class);
                        String userID = bookSnapshot.child("userID").getValue(String.class);
                        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("User")
                                .child(String.valueOf(userID));
                        DatabaseReference bikeRef = FirebaseDatabase.getInstance().getReference("Bike")
                                .child(String.valueOf(bikeID));

                        CompletableFuture<Void> future = new CompletableFuture<>();
                        futures.add(future);

                        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                                    String keyChild = childSnapshot.getKey();
                                    if(keyChild.equals("username")){
                                        order.setUserName(childSnapshot.getValue(String.class));
                                    }
                                }
                                future.complete(null);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                future.completeExceptionally(databaseError.toException());
                            }
                        });

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
                        order.setBookID(bookSnapshot.child("bookID").getValue(String.class));
                        order.setBikeID(bikeID);
                        order.setBookingStatus(bookSnapshot.child("bookingStatus").getValue(String.class));
                        order.setEndDate(bookSnapshot.child("endDate").getValue(String.class));
                        order.setStartDate(bookSnapshot.child("startDate").getValue(String.class));
                        order.setTotalPrice(bookSnapshot.child("totalPrice").getValue(Float.class).floatValue());
                        order.setUserID(bookSnapshot.child("userID").getValue(String.class));
                        orders.add(order);
                    }
                }

                CompletableFuture<Void> allFutures = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));

                allFutures.thenAccept((Void) -> {
                    callback.onOrdersLoaded(orders);
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });


    }
}