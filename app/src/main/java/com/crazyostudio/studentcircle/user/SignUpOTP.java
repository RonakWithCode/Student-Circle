package com.crazyostudio.studentcircle.user;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.crazyostudio.studentcircle.R;
import com.crazyostudio.studentcircle.databinding.ActivitySignUpOtpBinding;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class SignUpOTP extends AppCompatActivity {
    ActivitySignUpOtpBinding binding;
    ProgressDialog dialog;
    String Number,verificationId;
    FirebaseAuth firebaseAuth;
    private EditText mEt1, mEt2, mEt3, mEt4, mEt5, mEt6;
    private Context mContext;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpOtpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseAuth =  FirebaseAuth.getInstance();
        dialog = new ProgressDialog(this);
        Number = getIntent().getStringExtra("number");
        binding.fullNumber.setText("+91"+Number+" ");
        binding.tvPhoneNo.setOnClickListener(view-> onBackPressed());
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
    }

    private void initialize() {
        mEt1 = findViewById(R.id.otp_edit_text1);
        mEt2 = findViewById(R.id.otp_edit_text2);
        mEt3 = findViewById(R.id.otp_edit_text3);
        mEt4 = findViewById(R.id.otp_edit_text4);
        mEt5 = findViewById(R.id.otp_edit_text5);
        mEt6 = findViewById(R.id.otp_edit_text6);
        mContext = SignUpOTP.this;
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
                            InputMethodManager inputManager = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                            inputManager.hideSoftInputFromWindow(SignUpOTP.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
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
                        .setPhoneNumber("+91"+Number)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(SignUpOTP.this)
                        .setCallbacks(mCallback)
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
            signInWithPhoneAuthCredential(credential);
//            final String code = credential.getSmsCode();
//            if (code!=null)
//            {
////                verifycode(code);
//            }
            Toast.makeText(SignUpOTP.this, "on Verification Completed", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
//            Toast.makeText(getContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            Log.e("TAG", "onVerificationFailed: ", e);
            Toast.makeText(SignUpOTP.this, "Error"+e, Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onCodeSent(@NonNull String s,
                               @NonNull PhoneAuthProvider.ForceResendingToken token) {
            super.onCodeSent(s,token);
            verificationId = s;
            Toast.makeText(SignUpOTP.this, "Sent OTP", Toast.LENGTH_SHORT).show();
//            tokens = token;
        }
    };
    private void SignUp(){
        String UserOtp = mEt1.getText().toString() + mEt2.getText().toString() + mEt3.getText().toString() + mEt4.getText().toString() + mEt5.getText().toString() + mEt6.getText().toString();
        if (!(UserOtp.trim().length() ==6)) {
            if (Build.VERSION.SDK_INT >= 26) {
                ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(VibrationEffect.createOneShot(150, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(150);
            }
        }else {
//            if (UserOtp.equals(verificationId)) {
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId,UserOtp);
                signInWithPhoneAuthCredential(credential);
//            }

        }
    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        dialog.setTitle("Waiting We are Try to Login ");
        dialog.show();
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(SignUpOTP.this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                       if(firebaseAuth.getCurrentUser() !=null) {
                           dialog.setTitle("User is Successful Sign In we are check some information");
                           if (dialog.isShowing()) {dialog.dismiss();}
                           if (firebaseAuth.getCurrentUser().getDisplayName() == null) {
                               Intent intent = new Intent(SignUpOTP.this, SignupDetails.class);
                               intent.putExtra("Number", Number);
                               startActivity(intent);
                           }
                           else {
                               if (dialog.isShowing()) {
                                   dialog.dismiss();
                               }
                            Intent intent = new Intent(SignUpOTP.this, LockMangerActivity.class);
                            startActivity(intent);
                           }
                       }
                    }

                    else {
                        if (dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        Toast.makeText(mContext, "Retry after some time ", Toast.LENGTH_SHORT).show();
                        // Sign in failed, display a message and update the UI
                    }

                });


    }

}