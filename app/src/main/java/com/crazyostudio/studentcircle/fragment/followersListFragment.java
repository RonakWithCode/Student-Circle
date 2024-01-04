package com.crazyostudio.studentcircle.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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


public class followersListFragment extends Fragment implements followingListAdapter.followingListInterface {
    // TODO: 12/18/2023 Main TODO want to add remove follow or unfollow function or click on recycler view

    FragmentFollowingListBinding binding;
    followingListAdapter followerListAdapter;
    ArrayList<followUserInfo> followerModel;

    FirebaseDatabase db;
    public followersListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentFollowingListBinding.inflate(inflater,container,false);

        ArrayList<String> followingId = new ArrayList<>();
        followerModel = new ArrayList<>();

        followerListAdapter = new followingListAdapter(null,followerModel,requireContext(),this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false);
        binding.followingList.setLayoutManager(linearLayoutManager);
        binding.followingList.setAdapter(followerListAdapter);
        db = FirebaseDatabase.getInstance();



        db.getReference().child("followInfo").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).child("followers").addListenerForSingleValueEvent(new ValueEventListener() {
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
//                        Log.i("followUserInfoTAG", "userInfo childSnapshot: "+childSnapshot);

                        followUserInfo userInfo = new followUserInfo();
                        userInfo.setName(childSnapshot.child("username").getValue(String.class));
                        userInfo.setProfilePic(childSnapshot.child("profilePictureUrl").getValue(String.class));
                        userInfo.setId(id);
                        userInfo.setType("following");
                        followerModel.add(userInfo);
//                        Log.i("followUserInfoTAG", "userInfo name: "+userInfo.getName());
                        followerListAdapter.notifyDataSetChanged();
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
    public void followingList(String id, Button button) {

    }

    @Override
    public void followerList(String id, Button button) {

    }
}
