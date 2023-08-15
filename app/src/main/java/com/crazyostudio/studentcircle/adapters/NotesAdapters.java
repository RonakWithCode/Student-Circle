package com.crazyostudio.studentcircle.adapters;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.crazyostudio.studentcircle.R;
import com.crazyostudio.studentcircle.databinding.AllnoteslayoutBinding;
import com.crazyostudio.studentcircle.fragmentLoad;
import com.crazyostudio.studentcircle.model.SubjectModel;
import com.google.firebase.auth.FirebaseAuth;

import java.net.URLEncoder;
import java.util.ArrayList;

public class NotesAdapters extends RecyclerView.Adapter<NotesAdapters.NotesAdaptersViewHolder> {

    ArrayList<SubjectModel> models;
    Context context;

    public NotesAdapters(ArrayList<SubjectModel> models, Context context) {
        this.models = models;
        this.context = context;
    }

    @NonNull
    @Override
    public NotesAdapters.NotesAdaptersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NotesAdapters.NotesAdaptersViewHolder(LayoutInflater.from(context).inflate(R.layout.allnoteslayout,parent,false));

    }

    @Override
    public void onBindViewHolder(@NonNull NotesAdapters.NotesAdaptersViewHolder holder, int position) {
        SubjectModel subjectModel = models.get(position);
        Glide.with(context).load(subjectModel.getUri()).into(holder.binding.NotesIcon);
        holder.binding.notesName.setText(subjectModel.getSubName());
        holder.binding.getRoot().setOnClickListener(v -> {
            Intent intent = new Intent(context, fragmentLoad.class);
            intent.putExtra("LoadID","shareList");
            intent.putExtra("subjectModel", subjectModel);
//            intent.putExtra("Path",subjectModel.getPath());
//            intent.putExtra("Notes",subjectModel.getNotes());
            context.startActivity(intent);
        });
//        https://friend-circle-f948a-default-rtdb.firebaseio.com/Share/-NaQ3XTr8k6oqEkjeHa8/tmol7c4WLnah1v2Hc6zBEKee6kr2
        holder.binding.copy.setOnClickListener(view->{

//            String uriID = "https://.crazy-studio-website.firebaseapp.com/"+"Share/"+ FirebaseAuth.getInstance().getUid()+subjectModel.getTime()+subjectModel.getSubName();

//            String baseUrl = "https://crazy-studio-website.firebaseapp.com/";
//            String uid = FirebaseAuth.getInstance().getUid();
//            long time = subjectModel.getTime();
//            String subName = subjectModel.getSubName();
//            String path = "Share/" + uid +"/"+ time + subName;
//            String shareableLink = "https://crazy-studio-website.firebaseapp.com/userinfo?Uri=" + Uri.encode(path);
            // Copy the link to the clipboard
//            com.google.firebase.database.FirebaseDatabase@d19a69b

// Generate a random link with data (name and age in this example)
//            String baseUrl = "https://example.com/";
//            String name = "John Doe";
//            int age = 30;
//            String randomLink = baseUrl + "user?name=" + Uri.encode(name) + "&age=" + age;
            ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("Link", subjectModel.getPath());
            clipboard.setPrimaryClip(clip);
            Toast.makeText(context, "Link copied", Toast.LENGTH_SHORT).show();

        });

    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public static class NotesAdaptersViewHolder extends RecyclerView.ViewHolder {
        AllnoteslayoutBinding binding;
        public NotesAdaptersViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = AllnoteslayoutBinding.bind(itemView);
        }
    }
}
