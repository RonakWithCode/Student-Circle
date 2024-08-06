package com.crazyostudio.studentcircle.user;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.bumptech.glide.Glide;
import com.crazyostudio.studentcircle.R;
import com.crazyostudio.studentcircle.Service.AuthService;
import com.crazyostudio.studentcircle.databinding.FragmentProfileEditViewBinding;
import com.crazyostudio.studentcircle.model.UserInfo;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.storage.StorageReference;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;


public class ProfileEditViewFragment extends Fragment {
    FragmentProfileEditViewBinding binding;
    private final int IMAGE_REQUEST_CODE = 123;
    private  Uri userImage = Uri.parse("https://firebasestorage.googleapis.com/v0/b/friend-circle-f948a.appspot.com/o/Deffucts%2Fuserimage.png?alt=media&token=ed9dc13c-5810-42af-a66a-12cf861e9acc");
    private static final String ImageLink = "https://firebasestorage.googleapis.com/v0/b/friend-circle-f948a.appspot.com/o/Deffucts%2Fuserimage.png?alt=media&token=ed9dc13c-5810-42af-a66a-12cf861e9acc";
    private ProgressDialog dialog;
    AuthService authService;
    UserInfo userInfo;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileEditViewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileEditViewFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileEditViewFragment newInstance(String param1, String param2) {
        ProfileEditViewFragment fragment = new ProfileEditViewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentProfileEditViewBinding.inflate(inflater,container,false);
        dialog = new ProgressDialog(requireContext());
        userInfo = new UserInfo();
        authService = new AuthService();
        authService.getAccountInfo(new AuthService.getAccountInterface() {
            @Override
            public void onGet(UserInfo userInfo1) {
                userInfo = userInfo1;
            }

            @Override
            public void onFall(DatabaseError e) {
                Log.i("InUserInfo", "error: "+e.toString());

                binding.error.setText(e.getMessage());
                binding.error.setVisibility(View.VISIBLE);
            }
        });
        binding.addLinks.setOnClickListener(view->{
            NavHostFragment navHostFragment = (NavHostFragment) requireActivity().getSupportFragmentManager().findFragmentById(R.id.nav_profile_host_fragment);
            assert navHostFragment != null;
            NavController navController = navHostFragment.getNavController();
            navController.navigate(R.id.action_profileEditViewFragment_to_profileLinkViewFragment);
        });
        binding.userImage.setOnClickListener(view-> showBottomDialog());
//        binding.ShowContactEMail.setOnClickListener(view->{
//            setupEmailBox();
//        });
//        binding.ShowContactEMail.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                // Handle the check event here
//                setupEmailBox();
//
//            }
//        });
//        binding.ShowContactPhone.setOnClickListener(view->{
//            setupPhoneBox();
//        });

        return binding.getRoot();
    }



    private void showBottomDialog() {

        final Dialog dialogBox = new Dialog(requireContext());
        dialogBox.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogBox.setContentView(R.layout.profile_pic_selecter_bottom_sheet_layout);

        LinearLayout upload_profile_pic_Layout = dialogBox.findViewById(R.id.upload_profile_pic);
        LinearLayout delete_btu_Layout = dialogBox.findViewById(R.id.delete_btu);

        upload_profile_pic_Layout.setOnClickListener(v -> {
            Log.i("InUserInfo", "upload_profile_pic_Layout: "+userInfo);

            pickMedia.launch(new PickVisualMediaRequest.Builder()
                    .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                    .build());
            dialogBox.dismiss();

        });

        delete_btu_Layout.setOnClickListener(v -> {
            dialog.setTitle("removing you profile.");
            dialog.show();
            authService.Delete(ImageLink,userInfo, new AuthService.SetupAccountPic() {
                @Override
                public void onSet() {
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }

                }

                @Override
                public void onFall(Exception e) {
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    binding.error.setError(e.getMessage());
                    Log.d("SetupProfileImage", "onFall: "+e.toString());
                    binding.error.setVisibility(View.VISIBLE);
                }
            });

        });



        dialogBox.show();
        dialogBox.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogBox.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogBox.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialogBox.getWindow().setGravity(Gravity.BOTTOM);

    }

    ActivityResultLauncher<PickVisualMediaRequest> pickMedia =
            registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                // Callback is invoked after the user selects a media item or closes the
                // photo picker.
                if (uri != null) {
//                    Log.d("PhotoPicker", "Selected URI: " + uri);
                    userImage = uri;
                    dialog.setTitle("Image is uploading...");
                    dialog.show();
                    UpdateImage(userImage);
                    Glide.with(this).load(userImage).into(binding.userImage);

                } else {
//                    Log.d("PhotoPicker", "No media selected");
                    binding.error.setError("No media selected");
                    binding.error.setVisibility(View.VISIBLE);
                }
            });

    private void UpdateImage(Uri userImage) {
        authService.SetupProfileImage(userImage,requireContext() ,userInfo, new AuthService.SetupAccountPic() {
            @Override
            public void onSet() {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }

            @Override
            public void onFall(Exception e) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                binding.error.setError("No media selected");
                binding.error.setVisibility(View.VISIBLE);
            }
        });
    }


//    private void setupPhoneBox(){
//        PhoneBoxCenterLayoutBinding binding = PhoneBoxCenterLayoutBinding.inflate(LayoutInflater.from(requireContext()));
//
//// Create a dialog using the inflated layout from binding object
//        final Dialog dialog = new Dialog(requireContext());
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.setContentView(binding.getRoot()); // Set the root view of the inflated layout
//        dialog.show();
//        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
//        dialog.getWindow().setGravity(Gravity.CENTER);
//
//// Now you can access views and set click listeners using the binding object
//// For example, if you have a button in your layout with ID "buttonSave":
//        binding.save.setOnClickListener(v -> {
//            // Handle button click here
//            dialog.dismiss(); // Dismiss the dialog
//        });
//        binding.cancel.setOnClickListener(v -> {
//            // Handle button click here
//            dialog.dismiss(); // Dismiss the dialog
//        });
//    }
//    private void setupEmailBox(){
//        EMailSheetLayoutBinding binding = EMailSheetLayoutBinding.inflate(LayoutInflater.from(requireContext()));
//
//// Create a dialog using the inflated layout from binding object
//        final Dialog dialog = new Dialog(requireContext());
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.setContentView(binding.getRoot()); // Set the root view of the inflated layout
//        dialog.show();
//        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
//        dialog.getWindow().setGravity(Gravity.CENTER);
//
//// Now you can access views and set click listeners using the binding object
//// For example, if you have a button in your layout with ID "buttonSave":
//        binding.save.setOnClickListener(v -> {
//            // Handle button click here
//            dialog.dismiss(); // Dismiss the dialog
//        });
//        binding.cancel.setOnClickListener(v -> {
//            // Handle button click here
//            dialog.dismiss(); // Dismiss the dialog
//        });
//    }


}