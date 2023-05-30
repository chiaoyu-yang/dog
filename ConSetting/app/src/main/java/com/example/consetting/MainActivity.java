package com.example.consetting;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {

    private ImageButton settingIcon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        settingIcon = findViewById(R.id.settingIcon);
        settingIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openaSetting();
            }
        });
    }

    public void openaSetting() {
        Intent intent = new Intent(this,SettingActivity.class);
        startActivity(intent);
    }

}