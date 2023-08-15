package com.crazyostudio.studentcircle;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.crazyostudio.studentcircle.databinding.ActivityFullscreenBinding;
import com.crazyostudio.studentcircle.databinding.FragmentFullScreenBinding;

public class fullscreen extends AppCompatActivity {
    ActivityFullscreenBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFullscreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.close.setOnClickListener(view-> finish());
        // Get the image bitmap from the intent
        String image = getIntent().getStringExtra("image");

        // Set the image bitmap in the ImageView
//        binding.photoView.setImageBitmap(bitmap);
        Glide.with(this).load(image).into(binding.photoView);

        binding.photoView.setMaximumScale(5.0f); // Set the maximum zoom level
        binding.photoView.setMediumScale(3.0f); // Set the medium zoom level
        binding.photoView.setMinimumScale(1.0f);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}