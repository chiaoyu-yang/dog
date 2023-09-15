package com.example.soulgo;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class News extends AppCompatActivity {

    private MediaPlayer mediaPlayer;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        mediaPlayer = MediaPlayer.create(this, R.raw.beep);

        setupButtonListeners();

    }

    private void setupButtonListeners() {

        final ImageButton openHome = findViewById(R.id.to_home);

        openHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(News.this, uploadImage.class);
                startActivity(intent);
                playButtonClickSound();
            }
        });
    }

    private void playButtonClickSound() {
        if (mediaPlayer != null) {
            mediaPlayer.start();
        }
    }
}