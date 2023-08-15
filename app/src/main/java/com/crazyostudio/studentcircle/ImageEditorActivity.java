package com.crazyostudio.studentcircle;

import android.app.Activity;
import android.net.Uri;

import com.crazyostudio.studentcircle.databinding.ActivityImageEditorBinding;

public class ImageEditorActivity{
    public static ActivityImageEditorBinding binding;
//    private static StorageReference storageReference;
    public static void create(Uri uri, Activity activity){
        ActivityImageEditorBinding.inflate(activity.getLayoutInflater());
//        String uri  = String.valueOf(getIntent().getData());
//        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
//        storageReference = firebaseStorage.getReference().child("edited_images");

        binding.photoView.setImageURI(uri);


    }


}
