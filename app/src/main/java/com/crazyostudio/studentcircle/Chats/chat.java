package com.crazyostudio.studentcircle.Chats;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.OpenableColumns;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.PopupWindow;
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
import com.crazyostudio.studentcircle.model.Chat_Model;
import com.crazyostudio.studentcircle.user.SeeUserProfile;
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

public class chat extends AppCompatActivity {
    public static ActivityChatBinding binding;
    private StorageReference reference;
    String UserName, UserImage, UserId, SandId, UserBio;
    FirebaseDatabase firebaseDatabase;
    int documentCode = 1, CameraCode = 2,ContactCode=3;
    private static final long KB_SIZE = 1024; // 1 KB in bytes
    private static final long MB_SIZE = KB_SIZE * 1024; // 1 MB in bytes

    FirebaseAuth auth;
    ChatAdapters chatAdapters;
    String sanderRoom, recRoom;


    @SuppressLint({"NotifyDataSetChanged", "ResourceAsColor"})


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O) {
            try {
                binding.getRoot().setBackgroundColor(R.color.black);
//                binding.getRoot().setBackgroundResource(R.drawable.bglay);

            } catch (Exception e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        } else {
            try {
                binding.getRoot().setBackgroundResource(R.drawable.bglayout);
            } catch (Exception ignored) {

            }
        }
        setContentView(binding.getRoot());
        firebaseDatabase = FirebaseDatabase.getInstance();
        Objects.requireNonNull(getSupportActionBar()).hide();
        reference = FirebaseStorage.getInstance().getReference("ChatImage");
        auth = FirebaseAuth.getInstance();
        UserName = getIntent().getStringExtra("name");
        UserImage = getIntent().getStringExtra("Images");
        UserBio = getIntent().getStringExtra("Bio");
        SandId = Objects.requireNonNull(auth.getCurrentUser()).getUid();
        UserId = getIntent().getStringExtra("UserId");

        binding.toolbar2.setOnClickListener(view -> {
            Intent intent = new Intent(this, SeeUserProfile.class);
            intent.putExtra("name", UserName);
            intent.putExtra("Images", UserImage);
            intent.putExtra("Bio", UserBio);
            startActivity(intent);
        });


        binding.username.setText(UserName);
        Glide.with(this).load(UserImage).into(binding.userImage);
        binding.BackBts.setOnClickListener(view -> finish());

        ArrayList<Chat_Model> ChatModels = new ArrayList<>();
        chatAdapters = new ChatAdapters(ChatModels, this);
        binding.recyclerView2.setAdapter(chatAdapters);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.recyclerView2.setLayoutManager(layoutManager);

        sanderRoom = SandId + UserId;
        recRoom = UserId + SandId;

        firebaseDatabase.getReference().child("chats").child(sanderRoom).addValueEventListener(new ValueEventListener() {

            @SuppressLint("NotifyDataSetChanged")
            @Override

            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ChatModels.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {

                    Chat_Model _ChatModel = snapshot1.getValue(Chat_Model.class);
//                    if (!_ChatModel.isRead()) {
////                        firebaseDatabase.getReference().child("chats").child(sanderRoom).child(snapshot1.getKey()).child("isRead").setValue(true);
//                        firebaseDatabase.getReference().child("chats").child(recRoom).child(snapshot1.getKey()).child("isRead").setValue(true);
//                    }
                    ChatModels.add(_ChatModel);
                }
                chatAdapters.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

//        binding.imageBts.setOnClickListener(view ->
//


        binding.imageBts.setOnClickListener(view -> {
            SandingoptionsBinding sandingoptionsBinding = SandingoptionsBinding.inflate(getLayoutInflater());
            LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            View view1 = layoutInflater.inflate(R.layout.sandingoptions, null);
            PopupWindow popupWindow = new PopupWindow(view1, ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT, true);
//            popupWindow.showAtLocation(binding.getRoot(),Gravity.BOTTOM,0,0);
            sandingoptionsBinding.getRoot().setGravity(Gravity.BOTTOM);
            sandingoptionsBinding.getRoot().setY(0);
            sandingoptionsBinding.getRoot().setX(0);
            popupWindow.setContentView(sandingoptionsBinding.getRoot());
            popupWindow.showAtLocation(binding.getRoot(), Gravity.BOTTOM, 0, 0);

            sandingoptionsBinding.camera.setOnClickListener(view2 -> {
                ImagePicker.with(this)
                        .crop()
                        .compress(1024)
                        .maxResultSize(800, 800)
                        .start(CameraCode);
//                Intent intent = new Intent();
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                intent.setType("image/*");
//                startActivityForResult(intent, CameraCode);
//            browseDocuments();
            });
            sandingoptionsBinding.document.setOnClickListener(view2 -> {
                browseDocuments();
            });
            sandingoptionsBinding.contact.setOnClickListener(view3 -> {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT, ContactsContract.Contacts.CONTENT_URI);
//                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
//                intent.setType(ContactsContract.RawContacts.CONTENT_TYPE);

                intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
                startActivityForResult(intent, ContactCode);
            });
        });

        binding.SandBts.setOnClickListener(view -> {
            if (!binding.InputText.getText().toString().isEmpty()) {
                final Chat_Model Chat = new Chat_Model(SandId, binding.InputText.getText().toString(), System.currentTimeMillis(), false);
                binding.InputText.setText("");
                firebaseDatabase.getReference().child("chats").child(sanderRoom).push().setValue(Chat).addOnSuccessListener(unused -> firebaseDatabase.getReference().child("chats").child(recRoom).push().setValue(Chat).addOnSuccessListener(unused1 -> chatAdapters.notifyDataSetChanged()));
            } else {
                Toast.makeText(this, "Enter your text", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private String filletExtension(Uri Uri) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(Uri));
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
            StorageReference file = reference.child(Time + "." + filletExtension(dataUri));
            Toast.makeText(this, "Image Sand ", Toast.LENGTH_SHORT).show();
            file.putFile(dataUri).addOnSuccessListener(taskSnapshot -> file.getDownloadUrl().addOnSuccessListener(uri -> {
                final Chat_Model Chat = new Chat_Model(SandId, uri.toString(), true, false, false, Time);
                firebaseDatabase.getReference().child("chats").child(sanderRoom).push().setValue(Chat).addOnSuccessListener(unused -> firebaseDatabase.getReference().child("chats").child(recRoom).push().setValue(Chat).addOnSuccessListener(unused1 -> chatAdapters.notifyDataSetChanged()));
            })).addOnFailureListener(e -> Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show());

        } else if (data.getData() != null && requestCode == documentCode) {
            if (resultCode == RESULT_OK) {
                // Get the Uri of the selected file
                Uri uri = data.getData();

                /*
                 * Get the file's content URI from the incoming Intent,
                 * then query the server app to get the file's display name
                 * and size.
                 */
                Uri returnUri = data.getData();
                Cursor returnCursor =
                        getContentResolver().query(returnUri, null, null, null, null);
                /*
                 * Get the column indexes of the data in the Cursor,
                 * move to the first row in the Cursor, get the data,
                 * and display it.
                 */
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
                    firebaseDatabase.getReference().child("chats").child(sanderRoom).push().setValue(Chat).addOnSuccessListener(unused -> firebaseDatabase.getReference().child("chats").child(recRoom).push().setValue(Chat).addOnSuccessListener(unused1 -> chatAdapters.notifyDataSetChanged()));

                }).addOnFailureListener(e -> {
                }));
            }
        } else if (data.getData() != null && requestCode == ContactCode) {

            Cursor cursor = null;
            try {
                String phoneNo = null;
                String name = null;

                Uri uri = data.getData();
                cursor = getContentResolver().query(uri, null, null, null, null);
                cursor.moveToFirst();
                int  phoneIndex =cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                int  nameIndex =cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                phoneNo = cursor.getString(phoneIndex);
                name = cursor.getString(nameIndex);

                final Chat_Model Chat = new Chat_Model(SandId, name, System.currentTimeMillis(), false, false, false, false, true,phoneNo);
                firebaseDatabase.getReference().child("chats").child(sanderRoom).push().setValue(Chat).addOnSuccessListener(unused -> firebaseDatabase.getReference().child("chats").child(recRoom).push().setValue(Chat).
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
    @SuppressLint("Range")
    public String GetPhoneNumber(String id,Uri uri)
    {
        String number = "";
        Cursor phones = getContentResolver().query(uri, null, ContactsContract.CommonDataKinds.Phone._ID + " = " + id, null, null);

        if(phones.getCount() > 0)
        {

            number = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

            System.out.println(number);
        }

        phones.close();

        return number;
    }

}
