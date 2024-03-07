package com.crazyostudio.studentcircle.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.crazyostudio.studentcircle.R;
import com.crazyostudio.studentcircle.adapters.ViewPagerAdapter;
import com.crazyostudio.studentcircle.databinding.FragmentUserProfileViewBinding;
import com.crazyostudio.studentcircle.model.UserInfo;
import com.crazyostudio.studentcircle.user.FollowMangerActivity;
import com.crazyostudio.studentcircle.user.User_Profile;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class UserProfileViewFragment extends Fragment {
    FragmentUserProfileViewBinding binding;
    FirebaseDatabase users;
    UserInfo userInfo;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() !=null) {
//
//        }
    }

    @SuppressLint("CommitTransaction")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentUserProfileViewBinding.inflate(inflater,container,false);
        users = FirebaseDatabase.getInstance();

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(requireActivity().getSupportFragmentManager());
        viewPagerAdapter.add(new PostFragment(), "Post");
//        viewPagerAdapter.add(new StoryFragment(), "Story");
        viewPagerAdapter.add(new ShareFileFragment(), "Project");
        binding.viewpager.setAdapter(viewPagerAdapter);
        binding.tabLayout.setupWithViewPager(binding.viewpager);

        LoadUser();

        binding.following.setOnClickListener(view->{
            Intent intent = new Intent(requireContext(), FollowMangerActivity.class);
            intent.putExtra("follow",1);
            startActivity(intent);
        });
        binding.follow.setOnClickListener(view->{
            Intent intent = new Intent(requireContext(), FollowMangerActivity.class);
            intent.putExtra("follow",0);
            startActivity(intent);
        });
        binding.editBtu.setOnClickListener(view->{
            Intent intent = new Intent(requireContext(), User_Profile.class);
//            intent.putExtra("follow",0);
            startActivity(intent);
        });


        return binding.getRoot();
    }
    void LoadUser(){

        users.getReference().child("UserInfo").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userInfo = snapshot.getValue(UserInfo.class);
                if (userInfo!=null) {
                    binding.Name.setText(userInfo.getFullName());
                    binding.username.setText(userInfo.getUsername());
                    Glide.with(requireContext()).load(userInfo.getProfilePictureUrl()).placeholder(R.drawable.userimage).into(binding.UserDP);
                    binding.bio.setText(userInfo.getBio());
                    binding.follow.setText(userInfo.getFollowersCount()+"\nfollow");
                    binding.following.setText(userInfo.getFollowingCount()+"\nfollowing");
//                    binding.post.setText(userInfo.getMediaIds().size()+"\nPost");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(requireContext(), error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }
}