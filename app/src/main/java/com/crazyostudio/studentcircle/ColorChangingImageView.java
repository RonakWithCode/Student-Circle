package com.crazyostudio.studentcircle;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.ImageView;


@SuppressLint("AppCompatCustomView")
public class ColorChangingImageView extends ImageView {

    public ColorChangingImageView(Context context) {
        super(context);
        init();
    }

    public ColorChangingImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        // Set initial background color
        setBackgroundColor(Color.BLUE);
    }

    public void changeBackgroundColor(int color) {
        // Change the background color
        setBackgroundColor(color);
    }
}
