package com.crazyostudio.studentcircle.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.crazyostudio.studentcircle.InterfaceCLass.LinksInterfaceAdapter;
import com.crazyostudio.studentcircle.R;
import com.crazyostudio.studentcircle.Service.AuthService;
import com.crazyostudio.studentcircle.databinding.LinkListItemBinding;
import com.crazyostudio.studentcircle.model.LinksModels;

import java.util.ArrayList;


public class LinksAdapter  extends RecyclerView.Adapter<LinksAdapter.LinkViewHolder> {
    Context context;
    ArrayList<LinksModels> mData;
//
    LinksInterfaceAdapter interfaceAdapter;

    public LinksAdapter(Context context, ArrayList<LinksModels> mData, LinksInterfaceAdapter interfaceAdapter) {
        this.context = context;
        this.mData = mData;
        this.interfaceAdapter = interfaceAdapter;
    }

    @NonNull
    @Override
    public LinksAdapter.LinkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.link_list_item, parent, false);
        return new LinksAdapter.LinkViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LinksAdapter.LinkViewHolder holder, int position) {
        LinksModels linksModels = mData.get(position);

        holder.binding.title.setText(linksModels.getTitle());
        holder.binding.link.setText(linksModels.getLinks());
        holder.binding.getRoot().setOnClickListener(view->{
            interfaceAdapter.onClick(linksModels,position);
        });
//        holder.binding.link.setText(linksModels.getLinks());

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
    static class LinkViewHolder extends RecyclerView.ViewHolder {
        LinkListItemBinding binding;
        public LinkViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = LinkListItemBinding.bind(itemView);

        }
    }

}