package com.crazyostudio.studentcircle.fragment;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.crazyostudio.studentcircle.R;
import com.crazyostudio.studentcircle.adapters.UserFollowAdapter;
import com.crazyostudio.studentcircle.databinding.FragmentRecommendedScreenBinding;
import com.crazyostudio.studentcircle.model.UserInfo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class RecommendedScreenFragment extends Fragment implements UserFollowAdapter.UserFollowInterface {
    FragmentRecommendedScreenBinding binding;
    FirebaseDatabase database;
    UserFollowAdapter adapter;
    List<UserInfo> user;
    String currentUserId;
    FirebaseAuth auth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentRecommendedScreenBinding.inflate(inflater, container, false);
        database = FirebaseDatabase.getInstance();
        currentUserId = FirebaseAuth.getInstance().getUid();
        user = new ArrayList<>();
        adapter = new UserFollowAdapter(user, requireContext(), this);
        LinearLayoutManager manager = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
        binding.RecyclerUser.setLayoutManager(manager);
        binding.RecyclerUser.setAdapter(adapter);
        auth = FirebaseAuth.getInstance();
        LoadUser();
        return binding.getRoot();

    }


    void LoadUser() {
        database.getReference().child("UserInfo").addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    UserInfo userInfo = data.getValue(UserInfo.class);
                    if (userInfo != null && userInfo.isActive() && userInfo.getAccountVisibility().equals("public") && !userInfo.getId().equals(FirebaseAuth.getInstance().getUid())) {
                        user.add(userInfo);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("ERROR_onCancelled", "onCancelled: " + error);
            }
        });
    }
    @SuppressLint("SetTextI18n")
    @Override
    public void Follow(UserInfo userInfo, Button followBTN) {
        String otherUserId = userInfo.getId();
        ArrayList<String> currentUserFollowingIds = new ArrayList<>();
        ArrayList<String> otherUserIdsArray = new ArrayList<>();

        if (followBTN.getText().equals("following...")) {

            database.getReference().child("followInfo").child(Objects.requireNonNull(auth.getUid())).child("following").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    if (snapshot.exists())
                    for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                        String followingUserId = childSnapshot.getValue(String.class);
                        currentUserFollowingIds.add(followingUserId);
                    }
                    currentUserFollowingIds.remove(otherUserId);
                    database.getReference().child("followInfo").child(auth.getUid()).child("following").setValue(currentUserFollowingIds);
                    database.getReference().child("UserInfo").child(currentUserId).child("followingCount").setValue(currentUserFollowingIds.size());

                    database.getReference().child("followInfo").child(otherUserId).child("followers").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                if (snapshot.exists()) {
                            for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                                String OtherFollowingUserId = childSnapshot.getValue(String.class);
                                otherUserIdsArray.add(OtherFollowingUserId);
                            }
                            otherUserIdsArray.remove(currentUserId);
                            database.getReference().child("followInfo").child(otherUserId).child("followers").setValue(otherUserIdsArray);
                            database.getReference().child("UserInfo").child(otherUserId).child("followersCount").setValue(otherUserIdsArray.size());
                        }
//                                }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }


                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            followBTN.setText("follow");
            followBTN.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.purple_500));
            followBTN.setTextColor(Color.WHITE);

        }else {

            database.getReference().child("followInfo").child(Objects.requireNonNull(auth.getUid())).child("following").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    if (snapshot.exists())

                    for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                            String followingUserId = childSnapshot.getValue(String.class);
                            currentUserFollowingIds.add(followingUserId);
                        }
                        currentUserFollowingIds.add(otherUserId);
                        database.getReference().child("followInfo").child(auth.getUid()).child("following").setValue(currentUserFollowingIds);
                        database.getReference().child("UserInfo").child(currentUserId).child("followingCount").setValue(currentUserFollowingIds.size());
                        database.getReference().child("followInfo").child(otherUserId).child("followers").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                if (snapshot.exists()) {
                                    for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                                        String OtherFollowingUserId = childSnapshot.getValue(String.class);
                                        otherUserIdsArray.add(OtherFollowingUserId);
                                    }
                                    otherUserIdsArray.add(currentUserId);
                                    database.getReference().child("followInfo").child(otherUserId).child("followers").setValue(otherUserIdsArray);
                                    database.getReference().child("UserInfo").child(otherUserId).child("followersCount").setValue(otherUserIdsArray.size());
                            }
//                                }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }


                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


        followBTN.setText("following...");
        followBTN.setBackgroundColor(Color.WHITE);
        followBTN.setTextColor(Color.BLACK); // Replace with the desired text color


        }
    }


}