package com.crazyostudio.studentcircle.user;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.crazyostudio.studentcircle.databinding.ActivityUserProfileBinding;
import com.crazyostudio.studentcircle.model.UserInfo;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Objects;

public class User_Profile extends AppCompatActivity {
    ActivityUserProfileBinding binding;
    FirebaseDatabase database;
    ProgressDialog bar;
    private StorageReference reference;
    boolean IsImageUpdate = false;
    Uri dataUri;
    UserInfo _userInfo;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth = FirebaseAuth.getInstance();
        reference = FirebaseStorage.getInstance().getReference("Image");
        database = FirebaseDatabase.getInstance();
        bar = new ProgressDialog(this);
        database.getReference("UserInfo").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    if (Objects.equals(snapshot1.getKey(), auth.getUid())) {
                        _userInfo = snapshot1.getValue(UserInfo.class);
                        assert _userInfo != null;
                        Glide.with(User_Profile.this).load(_userInfo.getUserImage()).into(binding.userImage);
                        binding.Name.setText(_userInfo.getName());
                        binding.Mail.setText(_userInfo.getMail());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        binding.userImage.setOnClickListener(view ->
                ImagePicker.with(this)
                        .crop()
                        .compress(1024)
                        .maxResultSize(1080, 1080)
                        .start(1));

        binding.Name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (binding.Name.getText().toString().isEmpty()) {
                    binding.Name.setError("Enter you Name ");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        binding.next.setOnClickListener(view -> {
            bar.setMessage("Change in Database");
            bar.show();
            if (IsImageUpdate) {
                if (!binding.Name.getText().toString().isEmpty()) {
                    StorageReference file = reference.child(FirebaseAuth.getInstance().getUid()+"."+getfilleExtension(dataUri));
                    file.putFile(dataUri).addOnSuccessListener(taskSnapshot -> file.getDownloadUrl().addOnSuccessListener(uri -> {
                        database.getReference("UserInfo").child(auth.getUid()).child("name").setValue(binding.Name.getText().toString());
                        database.getReference("UserInfo").child(auth.getUid()).child("userImage").setValue(uri);
                        if (bar.isShowing()) {
                            bar.dismiss();
                        }


                    })).addOnFailureListener(e ->{
                        if (bar.isShowing()) {
                            bar.dismiss();
                        }
                        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    });

                } else {
                    if (bar.isShowing()) {
                        bar.dismiss();
                    }
                    Toast.makeText(this, "Check your input", Toast.LENGTH_SHORT).show();
                }
            }else {
                if (!binding.Name.getText().toString().isEmpty()) {
                    database.getReference("UserInfo").child(auth.getUid()).child("name").setValue(binding.Name.getText().toString());
                    if (bar.isShowing()) {
                        bar.dismiss();
                    }
                } else {
                    if (bar.isShowing()) {
                        bar.dismiss();
                    }
                    Toast.makeText(this, "Check your input", Toast.LENGTH_SHORT).show();
                }
            }
            });

    }

    private String getfilleExtension(Uri Uri) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(Uri));
    }
        @Override

        protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            assert data != null;
            if (data.getData() != null && requestCode==1) {
                dataUri = data.getData();
                binding.userImage.setImageURI(dataUri);
                IsImageUpdate = true;
            }
            else {
                IsImageUpdate = false;
            }
        }


}