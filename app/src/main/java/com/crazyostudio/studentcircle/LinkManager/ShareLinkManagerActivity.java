package com.crazyostudio.studentcircle.LinkManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import com.crazyostudio.studentcircle.MainActivity;
import com.crazyostudio.studentcircle.adapters.ShareNotesAdapters;
import com.crazyostudio.studentcircle.databinding.ActivityShareLinkManagerBinding;
import com.crazyostudio.studentcircle.model.SubjectModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
public class ShareLinkManagerActivity extends AppCompatActivity {
    ActivityShareLinkManagerBinding binding;
    FirebaseDatabase users;
    ArrayList<String> notes;
    String path;
    ShareNotesAdapters adapters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityShareLinkManagerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // Get the Intent that started this activity
        users = FirebaseDatabase.getInstance();
        notes = new ArrayList<>();
        Intent intent = getIntent();
        if (intent != null && Intent.ACTION_VIEW.equals(intent.getAction())) {
            Uri data = intent.getData();
            if (data != null) {
                path = data.getQueryParameter("path");
            }
        }
        binding.BackBts.setOnClickListener(back->startActivity(new Intent(this, MainActivity.class)));
        binding.share.setOnClickListener(share->{
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Share Notes");
            shareIntent.putExtra(Intent.EXTRA_TEXT, path);
            startActivity(Intent.createChooser(shareIntent, "Share link"));
        });


        adapters = new ShareNotesAdapters(notes, this);
        GridLayoutManager layoutManager = new GridLayoutManager(this,2);
        binding.notes.setLayoutManager(layoutManager);
        binding.notes.setAdapter(adapters);
        users.getReference().child(path).addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                        SubjectModel userInfo = snapshot.getValue(SubjectModel.class);
                        assert userInfo != null;
                        notes.addAll(userInfo.getNotes());
                        adapters.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}