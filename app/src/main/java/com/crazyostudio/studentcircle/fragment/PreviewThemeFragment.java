package com.crazyostudio.studentcircle.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.crazyostudio.studentcircle.MainActivity;
import com.crazyostudio.studentcircle.databinding.FragmentPreviewThemeBinding;

public class PreviewThemeFragment extends Fragment {
    FragmentPreviewThemeBinding binding;
    int imageResourceId;
    String Id;
    public PreviewThemeFragment(){}


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Id = getArguments().getString("ThemeID");
            imageResourceId = getArguments().getInt("ImageType",0);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding  = FragmentPreviewThemeBinding.inflate(inflater,container,false);
        binding.getRoot().setBackgroundResource(imageResourceId);
        binding.setButton.setOnClickListener(view->{
                SharedPreferences sharedPreferences = requireContext().getSharedPreferences("ImageResourceId", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt(Id, imageResourceId);
                editor.apply();
                startActivity(new Intent(requireActivity(), MainActivity.class));
        });
        return binding.getRoot();
    }
}