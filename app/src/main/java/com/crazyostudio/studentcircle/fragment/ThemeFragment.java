package com.crazyostudio.studentcircle.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.crazyostudio.studentcircle.R;
import com.crazyostudio.studentcircle.databinding.FragmentThemeBinding;
import com.crazyostudio.studentcircle.fragmentLoad;


public class ThemeFragment extends Fragment {
    FragmentThemeBinding binding;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentThemeBinding.inflate(inflater,container,false);

        binding.app.setOnClickListener(app->{
            Intent intent = new Intent(getContext(), fragmentLoad.class);
            intent.putExtra("LoadID","preview_theme");
            assert getArguments() != null;
            intent.putExtra("ThemeID",getArguments().getString("ThemeID"));
            intent.putExtra("ImageType",R.drawable.app);
            startActivity(intent);
        });
        binding.earthbg.setOnClickListener(app->{
            Intent intent = new Intent(getContext(), fragmentLoad.class);
            intent.putExtra("LoadID","preview_theme");
            intent.putExtra("ThemeID",getArguments().getString("ThemeID"));

            intent.putExtra("ImageType",R.drawable.earthbg);
            startActivity(intent);
        });
        binding.love.setOnClickListener(app->{
            Intent intent = new Intent(getContext(), fragmentLoad.class);
            intent.putExtra("LoadID","preview_theme");
            intent.putExtra("ThemeID",getArguments().getString("ThemeID"));

            intent.putExtra("ImageType",R.drawable.love);
            startActivity(intent);
        });
        binding.prinkbg.setOnClickListener(app->{
            Intent intent = new Intent(getContext(), fragmentLoad.class);
            intent.putExtra("LoadID","preview_theme");
            intent.putExtra("ThemeID",getArguments().getString("ThemeID"));

            intent.putExtra("ImageType",R.drawable.prinkbg);
            startActivity(intent);
        });
        binding.bluebg.setOnClickListener(app->{
            Intent intent = new Intent(getContext(), fragmentLoad.class);
            intent.putExtra("LoadID","preview_theme");
            intent.putExtra("ThemeID",getArguments().getString("ThemeID"));

            intent.putExtra("ImageType",R.drawable.bluebg);
            startActivity(intent);
        });
        binding.lovebg.setOnClickListener(app->{
            Intent intent = new Intent(getContext(), fragmentLoad.class);
            intent.putExtra("LoadID","preview_theme");
            intent.putExtra("ThemeID",getArguments().getString("ThemeID"));

            intent.putExtra("ImageType",R.drawable.lovebg);
            startActivity(intent);
        });
        return binding.getRoot();

    }
}