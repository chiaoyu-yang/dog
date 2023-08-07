package com.example.soulgo;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;


    private ImageButton paw;
    private ImageButton paw2;
    //new 2
    private static final String PREFS_NAME = "ImagePrefs";
    private static final String PREF_IMAGE_URI = "ImageUri";

    //new2
    private ImageView iv_pick_image;
    private ActivityResultLauncher<String> mGetContent;
    private SharedPreferences sharedPreferences;
    //new 3
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        paw = findViewById(R.id.to_home);
        paw2 = findViewById(R.id.logout);
        iv_pick_image = findViewById(R.id.iv_pick_image);


        // 恢复保存的图像 URI
        String savedUri = sharedPreferences.getString(PREF_IMAGE_URI, null);
        if (savedUri != null) {
            imageUri = Uri.parse(savedUri);
            iv_pick_image.setImageURI(imageUri);
        }

        paw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openHome();
            }
        });

        paw2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openHome2();
            }
        });

        iv_pick_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mGetContent.launch("image/*");
            }
        });

        mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {

                Intent intent = new Intent(SettingActivity.this,CropperActivity.class);
                intent.putExtra("DATA", result.toString());
                startActivityForResult(intent,101);

            }
        });



    //回到主畫面
    private void openHome() {
        Intent intent = new Intent(this,SecondActivity.class);
        startActivity(intent);
    }

    private void openHome2() {
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }



}