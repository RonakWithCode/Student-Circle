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

import com.crazyostudio.studentcircle.R;
import com.crazyostudio.studentcircle.databinding.AddimagelayoutBinding;

import java.util.ArrayList;

public class ImageAdapters extends RecyclerView.Adapter<ImageAdapters.ImageAdaptersViewHolder> {
    ArrayList<byte[]> image;
    Context context;

    public ImageAdapters(ArrayList<byte[]> image, Context context) {
        this.image = image;
        this.context = context;
    }

    @NonNull
    @Override
    public ImageAdapters.ImageAdaptersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ImageAdapters.ImageAdaptersViewHolder(LayoutInflater.from(context).inflate(R.layout.addimagelayout,parent,false));

    }

    @Override
    public void onBindViewHolder(@NonNull ImageAdapters.ImageAdaptersViewHolder holder, int position) {
        byte[] imagePos = image.get(position);
        Bitmap compressedBitmap = BitmapFactory.decodeByteArray(imagePos, 0, imagePos.length);
        holder.binding.image.setImageBitmap(compressedBitmap);
        holder.binding.removeImage.setOnClickListener(r->{
            image.remove(position);
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
