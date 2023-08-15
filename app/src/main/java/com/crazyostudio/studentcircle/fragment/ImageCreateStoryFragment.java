package com.crazyostudio.studentcircle.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.crazyostudio.studentcircle.R;
import com.crazyostudio.studentcircle.databinding.FragmentCreateStoryBinding;
import com.crazyostudio.studentcircle.databinding.FragmentImageCreateStoryBinding;

public class ImageCreateStoryFragment extends Fragment {
    FragmentImageCreateStoryBinding binding;
    public ImageCreateStoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentImageCreateStoryBinding.inflate(inflater,container,false);

        return binding.getRoot();
    }
}