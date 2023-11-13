package com.crazyostudio.studentcircle.Chats;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.OpenableColumns;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.crazyostudio.studentcircle.R;
import com.crazyostudio.studentcircle.adapters.ChatAdapters;
import com.crazyostudio.studentcircle.databinding.ActivityChatBinding;
import com.crazyostudio.studentcircle.databinding.SandingoptionsBinding;
import com.crazyostudio.studentcircle.fragmentLoad;
import com.crazyostudio.studentcircle.model.ChatAdaptersInterface;
import com.crazyostudio.studentcircle.model.Chat_Model;
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

public class chat extends AppCompatActivity implements ChatAdaptersInterface {
    public ActivityChatBinding binding;
    private StorageReference reference;
    String UserName, UserImage, UserId, SandId, UserBio;
    FirebaseDatabase firebaseDatabase;
    int documentCode = 1, CameraCode = 2,ContactCode=3;
    private static final long KB_SIZE = 1024; // 1 KB in bytes
    private static final long MB_SIZE = KB_SIZE * 1024; // 1 MB in bytes
    FirebaseAuth auth;
    ChatAdapters chatAdapters;
    String sanderRoom, recRoom;
    boolean IsChatsProgressBar = false;

    @SuppressLint({"NotifyDataSetChanged", "ResourceAsColor", "NonConstantResourceId", "IntentReset"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        if (!IsChatsProgressBar) {
            binding.ChatsProgressBar.setVisibility(View.VISIBLE);
            IsChatsProgressBar = true;
        }
        WhenAppStart();
        binding.toolbar2.setOnClickListener(view -> {
            Intent intent = new Intent(this, fragmentLoad.class);
            intent.putExtra("LoadID", "ReceiverProfileView");
            intent.putExtra("name", UserName);
            intent.putExtra("Images", UserImage);
            intent.putExtra("Bio", UserBio);
            intent.putExtra("ReceiverId", UserId);
            startActivity(intent);

//            ReceiverProfileView

//            imgaepicker

        });
        binding.toolbar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()){
                case R.id.menu_Profile:
                    Toast.makeText(this, "make", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.menu_Theme:
                    Intent intent = new Intent(this, fragmentLoad.class);
                    intent.putExtra("LoadID","ThemeChange");
                    intent.putExtra("ThemeID",UserId);
                    startActivity(intent);
                    return true;
                case R.id.menu_main_clear:
                    Toast.makeText(this, "mswake", Toast.LENGTH_SHORT).show();
                    return true;

                case R.id.menu_main_setting:
                    Toast.makeText(this, "maske", Toast.LENGTH_SHORT).show();
                    return true;
                default:
                    break;
            }
            return false;
        });
        binding.username.setText(UserName);
        Glide.with(this).load(UserImage).into(binding.userImage);
        binding.BackBts.setOnClickListener(view -> finish());
        binding.imageBts.setOnClickListener(view -> {
            SandingoptionsBinding sandingoptionsBinding = SandingoptionsBinding.inflate(getLayoutInflater());
            LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            View view1 = layoutInflater.inflate(R.layout.sandingoptions, null);
            PopupWindow popupWindow = new PopupWindow(view1, ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT, true);
//            popupWindow.showAtLocation(binding.getRoot(),Gravity.BOTTOM,0,0);
            popupWindow.setContentView(sandingoptionsBinding.getRoot());
            popupWindow.showAtLocation(binding.getRoot(), Gravity.BOTTOM, 0, 2);
            sandingoptionsBinding.camera.setOnClickListener(view2 -> ImagePicker.with(this)
                    .crop()
                    .compress(1024)
                    .maxResultSize(800, 800)
                    .start(CameraCode));
            sandingoptionsBinding.document.setOnClickListener(view2 -> {
                browseDocuments();
            });
            sandingoptionsBinding.contact.setOnClickListener(view3 -> {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT, ContactsContract.Contacts.CONTENT_URI);
                intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
                startActivityForResult(intent, ContactCode);
            });
        });
        binding.SandBts.setOnClickListener(view -> {
            if (!binding.InputText.getText().toString().isEmpty()) {
                long time = System.currentTimeMillis();
                final Chat_Model Chat = new Chat_Model(SandId, binding.InputText.getText().toString(), time, false);
                binding.InputText.setText("");
                firebaseDatabase.getReference().child("chats").child(sanderRoom).child("id="+time).setValue(Chat).addOnSuccessListener(unused -> firebaseDatabase.getReference().child("chats").child(recRoom).child("id="+time).setValue(Chat).addOnSuccessListener(unused1 -> chatAdapters.notifyDataSetChanged()));
            } else {
                Toast.makeText(this, "Enter your text", Toast.LENGTH_SHORT).show();
            }
        });
        LoadChats();

    }
    @SuppressLint("ResourceAsColor")
    void WhenAppStart() {
        Objects.requireNonNull(getSupportActionBar()).hide();
        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = FirebaseStorage.getInstance().getReference("ChatImage");
        auth = FirebaseAuth.getInstance();
        UserName = getIntent().getStringExtra("name");
        UserImage = getIntent().getStringExtra("Images");
        UserBio = getIntent().getStringExtra("Bio");
        SandId = Objects.requireNonNull(auth.getCurrentUser()).getUid();
        UserId = getIntent().getStringExtra("UserId");
        sanderRoom = SandId + UserId;
        recRoom = UserId + SandId;
        SharedPreferences sharedPreferences = getSharedPreferences("ImageResourceId", Context.MODE_PRIVATE);
        int imageResourceId = sharedPreferences.getInt(UserId, 2); // Use the same key as when you saved it
        if (imageResourceId != 2) {
            binding.getRoot().setBackgroundResource(imageResourceId);
        }
        else {
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O) {
                // Handle any other exceptions
                binding.getRoot().setBackgroundResource(R.drawable.app);
            }else {
                binding.getRoot().setBackgroundResource(R.drawable.bglayout);
            }
        }

    }
    private void LoadChats() {
        ArrayList<Chat_Model> ChatModels = new ArrayList<>();
        chatAdapters = new ChatAdapters(ChatModels, this,this,this);
        binding.recyclerView2.setAdapter(chatAdapters);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.recyclerView2.setLayoutManager(layoutManager);
        firebaseDatabase.getReference().child("chats").child(sanderRoom).addValueEventListener(new ValueEventListener() {

            @SuppressLint("NotifyDataSetChanged")
            @Override

            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ChatModels.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {

                    Chat_Model _ChatModel = snapshot1.getValue(Chat_Model.class);
                    if (IsChatsProgressBar) {
                        binding.ChatsProgressBar.setVisibility(View.GONE);
                    }
                    ChatModels.add(_ChatModel);
                }
                chatAdapters.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
    private String getFileExtensionFromUri(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
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
    @SuppressLint({"NotifyDataSetChanged", "Range"})
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        assert data != null;
        if (data.getData() != null && requestCode == CameraCode) {
            final Uri dataUri = data.getData();
//            binding.userImage.setImageURI(dataUri);
            long Time = System.currentTimeMillis();
            StorageReference file = reference.child(Time + "." + getFileExtensionFromUri(dataUri));
            Toast.makeText(this, "Image Sand ", Toast.LENGTH_SHORT).show();
            file.putFile(dataUri).addOnSuccessListener(taskSnapshot -> file.getDownloadUrl().addOnSuccessListener(uri -> {
                final Chat_Model Chat = new Chat_Model(SandId, uri.toString(), true, false, false, Time,file.getName());
                firebaseDatabase.getReference().child("chats").child(sanderRoom).child("id="+Time).setValue(Chat).addOnSuccessListener(unused -> firebaseDatabase.getReference().child("chats").child(recRoom).child("id="+Time).setValue(Chat).addOnSuccessListener(unused1 -> chatAdapters.notifyDataSetChanged()));
            })).addOnFailureListener(e -> Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show());

        } else if (data.getData() != null && requestCode == documentCode) {
            if (resultCode == RESULT_OK) {
                // Get the Uri of the selected file
                Uri uri = data.getData();
                Uri returnUri = data.getData();
                @SuppressLint("Recycle") Cursor returnCursor =
                        getContentResolver().query(returnUri, null, null, null, null);

                int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
                String size;
                returnCursor.moveToFirst();
                if (returnCursor.getLong(sizeIndex) >= MB_SIZE) {
                    size = (returnCursor.getLong(sizeIndex) / MB_SIZE) + " MB";
                } else if (returnCursor.getLong(sizeIndex) >= KB_SIZE) {
                    size = (returnCursor.getLong(sizeIndex) / KB_SIZE) + " KB";
                } else {
                    size = returnCursor.getLong(sizeIndex) + " bytes";
                }


                long Time = System.currentTimeMillis();
                StorageReference file = reference.child(Time + returnCursor.getString(nameIndex));
                Toast.makeText(this, "Image PDF ", Toast.LENGTH_SHORT).show();
//                String finalDisplayName = displayName;
                file.putFile(uri).addOnSuccessListener(taskSnapshot -> file.getDownloadUrl().addOnSuccessListener(uri1 -> {

                    final Chat_Model Chat = new Chat_Model(SandId, uri1.toString(), Time, false, false, false, true, returnCursor.getString(nameIndex), size, "1");
                    firebaseDatabase.getReference().child("chats").child(sanderRoom).child("id="+Time).setValue(Chat).addOnSuccessListener(unused -> firebaseDatabase.getReference().child("chats").child(recRoom).child("id="+Time).setValue(Chat).addOnSuccessListener(unused1 -> chatAdapters.notifyDataSetChanged()));

                }).addOnFailureListener(e -> {
                }));
            }
        } else if (data.getData() != null && requestCode == ContactCode) {

            Cursor cursor;
            try {
                String phoneNo;
                String name;

                Uri uri = data.getData();
                cursor = getContentResolver().query(uri, null, null, null, null);
                cursor.moveToFirst();
                int  phoneIndex =cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                int  nameIndex =cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                phoneNo = cursor.getString(phoneIndex);
                name = cursor.getString(nameIndex);
                long time = System.currentTimeMillis();
                final Chat_Model Chat = new Chat_Model(SandId, name, time,false, false, false, false, true,phoneNo);
                firebaseDatabase.getReference().child("chats").child(sanderRoom).child("id="+time).setValue(Chat).addOnSuccessListener(unused -> firebaseDatabase.getReference().child("chats").child(recRoom).child("id="+time).setValue(Chat).
                        addOnSuccessListener(unused1 -> chatAdapters.notifyDataSetChanged()));

            } catch (Exception e) {
                e.printStackTrace();
            }



        }
    }
    private void browseDocuments(){
        String[] mimeTypes =
                {"application/msword","application/vnd.openxmlformats-officedocument.wordprocessingml.document", // .doc & .docx
                        "application/vnd.ms-powerpoint","application/vnd.openxmlformats-officedocument.presentationml.presentation", // .ppt & .pptx
                        "application/vnd.ms-excel","application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", // .xls & .xlsx
                        "text/plain",
                        "application/pdf",
                        "application/zip",
                        "image/*"
                };

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        startActivityForResult(Intent.createChooser(intent,"ChooseFile"), documentCode);
    }

//  Sander Text

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void Delete(Chat_Model chatModel) {
        Dialog DeleteDialog = new Dialog(chat.this);
        DeleteDialog.setContentView(R.layout.deletelayoutbox);
        DeleteDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        DeleteDialog.getWindow().setBackgroundDrawableResource(R.drawable.roundlayoput);

        DeleteDialog.setCancelable(false);
        TextView deleteForMe = DeleteDialog.findViewById(R.id.deleteForMe);
        TextView deleteForEveryBody = DeleteDialog.findViewById(R.id.deleteForEveryBody);
        TextView cancel = DeleteDialog.findViewById(R.id.cancel_action_);
        deleteForMe.setOnClickListener(delete->{
            firebaseDatabase.getReference().child("chats").child(sanderRoom).child("id="+chatModel.getSandTime()).removeValue().addOnSuccessListener(unused1 -> chatAdapters.notifyDataSetChanged());
            DeleteDialog.dismiss();

            Toast.makeText(this, "delete", Toast.LENGTH_SHORT).show();
        });
        deleteForEveryBody.setOnClickListener(everyBody->{
            firebaseDatabase.getReference().child("chats").child(sanderRoom).child("id="+chatModel.getSandTime()).removeValue().addOnSuccessListener(unused -> firebaseDatabase.getReference().child("chats").child(recRoom).child("id="+chatModel.getSandTime()).removeValue().addOnSuccessListener(unused1 -> chatAdapters.notifyDataSetChanged()));
            DeleteDialog.dismiss();

            Toast.makeText(this, "delete", Toast.LENGTH_SHORT).show();
        });
        cancel.setOnClickListener(cancelBtu->{
            DeleteDialog.dismiss();
            Toast.makeText(this, "cancel", Toast.LENGTH_SHORT).show();

        });
        DeleteDialog.show();
    }

    @SuppressLint("NotifyDataSetChanged")
//  Receive Text
    @Override
    public void ReceiveDelete(Chat_Model chatModel) {
        Dialog DeleteDialog = new Dialog(chat.this);
        DeleteDialog.setContentView(R.layout.receiver_delete_message_layout);
        DeleteDialog.getWindow().setBackgroundDrawableResource(R.drawable.roundlayoput);
        DeleteDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        DeleteDialog.setCancelable(false);
        TextView deleteForMe = DeleteDialog.findViewById(R.id.receiverDeleteForMe);
        TextView cancel = DeleteDialog.findViewById(R.id.receiverCancel_action_);
        deleteForMe.setOnClickListener(delete->{
            firebaseDatabase.getReference().child("chats").child(sanderRoom).child("id="+chatModel.getSandTime()).removeValue().addOnSuccessListener(unused1 -> chatAdapters.notifyDataSetChanged());
            DeleteDialog.dismiss();

            Toast.makeText(this, "delete", Toast.LENGTH_SHORT).show();
        });
        cancel.setOnClickListener(cancelBtu->{
            DeleteDialog.dismiss();
            Toast.makeText(this, "cancel", Toast.LENGTH_SHORT).show();
        });
        DeleteDialog.show();
    }
    @SuppressLint("NotifyDataSetChanged")
//    Sander Image
    @Override
    public void DeleteImage(Chat_Model chatModel) {
        Dialog DeleteDialog = new Dialog(chat.this);
        DeleteDialog.setContentView(R.layout.deletelayoutbox);
        DeleteDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        DeleteDialog.getWindow().setBackgroundDrawableResource(R.drawable.roundlayoput);

        DeleteDialog.setCancelable(false);
        TextView deleteForMe = DeleteDialog.findViewById(R.id.deleteForMe);
        TextView deleteForEveryBody = DeleteDialog.findViewById(R.id.deleteForEveryBody);
        TextView cancel = DeleteDialog.findViewById(R.id.cancel_action_);
        deleteForMe.setOnClickListener(delete->{

            firebaseDatabase.getReference().child("chats").child(sanderRoom).child("id="+chatModel.getSandTime()).removeValue().addOnSuccessListener(unused1 -> chatAdapters.notifyDataSetChanged());
            DeleteDialog.dismiss();

            Toast.makeText(this, "delete", Toast.LENGTH_SHORT).show();
        });
        deleteForEveryBody.setOnClickListener(everyBody->{
            firebaseDatabase.getReference().child("chats").child(sanderRoom).child("id="+chatModel.getSandTime()).removeValue().addOnSuccessListener(unused -> firebaseDatabase.getReference().child("chats").child(recRoom).child("id="+chatModel.getSandTime()).removeValue().addOnSuccessListener(unused1 -> chatAdapters.notifyDataSetChanged()));
            DeleteDialog.dismiss();
            StorageReference deleteFile = reference.child(chatModel.getMessage());
            deleteFile.delete().addOnSuccessListener(aVoid -> {
                chatAdapters.notifyDataSetChanged();
            });
            Toast.makeText(this, "delete", Toast.LENGTH_SHORT).show();
        });
        cancel.setOnClickListener(cancelBtu->{
            DeleteDialog.dismiss();
            Toast.makeText(this, "cancel", Toast.LENGTH_SHORT).show();

        });
        DeleteDialog.show();
    }
    @SuppressLint("NotifyDataSetChanged")
    //    Receive Image
    @Override
    public void ReceiveDeleteImage(Chat_Model chatModel){
        Dialog DeleteDialog = new Dialog(chat.this);
        DeleteDialog.setContentView(R.layout.receiver_delete_message_layout);
        DeleteDialog.getWindow().setBackgroundDrawableResource(R.drawable.roundlayoput);
        DeleteDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        DeleteDialog.setCancelable(false);
        TextView deleteForMe = DeleteDialog.findViewById(R.id.receiverDeleteForMe);
        TextView cancel = DeleteDialog.findViewById(R.id.receiverCancel_action_);
        deleteForMe.setOnClickListener(delete->{
//            firebaseDatabase.getReference().child("chats").child(recRoom).child("id="+Time)
            firebaseDatabase.getReference().child("chats").child(sanderRoom).child("id="+chatModel.getSandTime()).removeValue().addOnSuccessListener(unused1 -> {
                DeleteDialog.dismiss();

                chatAdapters.notifyDataSetChanged();
            }).addOnFailureListener(e -> {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                DeleteDialog.dismiss();
            });

//            Toast.makeText(this, "delete", Toast.LENGTH_SHORT).show();
        });
        cancel.setOnClickListener(cancelBtu->{
            DeleteDialog.dismiss();
            Toast.makeText(this, "cancel", Toast.LENGTH_SHORT).show();
        });
        DeleteDialog.show();
    }
//  Full Screen Image
    @Override
    public void ImageView(Chat_Model chatModel) {
        binding.getRoot().setOnClickListener(root->binding.ImageFullScreenLayout.setVisibility(View.GONE));
        binding.ImageFullScreenLayout.setVisibility(View.VISIBLE);
        binding.close.setOnClickListener(view->binding.ImageFullScreenLayout.setVisibility(View.GONE));
        Glide.with(this).load(chatModel.getMessage()).into(binding.photoView);
    }


}
