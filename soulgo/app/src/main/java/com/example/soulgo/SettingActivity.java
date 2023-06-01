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
import android.widget.ImageButton;
import android.widget.ImageView;


public class SettingActivity extends AppCompatActivity {

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
        // 初始化 SharedPreferences new3
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);


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

    }


    //回到主畫面
    private void openHome() {
        Intent intent = new Intent(this,SecondActivity.class);
        startActivity(intent);
    }

    private void openHome2() {
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==-1 && requestCode==101 && data!=null) {
            String result = data.getStringExtra("RESULT");
            Uri resultUri = null;
            if(result!=null) {
                resultUri = Uri.parse(result);
            }

            iv_pick_image.setImageURI(resultUri);
            imageUri = resultUri;//new3

            //new3
            // 保存图像 URI 到 SharedPreferences
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(PREF_IMAGE_URI, result);
            editor.apply();
        }
    }

}