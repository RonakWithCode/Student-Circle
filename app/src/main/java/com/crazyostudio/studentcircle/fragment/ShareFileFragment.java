package com.crazyostudio.studentcircle.fragment;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.crazyostudio.studentcircle.InterfaceCLass.ImageInterface;
import com.crazyostudio.studentcircle.R;
import com.crazyostudio.studentcircle.adapters.ImageAdapters;
import com.crazyostudio.studentcircle.adapters.NotesAdapters;
import com.crazyostudio.studentcircle.databinding.CreatesubjectsBinding;
import com.crazyostudio.studentcircle.databinding.FragmentShareFileBinding;
import com.crazyostudio.studentcircle.model.SubjectModel;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Objects;

// TODO: 8/11/2023 Fix Some bugs like uploading image to firebase database add a ProgressDialog
//  (private all var)
// Under
public class ShareFileFragment extends Fragment implements ImageInterface {
    private FragmentShareFileBinding binding;
    private FirebaseDatabase firebaseDatabase;
    private ArrayList<Uri> subImageUri;
    private ImageAdapters imageAdapters;
    private CreatesubjectsBinding createsubjectsBinding;
    private NotesAdapters notesAdapters;
    private Dialog dialog;
    private Uri Data;
    private StorageReference storageRef;
    private ProgressDialog progressDialog;

    int UPLOAD_SIZE = 0;
    private static final int READ_STORAGE_PERMISSION_CODE = 100;
    private static final int SUB_NOTES_CODE = 1;
    private static final int USER_DP_CODE = 2;

    public ShareFileFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentShareFileBinding.inflate(inflater, container, false);
        OnStart();


        getNotes();
        binding.createSubject.setOnClickListener(v -> showCrateSubject());

        return binding.getRoot();
    }

    private void OnStart() {
        storageRef = FirebaseStorage.getInstance().getReference("Share");
        firebaseDatabase = FirebaseDatabase.getInstance();
        createsubjectsBinding = CreatesubjectsBinding.inflate(getLayoutInflater());
        subImageUri = new ArrayList<>();
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);
    }

    private void getNotes() {
        ArrayList<SubjectModel> subjectModel = new ArrayList<>();
        notesAdapters = new NotesAdapters(subjectModel, getContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.subjects.setLayoutManager(layoutManager);
        binding.subjects.setAdapter(notesAdapters);
        firebaseDatabase.getReference().child("Share").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                subjectModel.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    SubjectModel snapshot1Value = snapshot1.getValue(SubjectModel.class);
                    subjectModel.add(snapshot1Value);

                    notesAdapters.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @SuppressLint("RestrictedApi")
    private void showCrateSubject() {
        ViewGroup parent = (ViewGroup) createsubjectsBinding.getRoot().getParent();
        if (parent != null) {
            parent.removeView(createsubjectsBinding.getRoot());
        }
        dialog = new Dialog(getContext());
        dialog.setContentView(createsubjectsBinding.getRoot());
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.createsubjectsboxbg);
        dialog.setCancelable(false);
        dialog.getWindow().getAttributes().windowAnimations = R.style.Animationboy;
        imageAdapters = new ImageAdapters(subImageUri, getContext(), this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        createsubjectsBinding.recyclerView.setLayoutManager(layoutManager);
        createsubjectsBinding.recyclerView.setAdapter(imageAdapters);
        createsubjectsBinding.CANCEL.setOnClickListener(cancel -> dialog.dismiss());
        createsubjectsBinding.save.setOnClickListener(save -> {
            if (!createsubjectsBinding.subName.getText().toString().isEmpty()) {
                long time = System.currentTimeMillis();
                progressDialog.setTitle("Uploading....");
                progressDialog.show();
                StorageReference file = storageRef.child(time + "." + filletExtension(Data));
                file.putFile(Data).addOnSuccessListener(taskSnapshot -> file.getDownloadUrl().addOnSuccessListener(fileUri1 -> UploadToFirebase(time, fileUri1.toString()))).addOnFailureListener(e -> {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                });

            }

        });
        createsubjectsBinding.userAvatar.setOnClickListener(v -> ImagePicker.with(this)
                .crop()
                .compress(1024)
                .maxResultSize(1080, 1080)
                .start(USER_DP_CODE));

        createsubjectsBinding.addImage.setOnClickListener(v ->
        {
            if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_STORAGE_PERMISSION_CODE);
            } else {
                String[] mimeTypes =
                        {"application/msword", "application/vnd.openxmlformats-officedocument.wordprocessingml.document", // .doc & .docx
                                "application/vnd.ms-powerpoint", "application/vnd.openxmlformats-officedocument.presentationml.presentation", // .ppt & .pptx
                                "application/vnd.ms-excel", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", // .xls & .xlsx
                                "text/plain",
                                "application/pdf",
                                "application/zip",
                                "image/*"
                        };

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                startActivityForResult(Intent.createChooser(intent, "Choose File"), SUB_NOTES_CODE);


            }
        });
        dialog.show();
    }

    private void UploadToFirebase(long time, String USER_DP) {
        ArrayList<String> downloadURI = new ArrayList<>();
        for (int i = 0; i < subImageUri.size(); i++) {
            StorageReference imageRef = storageRef.child(System.currentTimeMillis() + filletExtension(subImageUri.get(i)));
            imageRef.putFile(subImageUri.get(i)).addOnCompleteListener(task -> {
                if (!task.isSuccessful()) {
                    Toast.makeText(getContext(), "One file is not upload error "+ Objects.requireNonNull(task.getException()).getMessage() , Toast.LENGTH_SHORT).show();
                    UPLOAD_SIZE--;
                } else {
                    UPLOAD_SIZE++;
                    imageRef.getDownloadUrl().addOnCompleteListener(downloadTask -> {
                        downloadURI.add(downloadTask.getResult().toString());
                        if (downloadURI.size() == subImageUri.size()) {
                            String id = "https://crazy-studio-website.web.app/user?path=" + Uri.encode("/Share/" + FirebaseAuth.getInstance().getUid() + "/" + time + createsubjectsBinding.subName.getText().toString());
                            SubjectModel model = new SubjectModel(createsubjectsBinding.subName.getText().toString(), USER_DP, time, downloadURI, id);
                            firebaseDatabase.getReference().child("Share").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).child(time + createsubjectsBinding.subName.getText().toString()).setValue(model).addOnCompleteListener(task1 -> {
                                if (task.isSuccessful()) {
                                    dialog.dismiss();
                                    if (progressDialog.isShowing()) {
                                        progressDialog.dismiss();
                                    }
                                }
                            }).addOnFailureListener(e -> {
                                firebaseDatabase.getReference().child("error").child("Share_createSubject").child(System.currentTimeMillis() + "").push().setValue(e.getMessage());
                                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                                if (progressDialog.isShowing()) {
                                    progressDialog.dismiss();
                                }

                            });
                        }
                    });
                }
            });


        }
    }


    private String filletExtension(Uri Uri) {
        ContentResolver contentResolver = requireContext().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

        // Get the file extension based on the Uri's MIME type
        String extension = mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(Uri));

        if (extension == null) {
            // If the MIME type doesn't provide an extension, try to extract from the Uri's path
            String path = Uri.getPath();
            if (path != null) {
                int extensionStartIndex = path.lastIndexOf('.');
                if (extensionStartIndex != -1) {
                    extension = path.substring(extensionStartIndex + 1);
                }
            }
        }

        return extension;
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        assert data != null;
        if (data.getData() != null && requestCode == USER_DP_CODE && resultCode == RESULT_OK) {
            Data = data.getData();
            createsubjectsBinding.userAvatar.setImageURI(data.getData());
//            userAvatar Image
        } else if (requestCode == SUB_NOTES_CODE && resultCode == RESULT_OK) {
            if (data.getClipData() != null) {
                int count = data.getClipData().getItemCount();
                for (int i = 0; i < count; i++) {
                    Uri fileUri = data.getClipData().getItemAt(i).getUri();
                    subImageUri.add(fileUri);
                    imageAdapters.notifyDataSetChanged();
                }
            } else {
                if (data.getData() != null) {
                    subImageUri.add(data.getData());
                    imageAdapters.notifyDataSetChanged();
                }
            }
        } else {
            Toast.makeText(getContext(), "RETRY", Toast.LENGTH_SHORT).show();
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void remove(int pos) {
        subImageUri.remove(pos);
        imageAdapters.notifyDataSetChanged();
    }
}