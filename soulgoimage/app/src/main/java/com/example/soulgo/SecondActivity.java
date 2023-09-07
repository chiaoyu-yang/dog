package com.example.soulgo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SecondActivity extends AppCompatActivity {
    private MediaPlayer mediaPlayer;
    private TextView userName, myusername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        mediaPlayer = MediaPlayer.create(this, R.raw.beep);

        // 初始化UI元素
        userName = findViewById(R.id.userName);
        myusername = findViewById(R.id.myusername);

        setupButtonListeners();

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            String personEmail = acct.getEmail();

            String url = Constants.URL_MYNICKNAME;

            final Map<String, String> params = new HashMap<>();
            params.put("gmail", personEmail);

            final StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject responseJson = new JSONObject(response);

                        JSONArray myNicknameArray = responseJson.getJSONArray("myNickname");

                        if (myNicknameArray.length() > 0) {
                            JSONObject nicknameObject = myNicknameArray.getJSONObject(0);
                            String nickname = nicknameObject.getString("Nickname");
                            String uid = nicknameObject.getString("Uid");

                            userName.setText(nickname); // 將值設置到userName的TextView
                            myusername.setText(uid); // 將值設置到myusername的TextView
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams() {
                    return params;
                }
            };

            RequestQueue queue = Volley.newRequestQueue(this);
            queue.add(request);
        }
    }

    private void setupButtonListeners() {
        final LinearLayout startRank = findViewById(R.id.startRankBtn);
        final LinearLayout startBook = findViewById(R.id.startBookBtn);
        final LinearLayout startBtn = findViewById(R.id.startQuizBtn);
        final ImageButton startimageBtn = findViewById(R.id.imageButton);
        final ImageButton startuploadBtn = findViewById(R.id.imageButton4);

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView textViewNickname = findViewById(R.id.myusername);
                String nickname = textViewNickname.getText().toString().trim();
                Intent intent = new Intent(SecondActivity.this, QuizActivity.class);
                intent.putExtra("nickname", nickname);
                startActivity(intent);
                playButtonClickSound();
            }
        });

        startRank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView textViewNickname = findViewById(R.id.userName);
                String nickname = textViewNickname.getText().toString().trim();
                Intent intent = new Intent(SecondActivity.this, RankingActivity.class);
                intent.putExtra("nickname", nickname);
                startActivity(intent);
                playButtonClickSound();
            }
        });

        startBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SecondActivity.this, BookActivity.class);
                startActivity(intent);
                playButtonClickSound();
            }
        });

        startimageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView textViewNickname = findViewById(R.id.userName);
                String nickname = textViewNickname.getText().toString().trim();
                Intent intent = new Intent(SecondActivity.this, SettingActivity.class);
                intent.putExtra("nickname", nickname);
                startActivity(intent);
                playButtonClickSound();
            }
        });

        startuploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SecondActivity.this, uploadImage.class);
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
