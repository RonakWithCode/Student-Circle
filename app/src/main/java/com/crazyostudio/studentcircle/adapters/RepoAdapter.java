package com.crazyostudio.studentcircle.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.crazyostudio.studentcircle.R;
import com.crazyostudio.studentcircle.databinding.RepoItemBinding;
import com.crazyostudio.studentcircle.model.Repository;

import java.util.List;

public class RepoAdapter extends RecyclerView.Adapter<RepoAdapter.ViewHolder> {

    private List<Repository> repositories;
    Context Context;
    public RepoAdapter(List<Repository> repositories,Context context) {
        this.repositories = repositories;
        this.Context  = context;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateData(List<Repository> repositories) {
        this.repositories = repositories;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.repo_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Repository repository = repositories.get(position);
        holder.binding.repName.setText(repository.getName());
        holder.binding.decs.setText(repository.getDescription());
        Glide.with(Context).load(repository.getAvatar_url()).placeholder(R.drawable.git_icon).into(holder.binding.userAvatar);
    }

    @Override
    public int getItemCount() {
        return repositories.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        RepoItemBinding binding;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = RepoItemBinding.bind(itemView);

        }
    }
}