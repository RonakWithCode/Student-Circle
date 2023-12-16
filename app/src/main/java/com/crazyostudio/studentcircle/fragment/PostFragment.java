package com.crazyostudio.studentcircle.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.crazyostudio.studentcircle.R;
import com.crazyostudio.studentcircle.databinding.FragmentPostBinding;


public class PostFragment extends Fragment {
    FragmentPostBinding binding;
    public PostFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPostBinding.inflate(inflater,container,false);
//        binding.postRecycler
//        PostAdapters
        return binding.getRoot();
    }
}