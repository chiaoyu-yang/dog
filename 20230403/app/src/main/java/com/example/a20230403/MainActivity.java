package com.example.a20230403;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private String selectedTopicName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final LinearLayout java = findViewById(R.id.javaLayout);

        final Button startBtn = findViewById(R.id.startQuizBtn);

        java.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                java.setBackgroundResource(R.drawable.round_back_white_stroke10);

            }
        });


        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,QuizActivity.class);
                intent.putExtra("selectedTopic", selectedTopicName);
                startActivity(intent);

            }
        });
    }
}