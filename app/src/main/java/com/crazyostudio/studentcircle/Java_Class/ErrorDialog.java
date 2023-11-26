package com.crazyostudio.studentcircle.Java_Class;

import android.app.AlertDialog;
import android.content.Context;

public class ErrorDialog {
    // Static method to show the error dialog
    public static void showErrorDialog(Context context, String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        // Customize your dialog appearance here
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setIcon(android.R.drawable.ic_dialog_alert);

        // Set a positive button and its click listener
        builder.setPositiveButton("OK", (dialog, which) -> {
            // Do something when OK button is clicked
            dialog.dismiss();
        });

        // Create and show the dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}