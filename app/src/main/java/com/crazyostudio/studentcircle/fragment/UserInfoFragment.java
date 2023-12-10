package com.crazyostudio.studentcircle.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.crazyostudio.studentcircle.adapters.UserInfoAdapters;
import com.crazyostudio.studentcircle.databinding.ActivityFragmentLoadBinding;
import com.crazyostudio.studentcircle.databinding.FragmentUserInfoBinding;
import com.crazyostudio.studentcircle.model.UserInfo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;


public class UserInfoFragment extends Fragment {
    FragmentUserInfoBinding binding;
    FirebaseAuth auth;
    UserInfoAdapters userInfoAdapters;
    FirebaseDatabase users;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentUserInfoBinding.inflate(inflater,container,false);
        auth = FirebaseAuth.getInstance();
        users = FirebaseDatabase.getInstance();
//        if (CurrentInternetConnection.getInternetConnectionType(getContext())){
//            Intent intent = new Intent(getContext(), ActivityFragmentLoadBinding.class);
//            intent.putExtra("LoadID","network");
//            startActivity(intent);
//        }
        getUser();
        return binding.getRoot();
    }


    void getUser() {
        ArrayList<UserInfo> userInfoS = new ArrayList<>();
        userInfoAdapters = new UserInfoAdapters(userInfoS, getContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.recyclerView.setLayoutManager(layoutManager);
        binding.recyclerView.setAdapter(userInfoAdapters);
        users.getReference().child("UserInfo").addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userInfoS.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    UserInfo userInfo = snapshot1.getValue(UserInfo.class);
                    if (!Objects.equals(snapshot1.getKey(), auth.getUid())) {
                        assert userInfo != null;
//                        userInfo.setUserid(snapshot1.getKey());
                        userInfoS.add(userInfo);

                    }
                    userInfoAdapters.notifyDataSetChanged();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}