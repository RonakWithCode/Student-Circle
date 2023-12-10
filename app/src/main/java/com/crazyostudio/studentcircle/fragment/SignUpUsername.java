package com.crazyostudio.studentcircle.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.crazyostudio.studentcircle.R;
import com.crazyostudio.studentcircle.databinding.FragmentSignUpUsernameBinding;

import java.util.Objects;

public class SignUpUsername extends Fragment {
    FragmentSignUpUsernameBinding binding;

    public SignUpUsername() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSignUpUsernameBinding.inflate(inflater,container,false);
        binding.Name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length()==0){
                    binding.NameField.setError("Input Username..");
                    binding.NameField.setErrorEnabled(true);
                    binding.Next.setEnabled(false);
                }else {
                    binding.NameField.setErrorEnabled(false);
                    binding.Next.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
//        binding.Next.setOnClickListener(go->{
//            Bundle bundle = new Bundle();
//            bundle.putString("username", Objects.requireNonNull(binding.Name.getText()).toString());
//            NavHostFragment navHostFragment = (NavHostFragment) requireActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
//            assert navHostFragment != null;
//            NavController navController = navHostFragment.getNavController();
//            navController.navigate(R.id.action_signUpUsername_to_signUpInfoFragment,bundle);
//        });
        return binding.getRoot();
    }
}