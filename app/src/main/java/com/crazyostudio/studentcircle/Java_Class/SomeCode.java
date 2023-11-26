package com.crazyostudio.studentcircle.Java_Class;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.webkit.MimeTypeMap;

public class SomeCode {

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
