package com.example.soulgo.News;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.soulgo.Beauty.BeautyActivity;
import com.example.soulgo.R;
import com.example.soulgo.Setting.Beep;

import java.util.Objects;

public class NewsPublishDireation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_publish_direation);
        Objects.requireNonNull(getSupportActionBar()).hide();

        ImageButton button = findViewById(R.id.back);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openactivity();
                Beep.playBeepSound(getApplicationContext());
            }
        });

        ImageButton button2 = findViewById(R.id.circle_help);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openactivity();
                Beep.playBeepSound(getApplicationContext());
            }
        });
    }
    public void openactivity() {
        Intent intent = new Intent(this, PublishActivity.class);
        startActivity(intent);
    }
}