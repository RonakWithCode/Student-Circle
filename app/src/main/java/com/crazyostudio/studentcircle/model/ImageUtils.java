package com.crazyostudio.studentcircle.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.WindowManager;

public class ImageUtils {
    public static Bitmap createBlueBackgroundImage(Context context) {
        // Get the screen dimensions
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        int screenWidth = displayMetrics.widthPixels;
        int screenHeight = displayMetrics.heightPixels;

        // Create a blank bitmap with screen dimensions
        Bitmap blueBackgroundImage = Bitmap.createBitmap(screenWidth, screenHeight, Bitmap.Config.ARGB_8888);

        // Create a canvas object to draw on the bitmap
        Canvas canvas = new Canvas(blueBackgroundImage);

        // Set the background color to blue
        canvas.drawColor(Color.BLUE);

        return blueBackgroundImage;
    }
}
