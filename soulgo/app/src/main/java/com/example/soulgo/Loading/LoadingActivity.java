package com.example.soulgo.Loading;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.example.soulgo.HomeActivity;
import com.example.soulgo.R;

import java.util.Objects;

public class LoadingActivity extends AppCompatActivity {

    LottieAnimationView dog;
    ProgressBar progressBar;
    TextView textView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        dog = findViewById(R.id.dog);
        progressBar = findViewById(R.id.progress_bar);
        textView = findViewById(R.id.text_view);


        // 設定動畫的速度，這裡的 2.67 是 8 秒 / 3 秒
        dog.setSpeed(1.0f); // 保持原本的速度

        // 設定動畫重複播放的次數，這裡的 2 是為了讓動畫在兩次重複中共花費 8 秒
        dog.setRepeatCount(1);

        dog.addAnimatorUpdateListener(animation -> {
            // 計算進度並更新 ProgressBar
            float progress = (float) animation.getAnimatedValue();
            int progressBarValue = (int) (progress * progressBar.getMax());
            progressBar.setProgress(progressBarValue);
        });

        // 開始播放動畫
        dog.playAnimation();


        new Handler().postDelayed(() -> {
            Intent i = new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(i);
        } ,5500);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        progressBar.setMax(100);
        progressBar.setScaleY(3f);

        progressAnimation();

    }

    public void progressAnimation() {
        ProgressBarAnimation animation = new ProgressBarAnimation(this, progressBar, textView, 0f, 100f);
        animation.setDuration(5500);
        progressBar.setAnimation(animation);
    }
}
