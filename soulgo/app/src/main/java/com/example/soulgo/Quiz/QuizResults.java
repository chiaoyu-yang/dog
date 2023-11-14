package com.example.soulgo.Quiz;

import static com.example.soulgo.Constants.URL_MYPOINT;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;import com.example.soulgo.R;import com.example.soulgo.HomeActivity;
import com.example.soulgo.Setting.Beep;

import java.util.HashMap;import java.util.Map;
import java.util.Objects;

public class QuizResults extends AppCompatActivity {
    private TextView textViewUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_results);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        final TextView correctAnswers = findViewById(R.id.correctAnswers);
        final TextView incorrectAnswers = findViewById(R.id.incorrectAnswers);
        final TextView totleScore = findViewById(R.id.totleScore);

        final int getCorrectAnswers = getIntent().getIntExtra("correct", 0);
        final int getIncorrectAnswers = getIntent().getIntExtra("incorrect", 0);
        final int getTotleScore = (getCorrectAnswers-getIncorrectAnswers)*10;
        textViewUsername = findViewById(R.id.myusername);
        String nickname = getIntent().getStringExtra("nickname");
        textViewUsername.setText(nickname);


        correctAnswers.setText(String.valueOf(getCorrectAnswers));
        incorrectAnswers.setText(String.valueOf(getIncorrectAnswers));
        totleScore.setText(String.valueOf(getTotleScore));
        if (getTotleScore < 0) {
            totleScore.setTextColor(Color.parseColor("#DD2C00"));
        } else {
            totleScore.setTextColor(Color.parseColor("#00C853"));
        }

        String url = URL_MYPOINT;
        final Map<String, String> params = new HashMap<>();
        params.put("uid", nickname);
        params.put("newPoint", String.valueOf(getTotleScore));

        // 建立StringRequest物件
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // 在請求成功時執行此處理邏輯
                        // 處理伺服器回應
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // 在請求失敗時執行此處理邏輯
                        // 處理錯誤訊息
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                return params;
            }
        };

        // 加入請求到請求佇列
        Volley.newRequestQueue(this).add(stringRequest);

        final ImageButton startNewBtn = findViewById(R.id.back);

        startNewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(QuizResults.this, HomeActivity.class));
                finish();
                Beep.playBeepSound(getApplicationContext());
            }
        });

    }



}