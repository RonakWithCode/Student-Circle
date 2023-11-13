package com.crazyostudio.studentcircle.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.crazyostudio.studentcircle.R;
import com.crazyostudio.studentcircle.adapters.NotesAdapters;
import com.crazyostudio.studentcircle.databinding.FragmentReceiverProfileViewBinding;
import com.crazyostudio.studentcircle.model.SubjectModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ReceiverProfileViewFragment extends Fragment {
    FragmentReceiverProfileViewBinding binding;
    String name,dp,bio,ReceiverId;
    FirebaseDatabase firebaseDatabase;
    boolean isProgressBar = false;
    NotesAdapters notesAdapters;
    public ReceiverProfileViewFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

            // 54
            name = getArguments().getString("name");
            dp = getArguments().getString("Images");
            bio = getArguments().getString("Bio");
            ReceiverId = getArguments().getString("ReceiverId");

        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentReceiverProfileViewBinding.inflate(inflater,container,false);
        binding.BackBts.setOnClickListener(view -> requireActivity().finish());
        binding.UserName.setText(name);
        binding.bio.setText(bio);
        Glide.with(this).load(dp).into(binding.userImage);
///////////////////////////////////////////////////////////////////////////////////////////
        firebaseDatabase = FirebaseDatabase.getInstance();
        if (!isProgressBar){
            binding.ProgressStory.setVisibility(View.VISIBLE);
            isProgressBar = true;
        }
        ShareFileLoad();
        return binding.getRoot();
    }

    private void ShareFileLoad() {
        ArrayList<SubjectModel> subjectModel = new ArrayList<>();
        notesAdapters = new NotesAdapters(subjectModel,getContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        binding.storyList.setLayoutManager(layoutManager);
        binding.storyList.setAdapter(notesAdapters);
        firebaseDatabase.getReference().child("Share").child(ReceiverId).addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    SubjectModel snapshot1Value = snapshot1.getValue(SubjectModel.class);
                    subjectModel.add(snapshot1Value);
                    binding.storyList.setVisibility(View.VISIBLE);
                    if (isProgressBar){
                        binding.ProgressStory.setVisibility(View.GONE);
                    }
                    notesAdapters.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
//                new PurchaseConfirmationDialogFragment().onCreateDialog()
//
                AlertDialog s = new AlertDialog.Builder(requireContext()).setMessage(error.getMessage()).setPositiveButton(getString(R.string.ok), (dialog, which) -> {} ).create();
                s.show();
            }
        });



    }

}