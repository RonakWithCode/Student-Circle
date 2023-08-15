package com.crazyostudio.studentcircle.user;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.crazyostudio.studentcircle.databinding.ActivitySignUpBinding;
import com.google.firebase.auth.FirebaseAuth;
public class SignUp extends AppCompatActivity {
    private ActivitySignUpBinding binding;
//
//    Context context;
//    Resources resources;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

//        load_lang();
        // Spinner Drop down elements

//        binding.texts.setOnClickListener(vie-> changeLangBox());

        binding.link.setOnClickListener(view ->
        {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://crazy-studio-website.web.app/PrivacyPolicy.html"));
            startActivity(browserIntent);
        });



        binding.button.setOnClickListener(view -> {
            if (binding.Number.getText().toString().length() == 10) {
                if (binding.checkBox.isChecked()) {
                    Intent intent = new Intent(this, SignUpOTP.class);
                    intent.putExtra("number", binding.Number.getText().toString());
                    startActivity(intent);
                }
            }
        });
    }

//    private void load_lang() {
//        SharedPreferences setting = getSharedPreferences("setting",MODE_PRIVATE);
//        String app_language = setting.getString("app_language","");
//        String country = setting.getString("app_country","");
//        Log.i("setting", "app_language: "+app_language);
//        Log.i("setting", "country: "+country);
//        Locale locales = new Locale(app_language,country);
//        Configuration configuration = new Configuration();
//        configuration.locale=locales;
//        getBaseContext().getResources().updateConfiguration(configuration,getBaseContext().getResources().getDisplayMetrics());
//        recreate();
//    }


    @Override
    protected void onStart() {
        super.onStart();
           if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            startActivity(new Intent(SignUp.this, LockMangerActivity.class));
            finish();
        }
    }

//
//    private void changeLangBox(){
//        final String lang[] = {"English","hindi"};
//        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
//        mBuilder.setTitle("Choose Language");
//        mBuilder.setSingleChoiceItems(lang, -1, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                if (which == 0) {
//
////                    changeLang("","");
////                    recreate();
//                    context = LocaleHelper.setLocale(SignUp.this, "");
//                    resources = context.getResources();
//                    recreate();
//
//                }
//                else if (which == 1) {
////                    changeLang("hi","rlN");
////                    recreate();
//                    context = LocaleHelper.setLocale(SignUp.this, "hi");
//                    resources = context.getResources();
//                    recreate();
//                }
//            }
//        });
//        mBuilder.create();
//        mBuilder.show();
//
//    }
//
//    private void changeLang(String lang,String country) {
//        Locale locales = new Locale(lang,country);
//        Configuration configuration = new Configuration();
//        configuration.locale=locales;
//        getBaseContext().getResources().updateConfiguration(configuration,getBaseContext().getResources().getDisplayMetrics());
//        SharedPreferences.Editor setting_lang = getSharedPreferences("setting",MODE_PRIVATE).edit();
//        setting_lang.putString("app_language",lang);
//        setting_lang.putString("app_country",country);
//        setting_lang.apply();
//    }

}