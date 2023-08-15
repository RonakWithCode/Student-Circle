package com.crazyostudio.studentcircle.fragment;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.crazyostudio.studentcircle.databinding.FragmentCreateStoryBinding;
import com.crazyostudio.studentcircle.model.StoryModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;
import java.util.Random;

public class CreateStoryFragment extends Fragment {
    FragmentCreateStoryBinding binding;
    ProgressDialog dialog;
    int color;
    FirebaseDatabase db;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCreateStoryBinding.inflate(inflater,container,false);
        db = FirebaseDatabase.getInstance();

        dialog = new ProgressDialog(getContext());

        binding.changeColorButton.setOnClickListener(v -> {
            Random random = new Random();
            color = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));
            binding.colorChangingImageView.changeBackgroundColor(color);
        });
        binding.AddStory.setOnClickListener(view->{
            dialog.setTitle("Adding Story");
            dialog.show();
            UploadStory();
        });
        return binding.getRoot();
//            <com.makeramen.roundedimageview.RoundedImageView
//        android:id="@+id/changeTextStyle"
//        android:layout_width="44dp"
//        android:layout_height="44dp"
//        android:layout_alignParentTop="true"
//        android:layout_marginTop="14dp"
//        android:layout_marginEnd="8dp"
//        android:layout_toStartOf="@+id/changeColorButton"
//        android:adjustViewBounds="true"
//        android:contentDescription="@string/tap_to_change_colors"
//        android:scaleType="fitXY"
//        android:src="@drawable/text_nomal"
//        app:riv_border_color="#333333"
//        app:riv_oval="true" />

    }

    private void UploadStory() {
        String ColorCode = String.valueOf(color);
        StoryModel model = new StoryModel(FirebaseAuth.getInstance().getUid(),Objects.requireNonNull(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getPhotoUrl()).toString(),Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getDisplayName(),ColorCode,"color",binding.text.getText().toString(),System.currentTimeMillis());
        db.getReference().child("Story").push().setValue(model).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(getContext(), "Story is Added ", Toast.LENGTH_SHORT).show();
                requireActivity().finish();
            }else {
                Toast.makeText(getContext(), "Story is not Added pls try after same time ", Toast.LENGTH_SHORT).show();
            }
        });
    }
}