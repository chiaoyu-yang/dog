package com.example.soulgo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
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
import com.example.soulgo.Setting.Beep;
import com.example.soulgo.Setting.SettingActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HomeActivity extends AppCompatActivity {
    // 声明全局变量
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    TextView userName, myusername;
    ImageButton image;
    ImageView imageview;
    private String uid, nickname, nid1, nid2, imageUrl;
    private static final String PREFS_NAME = "MyPrefsFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Objects.requireNonNull(getSupportActionBar()).hide();

        // 初始化SharedPreferences
        SharedPreferences sharedPreferences;
        sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        // 初始化Google登入選項和客戶端
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this, gso);

        // 初始化MediaPlayer
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
                        Intent intent = new Intent(HomeActivity.this, QuizActivity.class);
                        startActivity(intent);
                        Beep.playBeepSound(getApplicationContext());
                    }
                });

        startRank.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(HomeActivity.this, RankingActivity.class);
                        startActivity(intent);
                        Beep.playBeepSound(getApplicationContext());
                    }
                });

        startBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, BookActivity.class);
                intent.putExtra("uid", uid);
                startActivity(intent);
                Beep.playBeepSound(getApplicationContext());
            }
        });


        startimageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, SettingActivity.class);
                startActivity(intent);
                Beep.playBeepSound(getApplicationContext());
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
                Beep.playBeepSound(getApplicationContext());
            }
        });

        startNewsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, NewsActivity.class);
                intent.putExtra("uid", uid);
                intent.putExtra("nickname", nickname);
                startActivity(intent);
                Beep.playBeepSound(getApplicationContext());
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
                Beep.playBeepSound(getApplicationContext());
            }
        });

        // 取得視圖元件
        userName = findViewById(R.id.userName);
        myusername = findViewById(R.id.myusername);
        image = findViewById(R.id.imageButton);
        imageview = findViewById(R.id.imageView);

        // 檢查SharedPreferences是否包含這三個值，如果有則從SharedPreferences中讀取
        if (sharedPreferences.contains("uid") && sharedPreferences.contains("nickname") && sharedPreferences.contains("imageUrl")) {
            uid = sharedPreferences.getString("uid", "");
            nickname = sharedPreferences.getString("nickname", "");
            imageUrl = sharedPreferences.getString("imageUrl", "");
            setUserInfo();

            // 如果这三个值有任何一个不是空值，执行setUserInfo()
            if (uid.isEmpty() || nickname.isEmpty() || imageUrl.isEmpty()) {
                // 如果SharedPreferences中不包含這三個值，則發送Volley請求獲取資訊
                GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
                if (acct != null) {
                    handleGoogleSignInAccount(acct);
                }
            }

        } else {
            // 如果SharedPreferences中不包含這三個值，則發送Volley請求獲取資訊
            GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
            if (acct != null) {
                handleGoogleSignInAccount(acct);
            }
        }

        getPost();
        newsClick();
    }

    // 處理Google帳戶資訊
    private void handleGoogleSignInAccount(GoogleSignInAccount account) {
        String personEmail = account.getEmail();
        String url = Constants.URL_MYNICKNAME;

        final Map<String, String> params = new HashMap<>();
        params.put("gmail", personEmail);

        performVolleyRequest(url, params);
    }

    private void performVolleyRequest(String url, Map<String, String> params) {
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

                        // 將使用者資訊設定到相應的視圖元件
                        setUserInfo();

                        // 儲存使用者資訊到SharedPreferences
                        saveUserDataToSharedPreferences(nickname, uid, imageUrl);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }

    private void saveUserDataToSharedPreferences(String nickname, String uid, String imageUrl) {
        // 使用Context.MODE_PRIVATE
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("nickname", nickname);
        editor.putString("uid", uid);
        editor.putString("imageUrl", imageUrl);
        editor.apply();
    }

    // 將使用者資訊設定到相應的視圖元件
    private void setUserInfo() {
        userName.setText(nickname);
        myusername.setText(uid);

        Glide.with(HomeActivity.this)
                .load("http://140.131.114.145/Android/112_dog/setting/" + imageUrl)
                .error(R.drawable.error_image)
                .into(imageview);
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

}