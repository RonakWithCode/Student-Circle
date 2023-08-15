package com.crazyostudio.studentcircle.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.crazyostudio.studentcircle.databinding.FragmentSelfFullScreenBinding;


public class SelfFullScreenFragment extends Fragment {
    FragmentSelfFullScreenBinding binding;

    private String userName;
    private String Type;
    private String userDP;
    private String color;
    private String text;
    private String time;


    public SelfFullScreenFragment() {}


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSelfFullScreenBinding.inflate(inflater,container,false);

        Bundle data = getArguments();
        if (data != null) {
            userName = data.getString("userName");
            Type = data.getString("Type");
            userDP = data.getString("userDP");
            color = data.getString("color");
            text = data.getString("text");
            time = data.getString("time");

        }else {
            Toast.makeText(getContext(), "Is Null", Toast.LENGTH_SHORT).show();
        }
        if (color != null) {
            binding.ImageView.changeBackgroundColor(Integer.parseInt(color));
        }
        binding.username.setText(userName);
        Glide.with(requireContext()).load(userDP).into(binding.userImage);
        binding.text.setText(text);
        binding.BackBts.setOnClickListener(view-> getActivity().finish());
        return binding.getRoot();
    }
}