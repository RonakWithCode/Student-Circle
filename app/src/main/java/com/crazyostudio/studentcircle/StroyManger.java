package com.crazyostudio.studentcircle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;

import com.crazyostudio.studentcircle.fragment.CreateStoryFragment;
import com.crazyostudio.studentcircle.model.CurrentInternetConnection;

public class StroyManger extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stroy_manger);
        if (!CurrentInternetConnection.isInternetConnected(this)) {
            Intent intent = new Intent(this, fragmentLoad.class);
            intent.putExtra("LoadID","network");
            startActivity(intent);
        }
        FragmentManager fragmentManager = getSupportFragmentManager();

        // Begin the fragment transaction
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Create an instance of your fragment
        CreateStoryFragment myFragment = new CreateStoryFragment();

        // Add the fragment to the container view
        fragmentTransaction.add(R.id.canteaner, myFragment);

        // Commit the transaction
        fragmentTransaction.commit();
    }
}