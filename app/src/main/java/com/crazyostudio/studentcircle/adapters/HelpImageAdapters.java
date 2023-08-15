package com.crazyostudio.studentcircle.adapters;


import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.crazyostudio.studentcircle.R;
import com.crazyostudio.studentcircle.databinding.AddimagelayoutBinding;
import com.crazyostudio.studentcircle.model.HelpImageAdaptersOnclick;

import java.util.ArrayList;

public class HelpImageAdapters extends RecyclerView.Adapter<HelpImageAdapters.HelpImageAdaptersViewHolder> {
    ArrayList<Uri> image;
    Context context;
    HelpImageAdaptersOnclick onclick;

    public HelpImageAdapters(ArrayList<Uri> image, Context context,HelpImageAdaptersOnclick onclick1) {
        this.image = image;
        this.context = context;
        this.onclick = onclick1;
    }

    @NonNull
    @Override
    public HelpImageAdapters.HelpImageAdaptersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HelpImageAdapters.HelpImageAdaptersViewHolder(LayoutInflater.from(context).inflate(R.layout.addimagelayout,parent,false));

    }

    @Override
    public void onBindViewHolder(@NonNull HelpImageAdapters.HelpImageAdaptersViewHolder holder, int position) {
        Uri imagePos = image.get(position);
        holder.binding.image.setImageURI(imagePos);
        holder.binding.removeImage.setOnClickListener(r->{
            onclick.Remove(position);
        });
    }

    @Override
    public int getItemCount() {
        return image.size();
    }

    public static class HelpImageAdaptersViewHolder extends RecyclerView.ViewHolder {
        AddimagelayoutBinding binding;
        public HelpImageAdaptersViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = AddimagelayoutBinding.bind(itemView);
        }
    }
}
