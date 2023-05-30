package com.example.imagecropper;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.view.View;
import android.widget.ImageButton;
import de.hdodenhof.circleimageview.CircleImageView;

public class Home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ImageButton imageButton = findViewById(R.id.imageButton);
        CircleImageView circleImageView = findViewById(R.id.iv_pick_image);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                circleImageView.setImageResource(R.drawable.headshot);
            }
        });
    }
}