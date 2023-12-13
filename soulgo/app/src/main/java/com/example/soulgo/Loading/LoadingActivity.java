package com.example.soulgo.Loading;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.soulgo.R;

import java.util.Objects;

public class LoadingActivity extends AppCompatActivity {

    ProgressBar progressBar;
    TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
        ;
        progressBar = findViewById(R.id.progress_bar);
        textView = findViewById(R.id.text_view);

        progressBar.setMax(100);
        progressBar.setScaleY(3f);

        progressAnimation();
    }

    public void progressAnimation(){
        ProgressBarAnimation amin = new ProgressBarAnimation(this, progressBar, textView, 0f, 100f);
        amin.setDuration(8000);
        progressBar.setAnimation(amin);
    }

}
