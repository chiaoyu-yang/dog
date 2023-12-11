package com.example.soulgo.News;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.soulgo.Constants;
import com.example.soulgo.R;
import com.example.soulgo.Setting.Beep;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.json.JSONException;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PostActivity extends AppCompatActivity {
    private RecyclerView postRecview;
    private ImageButton backBtn, postLikeBtn, sendButton;
    private String newsId, uid, nick;
    private TextView postTitle, postContent, postLike, userName;
    private ImageView postImage;
    private boolean isLiked;
    private String action;
    private EditText messageInput;
    private static final long CLICK_INTERVAL_TIME = 300;
    private static long lastClickTime = 0;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        postRecview = findViewById(R.id.postRecview);
        postRecview.setLayoutManager(new GridLayoutManager(this, 1));

        Intent intent = getIntent();
        newsId = intent.getStringExtra("newsId");

        postTitle = findViewById(R.id.postTitle);
        postContent = findViewById(R.id.postContent);
        postLike = findViewById(R.id.postLike);
        postImage = findViewById(R.id.postImage);
        postLikeBtn = findViewById(R.id.postLikeBtn);
        sendButton = findViewById(R.id.sendButton);
        messageInput = findViewById(R.id.messageInput);
        userName = findViewById(R.id.userName);



        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefsFile", MODE_PRIVATE);
        uid = sharedPreferences.getString("uid", "");
        nick = sharedPreferences.getString("nickname", "");
        userName.setText(nick);

        isLiked = getLikeStatus(newsId);
        if (isLiked) {
            postLikeBtn.setImageResource(R.drawable.active_like);
        } else {
            postLikeBtn.setImageResource(R.drawable.like_button);
        }


        postTitle();
        postLikeBtn();
        backBtnProcess();
        sendMessageBtn();
        getMessage();
        clickImg();
        isLiked = getLikeStatus(newsId);
        if (isLiked) {
            postLikeBtn.setImageResource(R.drawable.active_like);
        } else {
            postLikeBtn.setImageResource(R.drawable.like_button);
        }
    }

    private void clickImg() {
        postImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long currentTomeMilles = SystemClock.uptimeMillis();
                if (currentTomeMilles - lastClickTime < CLICK_INTERVAL_TIME) {
                    Log.e("soulGoooo", "雙擊事件");
                    likeEvent();
                    return;
                }
                lastClickTime = currentTomeMilles;
            }
        });
    }

    private void getMessage() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://140.131.114.145/Android/v1/messageList.php/") // 請替換為實際的 API 基本 URL
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MessageApiService apiService = retrofit.create(MessageApiService.class);

        Call<PostResponseModel> call = apiService.getDataById(newsId);

        call.enqueue(new Callback<PostResponseModel>() {
            @Override
            public void onResponse(Call<PostResponseModel> call, retrofit2.Response<PostResponseModel> response) {
                if (response.isSuccessful()) {
                    PostResponseModel data = response.body();
                    List<PostModel> messageData = data.getData();

                    if (data.isError()) {
                        Log.e("soulgo", data.getMessage());
                    } else {
                        PostAdapter adapter = new PostAdapter(PostActivity.this, messageData);
                        postRecview.setAdapter(adapter);
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PostResponseModel> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "網路錯誤", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void postTitle() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_Post,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String[] data = response.split(",");
                        String title = data[0].trim();
                        String content = data[1];
                        String image = data[2];
                        String like = data[3];

                        // 顯示帖子的標題、內容、圖片和喜歡數
                        postTitle.setText(title);
                        postContent.setText(content);
                        postLike.setText(like);
                        Glide.with(PostActivity.this) // 使用當前活動的上下文
                                .load("http://140.131.114.145/Android/112_dog/news/" + image) // 加載圖片的 URL
                                .error(R.drawable.error_image) // 加載失敗時顯示的圖片（可選）
                                .into(postImage); // 加載圖片到 ImageView 中
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "失敗" + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                // 傳遞 Nid 到後端
                Map<String, String> params = new HashMap<>();
                params.put("Nid", String.valueOf(newsId));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void backBtnProcess() {
        backBtn = findViewById(R.id.back);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PostActivity.this, NewsActivity.class); // 替换成前一页的Activity类名
                startActivity(intent);
                Beep.playBeepSound(getApplicationContext());
            }
        });
    }

    private void postLikeBtn() {
        postLikeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                likeEvent();
            }
        });
    }

    private void likeEvent() {
        isLiked = !isLiked;
        saveLikeStatus(newsId, isLiked);

        if (isLiked) {
            postLikeBtn.setImageResource(R.drawable.active_like);
            action = "like";
        } else {
            postLikeBtn.setImageResource(R.drawable.like_button);
            action = "dislike";
        }

        updatePostLike();
    }

    private void saveLikeStatus(String newsId, boolean isLiked) {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs_" + uid, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(newsId, isLiked);
        editor.apply();
    }

    private boolean getLikeStatus(String newsId) {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs_" + uid, MODE_PRIVATE);
        return sharedPreferences.getBoolean(newsId, false);
    }



    private void updatePostLike() {
        String url = Constants.URL_UpdatePostLike;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean error = jsonResponse.getBoolean("error");
                            String message = jsonResponse.getString("message");
                            String like = jsonResponse.getString("like");
                            postLike.setText(like);

                            if (!error) {
                                Toast.makeText(PostActivity.this, "Success: " + message, Toast.LENGTH_SHORT).show();
                                messageInput.setText("");
                            } else {
                                Toast.makeText(PostActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Request failed: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("Nid", String.valueOf(newsId));
                params.put("action", String.valueOf(action));
                return params;
            }
        };

        Volley.newRequestQueue(this).add(stringRequest);
    }

    private void sendMessageBtn() {
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(sendButton.getWindowToken(), 0);
                Beep.playBeepSound(getApplicationContext());
            }
        });
    }

    private void sendMessage() {
        String url = Constants.URL_SendMessage;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean error = jsonResponse.getBoolean("error");
                            String message = jsonResponse.getString("message");

                            if (!error) {
                                Log.e("soulgo", message);
                                Toast.makeText(PostActivity.this, "留言: " + message, Toast.LENGTH_SHORT).show();
                                messageInput.setText("");
                                getMessage();
                            } else {
                                Log.e("soulgo", message);
                                Toast.makeText(PostActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Request failed: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("Nid", newsId);
                params.put("Uid", uid);
                params.put("message", messageInput.getText().toString());
                return params;
            }
        };

        Volley.newRequestQueue(this).add(stringRequest);
    }
}