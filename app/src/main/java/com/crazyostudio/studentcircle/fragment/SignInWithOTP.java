package com.crazyostudio.studentcircle.fragment;

import static android.content.Context.VIBRATOR_SERVICE;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.os.VibrationEffect;
import android.os.Vibrator;
import android.renderscript.ScriptGroup;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.crazyostudio.studentcircle.Java_Class.GetPublicIpAddressTask;
import com.crazyostudio.studentcircle.Java_Class.IpGeolocationTask;
import com.crazyostudio.studentcircle.R;
import com.crazyostudio.studentcircle.databinding.FragmentSignInWithOTPBinding;
import com.crazyostudio.studentcircle.fragmentLoad;
import com.crazyostudio.studentcircle.model.CurrentInternetConnection;
import com.crazyostudio.studentcircle.model.RecentLoginsModels;
import com.crazyostudio.studentcircle.user.LockMangerActivity;
import com.crazyostudio.studentcircle.user.SignUpOTP;
import com.crazyostudio.studentcircle.user.SignupDetails;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class SignInWithOTP extends Fragment {
    FragmentSignInWithOTPBinding binding;
    ProgressDialog dialog;
    String verificationId;
    FirebaseAuth firebaseAuth;
    private EditText mEt1, mEt2, mEt3, mEt4, mEt5, mEt6;
    private Context context;
    private static final String ARG_NUMBER = "number";
    private String number;


    public SignInWithOTP() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            number = getArguments().getString(ARG_NUMBER);
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSignInWithOTPBinding.inflate(inflater,container,false);
        context = getContext();


        firebaseAuth =  FirebaseAuth.getInstance();
        dialog = new ProgressDialog(context);
        binding.fullNumber.setText("+91"+number+" ");
//        binding.tvPhoneNo.setOnClickListener(view-> navController.popBackStack());
        binding.tvResend.setOnClickListener(view-> sentOTP());

        initialize();
        addTextWatcher(mEt1);
        addTextWatcher(mEt2);
        addTextWatcher(mEt3);
        addTextWatcher(mEt4);
        addTextWatcher(mEt5);
        addTextWatcher(mEt6);
        sentOTP();
        binding.btnVerify.setOnClickListener(view-> SignUp());

        return binding.getRoot();
    }
    private void initialize() {
        mEt1 = binding.otpEditText1;
        mEt2 = binding.otpEditText2;
        mEt3 = binding.otpEditText3;
        mEt4 = binding.otpEditText4;
        mEt5 = binding.otpEditText5;
        mEt6 = binding.otpEditText6;
    }
    private void addTextWatcher(@NonNull final EditText one) {
        one.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @SuppressLint("NonConstantResourceId")
            @Override
            public void afterTextChanged(Editable s) {
                switch (one.getId()) {
                    case R.id.otp_edit_text1:
                        if (one.length() == 1) {
                            mEt2.requestFocus();
                        }
                        break;
                    case R.id.otp_edit_text2:
                        if (one.length() == 1) {
                            mEt3.requestFocus();
                        } else if (one.length() == 0) {
                            mEt1.requestFocus();
                        }
                        break;
                    case R.id.otp_edit_text3:
                        if (one.length() == 1) {
                            mEt4.requestFocus();
                        } else if (one.length() == 0) {
                            mEt2.requestFocus();
                        }
                        break;
                    case R.id.otp_edit_text4:
                        if (one.length() == 1) {
                            mEt5.requestFocus();
                        } else if (one.length() == 0) {
                            mEt3.requestFocus();
                        }
                        break;
                    case R.id.otp_edit_text5:
                        if (one.length() == 1) {
                            mEt6.requestFocus();
                        } else if (one.length() == 0) {
                            mEt4.requestFocus();
                        }
                        break;
                    case R.id.otp_edit_text6:
                        if (one.length() == 1) {
                            InputMethodManager inputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                            inputManager.hideSoftInputFromWindow(requireActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                        } else if (one.length() == 0) {
                            mEt5.requestFocus();
                        }
                        break;
                }
            }
        });
    }
    private void sentOTP(){
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(firebaseAuth)
                        .setPhoneNumber("+91"+number)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(requireActivity())
                        .setCallbacks(mCallback)
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
            signInWithPhoneAuthCredential(credential);
            Toast.makeText(context, "on Verification Completed", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
//            Toast.makeText(getContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            Log.e("TAG", "onVerificationFailed: ", e);
            Toast.makeText(context, "Error"+e, Toast.LENGTH_SHORT).show();
            binding.ProgressBar.setVisibility(View.GONE);
            requireActivity().onBackPressed();
        }

        @Override
        public void onCodeSent(@NonNull String s,
                               @NonNull PhoneAuthProvider.ForceResendingToken token) {
            super.onCodeSent(s,token);
            verificationId = s;
            Toast.makeText(context, "Sent OTP", Toast.LENGTH_SHORT).show();
            binding.ProgressBar.setVisibility(View.GONE);

//            tokens = token;
        }
    };
    private void SignUp(){
        String UserOtp = mEt1.getText().toString() + mEt2.getText().toString() + mEt3.getText().toString() + mEt4.getText().toString() + mEt5.getText().toString() + mEt6.getText().toString();
        if (!(UserOtp.trim().length() == 6)) {
            if (Build.VERSION.SDK_INT >= 26) {
                ((Vibrator) context.getSystemService(VIBRATOR_SERVICE)).vibrate(VibrationEffect.createOneShot(150, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                ((Vibrator) context.getSystemService(VIBRATOR_SERVICE)).vibrate(150);
            }
        }else {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId,UserOtp);
            signInWithPhoneAuthCredential(credential);
        }
    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        binding.ProgressBar.setVisibility(View.VISIBLE);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(requireActivity(), task -> {
                    if (task.isSuccessful()) {
//                        RecentLogins();
                        Sigin();
                    }

                    else {
                        Toast.makeText(context, "Error to login try again", Toast.LENGTH_SHORT).show();
                        binding.ProgressBar.setVisibility(View.GONE);
                        // Sign in failed, display a message and update the UI
                    }
                });


    }

//    private void RecentLogins() {
//        ArrayList<String> time = new ArrayList<>();
//        ArrayList<String> device = new ArrayList<>();
//        ArrayList<String> IP_ARRAY = new ArrayList<>();
//        ArrayList<String> approximateLocation = new ArrayList<>();
//        RecentLoginsModels models = new RecentLoginsModels();
//        if (Objects.requireNonNull(firebaseAuth.getCurrentUser()).getDisplayName() != null) {
//            FirebaseDatabase.getInstance().getReference().child("RecentLogins").child(Objects.requireNonNull(firebaseAuth.getUid())).addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    RecentLoginsModels models5 = snapshot.getValue(RecentLoginsModels.class);
//                    assert models5 != null;
//                    time.addAll(models5.getTime());
//                    approximateLocation.addAll(models5.getApproximateLocation());
//                    IP_ARRAY.addAll(models5.getIp());
//                    device.addAll(models5.getDevice());
//
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//
//                }
//            });
//        }
//
//        String deviceName = Build.MANUFACTURER + " " + Build.MODEL;
//        device.add(deviceName);
//        time.add(String.valueOf(System.currentTimeMillis()));
//        final String[] ip = new String[1];
//        new GetPublicIpAddressTask(ipAddress -> {
//            if (ipAddress != null) {
//                ip[0] = ipAddress;
//                IP_ARRAY.add(ipAddress);
//            }
//        }).execute();
//
//        new IpGeolocationTask((city, country) -> {
//            if (city != null && country != null) {
//                approximateLocation.add("City: " + city + ", Country: " + country);
//                models.setIp(IP_ARRAY);
//                models.setDevice(device);
//                models.setTime(time);
//                models.setApproximateLocation(approximateLocation);
//                FirebaseDatabase.getInstance().getReference().child("RecentLogins").child(Objects.requireNonNull(firebaseAuth.getUid())).setValue(models).addOnCompleteListener(task->{
//                    if (task.isSuccessful()) {
//                        Sigin();
//                    }else {
//                        Toast.makeText(requireContext(), Objects.requireNonNull(task.getException()).toString(), Toast.LENGTH_SHORT).show();
//                        firebaseAuth.signOut();
//                    }
//                }).addOnFailureListener(fail->firebaseAuth.signOut());
//
//            } else {
//                firebaseAuth.signOut();
//            }
//        }).execute(ip); // Replace with the IP address you want to geolocate
//
//
//    }
    private void Sigin() {
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task_id -> {
            if (task_id.isSuccessful()) {
                String token = task_id.getResult();
                binding.ProgressBar.setVisibility(View.GONE);
                if (Objects.requireNonNull(firebaseAuth.getCurrentUser()).getDisplayName() == null) {
                    Bundle bundle = new Bundle();
                    bundle.putString("token", token);
                    bundle.putString("number", number);
                    NavHostFragment navHostFragment = (NavHostFragment) requireActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
                    assert navHostFragment != null;
                    NavController navController = navHostFragment.getNavController();
                    navController.navigate(R.id.action_signInWithOTP_to_signUpInfoFragment, bundle);





                }
                else {
                    UpdateToken(token,0);
                }
            }
            else {
                binding.ProgressBar.setVisibility(View.GONE);
                firebaseAuth.signOut();
                Toast.makeText(context, "Token is not generate try again", Toast.LENGTH_SHORT).show();
//                Log.e("TASK_token", "Token retrieval failed: " + task_id.getException());
            }
        });

    }
    private void UpdateToken(String token,int time) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseDatabase.getReference().child("UserInfo").child(Objects.requireNonNull(firebaseAuth.getUid())).child("token").setValue(token).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                binding.ProgressBar.setVisibility(View.GONE);
                requireActivity().finish();
            }else {
                if (time == 0) {
                    binding.ProgressBar.setVisibility(View.GONE);
                    UpdateToken(token, 1);
                }
                else {
                    firebaseAuth.signOut();
                    binding.ProgressBar.setVisibility(View.GONE);
                }
            }
        });

    }

}