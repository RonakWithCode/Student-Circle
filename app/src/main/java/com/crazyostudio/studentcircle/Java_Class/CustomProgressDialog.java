package com.crazyostudio.studentcircle.Java_Class;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.crazyostudio.studentcircle.R;

public class CustomProgressDialog {
    static TextView textView;
    static AlertDialog alertDialog;
//    s View dialogView;
    public static void showCustomProgressDialogLoading(Context context,int orientation,String text) {
        // Create a layout inflater to inflate the custom layout
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.custom_progress_dialog, null);
        // Access the ProgressBar and TextView in the custom layout
//        ProgressBar progressBar = dialogView.findViewById(R.id.progressBar);
        textView = dialogView.findViewById(R.id.Loading);
        LinearLayout linear = dialogView.findViewById(R.id.linear);
        textView.setText(text);
        linear.setOrientation(orientation);
        // Set layout parameters to make the width wrap_content
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        dialogView.setLayoutParams(layoutParams);
        // Set up the AlertDialog builder
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setView(dialogView);
        builder.setCancelable(false); // Prevent dismissing by clicking outside the dialog

        // Create the AlertDialog
         alertDialog = builder.create();

        // Show the dialog
        alertDialog.show();

    }
    public static void dismissAlertDialog(){
        alertDialog.dismiss();
    }

    public static void changeText(String text){
        textView.setText(text);
    }

    public static boolean isShowing(){
        return alertDialog.isShowing();
    }

}
