package com.crazyostudio.studentcircle.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.crazyostudio.studentcircle.R;
import com.crazyostudio.studentcircle.databinding.StorylayoutBinding;
import com.crazyostudio.studentcircle.fragmentLoad;
import com.crazyostudio.studentcircle.model.StoryModel;

import java.util.ArrayList;

public class StoryAdapters extends RecyclerView.Adapter<StoryAdapters.StoryAdaptersViewHolder> {

    ArrayList<StoryModel> models;
    Context context;

    public StoryAdapters(ArrayList<StoryModel> models, Context context) {
        this.models = models;
        this.context = context;
    }

    @NonNull
    @Override
    public StoryAdapters.StoryAdaptersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new StoryAdapters.StoryAdaptersViewHolder(LayoutInflater.from(context).inflate(R.layout.storylayout,parent,false));

    }

    @Override
    public void onBindViewHolder(@NonNull StoryAdapters.StoryAdaptersViewHolder holder, int position) {
        StoryModel storyModel = models.get(position);
        Glide.with(context).load(storyModel.getStoryHolderDP()).into(holder.binding.userImage);
        holder.binding.username.setText(storyModel.getStoryHolderName());
        holder.binding.getRoot().setOnClickListener(view->{
            Toast.makeText(context, "Set On Click", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(context, fragmentLoad.class);
            intent.putExtra("LoadID","StoryLoaderAll");
            intent.putExtra("Type","All");
            intent.putExtra("Id",storyModel.getId());
            intent.putExtra("userName",storyModel.getStoryHolderName());
            intent.putExtra("userDP",storyModel.getStoryHolderDP());
            if (storyModel.getStoryType().equals("color")) {
                intent.putExtra("color",storyModel.getStoryColor());
                intent.putExtra("text",storyModel.getStoryShortMsg());
                intent.putExtra("time",storyModel.getStoryStartTime());
            }
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public static class StoryAdaptersViewHolder extends RecyclerView.ViewHolder {
        StorylayoutBinding binding;
        public StoryAdaptersViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = StorylayoutBinding.bind(itemView);
        }
    }
}
