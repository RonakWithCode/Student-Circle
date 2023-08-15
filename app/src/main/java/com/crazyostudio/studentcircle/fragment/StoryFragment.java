package com.crazyostudio.studentcircle.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.crazyostudio.studentcircle.StroyManger;
import com.crazyostudio.studentcircle.adapters.StoryAdapters;
import com.crazyostudio.studentcircle.databinding.FragmentStoryBinding;
import com.crazyostudio.studentcircle.fragmentLoad;
import com.crazyostudio.studentcircle.model.StoryModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class StoryFragment extends Fragment {
    FragmentStoryBinding binding;
    StoryAdapters storyAdapters;
    FirebaseDatabase users;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentStoryBinding.inflate(inflater,container,false);
        users = FirebaseDatabase.getInstance();
        binding.storyAddBtu.setOnClickListener(view-> startActivity(new Intent(getContext(), StroyManger.class)));
        getStory();
        return binding.getRoot();
    }
    void getStory(){
        ArrayList<StoryModel> models = new ArrayList<>();
        storyAdapters = new StoryAdapters(models, getContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.storyList.setLayoutManager(layoutManager);
        binding.storyList.setAdapter(storyAdapters);
        users.getReference().child("Story").addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                models.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    StoryModel storyModel = snapshot1.getValue(StoryModel.class);
                    if (!storyModel.getId().equals(FirebaseAuth.getInstance().getUid())) {
//                        Toast.makeText(getContext(), storyModel.getStoryHolderName(), Toast.LENGTH_SHORT).show();
                        models.add(storyModel);
                    }else {
                        binding.storyAddBtu.setVisibility(View.GONE);
                        Glide.with(requireContext()).load(storyModel.getStoryHolderDP()).into(binding.userImage);
                        binding.getRoot().setOnClickListener(view->{
                            Toast.makeText(getContext(), "Set On Click", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getContext(), fragmentLoad.class);
                            intent.putExtra("LoadID","StoryLoaderSelf");
                            intent.putExtra("Type","Self");
                            intent.putExtra("userName",storyModel.getStoryHolderName());
                            intent.putExtra("userDP",storyModel.getStoryHolderDP());
                            if (storyModel.getStoryType().equals("color")) {
                                intent.putExtra("color",storyModel.getStoryColor());
                                intent.putExtra("text",storyModel.getStoryShortMsg());
                                intent.putExtra("time",storyModel.getStoryStartTime());
                            }
                            requireContext().startActivity(intent);
                        });

                    }
                    storyAdapters.notifyDataSetChanged();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}