package com.example.prm392_group3.activities.profile;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.prm392_group3.R;
import com.example.prm392_group3.activities.authen.Register;
import com.example.prm392_group3.adapters.ProfileAdapter;
import com.example.prm392_group3.models.OptionItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Profile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Profile extends Fragment {

    private RecyclerView recyclerView;
    private ProfileAdapter adapter;
    private List<OptionItem> dataList;

    private TextView username;
    private TextView email;

    private TextView editProfile;

    private FirebaseDatabase database;
    private DatabaseReference myRef;

    private FirebaseAuth firebaseAuth;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Profile() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Profile.
     */
    // TODO: Rename and change types and number of parameters
    public static Profile newInstance(String param1, String param2) {
        Profile fragment = new Profile();
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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dataList = new ArrayList<>();
        dataList.add(new OptionItem(R.drawable.baseline_save_24, "My Save", null));
        dataList.add(new OptionItem(R.drawable.baseline_password_24, "Change Password", null));
        dataList.add(new OptionItem(R.drawable.baseline_help_24, "FAQs", null));
        dataList.add(new OptionItem(R.drawable.baseline_logout_24, "Logout", null));
        // ...

        adapter = new ProfileAdapter(requireContext(), dataList);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);

        //Edit profile click
        editProfile = view.findViewById(R.id.editProfileText);
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), UpdateProfile.class);
                startActivity(intent);

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        username = rootView.findViewById(R.id.txtName);
        email = rootView.findViewById(R.id.txtEmail);
        recyclerView = rootView.findViewById(R.id.pofile_recycle_view);


        // Lấy dữ liệu từ DB
        database = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        myRef = database.getReference("User").child(firebaseAuth.getCurrentUser().getUid());


        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    String usernameDB= snapshot.child("username").getValue(String.class);
                    String emailDB = snapshot.child("email").getValue(String.class);
                    Log.i("Username", usernameDB);
                    Log.i("email", emailDB);
                    username.setText(usernameDB);
                    email.setText(emailDB);
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
        return rootView;
    }


}