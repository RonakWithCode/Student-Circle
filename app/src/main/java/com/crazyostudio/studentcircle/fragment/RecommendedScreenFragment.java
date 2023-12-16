package com.crazyostudio.studentcircle.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.crazyostudio.studentcircle.R;
import com.crazyostudio.studentcircle.adapters.UserFollowAdapter;
import com.crazyostudio.studentcircle.databinding.FragmentRecommendedScreenBinding;
import com.crazyostudio.studentcircle.model.UserInfo;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class RecommendedScreenFragment extends Fragment implements UserFollowAdapter.UserFollowInterface {
    FragmentRecommendedScreenBinding binding;
    FirebaseDatabase database;
    UserFollowAdapter adapter;
    List<UserInfo> user;
    String currentUserId;

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
        if (followBTN.getText().equals("following...")) {
//            for unfollow
            String FollowUserId = userInfo.getId();
            ArrayList<String> userID = new ArrayList<>();
            database.getReference().child("UserFollow").child(currentUserId).child("following").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        for (DataSnapshot childSnapshot : snapshot.getChildren()) {
//                            if (Objects.equals(childSnapshot.getKey(), FollowUserId)) {
                                String followingUserId = childSnapshot.getKey();
                                userID.add(followingUserId);
//                            }
                        }
                        userID.remove(FollowUserId);
                        if (userID.isEmpty()){
                            database.getReference().child("UserFollow").child(currentUserId).child("following").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        database.getReference().child("UserInfo").child(currentUserId).child("followingCount").setValue(userID.size());
                                        ArrayList<String> followersID = new ArrayList<>();

                                        database.getReference().child("UserFollow").child(FollowUserId).child("followers").addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                if (snapshot.exists()) {
                                                    for (DataSnapshot childSnapshot : snapshot.getChildren()) {
//                                                    if (!Objects.equals(childSnapshot.getKey(), currentUserId)) {
                                                        String followingUserId = childSnapshot.getKey();
                                                        followersID.add(followingUserId);
//                                                    }
                                                    }
                                                    followersID.remove(currentUserId);
                                                    if (followersID.isEmpty()){
                                                        database.getReference().child("UserFollow").child(FollowUserId).child("followers").removeValue();

                                                    }else {
                                                        database.getReference().child("UserFollow").child(FollowUserId).child("followers").setValue(followersID);
                                                    }
                                                    database.getReference().child("UserInfo").child(FollowUserId).child("followersCount").setValue(followersID.size());

                                                    adapter.notifyDataSetChanged();
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {
                                                // Handle onCancelled event if needed
                                            }
                                        });

                                    }
                                }
                            });

                        }else {
                            database.getReference().child("UserFollow").child(currentUserId).child("following").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {

                                        database.getReference().child("UserInfo").child(currentUserId).child("followingCount").setValue(userID.size());


                                        ArrayList<String> followersID = new ArrayList<>();
                                        database.getReference().child("UserFollow").child(FollowUserId).child("followers").addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                if (snapshot.exists()) {
                                                    for (DataSnapshot childSnapshot : snapshot.getChildren()) {
//                                                    if (!Objects.equals(childSnapshot.getKey(), currentUserId)) {
                                                        String followingUserId = childSnapshot.getKey();
                                                        followersID.add(followingUserId);
//                                                    }
                                                    }
                                                    followersID.remove(currentUserId);
                                                    if (followersID.isEmpty()){
                                                        database.getReference().child("UserFollow").child(FollowUserId).child("followers").setValue(followersID);

                                                    }else {
                                                        database.getReference().child("UserFollow").child(FollowUserId).child("followers").removeValue();

                                                    }
                                                    database.getReference().child("UserInfo").child(FollowUserId).child("followersCount").setValue(followersID.size());

                                                    adapter.notifyDataSetChanged();
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {
                                                // Handle onCancelled event if needed
                                            }
                                        });

                                    }
                                }
                            });

                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle onCancelled event if needed
                }
            });

            followBTN.setText("follow");
//            followBTN.setBackgroundColor(Color.WHITE);
            followBTN.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.purple_500));
            followBTN.setTextColor(Color.BLACK); // Replace with the desired text color
            adapter.notifyDataSetChanged();

        } else {
            String FollowUserId = userInfo.getId();
            ArrayList<String> userID = new ArrayList<>();
            database.getReference().child("UserFollow").child(currentUserId).child("following").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                            String followingUserId = childSnapshot.getKey();
                            userID.add(followingUserId);
                        }
                        userID.add(FollowUserId);
                        database.getReference().child("UserFollow").child(currentUserId).child("following").setValue(userID).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    database.getReference().child("UserInfo").child(currentUserId).child("followingCount").setValue(userID.size());
                                    ArrayList<String> followersID = new ArrayList<>();
                                    database.getReference().child("UserFollow").child(FollowUserId).child("followers").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if (snapshot.exists()) {
                                                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                                                    String followingUserId = childSnapshot.getKey();
                                                    followersID.add(followingUserId);
                                                }
                                            }
                                            followersID.add(currentUserId);
                                            database.getReference().child("UserFollow").child(FollowUserId).child("followers").setValue(followersID);
                                            database.getReference().child("UserInfo").child(FollowUserId).child("followersCount").setValue(followersID.size());

                                            adapter.notifyDataSetChanged();
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                            // Handle onCancelled event if needed
                                        }
                                    });

                                }
                            }
                        });
                    } else {
                        userID.add(FollowUserId);
                        database.getReference().child("UserFollow").child(currentUserId).child("following").setValue(userID).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    database.getReference().child("UserInfo").child(currentUserId).child("followingCount").setValue(userID.size());

                                    ArrayList<String> followersID = new ArrayList<>();
                                    database.getReference().child("UserFollow").child(FollowUserId).child("followers").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if (snapshot.exists()) {
                                                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                                                    String followingUserId = childSnapshot.getKey();
                                                    followersID.add(followingUserId);
                                                }
                                            }

                                            followersID.add(currentUserId);
                                            database.getReference().child("UserFollow").child(FollowUserId).child("followers").setValue(followersID);
                                            database.getReference().child("UserInfo").child(FollowUserId).child("followersCount").setValue(followersID.size());

                                            adapter.notifyDataSetChanged();

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                            // Handle onCancelled event if needed
                                        }
                                    });

                                }
                            }
                        });

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle onCancelled event if needed
                }
            });

            followBTN.setText("following...");
            followBTN.setBackgroundColor(Color.WHITE);
            followBTN.setTextColor(Color.BLACK); // Replace with the desired text color

        }
    }


}