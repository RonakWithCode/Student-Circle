package com.crazyostudio.studentcircle.user;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.crazyostudio.studentcircle.databinding.ActivitySignupDetailsBinding;
import com.crazyostudio.studentcircle.fragmentLoad;
import com.crazyostudio.studentcircle.model.CurrentInternetConnection;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Objects;



public class SignupDetails extends AppCompatActivity {
    ActivitySignupDetailsBinding binding;
    ProgressDialog bar;
    boolean imageBts = false;
    private String number;
    private StorageReference reference;
    FirebaseDatabase db;
    private Uri dataUri;
    private FirebaseAuth Auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        if (!CurrentInternetConnection.isInternetConnected(this)) {
            Intent intent = new Intent(this, fragmentLoad.class);
            intent.putExtra("LoadID","network");
            startActivity(intent);
        }
        binding.userImage.setOnClickListener(view ->
                ImagePicker.with(this)
                        .crop()
                        .compress(1024)
                        .maxResultSize(1080, 1080)
                        .start(1));
        Auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        reference = FirebaseStorage.getInstance().getReference(Objects.requireNonNull(Auth.getUid()));
        bar = new ProgressDialog(this);
        if (!getIntent().getStringExtra("Number").isEmpty()) {
            number = getIntent().getStringExtra("Number");
        }else {
            if (Objects.requireNonNull(Objects.requireNonNull(Auth.getCurrentUser()).getPhoneNumber()).isEmpty()) {
                number = Auth.getCurrentUser().getPhoneNumber();
            }
        }

        setupErrorHandling(binding.Name, binding.textField, "Name cannot be empty.");
        setupErrorHandling(binding.Mail, binding.MailLayout, "Mail cannot be empty.");
//        setupErrorHandling(binding.Bio, binding.bioLayout, "Bio cannot be empty.");

        String[] dropdownData = {"Hello", "Busy", "Typing..", "Coder", "At work","DND"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, dropdownData);
        binding.Bio.setAdapter(adapter);


        binding.save.setOnClickListener(view-> {
            if (validateInputs()) {
                if (imageBts) {
//                    UploadImage(dataUri);
                }
                else {
                    Toast.makeText(this, "Select the Image From Tap to User image icon.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            assert data != null;
            if (data.getData() != null && requestCode == 1) {
                dataUri = data.getData();
                binding.userImage.setImageURI(dataUri);
                imageBts = true;
            }
        }
//    private void UploadImage(Uri image) {
//        StorageReference file = reference.child("USER_DP").child(System.currentTimeMillis()+"."+ SomeCode.getFileExtensionFromUri(image,this));
//            file.putFile(image).addOnSuccessListener(taskSnapshot -> file.getDownloadUrl().addOnSuccessListener(uri -> {
//            com.crazyostudio.studentcircle.model.UserInfo userInfo;
//
////            userInfo = new com.crazyostudio.studentcircle.model.UserInfo(Objects.requireNonNull(binding.Name.getText()).toString(),uri.toString(), Objects.requireNonNull(binding.Mail.getText()).toString(),number, Objects.requireNonNull(binding.Bio.getText()).toString());
//            db.getReference().child("UserInfo").child(Objects.requireNonNull(Auth.getUid())).setValue(userInfo).addOnCompleteListener(task -> {
//                FirebaseUser user = Auth.getCurrentUser();
//                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
//                        .setDisplayName(binding.Name.getText().toString())
//                        .setPhotoUri(uri)
//                        .build();
//
//                assert user != null;
//                user.updateProfile(profileUpdates)
//                        .addOnCompleteListener(task1 -> {
//                            if (task1.isSuccessful()) {
//                                if (bar.isShowing()) {
//                                    bar.dismiss();
//                                    Dialog Pin_dialog = new Dialog(SignupDetails.this);
//                                    Pin_dialog.setContentView(R.layout.pindialogbox);
//                                    Pin_dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
//                                    Pin_dialog.getWindow().setBackgroundDrawableResource(R.drawable.pinboxlayout);
//                                    Pin_dialog.setCancelable(false);
//                                    Pin_dialog.show();
//
//                                    EditText fakePin = Pin_dialog.findViewById(R.id.fakePin);
//                                    EditText realPin = Pin_dialog.findViewById(R.id.real_pin);
//                                    FloatingActionButton button = Pin_dialog.findViewById(R.id.create_Btu);
//                                    fakePin.addTextChangedListener(new TextWatcher() {
//                                        @Override
//                                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//                                        }
//
//                                        @Override
//                                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                                            if (fakePin.getText().toString().length()==4) {
//                                                realPin.requestFocus();
//
////                            fakePin.getNextFocusDownId();
//                                            }
//                                            else    {
//                                                fakePin.setError("Length of a number is 4");
//                                            }
//                                        }
//
//                                        @Override
//                                        public void afterTextChanged(Editable editable) {
//                                            if (fakePin.getText().toString().length()==4) {
////                            fakePin.getNextFocusDownId();
//                                            }
//                                            else {
//                                                fakePin.setError("Length of a number is 4");
//                                            }
//                                        }
//                                    });
//                                    realPin.addTextChangedListener(new TextWatcher() {
//                                        @Override
//                                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//                                        }
//
//                                        @Override
//                                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                                            if (!(fakePin.getText().toString().length() == 4)) {
//                                                fakePin.requestFocus();
//                                            }
//                                            if (realPin.getText().toString().length()==4) {
//                                                button.setVisibility(View.VISIBLE);
//
//                                            }
//                                            else    {
//                                                realPin.setError("Length of a number is 4");
//                                            }
//                                        }
//
//                                        @Override
//                                        public void afterTextChanged(Editable editable) {
//                                            if (!(fakePin.getText().toString().length() == 4)) {
//                                                fakePin.requestFocus();
//                                            }
//                                            if (realPin.getText().toString().length()==4) {
//                                                button.setVisibility(View.VISIBLE);
//                                                realPin.clearFocus();
////                            fakePin.getNextFocusDownId();
//                                            }
//                                            else {
//                                                realPin.setError("Length of a number is 4");
//                                            }
//                                        }
//                                    });
//                                    button.setOnClickListener(view -> {
//                                        Map<String, String> pins = new HashMap<>();
//                                        pins.put("fakePin",fakePin.getText().toString());
//                                        pins.put("realPin",realPin.getText().toString());
//
//                                        FirebaseDatabase.getInstance().getReference().child("pin").child(Auth.getUid()).setValue(pins).addOnCompleteListener(task2 -> {
//                                            if (task2.isSuccessful()){
//                                                Pin_dialog.dismiss();
//                                                startActivity(new Intent(SignupDetails.this, MainActivity.class));
//                                                finish();
//                                            }
//                                            else {
//                                                Toast.makeText(SignupDetails.this, "ReTry", Toast.LENGTH_SHORT).show();
//                                                fakePin.setText("");
//                                                realPin.setText("");
//                                                button.setVisibility(View.INVISIBLE);
//                                            }
//                                        });
//                                    });
//
//                                }
//                            }
//                        });
//
//            });
//
//        })).addOnFailureListener(e -> Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show());
//    }

    private boolean validateInputs() {
        boolean isValid = true;

        // Check Name
        String name = binding.Name.getText().toString().trim();
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