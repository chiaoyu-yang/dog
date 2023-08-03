package com.example.soulgo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class uploadImage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_image);

        final ImageButton startnews = findViewById(R.id.upload_button);
        final ImageButton openHome = findViewById(R.id.to_home);

        startnews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(uploadImage.this, News.class);
                startActivity(intent);
            }
        });

        openHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(uploadImage.this, SecondActivity.class);
                startActivity(intent);
            }
        });



    }
}