package com.crazyostudio.studentcircle.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.crazyostudio.studentcircle.R;
import com.crazyostudio.studentcircle.Chats.chat;
import com.crazyostudio.studentcircle.databinding.MainlookBinding;
import com.crazyostudio.studentcircle.model.UserInfo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class UserInfoAdapters extends RecyclerView.Adapter<UserInfoAdapters.UserInfoAdaptersViewHolder> {

    ArrayList<UserInfo> userInfo;
    Context context;

    public UserInfoAdapters(ArrayList<UserInfo> userInfo, Context context) {
        this.userInfo = userInfo;
        this.context = context;
    }
    @NonNull
    @Override
    public UserInfoAdaptersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UserInfoAdaptersViewHolder(LayoutInflater.from(context).inflate(R.layout.mainlook,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull UserInfoAdaptersViewHolder holder, int position) {
        UserInfo product = userInfo.get(position);
        Glide.with(context).load(product.getUserImage()).into(holder.binding.userAvatar);
        holder.binding.username.setText(product.getName());
//        holder.binding.bio.setText(product.getBio());

        FirebaseDatabase.getInstance().getReference().child("chats").child(FirebaseAuth.getInstance().getUid()+product.getUserid()).orderByChild("sandTime").limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChildren()){
                    for (DataSnapshot snapshot1 : snapshot.getChildren()){
                        if (snapshot1.child("image").getValue().equals("false")){
                            holder.binding.lastMessage.setText("Photo");

                        }
                        else if (snapshot1.child("pdf").getValue().equals("true")) {
                            holder.binding.lastMessage.setText("PDF");
                        }
                        else {
                            holder.binding.lastMessage.setText(Objects.requireNonNull(snapshot1.child("message").getValue()).toString());
                        }
                        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm");
                        long sysTime = Long.parseLong(Objects.requireNonNull(snapshot1.child("sandTime").getValue()).toString());
                        Date date = new Date(sysTime);
                        String time = simpleDateFormat.format(date);

                        holder.binding.lastTime.setText(time);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, chat.class);
            intent.putExtra("name",product.getName());
            intent.putExtra("Images",product.getUserImage());
            intent.putExtra("Bio",product.getBio());
            intent.putExtra("UserId",product.getUserid());
            context.startActivity(intent);

        });
    }

    @Override
    public int getItemCount() {
        return userInfo.size();
    }
    public static class UserInfoAdaptersViewHolder  extends RecyclerView.ViewHolder {
        MainlookBinding binding;

        public UserInfoAdaptersViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = MainlookBinding.bind(itemView);
        }
    }
}

