package com.crazyostudio.studentcircle.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.crazyostudio.studentcircle.R;
import com.crazyostudio.studentcircle.databinding.FragmentFullScreenBinding;
import com.crazyostudio.studentcircle.model.Chat_Model;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;


public class FullScreenFragment extends Fragment {
    FragmentFullScreenBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase firebaseDatabase;
    private String userName;
    private String Id;
    private String Type;
    private String userDP;
    private String color;
    private String text;
    private String time;
    String sanderRoom, recRoom;

    public FullScreenFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentFullScreenBinding.inflate(inflater,container,false);
        firebaseDatabase = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        Bundle data = getArguments(); // In case of Fragment or

        if (data != null) {
            userName = data.getString("userName");
            Type = data.getString("Type");
            Id = data.getString("Id");
            userDP = data.getString("userDP");
            color = data.getString("color");
            text = data.getString("text");
            time = data.getString("time");
        }else {
            Toast.makeText(getContext(), "Is Null", Toast.LENGTH_SHORT).show();
        }
        sanderRoom = Id + Objects.requireNonNull(auth.getCurrentUser()).getUid();
        recRoom =  Objects.requireNonNull(auth.getCurrentUser()).getUid() + Id;
        if (color != null) {
            binding.ImageView.changeBackgroundColor(Integer.parseInt(color));
        }
        binding.username.setText(userName);
        Glide.with(requireContext()).load(userDP).into(binding.userImage);
        binding.text.setText(text);
        binding.BackBts.setOnClickListener(view-> getActivity().finish());
        binding.replay.setOnClickListener(view->{
            binding.replyMsg.setVisibility(View.VISIBLE);
            binding.SandBTS.setVisibility(View.VISIBLE);
        });


        binding.SandBTS.setOnClickListener(view->{
            Chat_Model Chat = new Chat_Model(auth.getUid(),binding.replyMsg.getText().toString(),userDP,color,System.currentTimeMillis(),true);
            firebaseDatabase.getReference().child("chats").child(sanderRoom).push().setValue(Chat).addOnSuccessListener(unused -> firebaseDatabase.getReference().child("chats").child(recRoom).push().setValue(Chat).addOnSuccessListener(unused1 ->{
                requireActivity().finish();
                Toast.makeText(getContext(), "Send", Toast.LENGTH_SHORT).show();
            }));
        });
        return binding.getRoot();
    }
}