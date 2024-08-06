package com.crazyostudio.studentcircle.fragment;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.bumptech.glide.Glide;
import com.crazyostudio.studentcircle.R;
import com.crazyostudio.studentcircle.Service.AuthService;
import com.crazyostudio.studentcircle.databinding.FragmentSignUpInfoBinding;
import com.crazyostudio.studentcircle.databinding.ImgaepickerBinding;
import com.crazyostudio.studentcircle.model.UserInfo;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public class SignUpInfoFragment extends Fragment {
    private FragmentSignUpInfoBinding binding;
    private AuthService service;
    private ProgressDialog bar;
    private String number;

    private String token;
    private final int IMAGE_REQUEST_CODE = 123;
    private Uri userImage = Uri.parse("https://firebasestorage.googleapis.com/v0/b/friend-circle-f948a.appspot.com/o/Deffucts%2Fuserimage.png?alt=media&token=ed9dc13c-5810-42af-a66a-12cf861e9acc");
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 100;

    public SignUpInfoFragment() {}
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
        binding = FragmentSignUpInfoBinding.inflate(inflater,container,false);
        service = new AuthService();
/*        Check the network
        if (!CurrentInternetConnection.isInternetConnected(requireContext())) {
           Intent intent = new Intent(requireContext(), fragmentLoad.class);
            intent.putExtra("LoadID","network");
           startActivity(intent);
      }*/

        binding.userImage.setOnClickListener(view -> ShowDialog());
        bar = new ProgressDialog(requireContext());
        setupErrorHandling(binding.Name, binding.textField, "Name cannot be empty.");
        setupErrorHandling(binding.Mail, binding.MailLayout, "Mail cannot be empty.");

        String[] dropdownData = {"Hello", "Busy", "Typing..", "Coder","Dev" ,"At work","DND"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, dropdownData);
        binding.Bio.setAdapter(adapter);


        binding.save.setOnClickListener(view-> {
            if (validateInputs()) {
                bar.setTitle("Creating your account...");
                bar.setMessage("This may take a few minutes");
                bar.show();
                UserInfo userInfo = new UserInfo(service.getUid(),token, Objects.requireNonNull(binding.Name.getText()).toString(), Objects.requireNonNull(binding.Name.getText()).toString(),binding.Bio.getText().toString(),null, Objects.requireNonNull(binding.Mail.getText()).toString(),number,null,null,
                        "public",null,0,0,null,System.currentTimeMillis());
                service.SetupAccount(userInfo, userImage, requireContext(),new AuthService.CallbackSetupAccount() {
                    @Override
                    public void onSuccess() {
                        if (bar.isShowing()) {
                            bar.dismiss();
                        }
                        NavHostFragment navHostFragment = (NavHostFragment) requireActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
                        assert navHostFragment != null;
                        NavController navController = navHostFragment.getNavController();
                        navController.navigate(R.id.action_signUpInfoFragment_to_recommendedScreenFragment);
                    }

                    @Override
                    public void onFailure(String error) {
                        if (bar.isShowing()) {
                            bar.dismiss();
                        }
                        binding.error.setError(error);
                    }
                });

            }
        });
        return binding.getRoot();

    }




    private void ShowDialog() {
        final Dialog dialogBox = new Dialog(requireContext());
        dialogBox.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogBox.setContentView(R.layout.profile_pic_selecter_bottom_sheet_layout);
        ImageView UserDP =  dialogBox.findViewById(R.id.Pic);
        Glide.with(requireContext()).load(userImage).into(UserDP);

        LinearLayout upload_profile_pic_Layout = dialogBox.findViewById(R.id.upload_profile_pic);
        LinearLayout delete_btu_Layout = dialogBox.findViewById(R.id.delete_btu);
        TextView textView = dialogBox.findViewById(R.id.text2);
        textView.setText("use default Image");
        upload_profile_pic_Layout.setOnClickListener(v -> {
            pickMedia.launch(new PickVisualMediaRequest.Builder()
                    .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                    .build());
            dialogBox.dismiss();

        });
        delete_btu_Layout.setOnClickListener(v -> {
            userImage = getUriFromDrawable(R.drawable.userimage);
            Glide.with(requireContext()).load(userImage).into(binding.userImage);
            dialogBox.dismiss();
        });



        dialogBox.show();
        dialogBox.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogBox.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogBox.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialogBox.getWindow().setGravity(Gravity.BOTTOM);

    }
    public  Uri getUriFromDrawable(int drawableId) {
        // Build a Uri with the resource identifier
        return Uri.parse("android.resource://" + requireContext().getPackageName() + "/" + drawableId);
    }
    ActivityResultLauncher<PickVisualMediaRequest> pickMedia =
            registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                // Callback is invoked after the user selects a media item or closes the
                // photo picker.
                if (uri != null) {
//                    Log.d("PhotoPicker", "Selected URI: " + uri);
                    userImage = uri;
                    Glide.with(this).load(userImage).into(binding.userImage);

                } else {
                    binding.error.setError("No media selected");
                    binding.error.setVisibility(View.VISIBLE);
                }
            });

    private void setupErrorHandling(TextInputEditText editText, TextInputLayout textInputLayout, String errorMessage)  {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Not needed
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().isEmpty()) {
                    textInputLayout.setError(errorMessage);
                } else {
                    textInputLayout.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Not needed
            }
        });
    }
    private boolean validateInputs() {
        boolean isValid = true;
        // Check Name
        String name = Objects.requireNonNull(binding.Name.getText()).toString().trim();
        setErrorOnField(binding.textField, TextUtils.isEmpty(name) ? "Name cannot be empty" : null);
        if (TextUtils.isEmpty(name)) isValid = false;

        // Check Email
        String email = Objects.requireNonNull(binding.Mail.getText()).toString().trim();
        setErrorOnField(binding.MailLayout, TextUtils.isEmpty(email) ? "Email cannot be empty" : null);
        if (TextUtils.isEmpty(email) || !service.isValidEmail(email)) {
            setErrorOnField(binding.MailLayout, "Please enter a valid email address");
            isValid = false;
        }

        // Check Bio (with potential for more specific validation)
        String bio = binding.Bio.getText().toString().trim();
        setErrorOnField(binding.bioLayout, TextUtils.isEmpty(bio) ? "Bio cannot be empty" : null);
        if (TextUtils.isEmpty(bio)) isValid = false;

        return isValid;
    }
    private void setErrorOnField(TextInputLayout layout, String errorMessage) {
        layout.setError(errorMessage);
    }

}
