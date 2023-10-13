package com.example.soulgo.Quiz;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;import com.example.soulgo.Constants;import com.example.soulgo.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class QuizActivity extends AppCompatActivity {
    private TextView questionText, textViewUsername;

    private AppCompatButton option1, option2, option3, option4;

    private AppCompatButton finishBtn;

    private CountDownTimer quizTimer;

    private int totalTimeInSeconds = 11;

    private int correctAnswers = 0;
    private int incorrectAnswers = 0;

    private String selectedOptionByUser = "";

    private Handler handler = new Handler();

    private String question = "";
    private String choice1 = "";
    private String choice2 = "";
    private String choice3 = "";
    private String choice4 = "";
    private String answer = "";

    private String nickName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        Objects.requireNonNull(getSupportActionBar()).hide();
        questionText = findViewById(R.id.question);
        option1 = findViewById(R.id.option1);
        option2 = findViewById(R.id.option2);
        option3 = findViewById(R.id.option3);
        option4 = findViewById(R.id.option4);
        finishBtn = findViewById(R.id.nextBtn);
        textViewUsername = findViewById(R.id.myusername);

        String nickname = getIntent().getStringExtra("nickname");
        textViewUsername.setText(nickname);

        sendRequest();

        View.OnClickListener optionClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedOptionByUser.isEmpty()) {
                    Button selectedOptionButton = (Button) v;
                    selectedOptionByUser = selectedOptionButton.getText().toString();

                    selectedOptionButton.setBackgroundResource(R.drawable.round_back_red10);
                    selectedOptionButton.setTextColor(Color.WHITE);

                    revealAnswer();

                    boolean isCorrect = selectedOptionByUser.equals(answer);
                    nickName = textViewUsername.getText().toString();
                    sendAnswerToServer(question, selectedOptionByUser, isCorrect, nickName);

                    quizTimer.cancel();
                    nextQuestion();

                    if (selectedOptionByUser.equals(answer)) {
                        correctAnswers++;
                    } else {
                        incorrectAnswers++;
                    }
                }
            }
        };

        option1.setOnClickListener(optionClickListener);
        option2.setOnClickListener(optionClickListener);
        option3.setOnClickListener(optionClickListener);
        option4.setOnClickListener(optionClickListener);

        finishBtn();
    }

    private void sendRequest() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        // 創建stringRequest請求
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_QUESTION, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("questions");
                    JSONObject questionObject = jsonArray.getJSONObject(0);
                    question = questionObject.getString("question");
                    choice1 = questionObject.getString("choice1");
                    choice2 = questionObject.getString("choice2");
                    choice3 = questionObject.getString("choice3");
                    choice4 = questionObject.getString("choice4");
                    answer = questionObject.getString("ans");

                    // 添加第一個問題和選項
                    questionText.setText(question);
                    option1.setText(choice1);
                    option2.setText(choice2);
                    option3.setText(choice3);
                    option4.setText(choice4);

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
    }

    private void finishBtn() {
        finishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 停止計時器
                quizTimer();

                handler.removeCallbacksAndMessages(null); // 取消延遲任務

                // 跳轉到結果頁面，傳回答對和答錯題數挑轉到結果頁面
                Intent intent = new Intent(QuizActivity.this, QuizResults.class);
                intent.putExtra("correct", getCorrectAnswers());
                intent.putExtra("incorrect", getInCorrectAnswers());
                intent.putExtra("nickname", nickName);
                startActivity(intent);
                finish(); // 结束當前的 QuizActivity
            }
        });
    }

    public void quizTimer() {
        if (quizTimer != null) {
            quizTimer.cancel();
            quizTimer = null;
        }
    }

    private void nextQuestion() {
        handler.removeCallbacksAndMessages(null); // 取消之前的延遲任務

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                sendRequest();

                selectedOptionByUser = "";
                TextView[] options = {option1, option2, option3, option4};

                for (TextView option : options) {
                    option.setBackgroundResource(R.drawable.round_back_white_stroke2_10);
                    option.setTextColor(Color.parseColor("#1F6BB8"));
                }
            }
        }, 2000);
    }

    private void sendAnswerToServer(String question, String selectedOptionByUser, boolean isCorrect, String nickName) {
        // 創建 RequestQueue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        // 創建 StringRequest
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_MYANSWER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // 獲取回傳的響應，可以根據需要進行處理
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                // 請求失敗時的錯誤處理
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                // 將答題結果作為參數傳遞到伺服器
                Map<String, String> params = new HashMap<>();
                params.put("uid", nickName);
                params.put("question", question);
                params.put("selectedOption", selectedOptionByUser);
                params.put("isCorrect", isCorrect ? "T" : "F");
                return params;
            }
        };

        // 發送請求
        requestQueue.add(stringRequest);
    }

    private void startTimer() {
        final TextView tvTimer = findViewById(R.id.timer);
        final ProgressBar progressBar = findViewById(R.id.progressBar);
        int millisecondsInFuture = totalTimeInSeconds * 1000;
        int countDownInterval = 1000;

        quizTimer = new CountDownTimer(millisecondsInFuture, countDownInterval) {
            @Override
            public void onTick(long millisUntilFinished) {
                long seconds = millisUntilFinished / 1000;
                int progress = (int) (millisUntilFinished / 1000);
                progressBar.setProgress(progress);

                String finalSeconds = String.valueOf(seconds);

                if (finalSeconds.length() == 1) {
                    finalSeconds = "0" + finalSeconds;
                }

                tvTimer.setText(String.valueOf(finalSeconds));
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

    private void revealAnswer(){
        TextView[] options = {option1, option2, option3, option4};

        for (TextView option : options) {
            if (option.getText().toString().equals(answer)) {
                option.setBackgroundResource(R.drawable.round_back_green10);
                option.setTextColor(Color.WHITE);
                break;
            }
        }
    }
}