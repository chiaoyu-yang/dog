package com.example.soulgo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.soulgo.Beauty.BeautyActivity;
import com.example.soulgo.Book.BookActivity;
import com.example.soulgo.News.NewsActivity;
import com.example.soulgo.News.PostActivity;
import com.example.soulgo.News.PublishActivity;
import com.example.soulgo.Quiz.QuizActivity;
import com.example.soulgo.Rank.RankingActivity;
import com.example.soulgo.Setting.SettingActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class HomeActivity extends AppCompatActivity {
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    TextView userName, myusername;
    ImageButton image;
    ImageView imageview;

    String imageUrl;

    private String uid, nickname;

    MediaPlayer mediaPlayer;

    private String nid1, nid2;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Objects.requireNonNull(getSupportActionBar()).hide();

        mediaPlayer = MediaPlayer.create(this, R.raw.beep);

        userName = findViewById(R.id.userName);
        myusername = findViewById(R.id.myusername);
        image = findViewById(R.id.imageButton);
        imageview = findViewById(R.id.imageView);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this, gso);

        getPost();
        newsClick();

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
                            nickname = nicknameObject.getString("Nickname");
                            uid = nicknameObject.getString("Uid");
                            imageUrl = nicknameObject.getString("userimage");

                            userName.setText(nickname); // 將值設置到userName的TextView
                            myusername.setText(uid); // 將值設置到myusername的TextView

                            Glide.with(HomeActivity.this) // 使用當前活動的上下文
                                    .load("http://140.131.114.145/Android/112_dog/setting/" + imageUrl) // 加載圖片的 URL
                                    .error(R.drawable.error_image) // 加載失敗時顯示的圖片（可選）
                                    .into(imageview); // 加載圖片到 ImageView 中
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

        setupButtonListeners();
    }

    private void setupButtonListeners() {
        final LinearLayout startRank = findViewById(R.id.startRankBtn);
        final LinearLayout startBook = findViewById(R.id.startBookBtn);
        final LinearLayout startBtn = findViewById(R.id.startQuizBtn);
        final ImageButton startimageBtn = findViewById(R.id.imageButton);
        final ImageButton startpublish = findViewById(R.id.imageButton4);
        final ImageButton startNewsBtn = findViewById(R.id.imageButton3);
        final Button startBeauty = findViewById(R.id.beautybutton);


        startBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TextView textViewNickname = findViewById(R.id.myusername);
                        String nickname = textViewNickname.getText().toString().trim();
                        Intent intent = new Intent(HomeActivity.this, QuizActivity.class);
                        intent.putExtra("nickname", nickname);
                        startActivity(intent);
                        playButtonClickSound();
                    }
                });

        startRank.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TextView textViewNickname = findViewById(R.id.userName);
                        String nickname = textViewNickname.getText().toString().trim();
                        Intent intent = new Intent(HomeActivity.this, RankingActivity.class);
                        intent.putExtra("nickname", nickname);
                        startActivity(intent);
                        playButtonClickSound();
                    }
                });

        startBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, BookActivity.class);
                intent.putExtra("uid", uid);
                startActivity(intent);
                playButtonClickSound();
            }
        });


        startimageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView textViewNickname = findViewById(R.id.userName);
                String nickname = textViewNickname.getText().toString().trim();
                Intent intent = new Intent(HomeActivity.this, SettingActivity.class);
                intent.putExtra("nickname", nickname);
                intent.putExtra("imageUrl", imageUrl);
                startActivity(intent);
                playButtonClickSound();
            }
        });

        startpublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView textViewNickname = findViewById(R.id.myusername);
                String nickname = textViewNickname.getText().toString().trim();
                Intent intent = new Intent(HomeActivity.this, PublishActivity.class);
                intent.putExtra("nickname", nickname);
                startActivity(intent);
                playButtonClickSound();
            }
        });

        startNewsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, NewsActivity.class);
                intent.putExtra("uid", uid);
                intent.putExtra("nickname", nickname);
                startActivity(intent);
                playButtonClickSound();
            }
        });

        startBeauty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView textViewNickname = findViewById(R.id.myusername);
                String nickname = textViewNickname.getText().toString().trim();
                Intent intent = new Intent(HomeActivity.this, BeautyActivity.class);
                intent.putExtra("nickname", nickname);
                startActivity(intent);
                playButtonClickSound();

            }
        });

    }

    private void getPost() {
        String url = Constants.URL_HomePost;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);

                            for (int i = 0; i < Math.min(jsonArray.length(), 2); i++) {
                                JSONObject newsItem = jsonArray.getJSONObject(i);
                                String title = newsItem.getString("title");
                                String content = newsItem.getString("content");
                                String imageUrl = newsItem.getString("newsimage");
                                String nid = newsItem.getString("nid");

                                TextView titleTextView = findViewById(i == 0 ? R.id.newsTitle_left : R.id.newsTitle_right);
                                TextView contentTextView = findViewById(i == 0 ? R.id.newsContent_left : R.id.newsContent_right);
                                ImageView imageView = findViewById(i == 0 ? R.id.booklist_image_button_left : R.id.booklist_image_button_right);
                                if (i == 0) {
                                    nid1 = nid;
                                } else {
                                    nid2 = nid;
                                }

                                titleTextView.setText(title);
                                contentTextView.setText(content);

                                Glide.with(getApplicationContext())
                                        .load("http://140.131.114.145/Android/112_dog/news/" + imageUrl)
                                        .error(R.drawable.error_image)
                                        .into(imageView);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("SoulGo", "Error: " + error.getMessage());
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void newsClick() {
        findViewById(R.id.items_news_left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPost(nid1);
            }
        });
        findViewById(R.id.items_news_right).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPost(nid2);
            }
        });
    }

    private void startPost(String nid) {
        Intent intent = new Intent(getApplicationContext(), PostActivity.class);
        intent.putExtra("newsId", nid);
        intent.putExtra("uid", uid);
        intent.putExtra("nickname", nickname);
        startActivity(intent);
    }

    private void playButtonClickSound() {
        if (mediaPlayer != null) {
            mediaPlayer.start();
        }
    }
}
