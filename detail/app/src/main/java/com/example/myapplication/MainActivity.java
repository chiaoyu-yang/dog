package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView textView4;
    private android.widget.LinearLayout LinearLayout, LinearLayout2;
    private boolean isTextEmpty = true;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton imageButton2 = findViewById(R.id.imageButton2);
        LinearLayout = findViewById(R.id.LinearLayout);
        LinearLayout2 = findViewById(R.id.LinearLayout2);

        LinearLayout2.setVisibility(View.GONE);

        imageButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 根据当前文字是否为空来更改文字
                if (isTextEmpty) {
                    isTextEmpty = false;
                    LinearLayout.setVisibility(View.GONE);
                    LinearLayout2.setVisibility(View.VISIBLE);
                } else {
                    isTextEmpty = true;
                    LinearLayout.setVisibility(View.VISIBLE);
                    LinearLayout2.setVisibility(View.GONE);
                }
            }
        });
    }
}