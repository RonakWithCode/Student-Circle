package com.crazyostudio.studentcircle.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.crazyostudio.studentcircle.R;
import com.crazyostudio.studentcircle.model.Repository;

import java.util.List;

public class RepoAdapter extends RecyclerView.Adapter<RepoAdapter.ViewHolder> {

    private List<Repository> repositories;

    public RepoAdapter(List<Repository> repositories) {
        this.repositories = repositories;
    }

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
        holder.repoName.setText(repository.getName());
        holder.repoDescription.setText(repository.getDescription());
    }

    @Override
    public int getItemCount() {
        return repositories.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView repoName;
        TextView repoDescription;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            repoName = itemView.findViewById(R.id.repoName);
            repoDescription = itemView.findViewById(R.id.repoDescription);
        }
    }
}
