package com.crazyostudio.studentcircle.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.crazyostudio.studentcircle.R;
import com.crazyostudio.studentcircle.databinding.UserfollowlayoutBinding;
import com.crazyostudio.studentcircle.model.UserInfo;

import java.util.List;

public class UserFollowAdapter extends RecyclerView.Adapter<UserFollowAdapter.UserFollowAdapterViewHolder> {

    List<UserInfo> user;
    Context context;
    UserFollowInterface userFollowInterface;
    public UserFollowAdapter(List<UserInfo> user, Context context,UserFollowInterface userFollowInterface) {
        this.user = user;
        this.context = context;
        this.userFollowInterface = userFollowInterface;
    }

    @NonNull
    @Override
    public UserFollowAdapter.UserFollowAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UserFollowAdapter.UserFollowAdapterViewHolder(LayoutInflater.from(context).inflate(R.layout.userfollowlayout,parent,false));

    }

    @Override
    public void onBindViewHolder(@NonNull UserFollowAdapter.UserFollowAdapterViewHolder holder, int position) {
        UserInfo userInfo = user.get(position);
        holder.binding.followUserNameTextView.setText(userInfo.getFullName());
        Glide.with(context).load(userInfo.getProfilePictureUrl()).placeholder(R.drawable.userimage).into(holder.binding.followProfileImageView);
        holder.binding.followButton.setOnClickListener(view->{
            userFollowInterface.Follow(userInfo,holder.binding.followButton);
        });
    }

    @Override
    public int getItemCount() {
        return user.size();
    }

    public static class UserFollowAdapterViewHolder extends RecyclerView.ViewHolder {
        UserfollowlayoutBinding binding;
        public UserFollowAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = UserfollowlayoutBinding.bind(itemView);
        }
    }
    public interface UserFollowInterface{
        void Follow(UserInfo userInfo, Button followBTN);
    }
}