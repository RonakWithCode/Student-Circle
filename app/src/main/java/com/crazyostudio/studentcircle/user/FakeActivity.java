package com.crazyostudio.studentcircle.user;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.crazyostudio.studentcircle.databinding.ActivityFakeBinding;

public class FakeActivity extends AppCompatActivity {
    ActivityFakeBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFakeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

    }
}