package com.crazyostudio.studentcircle.user;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.crazyostudio.studentcircle.MainActivity;
import com.crazyostudio.studentcircle.databinding.ActivitySignUpBinding;
import com.crazyostudio.studentcircle.fragmentLoad;
import com.crazyostudio.studentcircle.model.CurrentInternetConnection;
import com.google.firebase.auth.FirebaseAuth;

public class SignUp extends AppCompatActivity {
    private ActivitySignUpBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        if (!CurrentInternetConnection.isInternetConnected(this)) {
            Intent intent = new Intent(this, fragmentLoad.class);
            intent.putExtra("LoadID","network");
            startActivity(intent);
        }
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            startActivity(new Intent(SignUp.this, MainActivity.class));
            finish();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
//
    }


}