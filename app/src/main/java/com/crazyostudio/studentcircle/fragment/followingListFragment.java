package com.crazyostudio.studentcircle.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.crazyostudio.studentcircle.R;
import com.crazyostudio.studentcircle.databinding.FragmentFollowingListBinding;

import java.util.ArrayList;


public class followingListFragment extends Fragment {
    FragmentFollowingListBinding binding;
    public followingListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFollowingListBinding.inflate(inflater,container,false);

//        binding.followingList
        // Sample data for the ListView
        ArrayList<String> dataList = new ArrayList<>();
        dataList.add("Item 1");
        dataList.add("Item 2");
        dataList.add("Item 3");
        dataList.add("Item 4");
        dataList.add("Item 5");

        // Find the ListView in your layout


//        ListView listView = requireActivity().findViewById(R.id.followingList);
//        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(requireContext(),
//                android.R.layout.simple_list_item_1, android.R.id.text1, dataList);
//
//        listView.setAdapter(arrayAdapter);








        return binding.getRoot();
    }
}