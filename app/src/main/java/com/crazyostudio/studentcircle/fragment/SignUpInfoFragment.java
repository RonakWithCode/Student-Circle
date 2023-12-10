package com.crazyostudio.studentcircle.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.crazyostudio.studentcircle.Java_Class.SomeCode;
import com.crazyostudio.studentcircle.MainActivity;
import com.crazyostudio.studentcircle.R;
import com.crazyostudio.studentcircle.databinding.FragmentSignUpInfoBinding;
import com.crazyostudio.studentcircle.databinding.ImgaepickerBinding;
import com.crazyostudio.studentcircle.fragmentLoad;
import com.crazyostudio.studentcircle.model.CurrentInternetConnection;
import com.crazyostudio.studentcircle.model.UserInfo;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Objects;

public class SignUpInfoFragment extends Fragment {
    private FragmentSignUpInfoBinding binding;
    ProgressDialog bar;
//    boolean imageBts = false;
    private String number;
    private String token;
//    private StorageReference reference;
    FirebaseDatabase db;
//    private Uri dataUri;
    private FirebaseAuth Auth;



    private final int IMAGE_REQUEST_CODE = 123;
    private Uri userImage;
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
        if (!CurrentInternetConnection.isInternetConnected(requireContext())) {
            Intent intent = new Intent(requireContext(), fragmentLoad.class);
            intent.putExtra("LoadID","network");
            startActivity(intent);
        }
        binding.userImage.setOnClickListener(view -> ShowDialog());

        Auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
//        reference = FirebaseStorage.getInstance().getReference(Objects.requireNonNull(Auth.getUid()));
        bar = new ProgressDialog(requireContext());

        setupErrorHandling(binding.Name, binding.textField, "Name cannot be empty.");
        setupErrorHandling(binding.Mail, binding.MailLayout, "Mail cannot be empty.");
//        setupErrorHandling(binding.Bio, binding.bioLayout, "Bio cannot be empty.");

        String[] dropdownData = {"Hello", "Busy", "Typing..", "Coder", "At work","DND"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, dropdownData);
        binding.Bio.setAdapter(adapter);


        binding.save.setOnClickListener(view-> {
            if (validateInputs()) {
                binding.ProgressBar.setVisibility(View.VISIBLE);
                if (userImage == null) {
                    userImage = Uri.parse("https://firebasestorage.googleapis.com/v0/b/e-commerce-11d7d.appspot.com/o/UserImage%2Fperson.png?alt=media&token=599cbe56-3620-4d52-9e51-58e57b936596");
                    setupUser();
                }
                else {
                    FirebaseStorage storage = FirebaseStorage.getInstance(); // each user have own database
                    StorageReference storageRef = storage.getReference(Objects.requireNonNull(Objects.requireNonNull(binding.Name.getText()).toString()) +Auth.getUid());
                    StorageReference imageRef = storageRef.child("UserDPs").child(System.currentTimeMillis() + SomeCode.getFileExtensionFromUri(userImage,requireContext()));
                    Uri imageUri = userImage;

                    imageRef.putFile(imageUri)
                            .addOnSuccessListener(taskSnapshot -> imageRef.getDownloadUrl()
                                    .addOnSuccessListener(uri -> {
                                        // URI of the uploaded image
                                        userImage = uri;
                                        setupUser();
                                        // Use the download URL as needed (e.g., save it in your database)
                                        // Now, you have the download URL of the uploaded image.
                                    })
                                    .addOnFailureListener(exception -> {
                                        userImage = Uri.parse("https://firebasestorage.googleapis.com/v0/b/e-commerce-11d7d.appspot.com/o/UserImage%2Fperson.png?alt=media&token=599cbe56-3620-4d52-9e51-58e57b936596");
                                        setupUser();
                                    }))
                            .addOnFailureListener(exception -> {
                                userImage = Uri.parse("https://firebasestorage.googleapis.com/v0/b/e-commerce-11d7d.appspot.com/o/UserImage%2Fperson.png?alt=media&token=599cbe56-3620-4d52-9e51-58e57b936596");
                                setupUser();
                            });

                }
            }
        });


        return binding.getRoot();
    }

    private void setupUser() {
        UserInfo userInfo = new UserInfo(Auth.getUid(),token, Objects.requireNonNull(binding.Name.getText()).toString(), Objects.requireNonNull(binding.Name.getText()).toString(),binding.Bio.getText().toString(),userImage.toString(), Objects.requireNonNull(binding.Mail.getText()).toString(),number,null,null,
                "public",null,0,0,null);

        db.getReference().child("UserInfo").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).setValue(userInfo).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setPhotoUri(userImage)
                        .setDisplayName(binding.Name.getText().toString()).build();
                assert user != null;
                user.updateProfile(profileUpdates).addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful()) {
                        binding.ProgressBar.setVisibility(View.GONE);
//                        requireActivity().finish();
//                        requireContext().startActivity(new Intent(requireContext(), MainActivity.class));


                    }else {
                        SomeCode.AlertDialog(requireContext(),"Error in Setup Profile");
                    }
                }).addOnFailureListener(e -> SomeCode.AlertDialog(requireContext(),e.toString()));


            }else {
                SomeCode.AlertDialog(requireContext(),task.toString());
            }

        }).addOnFailureListener(e -> SomeCode.AlertDialog(requireContext(),e.toString()));
    }


    private void ShowDialog() {
        ImgaepickerBinding imgaepickerBinding = ImgaepickerBinding.inflate(getLayoutInflater());
        Dialog dialog = new Dialog(getContext());
// Set the layout parameters to center the layout
        dialog.setContentView(imgaepickerBinding.getRoot());
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.createsubjectsboxbg);
        dialog.setCancelable(true);
        dialog.getWindow().getAttributes().windowAnimations = R.style.Animationboy;
        dialog.show();
        imgaepickerBinding.camera.setOnClickListener(view -> {
            // Check if the camera permission is already granted.
            if (isCameraPermissionGranted()) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(requireContext().getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, IMAGE_REQUEST_CODE);
                }
            } else {
                // Camera permission is not granted. Request permission from the user.
                dialog.dismiss();
                requestCameraPermission();
            }

        });
        imgaepickerBinding.photo.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
//            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            startActivityForResult(intent, IMAGE_REQUEST_CODE);
            dialog.dismiss();
        });
        imgaepickerBinding.cancel.setOnClickListener(view -> dialog.dismiss());

    }



    private boolean isCameraPermissionGranted() {
        return ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Camera permission granted. You can use the camera here.
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(requireContext().getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, IMAGE_REQUEST_CODE);
                }

            } else {
                // Camera permission denied. Handle it accordingly (e.g., show a message to the user).
                Toast.makeText(requireContext(), "Camera permission denied", Toast.LENGTH_SHORT).show();
            }
        }
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
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            Glide.with(requireContext()).load(selectedImage).into(binding.userImage);
            userImage = selectedImage;
        }
    }
    private boolean validateInputs() {
        boolean isValid = true;

        // Check Name
        String name = Objects.requireNonNull(binding.Name.getText()).toString().trim();
        if (TextUtils.isEmpty(name)) {
            binding.textField.setError("Name cannot be empty");
            isValid = false;
        } else {
            binding.textField.setError(null);
        }

        // Check Email
        String email = Objects.requireNonNull(binding.Mail.getText()).toString().trim();
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