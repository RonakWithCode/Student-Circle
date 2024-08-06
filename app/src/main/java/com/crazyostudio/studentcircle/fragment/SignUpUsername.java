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
import com.crazyostudio.studentcircle.Service.AuthService;
import com.crazyostudio.studentcircle.databinding.FragmentSignUpUsernameBinding;
import com.google.firebase.database.DatabaseError;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpUsername extends Fragment {

//    AuthService

    FragmentSignUpUsernameBinding binding;
    private String number;
    private String token;
    public SignUpUsername() {}
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            number = getArguments().getString("number");
            token = getArguments().getString("token");
        }else {
            requireActivity().onBackPressed();
        }
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
                    SetError("Input the valid Username");
                }else {
                    binding.NameField.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        binding.Next.setOnClickListener(go->{
            String text = binding.Name.getText().toString();
            Pattern pattern = Pattern.compile("^[a-zA-Z0-9_]*$");

// Use a Matcher to check if the text matches the pattern
            Matcher matcher = pattern.matcher(text);

// Check if the text matches the pattern
            if (matcher.matches()) {
                checkUserName();
            } else {
                SetError("The text contains A TO Z 1234 _");

            }
        });
        return binding.getRoot();
    }


    void checkUserName(){
        AuthService authService = new AuthService();
        authService.checkUsername(Objects.requireNonNull(binding.Name.getText()).toString(), new AuthService.CallbackUsername() {
            @Override
            public void onUsernameExists(boolean Is) {
                if (!Is){
                    SendToSetupAccount();
                }else {
                    SetError("the username is exists");
                }
            }

            @Override
            public void onError(DatabaseError error) {
                SetError(error.toString());
            }
        });
    }
    void SetError(String error){
        binding.NameField.setError(error);
        binding.NameField.setErrorEnabled(true);
    }
    void SendToSetupAccount(){
        Bundle bundle = new Bundle();
        bundle.putString("username", Objects.requireNonNull(binding.Name.getText()).toString());
        bundle.putString("token",token);
        bundle.putString("number",number);
        NavHostFragment navHostFragment = (NavHostFragment) requireActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        assert navHostFragment != null;
        NavController navController = navHostFragment.getNavController();
        navController.navigate(R.id.action_signUpUsername_to_signUpInfoFragment,bundle);
    }
}