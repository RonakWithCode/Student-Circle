package com.crazyostudio.studentcircle.user;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.crazyostudio.studentcircle.R;
import com.crazyostudio.studentcircle.Service.AuthService;
import com.crazyostudio.studentcircle.adapters.LinksAdapter;
import com.crazyostudio.studentcircle.databinding.AddLinkBoxBinding;
import com.crazyostudio.studentcircle.databinding.FragmentProfileLinkViewBinding;
import com.crazyostudio.studentcircle.model.LinksModels;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileLinkViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileLinkViewFragment extends Fragment {

    FragmentProfileLinkViewBinding binding;
    AuthService service;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ArrayList<LinksModels> models;
    LinksAdapter customAdapter;
    public ProfileLinkViewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileLinkViewFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileLinkViewFragment newInstance(String param1, String param2) {
        ProfileLinkViewFragment fragment = new ProfileLinkViewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = FragmentProfileLinkViewBinding.inflate(inflater,container,false);
        service = new AuthService();
        models = new ArrayList<>();
//        customAdapter.notifyDataSetChanged();


        service.getAlMedialLinks(new AuthService.LinksModelsCallback() {
            @Override
            public void onLinksModelsRetrieved(ArrayList<LinksModels> linksModelsList) {
                models = linksModelsList;
                customAdapter = new LinksAdapter(requireContext(), models, (model,pos) -> {
                    addLink(model.title,model.links,pos);

                });

                binding.linksLists.setLayoutManager(new LinearLayoutManager(requireContext()));
                binding.linksLists.setAdapter(customAdapter);
                customAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {

//                  TODO 6/03/24 handle the error
//                addLinkBoxBinding.error.setVisibility(View.VISIBLE);
//                addLinkBoxBinding.error.setText(error.toString());
            }
        });
        binding.addLinks.setOnClickListener(view->{
            addLink(null,null,-99);
        });


        return binding.getRoot();
    }

    void addLink(String title,String link,int pos){
        AddLinkBoxBinding addLinkBoxBinding = AddLinkBoxBinding.inflate(requireActivity().getLayoutInflater());
        Dialog dialog = new Dialog(requireContext());
// Set the layout parameters to center the layout
        dialog.setContentView(addLinkBoxBinding.getRoot());
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.createsubjectsboxbg);
        dialog.setCancelable(true);

        dialog.getWindow().getAttributes().windowAnimations = R.style.Animationboy;
        dialog.show();
        // Set values if they are not null
        if (title != null) {
            addLinkBoxBinding.title.setText(title);
        }
        if (link != null) {
            addLinkBoxBinding.link.setText(link);
        }
        if (pos!=-99) {
            // Do something with pos if needed
            addLinkBoxBinding.remove.setVisibility(View.VISIBLE);
        }



        addLinkBoxBinding.remove.setOnClickListener(remove->{
            models.remove(pos);
            service.SetupMediaLinks(models, new AuthService.SetupMediaLinksCallback() {
                @Override
                public void onLinksModelsSetup() {
                    if (dialog.isShowing()){
                        dialog.dismiss();
                        customAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onFailure(Exception error) {
                    addLinkBoxBinding.error.setVisibility(View.VISIBLE);
                    addLinkBoxBinding.error.setText(error.getMessage());
                    customAdapter.notifyDataSetChanged();

                }
            });

        });




        addLinkBoxBinding.add.setOnClickListener(view1->{
            if (!TextUtils.isEmpty(Objects.requireNonNull(addLinkBoxBinding.title.getText()).toString()) && !TextUtils.isEmpty(Objects.requireNonNull(addLinkBoxBinding.link.getText()).toString())) {
                if (pos==-99) {
                    models.add(new LinksModels(addLinkBoxBinding.title.getText().toString(),addLinkBoxBinding.link.getText().toString()));
                }
                else {
                    models.set(pos, new LinksModels(addLinkBoxBinding.title.getText().toString(),addLinkBoxBinding.link.getText().toString()));

                }

                service.SetupMediaLinks(models, new AuthService.SetupMediaLinksCallback() {
                    @Override
                    public void onLinksModelsSetup() {
                        if (dialog.isShowing()){
                            dialog.dismiss();
                            customAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onFailure(Exception error) {
                        addLinkBoxBinding.error.setVisibility(View.VISIBLE);
                        addLinkBoxBinding.error.setText(error.getMessage());
                        customAdapter.notifyDataSetChanged();

                    }
                });
            }

            else {
                addLinkBoxBinding.error.setVisibility(View.VISIBLE);
                addLinkBoxBinding.error.setText("Input title or link");
                customAdapter.notifyDataSetChanged();

            }
        });
        addLinkBoxBinding.close.setOnClickListener(close->{
            if (dialog.isShowing()){
                dialog.dismiss();
            }
        });
        customAdapter.notifyDataSetChanged();
    }

}