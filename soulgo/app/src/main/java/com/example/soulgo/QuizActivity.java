package com.example.soulgo;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class QuizActivity extends AppCompatActivity {
    private TextView question, textViewUsername;

    private AppCompatButton option1, option2, option3, option4;

    private AppCompatButton nextBtn;

    private CountDownTimer quizTimer;

    private int totalTimeInSeconds = 11;
    private int answerDelayInSeconds = 1;

    private int correctAnswers = 0;
    private int incorrectAnswers = 0;

    private List<QuestionList> questionsLists;

    private int currentQuestionPosition = 0;

    private String selectedOptionByUser = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        TextView timer = findViewById(R.id.timer);

        question = findViewById(R.id.question);

        option1 = findViewById(R.id.option1);
        option2 = findViewById(R.id.option2);
        option3 = findViewById(R.id.option3);
        option4 = findViewById(R.id.option4);

        nextBtn = findViewById(R.id.nextBtn);
        textViewUsername = findViewById(R.id.myusername);

        String nickname = getIntent().getStringExtra("nickname");
        textViewUsername.setText(nickname);

        questionsLists = new ArrayList<>();

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        // 創建stringRequest請求
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_QUESTION, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    JSONArray jsonArray = jsonObject.getJSONArray("questions");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject questionObject = jsonArray.getJSONObject(i);
                        String question = questionObject.getString("question");
                        String choice1 = questionObject.getString("choice1");
                        String choice2 = questionObject.getString("choice2");
                        String choice3 = questionObject.getString("choice3");
                        String choice4 = questionObject.getString("choice4");
                        String answer = questionObject.getString("ans");
                        questionsLists.add(new QuestionList(question, choice1, choice2, choice3, choice4, answer, ""));
                    }

                    // 添加第一個問題和選項
                    question.setText(questionsLists.get(currentQuestionPosition).getQuestion());
                    option1.setText(questionsLists.get(currentQuestionPosition).getOption1());
                    option2.setText(questionsLists.get(currentQuestionPosition).getOption2());
                    option3.setText(questionsLists.get(currentQuestionPosition).getOption3());
                    option4.setText(questionsLists.get(currentQuestionPosition).getOption4());

                    startTimer();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        // 發送請求
        requestQueue.add(stringRequest);

        option1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedOptionByUser.isEmpty()) {
                    selectedOptionByUser = option1.getText().toString();

                    option1.setBackgroundResource(R.drawable.round_back_red10);
                    option1.setTextColor(Color.WHITE);

                    revealAnswer();

                    questionsLists.get(currentQuestionPosition).setUserSelectedAnswer(selectedOptionByUser);

                    // 取消計時器
                    quizTimer.cancel();
                    nextQuestionWithDelay();

                    // 判斷答題是否正確並增加計數
                    if (selectedOptionByUser.equals(questionsLists.get(currentQuestionPosition).getAnswer())) {
                        correctAnswers++;
                    } else {
                        incorrectAnswers++;
                    }
                }
            }
        });

        option2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedOptionByUser.isEmpty()) {
                    selectedOptionByUser = option2.getText().toString();

                    option2.setBackgroundResource(R.drawable.round_back_red10);
                    option2.setTextColor(Color.WHITE);

                    revealAnswer();

                    questionsLists.get(currentQuestionPosition).setUserSelectedAnswer(selectedOptionByUser);

                    // 取消計時器，準備進入下一題
                    quizTimer.cancel();
                    nextQuestionWithDelay();

                    // 判斷答題是否正確並增加計數
                    if (selectedOptionByUser.equals(questionsLists.get(currentQuestionPosition).getAnswer())) {
                        correctAnswers++;
                    } else {
                        incorrectAnswers++;
                    }
                }
            }
        });

        option3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedOptionByUser.isEmpty()) {
                    selectedOptionByUser = option3.getText().toString();

                    option3.setBackgroundResource(R.drawable.round_back_red10);
                    option3.setTextColor(Color.WHITE);

                    revealAnswer();

                    questionsLists.get(currentQuestionPosition).setUserSelectedAnswer(selectedOptionByUser);

                    // 取消計時器，準備進入下一題
                    quizTimer.cancel();
                    nextQuestionWithDelay();

                    // 判斷答題是否正確並增加計數
                    if (selectedOptionByUser.equals(questionsLists.get(currentQuestionPosition).getAnswer())) {
                        correctAnswers++;
                    } else {
                        incorrectAnswers++;
                    }
                }
            }
        });

        option4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedOptionByUser.isEmpty()) {
                    selectedOptionByUser = option4.getText().toString();

                    option4.setBackgroundResource(R.drawable.round_back_red10);
                    option4.setTextColor(Color.WHITE);

                    revealAnswer();

                    questionsLists.get(currentQuestionPosition).setUserSelectedAnswer(selectedOptionByUser);

                    // 取消計時器，準備進入下一題
                    quizTimer.cancel();
                    nextQuestionWithDelay();

                    // 判斷答題是否正確並增加計數
                    if (selectedOptionByUser.equals(questionsLists.get(currentQuestionPosition).getAnswer())) {
                        correctAnswers++;
                    } else {
                        incorrectAnswers++;
                    }
                }
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 停止計時器
                if (quizTimer != null) {
                    quizTimer.cancel();
                    quizTimer = null;
                }

                handler.removeCallbacksAndMessages(null); // 取消延遲任務

                // 跳轉到結果頁面，傳回答對和答錯題數挑轉到結果頁面
                Intent intent = new Intent(QuizActivity.this, QuizResults.class);
                intent.putExtra("correct", getCorrectAnswers());
                intent.putExtra("incorrect", getInCorrectAnswers());
                intent.putExtra("nickname", nickname);
                startActivity(intent);
                finish(); // 结束當前的 QuizActivity
            }
        });

    }

    private void nextQuestion() {
        currentQuestionPosition++;

        if (currentQuestionPosition >= questionsLists.size()) {
            // 當所有題目回答完後，將題目重新添加到列表中 實現無限重複答題
            for (QuestionList question : questionsLists) {
                question.setUserSelectedAnswer("");
            }
            currentQuestionPosition = 0;
        }

        selectedOptionByUser = "";

        option1.setBackgroundResource(R.drawable.round_back_white_stroke2_10);
        option1.setTextColor(Color.parseColor("#1F6BB8"));

        option2.setBackgroundResource(R.drawable.round_back_white_stroke2_10);
        option2.setTextColor(Color.parseColor("#1F6BB8"));

        option3.setBackgroundResource(R.drawable.round_back_white_stroke2_10);
        option3.setTextColor(Color.parseColor("#1F6BB8"));

        option4.setBackgroundResource(R.drawable.round_back_white_stroke2_10);
        option4.setTextColor(Color.parseColor("#1F6BB8"));

        question.setText(questionsLists.get(currentQuestionPosition).getQuestion());
        option1.setText(questionsLists.get(currentQuestionPosition).getOption1());
        option2.setText(questionsLists.get(currentQuestionPosition).getOption2());
        option3.setText(questionsLists.get(currentQuestionPosition).getOption3());
        option4.setText(questionsLists.get(currentQuestionPosition).getOption4());

        // 開始新的計時器
        startTimer();
    }

    private Handler handler = new Handler();

    private void nextQuestionWithDelay() {
        handler.removeCallbacksAndMessages(null); // 取消之前的延遲任務

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                nextQuestion();
            }
        }, 2000);
    }


    private void startTimer() {
        final TextView timerTextView = findViewById(R.id.timer);
        int millisecondsInFuture = totalTimeInSeconds * 1000;
        int countDownInterval = 1000;

        quizTimer = new CountDownTimer(millisecondsInFuture, countDownInterval) {
            @Override
            public void onTick(long millisUntilFinished) {
                long seconds = millisUntilFinished / 1000;
                String finalSeconds = String.valueOf(seconds);

                if (finalSeconds.length() == 1) {
                    finalSeconds = "0" + finalSeconds;
                }

                timerTextView.setText(finalSeconds);
            }

            @Override
            public void onFinish() {
                Toast.makeText(QuizActivity.this, "Time Over", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(QuizActivity.this, QuizResults.class);
                intent.putExtra("correct", getCorrectAnswers());
                intent.putExtra("incorrect", getInCorrectAnswers());
                startActivity(intent);
                finish();
            }
        };

        quizTimer.start();
    }

    private int getCorrectAnswers(){
        return correctAnswers;
    }

    private int getInCorrectAnswers() {
        return incorrectAnswers;
    }

    @Override
    public void onBackPressed() {
        if (quizTimer != null) {
            quizTimer.cancel();
            quizTimer = null;
        }

        handler.removeCallbacksAndMessages(null); // 取消之前的延遲任務

        startActivity(new Intent(QuizActivity.this, MainActivity.class));
        finish();
    }


    private void revealAnswer(){
        final String getAnswer = questionsLists.get(currentQuestionPosition).getAnswer();

        if(option1.getText().toString().equals(getAnswer)){
            option1.setBackgroundResource(R.drawable.round_back_green10);
            option1.setTextColor(Color.WHITE);
        }
        else if(option2.getText().toString().equals(getAnswer)){
            option2.setBackgroundResource(R.drawable.round_back_green10);
            option2.setTextColor(Color.WHITE);
        }
        else if(option3.getText().toString().equals(getAnswer)){
            option3.setBackgroundResource(R.drawable.round_back_green10);
            option3.setTextColor(Color.WHITE);
        }
        else if(option4.getText().toString().equals(getAnswer)){
            option4.setBackgroundResource(R.drawable.round_back_green10);
            option4.setTextColor(Color.WHITE);
        }
    }
}