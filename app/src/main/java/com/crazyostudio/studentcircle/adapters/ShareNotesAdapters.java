package com.crazyostudio.studentcircle.adapters;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.crazyostudio.studentcircle.R;
import com.crazyostudio.studentcircle.databinding.ImagesharenoteslayoutBinding;
import com.crazyostudio.studentcircle.fullscreen;

import java.util.ArrayList;

public class ShareNotesAdapters extends RecyclerView.Adapter<ShareNotesAdapters.ShareNotesAdaptersViewHolder> {

    ArrayList<String> uri;
    Context context;

    public ShareNotesAdapters(ArrayList<String> uri, Context context) {
        this.uri = uri;
        this.context = context;
    }

    @NonNull
    @Override
    public ShareNotesAdapters.ShareNotesAdaptersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ShareNotesAdapters.ShareNotesAdaptersViewHolder(LayoutInflater.from(context).inflate(R.layout.imagesharenoteslayout,parent,false));
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onBindViewHolder(@NonNull ShareNotesAdapters.ShareNotesAdaptersViewHolder holder, int position) {
        String imagePos = uri.get(position);
        Glide.with(context).load(imagePos).into(holder.binding.image);
        holder.binding.image.setOnLongClickListener(v -> false);
        holder.binding.image.setOnClickListener(view->{
            Intent intent = new Intent(context, fullscreen.class);
//            BitmapDrawable bitmapDrawable = (BitmapDrawable) holder.binding.image.getDrawable();
//            Bitmap bitmap = bitmapDrawable.getBitmap();
            intent.putExtra("image", imagePos);
            context.startActivity(intent);
        });

        holder.binding.image.setOnLongClickListener(v -> {
            PopupMenu popup = new PopupMenu(context,  holder.binding.image);
            MenuInflater inflater = popup.getMenuInflater();
            inflater.inflate(R.menu.imagehold, popup.getMenu());
            popup.setOnMenuItemClickListener(item -> {
                ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip;
                switch (item.getItemId()) {
                    case R.id.open:
                        Intent intent = new Intent(context, fullscreen.class);
                        intent.putExtra("image", imagePos);
                        context.startActivity(intent);
                        return true;
                    case R.id.download:
                        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(imagePos));
                        request.allowScanningByMediaScanner();
                        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, System.currentTimeMillis()+"s");
                        DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
                        manager.enqueue(request);
                        Toast.makeText(context, "Check Notification Bar or Download Folder ", Toast.LENGTH_SHORT).show();
//                            clip = ClipData.newPlainText("number", chatModel.getFilename());
//                            clipboard.setPrimaryClip(clip);
                        return false;
                    case R.id.copy_Link:
                        clip = ClipData.newPlainText("Link",imagePos);
                        clipboard.setPrimaryClip(clip);

                        Toast.makeText(context, "Copy", Toast.LENGTH_SHORT).show();
                        return false;




////            case R.id.delete:
////                delete(item);
//                return true;
                    default:
                        return false;
                }
            });
            popup.show();

            return true;
        });

    }

    @Override
    public int getItemCount() {
        return uri.size();
    }

    public static class ShareNotesAdaptersViewHolder extends RecyclerView.ViewHolder {
        ImagesharenoteslayoutBinding binding;
        public ShareNotesAdaptersViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ImagesharenoteslayoutBinding.bind(itemView);
        }
    }
}
