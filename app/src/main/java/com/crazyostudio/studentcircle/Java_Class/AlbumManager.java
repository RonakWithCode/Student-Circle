package com.crazyostudio.studentcircle.Java_Class;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;

public class AlbumManager {

    private static final String IMAGE_ALBUM_NAME = "StudentCircle";

    public static void createImageAlbum(Context context) {
        File albumDirectory = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), IMAGE_ALBUM_NAME);

        if (!albumDirectory.exists()) {
            if (albumDirectory.mkdirs()) {
                Log.d("AlbumManager", "Image Album created at: " + albumDirectory.getAbsolutePath());
            } else {
                Log.e("AlbumManager", "Failed to create Image Album directory");
            }
        }
    }

    public static void moveToImageAlbum(Context context, Uri uri) {
        String filePath = getRealPathFromURI(context, uri);
        if (isImageFile(filePath)) {
            File sourceFile = new File(filePath);
            File destinationDirectory = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), IMAGE_ALBUM_NAME);

            if (!destinationDirectory.exists()) {
                if (destinationDirectory.mkdirs()) {
                    Log.d("AlbumManager", "Image Album created at: " + destinationDirectory.getAbsolutePath());
                } else {
                    Log.e("AlbumManager", "Failed to create Image Album directory");
                }
            }

            File destinationFile = new File(destinationDirectory, sourceFile.getName());

            if (sourceFile.renameTo(destinationFile)) {
                Log.d("AlbumManager", "File moved to Image Album: " + destinationFile.getAbsolutePath());
            } else {
                Log.e("AlbumManager", "Failed to move file to Image Album");
            }
        }
    }

    private static String getRealPathFromURI(Context context, Uri uri) {
        Cursor cursor = null;
        try {
            String[] projection = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(uri, projection, null, null, null);
            if (cursor != null) {
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return uri.getPath();
    }

    private static boolean isImageFile(String filePath) {
        String extension = getFileExtension(filePath);
        return extension != null && (extension.equals("jpg") || extension.equals("jpeg") || extension.equals("png"));
    }

    private static String getFileExtension(String filePath) {
        int dotIndex = filePath.lastIndexOf('.');
        return dotIndex == -1 ? null : filePath.substring(dotIndex + 1).toLowerCase();
    }
}
