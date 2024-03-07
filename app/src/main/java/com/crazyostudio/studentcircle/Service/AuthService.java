package com.crazyostudio.studentcircle.Service;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.util.Patterns;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.crazyostudio.studentcircle.R;
import com.crazyostudio.studentcircle.databinding.MessageBoxBinding;
import com.crazyostudio.studentcircle.model.LinksModels;
import com.crazyostudio.studentcircle.model.UserInfo;
import com.crazyostudio.studentcircle.user.SignUp;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
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
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Objects;

public class AuthService {
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    FirebaseUser user = auth.getCurrentUser();

    public interface CallbackUsername{
        void onUsernameExists(boolean Is);
        void onError(DatabaseError error);

    }
    public interface CallbackSetupAccount{
        void onSuccess();
        void onFailure(String error);

    }

    public interface LinksModelsCallback {
        void onLinksModelsRetrieved(ArrayList<LinksModels> linksModelsList);
        void onCancelled(DatabaseError error);
    }
    public interface SetupMediaLinksCallback {
        void onLinksModelsSetup();
        void onFailure(Exception error);
    }

    public String getUid(){
        return auth.getUid();
    }
    @SuppressLint("SetTextI18n")
    public void checkProfileComplete(Activity context){
        database.getReference().child("UserInfo").child(Objects.requireNonNull(auth.getUid())).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    MessageBoxBinding Binding = MessageBoxBinding.inflate(context.getLayoutInflater());
                    Dialog dialog = new Dialog(context);
// Set the layout parameters to center the layout
                    dialog.setContentView(Binding.getRoot());
                    dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    dialog.getWindow().setBackgroundDrawableResource(R.drawable.createsubjectsboxbg);
                    dialog.setCancelable(true);
                    dialog.getWindow().getAttributes().windowAnimations = R.style.Animationboy;
                    dialog.show();
                    Binding.tile.setText("Account Setup Incomplete");
                    Binding.message.setText("There may be a few steps left to finish setting up your account. Please try logging in again or contact our team for help");
                    Binding.okButton.setText("OK");
                    auth.signOut();
                    Binding.okButton.setOnClickListener(view->{
                        dialog.dismiss();
                        context.finish();
                        context.startActivity(new Intent(context, SignUp.class));
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                MessageBoxBinding Binding = MessageBoxBinding.inflate(context.getLayoutInflater());
                Dialog dialog = new Dialog(context);
                dialog.setContentView(Binding.getRoot());
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.getWindow().setBackgroundDrawableResource(R.drawable.createsubjectsboxbg);
                dialog.setCancelable(true);
                dialog.getWindow().getAttributes().windowAnimations = R.style.Animationboy;
                dialog.show();
                Binding.tile.setText(error.getCode());
                Binding.message.setText(error.getMessage());
                Binding.okButton.setText("OK");
                Binding.okButton.setOnClickListener(view-> dialog.dismiss());
            }
        });
    }
    public void checkUsername(String UserName, CallbackUsername callback) {
        database.getReference().child("usernames").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                        String username = childSnapshot.getValue(String.class);
                        if (username != null && username.equals(UserName)) {
                            // Match found, invoke the callback
                            callback.onUsernameExists(true);
                            return;
                        }
                    }
                }
                // No match found, invoke the callback
                callback.onUsernameExists(false);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle database error
                callback.onError(error);
            }
        });
}
    public boolean isValidEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    public void SetupAccount(UserInfo userInfo, Uri profilePic,CallbackSetupAccount callback){
        long time = System.currentTimeMillis();
        if (userInfo != null && profilePic != null) {
            StorageReference storageRef = storage.getReference();
            StorageReference imageRef = storageRef.child(userInfo.getUsername()).child(time+profilePic.getLastPathSegment());

            UploadTask uploadTask = imageRef.putFile(profilePic);

// 3. (Optional) Track progress, success, and failure
            uploadTask.addOnProgressListener(taskSnapshot -> {

                // ... update a progress bar, etc.
            }).addOnSuccessListener(taskSnapshot -> {
                // Image uploaded successfully
                // You can get the download URL using:
                Objects.requireNonNull(Objects.requireNonNull(taskSnapshot.getMetadata()).getReference()).getDownloadUrl().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Uri downloadUrl = task.getResult();
                        String imageUrl = downloadUrl.toString();

                        // Now you have the URL in the 'imageUrl' variable
                        userInfo.setProfilePictureUrl(imageUrl);
                        ArrayList<String> userName = new ArrayList<>();
                        database.getReference().child("usernames").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                                        String username = childSnapshot.getValue(String.class);
                                        userName.add(username);
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }

                        });
                        userName.add(userInfo.getUsername());
                        database.getReference().child("usernames").setValue(userName).addOnCompleteListener(main -> database.getReference().child("UserInfo").child(Objects.requireNonNull(auth.getUid())).setValue(userInfo).addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()){
                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(userInfo.getFullName())
                                        .setPhotoUri(Uri.parse(userInfo.getProfilePictureUrl()))
                                        .build();
                                user.updateProfile(profileUpdates)
                                        .addOnCompleteListener(task2 -> {
                                            if (task2.isSuccessful()) {
                                                callback.onSuccess();
                                            }
                                        });
                            }
                        }).addOnFailureListener(e -> callback.onFailure(e.getMessage())));





                        } else {
                        // Handle errors if getting the download URL fails
                        callback.onFailure("Profile Pic Update error: download URL fails");

                    }
                });
            }).addOnFailureListener(exception -> {
                // Handle upload failure
                callback.onFailure(exception.toString());
            });


//            callback.onSuccess();
        } else {
            // Either userInfo or profilePic (or both) is null
            // Handle the case where any of these parameters are null
            callback.onFailure("Info or profilePic are null");
        }
    }




    public void SetupMediaLinks(ArrayList<LinksModels> models,SetupMediaLinksCallback callback){
        database.getReference().child("UserInfo").child(getUid()).child("LinksModels").setValue(models).addOnCompleteListener(task -> {
//  TODO: add call back to this function
            callback.onLinksModelsSetup();
        }).addOnFailureListener(e -> callback.onFailure(e));

    }

    public void getAlMedialLinks(LinksModelsCallback callback) {
        DatabaseReference userInfoRef = database.getReference().child("UserInfo").child(getUid()).child("LinksModels");
        userInfoRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    ArrayList<LinksModels> linksModelsList = new ArrayList<>();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren())
                    {
                        // Assuming LinksModels has a constructor that takes DataSnapshot as an argument
                        LinksModels linksModel = dataSnapshot.getValue(LinksModels.class);
                        linksModelsList.add(linksModel);
                    }
                    // Invoke the callback method with the retrieved list
                    callback.onLinksModelsRetrieved(linksModelsList);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Pass the error to the callback method
                callback.onCancelled(error);
            }
        });
    }
}
