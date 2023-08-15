package com.crazyostudio.studentcircle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;

import com.crazyostudio.studentcircle.fragment.FullScreenFragment;
import com.crazyostudio.studentcircle.fragment.SelfFullScreenFragment;
import com.crazyostudio.studentcircle.fragment.ShareNotesFragment;
import com.crazyostudio.studentcircle.model.SubjectModel;
import com.crazyostudio.studentcircle.model.noNetworkFragment;

import java.util.ArrayList;

public class fragmentLoad extends AppCompatActivity {
    String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_load);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (!getIntent().getStringExtra("LoadID").isEmpty()) {
             id = getIntent().getStringExtra("LoadID");
        }else {
            finish();
        }
        Intent intent = getIntent();
        Bundle data = new Bundle();

        switch (id){
            case "network":
                // Load the fragment in the container
                fragmentTransaction.replace(R.id.loader, new noNetworkFragment());
                fragmentTransaction.commit();
                break;
            case "StoryLoaderAll":
                data.putString("userName", intent.getStringExtra("userName"));
                data.putString("Type", intent.getStringExtra("Type"));
                data.putString("Id", intent.getStringExtra("Id"));
                data.putString("userDP", intent.getStringExtra("userDP"));
                data.putString("color", intent.getStringExtra("color"));
                data.putString("text", intent.getStringExtra("text"));
                data.putString("time",  intent.getStringExtra("time"));
                Fragment f = new FullScreenFragment();
                f.setArguments(data);
                fragmentTransaction.replace(R.id.loader,f);
                fragmentTransaction.commit();
                break;
            case "StoryLoaderSelf":
                data.putString("userName", intent.getStringExtra("userName"));
                data.putString("Type", intent.getStringExtra("Type"));
                data.putString("Id", intent.getStringExtra("Id"));
                data.putString("userDP", intent.getStringExtra("userDP"));
                data.putString("color", intent.getStringExtra("color"));
                data.putString("text", intent.getStringExtra("text"));
                data.putString("time",  intent.getStringExtra("time"));
                Fragment fullScreenFragment = new SelfFullScreenFragment();
                fullScreenFragment.setArguments(data);
                fragmentTransaction.replace(R.id.loader,fullScreenFragment);
                fragmentTransaction.commit();
                break;
            case "shareList":
//                SubjectModel receivedModel = getIntent().getParcelableExtra("subjectModel");
                data.putParcelable("subjectModel",getIntent().getParcelableExtra("subjectModel"));
                Fragment NotesImage = new ShareNotesFragment();
                NotesImage.setArguments(data);
                fragmentTransaction.replace(R.id.loader,NotesImage);
                fragmentTransaction.commit();
                break;
        }
    }
}