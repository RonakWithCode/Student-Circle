package com.crazyostudio.studentcircle.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.crazyostudio.studentcircle.InterfaceCLass.ImageInterface;
import com.crazyostudio.studentcircle.R;
import com.crazyostudio.studentcircle.databinding.AddimagelayoutBinding;

import java.util.ArrayList;

public class ImageAdapters extends RecyclerView.Adapter<ImageAdapters.ImageAdaptersViewHolder> {
    ArrayList<Uri> image;
    Context context;
    ImageInterface imageInterface;

    public ImageAdapters(ArrayList<Uri> image, Context context,ImageInterface imageInterface) {
        this.image = image;
        this.context = context;
        this.imageInterface = imageInterface;
    }

    @NonNull
    @Override
    public ImageAdapters.ImageAdaptersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ImageAdapters.ImageAdaptersViewHolder(LayoutInflater.from(context).inflate(R.layout.addimagelayout,parent,false));

    }

    @Override
    public void onBindViewHolder(@NonNull ImageAdapters.ImageAdaptersViewHolder holder, int position) {
        Uri imagePos = image.get(position);
        Glide.with(context).load(imagePos).into(holder.binding.image);
        holder.binding.removeImage.setOnClickListener(r->{
            imageInterface.remove(position);
        });
    }

    @Override
    public int getItemCount() {
        return image.size();
    }

    public static class ImageAdaptersViewHolder extends RecyclerView.ViewHolder {
        AddimagelayoutBinding binding;
        public ImageAdaptersViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = AddimagelayoutBinding.bind(itemView);
        }
    }
}

