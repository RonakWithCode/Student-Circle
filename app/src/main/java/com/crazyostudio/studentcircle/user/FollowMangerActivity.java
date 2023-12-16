package com.crazyostudio.studentcircle.user;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.crazyostudio.studentcircle.adapters.ViewPagerAdapter;
import com.crazyostudio.studentcircle.databinding.ActivityFollowMangerBinding;
import com.crazyostudio.studentcircle.fragment.followersListFragment;
import com.crazyostudio.studentcircle.fragment.followingListFragment;

public class FollowMangerActivity extends AppCompatActivity {
    ActivityFollowMangerBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFollowMangerBinding.inflate(getLayoutInflater());

        int follow = getIntent().getIntExtra("follow",0);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.add(new followersListFragment(), "followers");
        viewPagerAdapter.add(new followingListFragment(), "following");
//        viewPagerAdapter.add(new StoryFragment(), "Story");
        binding.viewpager.setAdapter(viewPagerAdapter);
        binding.tabLayout.selectTab(binding.tabLayout.getTabAt(follow));
        binding.tabLayout.setupWithViewPager(binding.viewpager);
//

        setContentView(binding.getRoot());
    }
}