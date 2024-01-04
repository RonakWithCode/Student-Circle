package com.crazyostudio.studentcircle.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.crazyostudio.studentcircle.R;
import com.crazyostudio.studentcircle.databinding.FollowerItemBinding;
import com.crazyostudio.studentcircle.databinding.FollowingItemBinding;
import com.crazyostudio.studentcircle.model.followUserInfo;

import java.util.ArrayList;

public class followingListAdapter extends RecyclerView.Adapter {
    ArrayList<followUserInfo> followingList;
    ArrayList<followUserInfo> followerList;
    Context context;
    followingListInterface listInterface;

    public followingListAdapter(ArrayList<followUserInfo> followingList, ArrayList<followUserInfo> followerList, Context context, followingListInterface listInterface) {
        this.followingList = followingList;
        this.followerList = followerList;
        this.context = context;
        this.listInterface = listInterface;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (followingList==null){
//            Show this followerList
            View view = LayoutInflater.from(context).inflate(R.layout.follower_item,parent,false);
            return new followerListAdapterViewHolder(view);
        }else {
//            show this followingList
            View view = LayoutInflater.from(context).inflate(R.layout.following_item,parent,false);
            return new followingListAdapterViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder.getClass()== followingListAdapter.followingListAdapterViewHolder.class) {
            followUserInfo userInfo =  followingList.get(position);
            ((followingListAdapterViewHolder)holder).binding.followUserNameTextView.setText(userInfo.getName());
            Glide.with(context).load(userInfo.getProfilePic()).into(((followingListAdapterViewHolder)holder).binding.followProfileImageView);
            ((followingListAdapterViewHolder)holder).binding.followButton.setOnClickListener(v -> listInterface.followingList(userInfo.getId(),((followingListAdapterViewHolder)holder).binding.followButton));

        }else {
            followUserInfo userInfo =  followerList.get(position);
            ((followerListAdapterViewHolder)holder).binding.followUserNameTextView.setText(userInfo.getName());
            Glide.with(context).load(userInfo.getProfilePic()).into(((followerListAdapterViewHolder)holder).binding.followProfileImageView);
            ((followerListAdapterViewHolder)holder).binding.followButton.setOnClickListener(v -> listInterface.followerList(userInfo.getId(),((followerListAdapterViewHolder)holder).binding.followButton));
        }
    }

    @Override
    public int getItemCount() {
        if (followingList==null){
//            Show this followerList
            return followerList.size();
        }else {
//            show this followingList
           return followingList.size();
        }
    }




    public static class followingListAdapterViewHolder extends RecyclerView.ViewHolder {
        FollowingItemBinding binding;
        public followingListAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = FollowingItemBinding.bind(itemView);
        }
    }
    public static class followerListAdapterViewHolder extends RecyclerView.ViewHolder {
        FollowerItemBinding binding;
        public followerListAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = FollowerItemBinding.bind(itemView);
        }
    }

    public interface followingListInterface{


        void followingList(String id, Button button);
        void followerList(String id,Button button );

    }
}
