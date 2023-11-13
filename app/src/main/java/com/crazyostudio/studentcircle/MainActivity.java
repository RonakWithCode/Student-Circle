package com.crazyostudio.studentcircle;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.crazyostudio.studentcircle.adapters.ViewPagerAdapter;
import com.crazyostudio.studentcircle.databinding.ActivityMainBinding;
import com.crazyostudio.studentcircle.fragment.ShareFileFragment;
import com.crazyostudio.studentcircle.fragment.StoryFragment;
import com.crazyostudio.studentcircle.fragment.UserInfoFragment;
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
//            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CALL_PHONE,
//            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    private static final String[] PERMISSIONS_10_AND_ABOVE = {
            Manifest.permission.CALL_PHONE
    };
//    ActivityResultLauncher<Intent> resultLauncher;
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

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.add(new UserInfoFragment(), "Chat");
//        viewPagerAdapter.add(new StoryFragment(), "Story");
        viewPagerAdapter.add(new ShareFileFragment(), "Share");
        binding.viewpager.setAdapter(viewPagerAdapter);
        binding.tabLayout.setupWithViewPager(binding.viewpager);
        auth = FirebaseAuth.getInstance();

        String[] permissions;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            permissions = PERMISSIONS_BELOW_10;
        } else {
            permissions = PERMISSIONS_10_AND_ABOVE;
        }

        requestPermissions(permissions);

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
        if (item.getItemId() == R.id.Profile) {
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
}