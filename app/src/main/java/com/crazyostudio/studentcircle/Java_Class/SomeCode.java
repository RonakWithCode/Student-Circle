package com.crazyostudio.studentcircle.Java_Class;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.webkit.MimeTypeMap;

public class SomeCode {

    public static void AlertDialog(Context context, String msg){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(msg);
        builder.setTitle("Alert !");
        builder.setCancelable(false);
        builder.setPositiveButton("ok", (DialogInterface.OnClickListener) (dialog, which) -> {
            dialog.dismiss();
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    public static String getFileExtensionFromUri(Uri uri, Context context) {
        ContentResolver contentResolver = context.getContentResolver();
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
}
