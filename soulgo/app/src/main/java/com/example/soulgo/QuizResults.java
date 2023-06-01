package com.example.soulgo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class QuizResults extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_results);

        final ImageButton startNewBtn = findViewById(R.id.startNewQuizBtn);
        final TextView correctAnswers = findViewById(R.id.correctAnswers);
        final TextView incorrectAnswers = findViewById(R.id.incorrectAnswers);
        final TextView totleScore = findViewById(R.id.totleScore);

        final int getCorrectAnswers = getIntent().getIntExtra("correct", 0);
        final int getIncorrectAnswers = getIntent().getIntExtra("incorrect", 0);

        correctAnswers.setText(String.valueOf(getCorrectAnswers));
        incorrectAnswers.setText(String.valueOf(getIncorrectAnswers-112));
        totleScore.setText(String.valueOf((getCorrectAnswers-getIncorrectAnswers+112)*10));

        startNewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(QuizResults.this, SecondActivity.class));
                finish();
            }
        });
    }

    public void onBackPressed(){
        startActivity(new Intent(QuizResults.this, SecondActivity.class));
        finish();
    }
}