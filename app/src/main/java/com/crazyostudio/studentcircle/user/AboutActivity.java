package com.crazyostudio.studentcircle.user;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.crazyostudio.studentcircle.ContactFromActivity;
import com.crazyostudio.studentcircle.databinding.ActivityAboutBinding;

public class AboutActivity extends AppCompatActivity {
    ActivityAboutBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAboutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.Terms.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://crazy-studio-website.web.app/PrivacyPolicy.html"));
            startActivity(browserIntent);
        });

        binding.contact.setOnClickListener(v -> {
//            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://crazy-studio-website.web.app/index.html#contact"));
//            startActivity(browserIntent);
            startActivity(new Intent(this, ContactFromActivity.class));
        });
//        binding.aboutApp.setOnClickListener(v -> {
//        });
    }

}