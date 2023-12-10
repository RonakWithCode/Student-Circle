package com.crazyostudio.studentcircle.user;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.crazyostudio.studentcircle.Java_Class.ErrorDialog;
import com.crazyostudio.studentcircle.Java_Class.SomeCode;
import com.crazyostudio.studentcircle.databinding.ActivityUserProfileBinding;
import com.crazyostudio.studentcircle.fragmentLoad;
import com.crazyostudio.studentcircle.model.CurrentInternetConnection;
import com.crazyostudio.studentcircle.model.UserInfo;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Objects;


public class User_Profile extends AppCompatActivity {
    ActivityUserProfileBinding binding;
    private FirebaseDatabase databaseReference;
    private StorageReference storageReference;
    boolean IsImageUpdate = false;
    private Uri dataUri = null;
    private UserInfo _userInfo;
    private FirebaseAuth auth;

    private final int USER_DP_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        checkNetwork();
        auth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference(Objects.requireNonNull(auth.getUid()));
        databaseReference = FirebaseDatabase.getInstance();


        getUser();

        binding.userImage.setOnClickListener(view ->
                ImagePicker.with(this)
                        .crop()
                        .compress(1024)
                        .maxResultSize(1080, 1080)
                        .start(USER_DP_CODE));

        setupErrorHandling(binding.NameEditText, binding.textField, "Name cannot be empty.");
        setupErrorHandling(binding.Mail, binding.MailLayout, "Mail cannot be empty.");
//        setupErrorHandling(binding.Bio, binding.bioLayout, "Bio cannot be empty.");

        String[] dropdownData = {"Hello", "Busy", "Typing..", "Coder", "At work","DND"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, dropdownData);
        binding.Bio.setAdapter(adapter);

        binding.save.setOnClickListener(view -> {
            if (validateInputs()) {
                // Inputs are valid, proceed with saving
                Save();
            }
        });

    }

    private void Save() {
        binding.progressCircular.setVisibility(View.VISIBLE);
        FirebaseUser user = auth.getCurrentUser();
        // Update the user profile in Firebase Realtime Database
        DatabaseReference userReference = databaseReference.getReference("UserInfo").child(user.getUid());
// TODO: This is pass error change child resolve the error
        userReference.child("name").setValue(Objects.requireNonNull(binding.NameEditText.getText()).toString());
        userReference.child("mail").setValue(Objects.requireNonNull(binding.Mail.getText()).toString());
        userReference.child("bio").setValue(binding.Bio.getText().toString());
        // Upload the image to Firebase Storage if an image is selected
        if (dataUri==null) {
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(binding.NameEditText.getText().toString())
                    .build();
            user.updateProfile(profileUpdates);
            binding.progressCircular.setVisibility(View.GONE);
            finish();
        } else {
            // If no image is selected, show a success message
            uploadImage(user.getUid());
            Toast.makeText(this, "User profile updated successfully", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadImage(String userId) {
        // Upload the image to Firebase Storage
        Uri imageUri = dataUri;
        StorageReference imageRef = storageReference.child("USER_DP").child(System.currentTimeMillis()+"."+ SomeCode.getFileExtensionFromUri(imageUri,this));
        imageRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    // Get the download URL of the uploaded image
                    imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        // Update the user's profile image URL in the database
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setPhotoUri(uri)
                                .build();
                        Objects.requireNonNull(auth.getCurrentUser()).updateProfile(profileUpdates);
                        databaseReference.getReference("UserInfo").child(userId).child("userImage").setValue(uri.toString());
                        // Show success message
                        finish();
                    });
                })
                .addOnFailureListener(e -> {
                    // Handle the failure
                    ErrorDialog.showErrorDialog(User_Profile.this,"Failed to upload image",e.getMessage());
                    binding.progressCircular.setVisibility(View.GONE);
                });
    }


    private void setupErrorHandling(TextInputEditText editText, TextInputLayout textInputLayout, String errorMessage) {
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

    private void getUser() {
        databaseReference.getReference("UserInfo").child(Objects.requireNonNull(auth.getUid())).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                    _userInfo = snapshot.getValue(UserInfo.class);
                    Glide.with(User_Profile.this).load(_userInfo.getProfilePictureUrl()).into(binding.userImage);
                    binding.NameEditText.setText(_userInfo.getFullName());
                    binding.Mail.setText(_userInfo.getEmail());
                    binding.Bio.setText(_userInfo.getBio());
                    binding.progressCircular.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                binding.progressCircular.setVisibility(View.GONE);
                ErrorDialog.showErrorDialog(User_Profile.this, "Error Code "+ error.getCode(), error.getMessage());
                checkNetwork();
            }
        });
    }

    private void checkNetwork() {
        if (!CurrentInternetConnection.isInternetConnected(User_Profile.this)) {
            Intent intent = new Intent(User_Profile.this, fragmentLoad.class);
            intent.putExtra("LoadID","network");
            startActivity(intent);
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            assert data != null;
            if (data.getData() != null && requestCode==USER_DP_CODE) {
                dataUri = data.getData();
                binding.userImage.setImageURI(dataUri);
                IsImageUpdate = true;
            }
            else {
                dataUri = null;
                IsImageUpdate = false;
            }
        }

    private boolean validateInputs() {
        boolean isValid = true;

        // Check Name
        String name = binding.NameEditText.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            binding.textField.setError("Name cannot be empty");
            isValid = false;
        } else {
            binding.textField.setError(null);
        }

        // Check Email
        String email = binding.Mail.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            binding.MailLayout.setError("Email cannot be empty");
            isValid = false;
        } else {
            binding.MailLayout.setError(null);
        }

        // Check Bio
        String bio = binding.Bio.getText().toString().trim();
        if (TextUtils.isEmpty(bio)) {
            binding.bioLayout.setError("Bio cannot be empty");
            isValid = false;
        } else {
            binding.bioLayout.setError(null);
        }

        return isValid;
    }


}