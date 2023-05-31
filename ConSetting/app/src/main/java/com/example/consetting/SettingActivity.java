package com.example.consetting;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

//新添加
import android.os.AsyncTask;
import android.widget.EditText;

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

        // new
        EditText editText = findViewById(R.id.editNickname);
        Button updateButton = findViewById(R.id.updateButton);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newNickname = editText.getText().toString().trim();
                updateNickname(newNickname);
            }
        });
    }

    // new
    private void updateNickname(String newNickname) {
        UpdateNicknameTask updateNicknameTask = new UpdateNicknameTask();
        updateNicknameTask.execute(newNickname);
    }








    //回到主畫面
    private void openHome() {
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
        }
    }

}