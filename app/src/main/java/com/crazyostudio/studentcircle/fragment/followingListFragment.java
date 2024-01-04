package com.crazyostudio.studentcircle.fragment;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.crazyostudio.studentcircle.R;
import com.crazyostudio.studentcircle.adapters.followingListAdapter;
import com.crazyostudio.studentcircle.databinding.FragmentFollowingListBinding;
import com.crazyostudio.studentcircle.model.followUserInfo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;


public class followingListFragment extends Fragment implements com.crazyostudio.studentcircle.adapters.followingListAdapter.followingListInterface {
    FragmentFollowingListBinding binding;
    followingListAdapter followingListAdapter;
    ArrayList<followUserInfo> followingModel;

    FirebaseDatabase db;
    public followingListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentFollowingListBinding.inflate(inflater, container, false);
        ArrayList<String> followingId = new ArrayList<>();
        followingModel = new ArrayList<>();
        followingListAdapter = new followingListAdapter(followingModel,null,requireContext(),this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false);
        binding.followingList.setLayoutManager(linearLayoutManager);
        binding.followingList.setAdapter(followingListAdapter);
        db = FirebaseDatabase.getInstance();

        db.getReference().child("followInfo").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).child("following").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    String followingUserId = childSnapshot.getValue(String.class);
                    followingId.add(followingUserId);
                }
                LoadUser(followingId);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return binding.getRoot();
    }

    void LoadUser(ArrayList<String> UserIds){
        if (UserIds.isEmpty()){
//TODO       write code on it..
        }else {
            for (String id: UserIds) {

                db.getReference().child("UserInfo").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot childSnapshot) {
                            Log.i("followUserInfoTAG", "userInfo childSnapshot: "+childSnapshot);

                            followUserInfo userInfo = new followUserInfo();
                            userInfo.setName(childSnapshot.child("username").getValue(String.class));
                            userInfo.setProfilePic(childSnapshot.child("profilePictureUrl").getValue(String.class));
                            userInfo.setId(id);
                            userInfo.setType("following");
                            followingModel.add(userInfo);
                            Log.i("followUserInfoTAG", "userInfo name: "+userInfo.getName());
                            followingListAdapter.notifyDataSetChanged();
//                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // TODO: 12/18/2023  write code of dialog box
                    }
                });
            }
        }
    }

    @Override
    public void followingList(String id, Button followBTN) {
        Toast.makeText(requireContext(), "Some", Toast.LENGTH_SHORT).show();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        ArrayList<String> currentUserFollowingIds = new ArrayList<>();
        ArrayList<String> otherUserIdsArray = new ArrayList<>();

        database.getReference().child("followInfo").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).child("following").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    if (snapshot.exists())
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    String followingUserId = childSnapshot.getValue(String.class);
                    currentUserFollowingIds.add(followingUserId);
                }
                currentUserFollowingIds.remove(id);
                database.getReference().child("followInfo").child(FirebaseAuth.getInstance().getUid()).child("following").setValue(currentUserFollowingIds);
                database.getReference().child("UserInfo").child(FirebaseAuth.getInstance().getUid()).child("followingCount").setValue(currentUserFollowingIds.size());
                database.getReference().child("followInfo").child(id).child("followers").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                if (snapshot.exists()) {
                        for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                            String OtherFollowingUserId = childSnapshot.getValue(String.class);
                            otherUserIdsArray.add(OtherFollowingUserId);
                        }
                        otherUserIdsArray.remove(FirebaseAuth.getInstance().getUid());
                        database.getReference().child("followInfo").child(id).child("followers").setValue(otherUserIdsArray);
                        database.getReference().child("UserInfo").child(id).child("followersCount").setValue(otherUserIdsArray.size());
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


    }

    @Override
    public void followerList(String id, Button button) {

    }


}