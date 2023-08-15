package com.crazyostudio.studentcircle;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.crazyostudio.studentcircle.adapters.HelpImageAdapters;
import com.crazyostudio.studentcircle.databinding.ActivityContactFromBinding;
import com.crazyostudio.studentcircle.model.BugModel;
import com.crazyostudio.studentcircle.model.HelpImageAdaptersOnclick;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Objects;

public class ContactFromActivity extends AppCompatActivity implements HelpImageAdaptersOnclick {
    ActivityContactFromBinding binding;
    ArrayList<Uri> uri;
    ArrayList<String> imageUrls;
    private ProgressDialog progressDialog;
    HelpImageAdapters imageAdapters;
    private FirebaseDatabase firebaseDatabase;
    private StorageReference storageRef;
    private int uploadCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityContactFromBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        storageRef = FirebaseStorage.getInstance().getReference("bug");
        firebaseDatabase = FirebaseDatabase.getInstance();
        progressDialog = new ProgressDialog(this);
        uri = new ArrayList<>();
        imageUrls = new ArrayList<>();
        imageAdapters = new HelpImageAdapters(uri,this,this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        binding.recyclerView.setLayoutManager(layoutManager);
        binding.recyclerView.setAdapter(imageAdapters);
        binding.addImage.setOnClickListener(v ->
                ImagePicker.with(this)
                .crop()
                .compress(1024)
                .maxResultSize(1080, 1080)
                .start(159));
        binding.report.setOnClickListener(view->{
            if (!binding.Problem.getText().toString().isEmpty()||binding.example.getText().toString().isEmpty()) {
                uploadMultipleImages();
            }
            else {
                Toast.makeText(this, "Check filed", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.toLowerCase().startsWith(manufacturer.toLowerCase())) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }
    @NonNull
    private String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }

    private void uploadMultipleImages() {
        for (Uri imageUri : uri) {
            // Get a unique filename for each image
            String imageName = System.currentTimeMillis() + filletExtension(Uri.parse(String.valueOf(imageUri)));
            StorageReference imageRef = storageRef.child(imageName);

            // Upload the image to Firebase Storage
            UploadTask uploadTask = imageRef.putFile(imageUri);
            uploadTask.addOnCompleteListener(task -> {
                if (task.isSuccessful()) {

                    // Image uploaded successfully, get its download URL
                    imageRef.getDownloadUrl().addOnCompleteListener(downloadTask -> {
                        if (downloadTask.isSuccessful()) {
                            // Add the download URL to the list
                            imageUrls.add(downloadTask.getResult().toString());
                        } else {
                            // Handle the error
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                        }

                        // Check if all images have been uploaded and their URLs obtained
                        uploadCount++;
                        if (uploadCount == uri.size()) {
                            // All images have been uploaded, do something with the URLs
//                            SubjectModel model = new SubjectModel(createsubjectsBinding.subName.getText().toString(), fileUri1,time,imageUrls,id);
                            BugModel bugModel;
                            if (binding.checkbox.isChecked()) {
                                DisplayMetrics displayMetrics = new DisplayMetrics();
                                getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                                int height = displayMetrics.heightPixels;
                                int width = displayMetrics.widthPixels;

                                bugModel = new BugModel(binding.Problem.getText().toString(),binding.example.getText().toString(),getDeviceName(),width,height,imageUrls);
                            }else {
                                bugModel = new BugModel(binding.Problem.getText().toString(),binding.example.getText().toString(),imageUrls);
                            }
                            firebaseDatabase.getReference().child("Bug").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).setValue(bugModel).addOnCompleteListener(task1 -> {
                                if (task.isSuccessful()) {
                                    progressDialog.dismiss();
                                    if (progressDialog.isShowing()) {
                                        progressDialog.dismiss();
                                        finish();
                                    }
                                }
                            }).addOnFailureListener(e -> {
                                firebaseDatabase.getReference().child("error").child("Share_createSubject").child(System.currentTimeMillis()+"").push().setValue(e.getMessage());
//                                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
//                                progressDialog.dismiss();
                                Toast.makeText(this, "Retry after some time ", Toast.LENGTH_SHORT).show();
                                if (progressDialog.isShowing()) {
                                    progressDialog.dismiss();
                                }
                            });
                        }
                    });
                } else {
                    uploadCount--;
                }
            });
        }
    }
    private String filletExtension(Uri Uri) {
        ContentResolver cr = this.getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(Uri));
    }
    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void Remove(int pos) {
        uri.remove(pos);
        imageAdapters.notifyDataSetChanged();
    }

    @Override
    public void OnClickOnImage(Uri uri) {

    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        assert data != null;
//        && resultCode== Activity.RESULT_OK
        if (data.getData() != null && requestCode==159 )  {
            uri.add(data.getData());
            imageAdapters.notifyDataSetChanged();
        }
    }
}