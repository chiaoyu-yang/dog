package com.example.setting;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


public class SettingActivity extends AppCompatActivity {

    private ImageButton paw;

    ImageView iv_pick_image;
    ActivityResultLauncher<String> mGetContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        paw = findViewById(R.id.to_home);
        iv_pick_image = findViewById(R.id.iv_pick_image);

        paw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openHome();
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
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //用戶授予存儲權限，打開圖像選擇器
                mGetContent.launch("image/*");
            } else {
                // 用戶拒絕了存儲權限，處理髮布（例如顯示一個錯誤消息）
            }
        }
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
        }
    }

}