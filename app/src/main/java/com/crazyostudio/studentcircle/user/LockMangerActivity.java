package com.crazyostudio.studentcircle.user;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.crazyostudio.studentcircle.MainActivity;
import com.crazyostudio.studentcircle.R;
import com.crazyostudio.studentcircle.databinding.ActivityLockMangerBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class LockMangerActivity extends AppCompatActivity {
    ActivityLockMangerBinding binding;
    String fakePin,realPin;
    FirebaseAuth auth;
    ProgressDialog d,dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLockMangerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        d = new ProgressDialog(this);
        dialog = new ProgressDialog(this);



        auth = FirebaseAuth.getInstance();
        d.setTitle("Check Your details ");
        d.show();
        FirebaseDatabase.getInstance().getReference().child("pin").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).get().addOnCompleteListener(task -> {
            DataSnapshot dataSnapshot = task.getResult();


            if (dataSnapshot.exists()) {
                if (d.isShowing()){
                    d.dismiss();
                }
                fakePin = Objects.requireNonNull(dataSnapshot.child("fakePin").getValue()).toString();
                realPin = Objects.requireNonNull(dataSnapshot.child("realPin").getValue()).toString();
            }else {
                if (auth.getCurrentUser() == null) {
                    if (d.isShowing()){
                        d.dismiss();
                    }
                    Toast.makeText(this, "Sorry We Not Found your Account Please Login Anger", Toast.LENGTH_SHORT).show();
                    auth.signOut();
                    startActivity(new Intent(LockMangerActivity.this,SignUp.class));

                }else {
                    if (Objects.requireNonNull(auth.getCurrentUser().getDisplayName()).isEmpty()) {
                        if (d.isShowing()){
                            d.dismiss();
                        }
                        Toast.makeText(this, "Sorry We Not Found Your Pin or Some details Please ReEnter your Details", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LockMangerActivity.this,SignupDetails.class));
                    }else {
                        if (d.isShowing()){
                            d.dismiss();
                        }
                        Dialog Pin_dialog = new Dialog(LockMangerActivity.this);
                        Pin_dialog.setContentView(R.layout.pindialogbox);
                        Pin_dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                        Pin_dialog.getWindow().setBackgroundDrawableResource(R.drawable.pinboxlayout);
                        Pin_dialog.setCancelable(false);
                        Pin_dialog.show();

                        EditText fakePin = Pin_dialog.findViewById(R.id.fakePin);
                        EditText realPin = Pin_dialog.findViewById(R.id.real_pin);
                        FloatingActionButton button = Pin_dialog.findViewById(R.id.create_Btu);
                        fakePin.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                            }

                            @Override
                            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                                if (fakePin.getText().toString().length()==4) {
                                    realPin.requestFocus();

//                            fakePin.getNextFocusDownId();
                                }
                                else    {
                                    fakePin.setError("Length of a number is 4");
                                }
                            }

                            @Override
                            public void afterTextChanged(Editable editable) {
                                if (fakePin.getText().toString().length()==4) {
//                            fakePin.getNextFocusDownId();
                                }
                                else {
                                    fakePin.setError("Length of a number is 4");
                                }
                            }
                        });
                        realPin.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                            }

                            @Override
                            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                                if (!(fakePin.getText().toString().length() == 4)) {
                                    fakePin.requestFocus();
                                }
                                if (realPin.getText().toString().length()==4) {
                                    button.setVisibility(View.VISIBLE);

                                }
                                else    {
                                    realPin.setError("Length of a number is 4");
                                }
                            }

                            @Override
                            public void afterTextChanged(Editable editable) {
                                if (!(fakePin.getText().toString().length() == 4)) {
                                    fakePin.requestFocus();
                                }
                                if (realPin.getText().toString().length()==4) {
                                    button.setVisibility(View.VISIBLE);
                                    realPin.clearFocus();
//                            fakePin.getNextFocusDownId();
                                }
                                else {
                                    realPin.setError("Length of a number is 4");
                                }
                            }
                        });
                        button.setOnClickListener(view -> {
                            dialog.setTitle("Waiting Upload Pin ");
                            dialog.show();

                            Map<String, String> pins = new HashMap<>();
                            pins.put("fakePin",fakePin.getText().toString());
                            pins.put("realPin",realPin.getText().toString());

                            FirebaseDatabase.getInstance().getReference().child("pin").child(auth.getUid()).setValue(pins).addOnCompleteListener(task2 -> {
                                if (task2.isSuccessful()){
                                    if (dialog.isShowing()){
                                        dialog.dismiss();
                                    }
                                    Pin_dialog.dismiss();
                                    recreate();
                                }
                                else {
                                    if (dialog.isShowing()){
                                        dialog.dismiss();
                                    }
                                    Toast.makeText(LockMangerActivity.this, "ReTry", Toast.LENGTH_SHORT).show();
                                    fakePin.setText("");
                                    realPin.setText("");
                                    button.setVisibility(View.INVISIBLE);
                                }
                            });
                        });

                    }

                }

            }

        });

        binding.pass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (binding.pass.getText().toString().length() == 4) {
                    check();
                    binding.floatingActionButton.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (binding.pass.getText().toString().length() == 4) {
                    check();
                    binding.floatingActionButton.setVisibility(View.VISIBLE);
                }

            }
        });
        binding.floatingActionButton.setOnClickListener(view -> check());
    }

    private void check() {
        d.setTitle("Checking Pin");
        d.show();

        if (binding.pass.getText().toString().equals(realPin)) {
            if (d.isShowing()) {
                d.dismiss();
            }
            startActivity(new Intent(LockMangerActivity.this, MainActivity.class));
            finish();
        } else if (binding.pass.getText().toString().equals(fakePin)) {
            if (d.isShowing()) {
                d.dismiss();
            }
            startActivity(new Intent(LockMangerActivity.this, FakeActivity.class));
        } else {
            if (d.isShowing()) {
                d.dismiss();
            }
            binding.pass.setError("Password -:- ");
        }
    }
}


