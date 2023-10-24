package com.example.soulgo.News;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PostActivity extends AppCompatActivity {
    private RecyclerView postRecview;
    private ImageButton backBtn, postLikeBtn, sendButton;
    private String newsId, uid, nickname;
    private TextView postTitle, postContent, postLike, userName;
    private ImageView postImage;
    private boolean isLiked;
    private String action;
    private EditText messageInput;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        postRecview = findViewById(R.id.postRecview);
        postRecview.setLayoutManager(new GridLayoutManager(this, 1));

        Intent intent = getIntent();
        newsId = intent.getStringExtra("newsId");
        uid = intent.getStringExtra("uid");
        nickname = intent.getStringExtra("nickname");

        postTitle = findViewById(R.id.postTitle);
        postContent = findViewById(R.id.postContent);
        postLike = findViewById(R.id.postLike);
        postImage = findViewById(R.id.postImage);
        postLikeBtn = findViewById(R.id.postLikeBtn);
        sendButton = findViewById(R.id.sendButton);
        messageInput = findViewById(R.id.messageInput);
        userName = findViewById(R.id.userName);

        userName.setText(nickname);

        postTitle();
        postLikeBtn();
        backBtnProcess();
        sendMessageBtn();
        getMessage();
        isLiked = getLikeStatus(newsId);
        if (isLiked) {
            postLikeBtn.setImageResource(R.drawable.active_like);
        } else {
            postLikeBtn.setImageResource(R.drawable.like_button);
        }
    }

    private void getMessage() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://140.131.114.145/Android/v1/messageList.php/") // 請替換為實際的 API 基本 URL
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);

        Call<PostResponseModel> call = apiService.getDataById(newsId);

        call.enqueue(new Callback<PostResponseModel>() {
            @Override
            public void onResponse(Call<PostResponseModel> call, retrofit2.Response<PostResponseModel> response) {
                if (response.isSuccessful()) {
                    PostResponseModel data = response.body();
                    List<PostModel> messageData = data.getData();

                    if (data.isError()) {
                        Log.e("soulgo", data.getMessage());
                        Toast.makeText(getApplicationContext(), "Error: " + data.getMessage(), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getApplicationContext(), "Connection Error", Toast.LENGTH_SHORT).show();
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
        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PostActivity.this, NewsActivity.class); // 替换成前一页的Activity类名
                startActivity(intent);
            }
        });
    }

    private void postLikeBtn() {
        postLikeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        });
    }

    private void saveLikeStatus(String newsId, boolean isLiked) {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(newsId, isLiked);
        editor.apply();
    }

    private boolean getLikeStatus(String newsId) {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
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