package com.crazyostudio.studentcircle.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MotionEventCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.crazyostudio.studentcircle.R;
import com.crazyostudio.studentcircle.adapters.ImageAdapters;
import com.crazyostudio.studentcircle.adapters.NotesAdapters;
import com.crazyostudio.studentcircle.databinding.ActivityImageEditorBinding;
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
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Objects;

// TODO: 8/12/2023 Fix Some like uploading image to firebase database add a ProgressDialog
//  (private all var)
// Under
public class ShareFileFragment extends Fragment {
    private FragmentShareFileBinding binding;
    private FirebaseDatabase firebaseDatabase;
    private ArrayList<byte[]> subImageUri;
    private ImageAdapters imageAdapters;
    private CreatesubjectsBinding createsubjectsBinding;
    private NotesAdapters notesAdapters;
    private Dialog dialog;
    private Uri Data;
    private StorageReference storageRef;
    private int uploadCount = 0;
    private ArrayList<String> imageUrls;
    private float[] lastEvent = null;
    private Dialog EditorDialog;
    private ActivityImageEditorBinding editorBinding;
    private ProgressDialog progressDialog;

    public ShareFileFragment() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentShareFileBinding.inflate(inflater,container,false);
        OnStart();



        getNotes();
        binding.createSubject.setOnClickListener(v -> showCrateSubject());

        return binding.getRoot();
    }

    private void OnStart(){
        storageRef = FirebaseStorage.getInstance().getReference("Share");
        firebaseDatabase = FirebaseDatabase.getInstance();
        createsubjectsBinding = CreatesubjectsBinding.inflate(getLayoutInflater());
        imageUrls = new ArrayList<>();
        subImageUri = new ArrayList<>();
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);
    }

    private void getNotes(){
        ArrayList<SubjectModel> subjectModel = new ArrayList<>();
        notesAdapters = new NotesAdapters(subjectModel,getContext());
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
    private void showCrateSubject(){
        dialog = new Dialog(getContext());
        dialog.setContentView(createsubjectsBinding.getRoot());
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.createsubjectsboxbg);
        dialog.setCancelable(false);
        dialog.getWindow().getAttributes().windowAnimations = R.style.Animationboy;
        imageAdapters = new ImageAdapters(subImageUri,getContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        createsubjectsBinding.recyclerView.setLayoutManager(layoutManager);
        createsubjectsBinding.recyclerView.setAdapter(imageAdapters);
        createsubjectsBinding.CANCEL.setOnClickListener(cancel->dialog.dismiss());
        createsubjectsBinding.save.setOnClickListener(save->{
            if (!createsubjectsBinding.subName.getText().toString().isEmpty()) {
                progressDialog.setTitle("Uploading....");
                progressDialog.show();
                StorageReference file = storageRef.child(System.currentTimeMillis()+"."+ filletExtension(Data));
                file.putFile(Data).addOnSuccessListener(taskSnapshot -> file.getDownloadUrl().addOnSuccessListener(fileUri1 -> {
                    long time = System.currentTimeMillis();
                    uploadMultipleImages(fileUri1.toString(),time);
//                    for (int i = 0; i < subImageUri.size(); i++) {
////                        uploadNotes(Uri.parse(subImageUri.get(i)));
//                    }
//                    https://friend-circle-f948a-default-rtdb.firebaseio.com
                })).addOnFailureListener(e -> {
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
                .start(159));
        createsubjectsBinding.addImage.setOnClickListener(v ->
                ImagePicker.with(this)

                .compress(1024).maxResultSize(1080, 1080)
                .start(160));
//        createsubjectsBinding.addImage.setOnClickListener(view->{
//            ShowDialog();
//        });

//        createsubjectsBinding.addImage.setOnClickListener(p->
//        {
//            dialog = new Dialog(getContext());
//            dialog.setContentView(createsubjectsBinding.getRoot());
//            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//            dialog.getWindow().setBackgroundDrawableResource(R.drawable.createsubjectsboxbg);
//            dialog.setCancelable(false);
//            dialog.getWindow().getAttributes().windowAnimations = R.style.Animationboy;
//        });

        dialog.show();
    }

    @SuppressLint("ClickableViewAccessibility")
    void  ShowDialog(Uri uri){

        editorBinding = ActivityImageEditorBinding.inflate(getLayoutInflater());
        EditorDialog = new Dialog(getContext());
        EditorDialog.setContentView(editorBinding.getRoot());
        EditorDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        EditorDialog.getWindow().setBackgroundDrawableResource(R.drawable.createsubjectsboxbg);
        EditorDialog.setCancelable(false);
        EditorDialog.getWindow().getAttributes().windowAnimations = R.style.Animationboy;
        EditorDialog.show();

        editorBinding.addTextButton.setOnClickListener(v -> {
            editorBinding.editTextOverlay.setVisibility(View.VISIBLE);
            editorBinding.editTextOverlay.setText("");
            editorBinding.editTextOverlay.requestFocus();
        });

        editorBinding.photoView.setImageURI(uri);

        editorBinding.editTextOverlay.setOnTouchListener((v, event) -> {
            int action = MotionEventCompat.getActionMasked(event);
            if (action == MotionEvent.ACTION_MOVE) {
                if (lastEvent != null && event.getPointerCount() == 1) {
                    float dx = event.getX() - lastEvent[0];
                    float dy = event.getY() - lastEvent[1];
                    Matrix textMatrix = editorBinding.editTextOverlay.getMatrix();
                    textMatrix.postTranslate(dx, dy);
                }
                lastEvent = new float[]{event.getX(), event.getY()};
            } else {
                lastEvent = null;
            }
            return true;
        });

        editorBinding.saveButton.setOnClickListener(v -> {
            Bitmap editedImage = createEditedImage();

            // Convert Bitmap to byte array
            ByteArrayOutputStream bass = new ByteArrayOutputStream();
            editedImage.compress(Bitmap.CompressFormat.JPEG, 100, bass);
            byte[] data = bass.toByteArray();
            subImageUri.add(data);
            EditorDialog.dismiss();
//             finishActivity(55);
//            Intent previousScreen = new Intent();
//            //Sending the data to Activity_A
//            previousScreen.putExtra("Image",data);
//            previousScreen.putExtra("hello","data");
//            setResult(1000, previousScreen);
//            finish();
            // Define the path where the image will be stored in Firebase Storage
//            String fileName = "edited_image.jpg";
//            StorageReference imageRef = storageReference.child(fileName);
//
//            // Upload the image to Firebase Storage
//            UploadTask uploadTask = imageRef.putBytes(data);
//            uploadTask.addOnCompleteListener(task -> {
//                if (task.isSuccessful()) {
//                    // Image upload is successful
//                    // You can now get the download URL of the image to use it later
//                    imageRef.getDownloadUrl().addOnCompleteListener(task1 -> {
//                        if (task1.isSuccessful()) {
//                            Uri downloadUri = task1.getResult();
//                            // Here you have the download URL of the uploaded image
//                            // You can use this URL to display the image or store it in your database.
//                        } else {
//                            // Failed to get the download URL
//                        }
//                    });
//                } else {
//                    // Image upload failed
//                }
//            });
//            // Save the 'editedImage' to Firebase Storage or process it further.
//            // You can use the Bitmap for any other operation, like uploading to Firebase Storage.
//
//            // Ensure that you have proper Firebase setup and appropriate rules for storage access.
        });


    }

    private void uploadMultipleImages(String fileUri1,long time) {
        for (byte[] imageUri : subImageUri) {
            // Get a unique filename for each image
            String imageName = time + filletExtension(Uri.parse(String.valueOf(imageUri)));
            StorageReference imageRef = storageRef.child(imageName);

            // Upload the image to Firebase Storage
            UploadTask uploadTask = imageRef.putBytes(imageUri);
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
                        if (uploadCount == subImageUri.size()) {
                            // All images have been uploaded, do something with the URLs
//                            saveUrlsToDatabase(imageUrls);
                            String id =  "https://crazy-studio-website.web.app/user?path="+Uri.encode( "/Share/"+FirebaseAuth.getInstance().getUid()+"/"+time+createsubjectsBinding.subName.getText().toString());
                            Log.i("SubNotesUri", "showCrateSubject: "+imageUrls);
                            SubjectModel model = new SubjectModel(createsubjectsBinding.subName.getText().toString(), fileUri1,time,imageUrls,id);
                            firebaseDatabase.getReference().child("Share").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).child(time+createsubjectsBinding.subName.getText().toString()).setValue(model).addOnCompleteListener(task1 -> {
                                if (task.isSuccessful()) {
                                    dialog.dismiss();
                                    if (progressDialog.isShowing()) {
                                        progressDialog.dismiss();
                                    }
                                }
                            }).addOnFailureListener(e -> {
                                firebaseDatabase.getReference().child("error").child("Share_createSubject").child(System.currentTimeMillis()+"").push().setValue(e.getMessage());
                                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                                if (progressDialog.isShowing()) {
                                    progressDialog.dismiss();
                                }

                            });
                        }
                    });
                } else {
                    // Handle the error
                    // If there was an error, decrement the uploadCount to avoid blocking further uploads
                    uploadCount--;
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
        if (data.getData() != null && requestCode==159) {
            Data = data.getData();
            createsubjectsBinding.userAvatar.setImageURI(data.getData());
//            userAvatar Image
        }
        else if (data.getData()!=null&&requestCode==160) {
//            subImageUri.add(data.getData().toString());
//            startImageCrop(data.getData());
            ShowDialog(data.getData());
            imageAdapters.notifyDataSetChanged();
        }
//        }else if (data.getData()!=null&&requestCode==1000){
//            Toast.makeText(getContext(), data.getExtras().getString("hello"), Toast.LENGTH_SHORT).show();
//        }
    }

//    private void startImageCrop(Uri sourceUri) {
//        Intent intent = new Intent(getContext(), ImageEditorActivity.class);
//        intent.putExtra("sourceUri",sourceUri);
//        startActivityForResult(intent,44);
//
//    }
//
    public Bitmap createEditedImage() {
        // Create a new Bitmap to hold the edited image
        Bitmap editedBitmap = Bitmap.createBitmap(
                editorBinding.photoView.getWidth(), editorBinding.photoView.getHeight(),
                Bitmap.Config.ARGB_8888);

        // Create a Canvas with the new Bitmap
        Canvas canvas = new Canvas(editedBitmap);

        // Draw the original photoView's image on the Canvas
        editorBinding.photoView.draw(canvas);

        // Calculate the text position on the photoView's image
        float x = editorBinding.editTextOverlay.getX();
        float y = editorBinding.editTextOverlay.getY();
        x = (x / editorBinding.photoView.getWidth()) * editedBitmap.getWidth();
        y = (y / editorBinding.photoView.getHeight()) * editedBitmap.getHeight();

        // Draw the text on the Canvas at the calculated position
        canvas.drawText(editorBinding.editTextOverlay.getText().toString(), x, y, editorBinding.editTextOverlay.getPaint());

        // Hide the EditText for the final image capture
        editorBinding.editTextOverlay.setVisibility(View.INVISIBLE);

        return editedBitmap;
    }

}