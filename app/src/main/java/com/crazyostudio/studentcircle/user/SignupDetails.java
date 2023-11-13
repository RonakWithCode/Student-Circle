package com.crazyostudio.studentcircle.user;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.crazyostudio.studentcircle.MainActivity;
import com.crazyostudio.studentcircle.R;
import com.crazyostudio.studentcircle.databinding.ActivitySignupDetailsBinding;
import com.crazyostudio.studentcircle.fragmentLoad;
import com.crazyostudio.studentcircle.model.CurrentInternetConnection;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SignupDetails extends AppCompatActivity {
    ActivitySignupDetailsBinding binding;
    ProgressDialog bar;
    boolean imageBts = false,IsUseNumber=false;
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
        reference = FirebaseStorage.getInstance().getReference("Image");
        bar = new ProgressDialog(this);
        if (!getIntent().getStringExtra("Number").isEmpty()) {
            number = getIntent().getStringExtra("Number");
        }else {
            if (Objects.requireNonNull(Objects.requireNonNull(Auth.getCurrentUser()).getPhoneNumber()).isEmpty()) {
                number = Auth.getCurrentUser().getPhoneNumber();
            }else {
                binding.number.setVisibility(View.VISIBLE);
                binding.number.setError("Enter Your Number ");
                IsUseNumber = true;
            }
        }

        binding.signup.setOnClickListener(view->{
            if (!binding.Name.getText().toString().isEmpty()) {
                if (!binding.Mail.getText().toString().isEmpty()){
                    if (imageBts) {
                        if (IsUseNumber){
                            if (binding.number.getText().toString().length()==10) {
                                bar.show();
                                bar.setTitle("Waiting...");
                                bar.setMessage("Uploading Data to Database ");
                                UploadImage(dataUri);
                            }else {
                                binding.number.setError("Enter Your Number ");
                            }
                        }else {
                            bar.show();
                            bar.setTitle("Waiting...");
                            bar.setMessage("Uploading Data to Database ");
                            UploadImage(dataUri);
                        }
                    }else {
                        Toast.makeText(this, "Select Image From Tap on This image icon ", Toast.LENGTH_SHORT).show();
//                        UploadImage(Uri.parse(String.valueOf(R.drawable.userimage)));
                    }
                }else {
                    binding.Mail.setError("Enter Your E-Mail");
                }
            }else {
                binding.Name.setError("Enter Your Full Name");
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
      private String filletExtension(Uri uri) {
          ContentResolver contentResolver = getContentResolver();
          MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

          // Get the file extension based on the Uri's MIME type
          String extension = mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));

          if (extension == null) {
              // If the MIME type doesn't provide an extension, try to extract from the Uri's path
              String path = uri.getPath();
              if (path != null) {
                  int extensionStartIndex = path.lastIndexOf('.');
                  if (extensionStartIndex != -1) {
                      extension = path.substring(extensionStartIndex + 1);
                  }
              }
          }

          return extension;
    }

    private void UploadImage(Uri image) {
        StorageReference file = reference.child(System.currentTimeMillis()+"."+ filletExtension(image));
            file.putFile(image).addOnSuccessListener(taskSnapshot -> file.getDownloadUrl().addOnSuccessListener(uri -> {
            com.crazyostudio.studentcircle.model.UserInfo userInfo;
            userInfo = new com.crazyostudio.studentcircle.model.UserInfo(binding.Name.getText().toString(),uri.toString(),binding.Mail.getText().toString(),number);
            db.getReference().child("UserInfo").child(Objects.requireNonNull(Auth.getUid())).setValue(userInfo).addOnCompleteListener(task -> {
                FirebaseUser user = Auth.getCurrentUser();
                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                        .setDisplayName(binding.Name.getText().toString())
                        .setPhotoUri(uri)
                        .build();

                assert user != null;
                user.updateProfile(profileUpdates)
                        .addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()) {
                                if (bar.isShowing()) {
                                    bar.dismiss();
                                    Dialog Pin_dialog = new Dialog(SignupDetails.this);
                                    Pin_dialog.setContentView(R.layout.pindialogbox);
                                    Pin_dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                                    Pin_dialog.getWindow().setBackgroundDrawableResource(R.drawable.pinboxlayout);
                                    Pin_dialog.setCancelable(false);
                                    Pin_dialog.show();

                                    EditText fakePin = Pin_dialog.findViewById(R.id.fakePin);
                                    EditText realPin = Pin_dialog.findViewById(R.id.real_pin);
                                    FloatingActionButton button = Pin_dialog.findViewById(R.id.create_Btu);
                                    fakePin.addTextChangedListener(new TextWatcher() {
                                        @Override
                                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                                        }

                                        @Override
                                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                                            if (fakePin.getText().toString().length()==4) {
                                                realPin.requestFocus();

//                            fakePin.getNextFocusDownId();
                                            }
                                            else    {
                                                fakePin.setError("Length of a number is 4");
                                            }
                                        }

                                        @Override
                                        public void afterTextChanged(Editable editable) {
                                            if (fakePin.getText().toString().length()==4) {
//                            fakePin.getNextFocusDownId();
                                            }
                                            else {
                                                fakePin.setError("Length of a number is 4");
                                            }
                                        }
                                    });
                                    realPin.addTextChangedListener(new TextWatcher() {
                                        @Override
                                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                                        }

                                        @Override
                                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                                            if (!(fakePin.getText().toString().length() == 4)) {
                                                fakePin.requestFocus();
                                            }
                                            if (realPin.getText().toString().length()==4) {
                                                button.setVisibility(View.VISIBLE);

                                            }
                                            else    {
                                                realPin.setError("Length of a number is 4");
                                            }
                                        }

                                        @Override
                                        public void afterTextChanged(Editable editable) {
                                            if (!(fakePin.getText().toString().length() == 4)) {
                                                fakePin.requestFocus();
                                            }
                                            if (realPin.getText().toString().length()==4) {
                                                button.setVisibility(View.VISIBLE);
                                                realPin.clearFocus();
//                            fakePin.getNextFocusDownId();
                                            }
                                            else {
                                                realPin.setError("Length of a number is 4");
                                            }
                                        }
                                    });
                                    button.setOnClickListener(view -> {
                                        Map<String, String> pins = new HashMap<>();
                                        pins.put("fakePin",fakePin.getText().toString());
                                        pins.put("realPin",realPin.getText().toString());

                                        FirebaseDatabase.getInstance().getReference().child("pin").child(Auth.getUid()).setValue(pins).addOnCompleteListener(task2 -> {
                                            if (task2.isSuccessful()){
                                                Pin_dialog.dismiss();
                                                startActivity(new Intent(SignupDetails.this, MainActivity.class));
                                                finish();
                                            }
                                            else {
                                                Toast.makeText(SignupDetails.this, "ReTry", Toast.LENGTH_SHORT).show();
                                                fakePin.setText("");
                                                realPin.setText("");
                                                button.setVisibility(View.INVISIBLE);
                                            }
                                        });
                                    });

                                }
                            }
                        });

            });

        })).addOnFailureListener(e -> Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}