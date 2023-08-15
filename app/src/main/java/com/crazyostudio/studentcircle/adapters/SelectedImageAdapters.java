package com.crazyostudio.studentcircle.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.view.MotionEventCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.crazyostudio.studentcircle.R;
import com.crazyostudio.studentcircle.databinding.ActivityImageEditorBinding;
import com.crazyostudio.studentcircle.databinding.ImagelayoutBinding;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class SelectedImageAdapters extends RecyclerView.Adapter<SelectedImageAdapters.SelectedImageAdaptersViewHolder>  {
    float[] lastEvent = null;
    Dialog EditorDialog;
    ActivityImageEditorBinding editorBinding;
    ArrayList<Uri> Uri;
    ArrayList<byte[]> EditImages = new ArrayList<>();
    Context context;
    Activity activity;

    public SelectedImageAdapters(ArrayList<Uri> uri, Context context,    Activity Activity) {
        this.Uri = uri;
        this.context = context;
        activity = Activity;
    }

    @NonNull
    @Override
    public SelectedImageAdapters.SelectedImageAdaptersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SelectedImageAdapters.SelectedImageAdaptersViewHolder(LayoutInflater.from(context).inflate(R.layout.imagelayout,parent,false));

    }

    @Override
    public void onBindViewHolder(@NonNull SelectedImageAdapters.SelectedImageAdaptersViewHolder holder, int position) {
        Uri image = Uri.get(position);
//            Bitmap compressedBitmap = BitmapFactory.decodeFile(String.valueOf(image));
        try {
            holder.itemView.removeOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                @Override
                public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                }
            });
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), image);
            holder.binding.holderimage.setImageBitmap(bitmap);
            holder.binding.holderimage.setOnClickListener(edit->{
            ShowDialog(holder,image,position);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }


    }



    @Override
    public int getItemCount() {
        return Uri.size();
    }


    @SuppressLint("ClickableViewAccessibility")
    void  ShowDialog(SelectedImageAdaptersViewHolder holder, Uri uri, int pos){

        editorBinding = ActivityImageEditorBinding.inflate(activity.getLayoutInflater());
        EditorDialog = new Dialog(context);
        EditorDialog.setContentView(editorBinding.getRoot());
        EditorDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        EditorDialog.getWindow().setBackgroundDrawableResource(R.drawable.createsubjectsboxbg);
        EditorDialog.setCancelable(false);
        EditorDialog.getWindow().getAttributes().windowAnimations = R.style.Animationboy;
        EditorDialog.show();
        editorBinding.addTextButton.setOnClickListener(v -> {
            editorBinding.editTextOverlay.setVisibility(View.VISIBLE);
            editorBinding.editTextOverlay.setText("");
            editorBinding.editTextOverlay.requestFocus();
        });

        editorBinding.photoView.setImageURI(uri);

        editorBinding.editTextOverlay.setOnTouchListener((v, event) -> {
            int action = MotionEventCompat.getActionMasked(event);
            if (action == MotionEvent.ACTION_MOVE) {
                if (lastEvent != null && event.getPointerCount() == 1) {
                    float dx = event.getX() - lastEvent[0];
                    float dy = event.getY() - lastEvent[1];
                    Matrix textMatrix = editorBinding.editTextOverlay.getMatrix();
                    textMatrix.postTranslate(dx, dy);
                }
                lastEvent = new float[]{event.getX(), event.getY()};
            } else {
                lastEvent = null;
            }
            return true;
        });

        editorBinding.saveButton.setOnClickListener(v -> {
            Bitmap editedImage = createEditedImage();

            // Convert Bitmap to byte array
            ByteArrayOutputStream bass = new ByteArrayOutputStream();
            editedImage.compress(Bitmap.CompressFormat.JPEG, 100, bass);
            byte[] data = bass.toByteArray();

            EditImages.add(data);

            Uri.remove(pos);
//            subImageUri.add(data);
            EditorDialog.dismiss();
//             finishActivity(55);
//            Intent previousScreen = new Intent();
//            //Sending the data to Activity_A
//            previousScreen.putExtra("Image",data);
//            previousScreen.putExtra("hello","data");
//            setResult(1000, previousScreen);
//            finish();
            // Define the path where the image will be stored in Firebase Storage
//            String fileName = "edited_image.jpg";
//            StorageReference imageRef = storageReference.child(fileName);
//
//            // Upload the image to Firebase Storage
//            UploadTask uploadTask = imageRef.putBytes(data);
//            uploadTask.addOnCompleteListener(task -> {
//                if (task.isSuccessful()) {
//                    // Image upload is successful
//                    // You can now get the download URL of the image to use it later
//                    imageRef.getDownloadUrl().addOnCompleteListener(task1 -> {
//                        if (task1.isSuccessful()) {
//                            Uri downloadUri = task1.getResult();
//                            // Here you have the download URL of the uploaded image
//                            // You can use this URL to display the image or store it in your database.
//                        } else {
//                            // Failed to get the download URL
//                        }
//                    });
//                } else {
//                    // Image upload failed
//                }
//            });
//            // Save the 'editedImage' to Firebase Storage or process it further.
//            // You can use the Bitmap for any other operation, like uploading to Firebase Storage.
//
//            // Ensure that you have proper Firebase setup and appropriate rules for storage access.
        });


    }
    public Bitmap createEditedImage() {
        // Create a new Bitmap to hold the edited image
        Bitmap editedBitmap = Bitmap.createBitmap(
                editorBinding.photoView.getWidth(), editorBinding.photoView.getHeight(),
                Bitmap.Config.ARGB_8888);

        // Create a Canvas with the new Bitmap
        Canvas canvas = new Canvas(editedBitmap);

        // Draw the original photoView's image on the Canvas
        editorBinding.photoView.draw(canvas);

        // Calculate the text position on the photoView's image
        float x = editorBinding.editTextOverlay.getX();
        float y = editorBinding.editTextOverlay.getY();
        x = (x / editorBinding.photoView.getWidth()) * editedBitmap.getWidth();
        y = (y / editorBinding.photoView.getHeight()) * editedBitmap.getHeight();

        // Draw the text on the Canvas at the calculated position
        canvas.drawText(editorBinding.editTextOverlay.getText().toString(), x, y, editorBinding.editTextOverlay.getPaint());

        // Hide the EditText for the final image capture
        editorBinding.editTextOverlay.setVisibility(View.INVISIBLE);

        return editedBitmap;
    }





    public static class SelectedImageAdaptersViewHolder extends RecyclerView.ViewHolder {
        ImagelayoutBinding binding;
        public SelectedImageAdaptersViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ImagelayoutBinding.bind(itemView);
        }
    }
}
