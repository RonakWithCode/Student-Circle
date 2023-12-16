package com.crazyostudio.studentcircle.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.crazyostudio.studentcircle.R;
import com.crazyostudio.studentcircle.model.PostModel;
import android.content.Context;


import java.util.ArrayList;

public class PostAdapters extends RecyclerView.Adapter<PostAdapters.PostAdaptersViewHolder>{
    ArrayList<PostModel> postModels;
    Context context;
    String PostType;

    public PostAdapters(ArrayList<PostModel> postModels, Context context, String postType) {
        this.postModels = postModels;
        this.context = context;
        PostType = postType;
    }

    @NonNull
    @Override
    public PostAdapters.PostAdaptersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PostAdapters.PostAdaptersViewHolder(LayoutInflater.from(context).inflate(R.layout.post_view,parent,false));

    }

    @Override
    public void onBindViewHolder(@NonNull PostAdapters.PostAdaptersViewHolder holder, int position) {
        PostModel post = postModels.get(position);
        // Write code

    }

    @Override
    public int getItemCount() {
        return postModels.size();
    }

    public static class PostAdaptersViewHolder extends RecyclerView.ViewHolder {
        public PostAdaptersViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
