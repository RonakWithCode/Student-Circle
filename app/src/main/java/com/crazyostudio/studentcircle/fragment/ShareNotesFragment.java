package com.crazyostudio.studentcircle.fragment;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.crazyostudio.studentcircle.adapters.ShareNotesAdapters;
import com.crazyostudio.studentcircle.databinding.FragmentShareNotesBinding;
import com.crazyostudio.studentcircle.model.SubjectModel;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Objects;

public class ShareNotesFragment extends Fragment {
    //    ArrayList<String> uri = new ArrayList<>();
    private SubjectModel receivedModel;
    private static final String TAG = "MultipleImageUpload";
    private StorageReference storageRef;
    private FirebaseDatabase firebaseDatabase;
    private ShareNotesAdapters adapters;
    private ArrayList<String> imageUrls;
    private int uploadCount = 0;
    private ProgressDialog progressDialog;
    public ShareNotesFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            uri = getArguments().getStringArrayList("Notes");
            receivedModel = getArguments().getParcelable("subjectModel");
//            uri.addAll(receivedModel.getNotes());
            if (receivedModel != null) {
                imageUrls = new ArrayList<>();
                imageUrls.addAll(receivedModel.getNotes());

            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FragmentShareNotesBinding binding = FragmentShareNotesBinding.inflate(inflater, container, false);

        progressDialog = new ProgressDialog(getContext());
        storageRef = FirebaseStorage.getInstance().getReference("Share");
        firebaseDatabase = FirebaseDatabase.getInstance();
        adapters = new ShareNotesAdapters(imageUrls, getContext());
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(),2);
        binding.notes.setLayoutManager(layoutManager);
        binding.notes.setAdapter(adapters);
        adapters.notifyDataSetChanged();

        binding.addBtu.setOnClickListener(add->{
            // Check and request permission if needed
            if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 159);
            }
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            startActivityForResult(Intent.createChooser(intent, "Select Pictures"), 123);
        });
        binding.share.setOnClickListener(share -> {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Share Notes");
            shareIntent.putExtra(Intent.EXTRA_TEXT, receivedModel.getPath());
            startActivity(Intent.createChooser(shareIntent, "Share link"));
        });
        return binding.getRoot();
    }


    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 123 && resultCode == RESULT_OK) {
            if (data != null) {
                progressDialog.setTitle("Uploading....");
                progressDialog.show();
                if (data.getClipData() != null) {
                    // Multiple files selected
                    int count = data.getClipData().getItemCount();
                    for (int i = 0; i < count; i++) {
                        Uri fileUri = data.getClipData().getItemAt(i).getUri();
//                        String mimeType = requireActivity().getContentResolver().getType(fileUri);
                        String fileType = getFileTypeFromMimeType(fileUri);
                        uploadMultipleImages(count, fileUri.toString(), fileType);
                    }
                }





                else if (data.getData() != null) {
                    // Single file selected
                    Uri fileUri = data.getData();
//                    String mimeType = requireActivity().getContentResolver().getType(fileUri);
                    String fileType = getFileTypeFromMimeType(fileUri);
                    uploadMultipleImages(1,fileUri.toString(),fileType);
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private void uploadMultipleImages(int Size, String subImageUri, String imageName) {
        StorageReference imageRef = storageRef.child(System.currentTimeMillis()+imageName);
        UploadTask uploadTask = imageRef.putFile(Uri.parse(subImageUri));
        uploadTask.addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                imageRef.getDownloadUrl().addOnCompleteListener(downloadTask -> {
                    if (downloadTask.isSuccessful()) {
                        // Add the download URL to the list
//                            imageUrls.clear();
                        imageUrls.add(downloadTask.getResult().toString());
                    } else {
                        Log.e(TAG, "Error getting download URL: ", downloadTask.getException());
                    }
                    uploadCount++;
                    if (uploadCount == Size) {
                        receivedModel.setNotes(imageUrls);
                        Uri data = Uri.parse(receivedModel.getPath());
                        String path = data.getQueryParameter("path");

//                            Uri path = ?Uri.parse(Uri.parse(receivedModel.getPath()).getQueryParameter("path"));

                        firebaseDatabase.getReference().child(path).setValue(receivedModel).addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()) {
//                                    dialog.dismiss();
                                progressDialog.dismiss();
                                adapters.notifyDataSetChanged();
                            }
                        }).addOnFailureListener(e -> {
                            firebaseDatabase.getReference().child("error").child("Share_createSubject").child(System.currentTimeMillis()+"").push().setValue(e.getMessage());
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

                            progressDialog.dismiss();

                        });
                    }
                });
            } else {
                // Handle the error
                Log.e(TAG, "Error uploading image: ", task.getException());
                // If there was an error, decrement the uploadCount to avoid blocking further uploads
                uploadCount--;
            }
        });
    }
    private String getFileTypeFromMimeType(Uri uri) {
        ContentResolver contentResolver = requireContext().getContentResolver();
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

//        if (mimeType == null) {
//            return "Unknown";
//        } else if (mimeType.startsWith("image/")) {
//            return "Image";
//        } else if (mimeType.equals("application/pdf")) {
//            return "PDF";
//        } else if (mimeType.equals("application/msword")) {
//            return "DOC";
//        } else {
//            return "Unknown";
//        }
    }
}