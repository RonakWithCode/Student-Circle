package com.crazyostudio.studentcircle;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.crazyostudio.studentcircle.databinding.ActivityMainBinding;
import com.crazyostudio.studentcircle.fragment.HomeFragment;
import com.crazyostudio.studentcircle.fragment.RecommendedScreenFragment;
import com.crazyostudio.studentcircle.fragment.ShareFileFragment;
import com.crazyostudio.studentcircle.fragment.UserInfoFragment;
import com.crazyostudio.studentcircle.fragment.UserProfileViewFragment;
import com.crazyostudio.studentcircle.model.CurrentInternetConnection;
import com.crazyostudio.studentcircle.user.AboutActivity;
import com.crazyostudio.studentcircle.user.SignUp;
import com.crazyostudio.studentcircle.user.User_Profile;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    FirebaseAuth auth;
    private static final int PERMISSION_REQUEST_CODE = 1;

    private static final String[] PERMISSIONS_BELOW_10 = {
//            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    private static final String[] PERMISSIONS_10_AND_ABOVE = {
            Manifest.permission.CALL_PHONE
    };
    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (!CurrentInternetConnection.isInternetConnected(this)) {
            Intent intent = new Intent(this, fragmentLoad.class);
            intent.putExtra("LoadID","network");
            startActivity(intent);
        }
        auth = FirebaseAuth.getInstance();
//
//        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
//        viewPagerAdapter.add(new HomeFragment(), "Home");
//        viewPagerAdapter.add(new UserProfileViewFragment(), "User Profile");
//        viewPagerAdapter.add(new RecommendedScreenFragment(), "Chat");
////        viewPagerAdapter.add(new StoryFragment(), "Story");
//        viewPagerAdapter.add(new ShareFileFragment(), "Share");
//        binding.viewpager.setAdapter(viewPagerAdapter);
//        binding.tabLayout.setupWithViewPager(binding.viewpager);
        String[] permissions;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            permissions = PERMISSIONS_BELOW_10;
        } else {
            permissions = PERMISSIONS_10_AND_ABOVE;
        }
        requestPermissions(permissions);

//        replaceFragment(new HomeFragment());
        replaceFragment(new RecommendedScreenFragment());

        binding.bottomNavigationView.setBackground(null);
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {

            switch (item.getItemId()) {
                case R.id.home:
                    replaceFragment(new HomeFragment());
                    break;
                case R.id.share:
                    replaceFragment(new ShareFileFragment());
                    break;
                case R.id.Viedeo:
//                    replaceFragment(new SubscriptionFragment());
                    break;
                case R.id.profile:
                    replaceFragment(new UserProfileViewFragment());
                    break;
            }

            return true;
        });

        binding.fab.setOnClickListener(view -> showBottomDialog());



    }

    private void requestPermissions(String[] permissions) {
        if (!arePermissionsGranted(permissions)) {
            ActivityCompat.requestPermissions(this, permissions, PERMISSION_REQUEST_CODE);
        }

        // Permissions are already granted, proceed with your logic
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//            if (!Environment.isExternalStorageManager()) {
//
//                try {
//                    Uri uri = Uri.parse("package:" + BuildConfig.APPLICATION_ID);
//                    Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION, uri);
//                    startActivity(intent);
//                } catch (Exception ex) {
//                    Intent intent = new Intent();
//                    intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
//                    startActivity(intent);
//                }
//            }
//        }
    }

    private boolean arePermissionsGranted(String[] permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (areAllPermissionsGranted(grantResults)) {
                // All permissions are granted, proceed with your logic
            } else {
                // Permissions are not granted, handle the denied permissions
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

    }

    private boolean areAllPermissionsGranted(int[] grantResults) {
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mainitemmenu, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.Post) {
            Toast.makeText(this, "post", Toast.LENGTH_SHORT).show();
//            startActivity(new Intent(MainActivity.this, User_Profile.class));
        }
        else if (item.getItemId() == R.id.chat) {
            replaceFragment(new  UserInfoFragment());
        }
        else if (item.getItemId() == R.id.Profile) {
            startActivity(new Intent(MainActivity.this, User_Profile.class));
        }
        else if (item.getItemId() == R.id.about) {
//            Toast.makeText(this, "Coming soon   ", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MainActivity.this, AboutActivity.class));
        }
        else if (item.getItemId() == R.id.logout) {
            auth.signOut();
            startActivity(new Intent(MainActivity.this, SignUp.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    private  void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    private void showBottomDialog() {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottomsheetlayout);

        LinearLayout videoLayout = dialog.findViewById(R.id.layoutVideo);
        LinearLayout shortsLayout = dialog.findViewById(R.id.layoutShorts);
        ImageView cancelButton = dialog.findViewById(R.id.cancelButton);

        videoLayout.setOnClickListener(v -> {

            dialog.dismiss();
            Toast.makeText(MainActivity.this,"Upload a Video is clicked",Toast.LENGTH_SHORT).show();

        });

        shortsLayout.setOnClickListener(v -> {
            dialog.dismiss();
            Toast.makeText(MainActivity.this,"Create a short is Clicked",Toast.LENGTH_SHORT).show();
        });

        cancelButton.setOnClickListener(view -> dialog.dismiss());

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);

    }
}