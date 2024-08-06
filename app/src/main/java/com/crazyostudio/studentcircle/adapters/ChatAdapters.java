package com.crazyostudio.studentcircle.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.crazyostudio.studentcircle.R;
import com.crazyostudio.studentcircle.databinding.ReceiverBinding;
import com.crazyostudio.studentcircle.databinding.ReceiverImageBinding;
import com.crazyostudio.studentcircle.databinding.ReceivercontactBinding;
import com.crazyostudio.studentcircle.databinding.ReceiverpdfBinding;
import com.crazyostudio.studentcircle.databinding.SanderImageBinding;
import com.crazyostudio.studentcircle.databinding.SanderPdfBinding;
import com.crazyostudio.studentcircle.databinding.SenderBinding;
import com.crazyostudio.studentcircle.databinding.SnadercontactlayoutBinding;
import com.crazyostudio.studentcircle.databinding.StoryreplyrcriverBinding;
import com.crazyostudio.studentcircle.databinding.StoryreplysenderBinding;
import com.crazyostudio.studentcircle.model.ChatAdaptersInterface;
import com.crazyostudio.studentcircle.model.Chat_Model;
import com.crazyostudio.studentcircle.model.UserInfo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
public class ChatAdapters extends  RecyclerView.Adapter{
    ChatAdaptersInterface adaptersInterface;

    ArrayList<Chat_Model> ChatModels;
    Activity activity_;
    Context context;
    int SANDER_VIEW_TYPE=1
            ,IMAGE_SANDER_VIEW_TYPE=2
            ,SANDER_PDF_VIEW_TYPE=3
            ,SANDER_CONTACT_VIEW_TYPE=4,SANDER_STORY_REPLY;
    int
            RECEIVER_VIEW_TYPE=103
            ,IMAGE_RECEIVER_VIEW_TYPE=104
            ,RECEIVER_PDF_VIEW_TYPE=105
            ,RECEIVER_CONTACT_VIEW_TYPE=106,RECEIVER_STORY_REPLY;

    public ChatAdapters(ArrayList<Chat_Model> chatModels, Context context,ChatAdaptersInterface ChatAdaptersInterface,Activity activity) {
        ChatModels = chatModels;
        this.context = context;
        this.adaptersInterface = ChatAdaptersInterface;
        this.activity_ = activity;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType==SANDER_VIEW_TYPE){
            View view = LayoutInflater.from(context).inflate(R.layout.sender,parent,false);
            return new SanderViewHolder(view);
        }
        else if (viewType==SANDER_PDF_VIEW_TYPE){
            View view = LayoutInflater.from(context).inflate(R.layout.sander_pdf,parent,false);
            return new SanderPDFViewHolder(view);
        }
        else if (viewType==SANDER_CONTACT_VIEW_TYPE){
            View view = LayoutInflater.from(context).inflate(R.layout.snadercontactlayout,parent,false);
            return new SANDERCONTACTViewHolder(view);
        }
        else if (viewType==SANDER_STORY_REPLY){
            View view = LayoutInflater.from(context).inflate(R.layout.storyreplysender,parent,false);
            return new SANDERSTORYREPLYViewHolder(view);
        }

        else if (viewType==RECEIVER_PDF_VIEW_TYPE){
            View view = LayoutInflater.from(context).inflate(R.layout.receiverpdf,parent,false);
            return new ReceiverPDFViewHolder(view);
        }
        else if (viewType==RECEIVER_CONTACT_VIEW_TYPE){
            View view = LayoutInflater.from(context).inflate(R.layout.receivercontact,parent,false);
            return new ReceiverCONTACTViewHolder(view);
        }
        else if (viewType==IMAGE_SANDER_VIEW_TYPE)
        {
            View view = LayoutInflater.from(context).inflate(R.layout.sander_image,parent,false);
            return new ImageSanderViewHolder(view);
        }
        else if (viewType==IMAGE_RECEIVER_VIEW_TYPE)
        {
            View view = LayoutInflater.from(context).inflate(R.layout.receiver_image,parent,false);
            return new ImageReceiverViewHolder(view);
        }

        else if (viewType==RECEIVER_VIEW_TYPE){
            View view = LayoutInflater.from(context).inflate(R.layout.receiver,parent,false);
            return new ReceiverViewHolder(view);
        }
        else if (viewType==RECEIVER_STORY_REPLY){
            View view = LayoutInflater.from(context).inflate(R.layout.storyreplyrcriver,parent,false);
            return new StoryReplyRcriverViewHolder(view);
        }

        else {
            return null;
        }
    }
    @Override
    public int getItemViewType(int position) {
//   SANDER
        if (ChatModels.get(position).getID().equals(FirebaseAuth.getInstance().getUid())) {
            if (ChatModels.get(position).isImage()) {
                return IMAGE_SANDER_VIEW_TYPE;
            } else if (ChatModels.get(position).isPDF()) {
                return SANDER_PDF_VIEW_TYPE;
            }
            else if (ChatModels.get(position).isContact()) {
                return SANDER_CONTACT_VIEW_TYPE;
            }
            else if (ChatModels.get(position).isStoryReply()) {
                return SANDER_STORY_REPLY;
            }
            else {
                return SANDER_VIEW_TYPE;
            }
        } else {
            if (ChatModels.get(position).isImage()) {
                return IMAGE_RECEIVER_VIEW_TYPE;
            }
            else if (ChatModels.get(position).isPDF()) {
                return RECEIVER_PDF_VIEW_TYPE;
            }
            else if (ChatModels.get(position).isContact()) {
                return RECEIVER_CONTACT_VIEW_TYPE;
            }
            else if (ChatModels.get(position).isStoryReply()) {
                return RECEIVER_STORY_REPLY;
            }
            else {
                return RECEIVER_VIEW_TYPE;
            }
        }

//        return
    }
    @SuppressLint({"SetTextI18n", "NonConstantResourceId"})
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Chat_Model chatModel = ChatModels.get(position);
        if (holder.getClass()==SanderViewHolder.class){
            ((SanderViewHolder)holder).SendBinding.messageText.setText(chatModel.getMessage());
            @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm");
            Date date = new Date(chatModel.getSandTime());
            String time = simpleDateFormat.format(date);

            ((SanderViewHolder)holder).SendBinding.messageTime.setText(time);
            ((SanderViewHolder)holder).SendBinding.getRoot().setOnLongClickListener(v -> {

                PopupMenu popup = new PopupMenu(context,  ((SanderViewHolder)holder).SendBinding.messageText);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.textmenu, popup.getMenu());
                popup.setOnMenuItemClickListener(item -> {
                    if (item.getItemId()== R.id.copy) {
                        Toast.makeText(context, "Coppy", Toast.LENGTH_SHORT).show();
                        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData clip;
                        clip = ClipData.newPlainText("text", chatModel.getMessage());
                        clipboard.setPrimaryClip(clip);
                        popup.dismiss();
                    }
                    else if (item.getItemId()==R.id.delete){
                            Toast.makeText(context, "delete", Toast.LENGTH_SHORT).show();
                            adaptersInterface.Delete(chatModel);
                            popup.dismiss();
                    }
                    return true;
                });
                popup.show();
                return false;
            });

        }
        else if (holder.getClass() == SanderPDFViewHolder.class) {
            if (chatModel.getFilename().endsWith(".pdf")){
                ((SanderPDFViewHolder)holder).pdfBinding.pdfIcon.setImageResource(R.drawable.pdf);
            }
            else if (chatModel.getFilename().endsWith(".apk")){
                ((SanderPDFViewHolder)holder).pdfBinding.pdfIcon.setImageResource(R.drawable.apk);
            }else {
                ((SanderPDFViewHolder)holder).pdfBinding.pdfIcon.setImageResource(R.drawable.document);
            }
//            ((SanderPDFViewHolder)holder).pdfBinding.filename.setText(chatModel.getMessage());
            @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm");
            Date date = new Date(chatModel.getSandTime());
            String time = simpleDateFormat.format(date);
            ((SanderPDFViewHolder)holder).pdfBinding.time.setText(time);

            ((SanderPDFViewHolder)holder).pdfBinding.filename.setText(chatModel.getFilename());
            ((SanderPDFViewHolder)holder).pdfBinding.size.setText(chatModel.getFileSize());
//            ((SanderPDFViewHolder)holder).pdfBinding.pages.setText(chatModel.getFilePage());
            ((SanderPDFViewHolder)holder).pdfBinding.Download.setOnClickListener(view->
            {
                PopupMenu popup = new PopupMenu(context,  ((SanderPDFViewHolder)holder).pdfBinding.Download);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.imagemenu, popup.getMenu());
                popup.setOnMenuItemClickListener(item -> {
                    switch (item.getItemId()) {
                        case R.id.openFull:
                            adaptersInterface.ImageView(chatModel);
                            popup.dismiss();
                            return false;
                        case R.id.Download:
                            adaptersInterface.Download(Uri.parse(chatModel.getMessage()));
                            popup.dismiss();
                            return false;
                        case R.id.delete_btu:
                            adaptersInterface.DeleteImage(chatModel);
                            popup.dismiss();
                            return false;
                        default:
                            return false;
                    }
                });
                popup.show();
            });
        }
        else if (holder.getClass()==SANDERCONTACTViewHolder.class){
            @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm");
            Date date = new Date(chatModel.getSandTime());
            String time = simpleDateFormat.format(date);
            ((SANDERCONTACTViewHolder)holder).CBinding.time.setText(time);
            ((SANDERCONTACTViewHolder)holder).CBinding.filename.setText(chatModel.getMessage());
            ((SANDERCONTACTViewHolder)holder).CBinding.number.setText(chatModel.getFilename());
            ((SANDERCONTACTViewHolder)holder).CBinding.getRoot().setOnLongClickListener(v -> {
                PopupMenu popup = new PopupMenu(context,  ((SANDERCONTACTViewHolder)holder).CBinding.main);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.contactmenu, popup.getMenu());
                popup.setOnMenuItemClickListener(item -> {
                    ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip;
                    switch (item.getItemId()) {
                        case R.id.NameCopy:
                            clip = ClipData.newPlainText("Name", chatModel.getMessage());
                            clipboard.setPrimaryClip(clip);
                            return true;
                        case R.id.NumberCopy:
                            clip = ClipData.newPlainText("number", chatModel.getFilename());
                            clipboard.setPrimaryClip(clip);
                            return false;
                        case R.id.findName:
//                            findingUser(chatModel.getMessage(), holder,position,"name");
                            return false;
                        case R.id.findNumber:
//                            findingUser(chatModel.getFilename(), holder,position,"number");
                            return false;




////            case R.id.delete:
////                delete(item);
//                return true;
                        default:
                            return false;
                    }
                });
                popup.show();
                return false;
            });
        }
        else if (holder.getClass()==ImageSanderViewHolder.class) {
            @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm");
            Date date = new Date(chatModel.getSandTime());
            String time = simpleDateFormat.format(date);
            ((ImageSanderViewHolder)holder).SendBinding.time.setText(time);

            Glide.with(context).load(chatModel.getMessage()).into(((ImageSanderViewHolder)holder).SendBinding.SanderImageview);
            ((ImageSanderViewHolder) holder).SendBinding.SanderImageview.setMaximumScale(5.0f); // Set the maximum zoom level
            ((ImageSanderViewHolder) holder).SendBinding.SanderImageview.setMediumScale(3.0f); // Set the medium zoom level
            ((ImageSanderViewHolder) holder).SendBinding.SanderImageview.setMinimumScale(1.0f);
//            ((ImageSanderViewHolder) holder).SendBinding.SanderImageview.setOnClickListener(view -> );

            ((ImageSanderViewHolder) holder).SendBinding.SanderImageview.setOnClickListener(Long->{
                PopupMenu popup = new PopupMenu(context,  ((ImageSanderViewHolder)holder).SendBinding.SanderImageview);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.imagemenu, popup.getMenu());
                popup.setOnMenuItemClickListener(item -> {
                    switch (item.getItemId()) {
                        case R.id.openFull:
                            adaptersInterface.ImageView(chatModel);
                            popup.dismiss();

                            return false;
                        case R.id.Download:
                            adaptersInterface.Download(Uri.parse(chatModel.getMessage()));
                            popup.dismiss();
                            return false;
                        case R.id.delete_btu:
                            adaptersInterface.DeleteImage(chatModel);
                            popup.dismiss();
                            return false;
                        default:
                            return false;
                    }
                });
                popup.show();
            });


        }
        else if (holder.getClass()==SANDERSTORYREPLYViewHolder.class) {
            ((SANDERSTORYREPLYViewHolder)holder).binding.messageText.setText(chatModel.getMessage());
            @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm");
            Date date = new Date(chatModel.getSandTime());
            String time = simpleDateFormat.format(date);

            ((SANDERSTORYREPLYViewHolder)holder).binding.messageTime.setText(time);
            Glide.with(context).load(chatModel.getSanderImage()).into(((SANDERSTORYREPLYViewHolder)holder).binding.imageCavar);

        }
//       Receiver
        else if (holder.getClass()==ReceiverCONTACTViewHolder.class) {
            @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm");
            Date date = new Date(chatModel.getSandTime());
            String time = simpleDateFormat.format(date);
            ((ReceiverCONTACTViewHolder)holder).ContactBinding.time.setText(time);
            ((ReceiverCONTACTViewHolder)holder).ContactBinding.filename.setText(chatModel.getMessage());
            ((ReceiverCONTACTViewHolder)holder).ContactBinding.number.setText(chatModel.getFilename());
            ((ReceiverCONTACTViewHolder)holder).ContactBinding.main.setOnClickListener(view->{
                PopupMenu popup = new PopupMenu(context,  ((ReceiverCONTACTViewHolder)holder).ContactBinding.main);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.contactmenu, popup.getMenu());
                popup.setOnMenuItemClickListener(item -> {
                    ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip;
                    switch (item.getItemId()) {
                        case R.id.NameCopy:
                            clip = ClipData.newPlainText("Name", chatModel.getMessage());
                            clipboard.setPrimaryClip(clip);
                            return true;
                        case R.id.NumberCopy:
                            clip = ClipData.newPlainText("number", chatModel.getFilename());
                            clipboard.setPrimaryClip(clip);
                            return false;
                        case R.id.findName:
//                            findingUser(chatModel.getMessage(), holder,position,"name");
                            return false;
                        case R.id.findNumber:
//                            findingUser(chatModel.getFilename(), holder,position,"number");
                            return false;
////            case R.id.delete:
////                delete(item);
//                return true;
                        default:
                            return false;
                    }
                });
                popup.show();

            });
            ((ReceiverCONTACTViewHolder)holder).ContactBinding.add.setOnClickListener(view->{
                Intent intent = new Intent(Intent.ACTION_INSERT);
                intent.setType(ContactsContract.Contacts.CONTENT_TYPE);

                intent.putExtra(ContactsContract.Intents.Insert.NAME, chatModel.getMessage());
                intent.putExtra(ContactsContract.Intents.Insert.PHONE, chatModel.getFilename());

                context.startActivity(intent);
            });
            ((ReceiverCONTACTViewHolder)holder).ContactBinding.call.setOnClickListener(view->{
                Intent phone_intent = new Intent(Intent.ACTION_CALL);
                // Set data of Intent through Uri by parsing phone number

                phone_intent.setData(Uri.parse("tel:" + chatModel.getFilename()));

                // start Intent
                context.startActivity(phone_intent);
            });
        }
        else if (holder.getClass()==ImageReceiverViewHolder.class) {
            @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm");
            Date date = new Date(chatModel.getSandTime());
            String time = simpleDateFormat.format(date);
            ((ImageReceiverViewHolder)holder).Binding.time.setText(time);
            Glide.with(context).load(chatModel.getMessage()).into(((ImageReceiverViewHolder)holder).Binding.Imageview);
            ((ImageReceiverViewHolder) holder).Binding.Imageview.setMaximumScale(5.0f); // Set the maximum zoom level
            ((ImageReceiverViewHolder) holder).Binding.Imageview.setMediumScale(3.0f); // Set the medium zoom level
            ((ImageReceiverViewHolder) holder).Binding.Imageview.setMinimumScale(1.0f);
//                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(chatModel.getMessage())));
            ((ImageReceiverViewHolder) holder).Binding.Imageview.setOnClickListener(on -> adaptersInterface.ImageView(chatModel));
            ((ImageReceiverViewHolder) holder).Binding.getRoot().setOnLongClickListener(v -> {
                PopupMenu popup = new PopupMenu(context,  ((ImageReceiverViewHolder)holder).Binding.getRoot());
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.imagemenu, popup.getMenu());
                popup.setOnMenuItemClickListener(item -> {
                    switch (item.getItemId()) {
                        case R.id.openFull:
                            adaptersInterface.ImageView(chatModel);
                            popup.dismiss();

                            return false;
                        case R.id.Download:
                            adaptersInterface.Download(Uri.parse(chatModel.getMessage()));
                            popup.dismiss();
                            return false;
                        case R.id.delete_btu:
                            adaptersInterface.ReceiveDeleteImage(chatModel);
                            return false;
                        default:
                            return false;
                    }
                });
                popup.show();
                return false;
            });

        }
        else if (holder.getClass() == ReceiverPDFViewHolder.class) {
            if (chatModel.getFilename().endsWith(".pdf")){
                ((ReceiverPDFViewHolder)holder).pdfBinding.pdfIcon.setImageResource(R.drawable.pdf);
            }
            else if (chatModel.getFilename().endsWith(".apk")){
                ((ReceiverPDFViewHolder)holder).pdfBinding.pdfIcon.setImageResource(R.drawable.apk);
            }else {
                ((ReceiverPDFViewHolder)holder).pdfBinding.pdfIcon.setImageResource(R.drawable.document);
            }
//            ((SanderPDFViewHolder)holder).pdfBinding.filename.setText(chatModel.getMessage());
            @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm");
            Date date = new Date(chatModel.getSandTime());
            String time = simpleDateFormat.format(date);
            ((ReceiverPDFViewHolder)holder).pdfBinding.filename.setText(chatModel.getFilename());
            ((ReceiverPDFViewHolder)holder).pdfBinding.size.setText(chatModel.getFileSize());
//            ((ReceiverPDFViewHolder)holder).pdfBinding.pages.setText(chatModel.getFilePage());
            ((ReceiverPDFViewHolder)holder).pdfBinding.time.setText(time);
            ((ReceiverPDFViewHolder)holder).pdfBinding.Download.setOnClickListener(view->
            {
                PopupMenu popup = new PopupMenu(context,  ((ReceiverPDFViewHolder)holder).pdfBinding.Download);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.imagemenu, popup.getMenu());
                popup.setOnMenuItemClickListener(item -> {
                    switch (item.getItemId()) {
                        case R.id.openFull:
                            adaptersInterface.ImageView(chatModel);
                            popup.dismiss();

                            return false;
                        case R.id.Download:
                            adaptersInterface.Download(Uri.parse(chatModel.getMessage()));

                            popup.dismiss();
                            return false;
                        case R.id.delete_btu:
                            adaptersInterface.ReceiveDeleteImage(chatModel);
                            return false;
                        default:
                            return false;
                    }
                });
                popup.show();
            });
        }
        else if (holder.getClass()==ReceiverViewHolder.class){
            ((ReceiverViewHolder)holder).binding.messageText.setText(chatModel.getMessage());
            @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm");
            Date date = new Date(chatModel.getSandTime());
            String time = simpleDateFormat.format(date);

            ((ReceiverViewHolder)holder).binding.messageTime.setText(time);
            ((ReceiverViewHolder)holder).binding.getRoot().setOnLongClickListener(v -> {

                PopupMenu popup = new PopupMenu(context,  ((ReceiverViewHolder)holder).binding.messageText);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.textmenu, popup.getMenu());
                popup.setOnMenuItemClickListener(item -> {
                    if (item.getItemId()== R.id.copy) {
                        Toast.makeText(context, "Coppy", Toast.LENGTH_SHORT).show();
                        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData clip;
                        clip = ClipData.newPlainText("text", chatModel.getMessage());
                        clipboard.setPrimaryClip(clip);
                        popup.dismiss();
                    }
                    else if (item.getItemId()==R.id.delete){
                        Toast.makeText(context, "dd", Toast.LENGTH_SHORT).show();
                        adaptersInterface.ReceiveDelete(chatModel);
                        popup.dismiss();
                    }
                    return true;
                });
                popup.show();
                return false;
            });

        }
    }
    @Override
    public int getItemCount() {
        return ChatModels.size();
    }
    //    Find of Out side function
//    private void findingUser(String name,  RecyclerView.ViewHolder view, int position,String FindBy) {
//        Dialog findUser = new Dialog(context);
//        findUser.setContentView(R.layout.findinguserlayout);
//        findUser.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
//        findUser.setCanceledOnTouchOutside(true);
//        findUser.onBackPressed();
//        findUser.show();
//        RecyclerView recyclerViewUsers = findUser.findViewById(R.id.users);
//        TextView textView = findUser.findViewById(R.id.not_Found);
//        FirebaseDatabase users;
//        FirebaseAuth auth;
//        UserInfoAdapters userInfoAdapters;
//        auth = FirebaseAuth.getInstance();
//        users = FirebaseDatabase.getInstance();
//        ArrayList<UserInfo> userInfoS = new ArrayList<>();
//        userInfoAdapters = new UserInfoAdapters(userInfoS, context);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
//        recyclerViewUsers.setLayoutManager(layoutManager);
//        recyclerViewUsers.setAdapter(userInfoAdapters);
//        users.getReference().child("UserInfo").addValueEventListener(new ValueEventListener() {
//            @SuppressLint("NotifyDataSetChanged")
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                userInfoS.clear();
//                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
//                    UserInfo userInfo = snapshot1.getValue(UserInfo.class);
//                    if (!Objects.equals(snapshot1.getKey(), auth.getUid())) {
//                        assert userInfo != null;
//                        if (FindBy.trim().equals("name")){
//                            if (userInfo.getName().equals(name)){
//                                textView.setVisibility(View.INVISIBLE);
//                                recyclerViewUsers.setVisibility(View.VISIBLE);
//                                userInfoS.add(userInfo);
//                            }else {
//                                textView.setVisibility(View.VISIBLE);
//                                recyclerViewUsers.setVisibility(View.INVISIBLE);
//                                textView.setText("We are Not Found amy User of Nmae "+ name);
//
//                            }
//                        }
//                        else if (FindBy.trim().equals("number")){
//                            Toast.makeText(context, "Now"+userInfo.getFullName(), Toast.LENGTH_SHORT).show();
//                            if (userInfo.getNumber().equals(name)) {
//                                Toast.makeText(context, userInfo.getNumber(), Toast.LENGTH_SHORT).show();
//                                textView.setVisibility(View.INVISIBLE);
//                                recyclerViewUsers.setVisibility(View.VISIBLE);
//                                userInfoS.add(userInfo);
//                            }
//                            else {
//                                textView.setVisibility(View.VISIBLE);
//                                textView.setText("We are Not Found amy User of Number "+ name);
//                                recyclerViewUsers.setVisibility(View.INVISIBLE);
//                            }
//
//                        }
//                    }
//                    userInfoAdapters.notifyDataSetChanged();
//
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//
//    }
//    Ending

    //    Starting Holder Class hear
    public static class SanderViewHolder extends RecyclerView.ViewHolder {
        SenderBinding SendBinding;
        public SanderViewHolder(@NonNull View itemView) {
            super(itemView);
            SendBinding = SenderBinding.bind(itemView);
        }
    }
    public static class ImageSanderViewHolder extends RecyclerView.ViewHolder {
        SanderImageBinding SendBinding;
        public ImageSanderViewHolder(@NonNull View itemView) {
            super(itemView);
            SendBinding = SanderImageBinding.bind(itemView);
        }
    }
    public static class ReceiverViewHolder extends RecyclerView.ViewHolder {
        ReceiverBinding binding;
        public ReceiverViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ReceiverBinding.bind(itemView);
        }
    }
    public static class ImageReceiverViewHolder extends RecyclerView.ViewHolder {
        ReceiverImageBinding Binding;
        public ImageReceiverViewHolder(@NonNull View itemView) {
            super(itemView);
            Binding = ReceiverImageBinding.bind(itemView);
        }
    }
    public static class SanderPDFViewHolder extends RecyclerView.ViewHolder {
        SanderPdfBinding pdfBinding;

        public SanderPDFViewHolder(@NonNull View itemView) {
            super(itemView);
            pdfBinding = SanderPdfBinding.bind(itemView);
        }
    }
    public static class SANDERCONTACTViewHolder extends RecyclerView.ViewHolder {
        SnadercontactlayoutBinding CBinding;

        public SANDERCONTACTViewHolder(@NonNull View itemView) {
            super(itemView);
            CBinding = SnadercontactlayoutBinding.bind(itemView);
        }
    }
    public static class ReceiverPDFViewHolder extends RecyclerView.ViewHolder {
        ReceiverpdfBinding pdfBinding;

        public ReceiverPDFViewHolder(@NonNull View itemView) {
            super(itemView);
            pdfBinding = ReceiverpdfBinding.bind(itemView);
        }
    }
    public static class ReceiverCONTACTViewHolder extends RecyclerView.ViewHolder {
        ReceivercontactBinding ContactBinding;

        public ReceiverCONTACTViewHolder(@NonNull View itemView) {
            super(itemView);
            ContactBinding = ReceivercontactBinding.bind(itemView);
        }
    }

    private static class SANDERSTORYREPLYViewHolder extends RecyclerView.ViewHolder {
        StoryreplysenderBinding binding;
        public SANDERSTORYREPLYViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = StoryreplysenderBinding.bind(itemView);
        }
    }

    private static class StoryReplyRcriverViewHolder extends RecyclerView.ViewHolder {
        StoryreplyrcriverBinding binding;
        public StoryReplyRcriverViewHolder(View itemView) {
            super(itemView);
            binding = StoryreplyrcriverBinding.bind(itemView);
        }
    }
//    Ending
}