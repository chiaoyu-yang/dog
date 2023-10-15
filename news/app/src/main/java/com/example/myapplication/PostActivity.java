package com.example.myapplication;

import android.content.Intent;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
//import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PostActivity extends AppCompatActivity {

    public static final String NEWS_ID_KEY = "news_id";
    private RecyclerView postRecview;
    private List<PostModel> data;
    private ImageButton backBtn, postLikeBtn, sendButton;
    private String newsId;
    private TextView postTitle, postContent, postLike;
    private ImageView postImage;
    private boolean isLiked;
    private String action;
    private EditText messageInput;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        postRecview = findViewById(R.id.postRecview);
        postRecview.setLayoutManager(new GridLayoutManager(this, 1));

        newsId = getIntent().getStringExtra(NEWS_ID_KEY);

        postTitle = findViewById(R.id.postTitle);
        postContent = findViewById(R.id.postContent);
        postLike = findViewById(R.id.postLike);
        postImage = findViewById(R.id.postImage);
        postLikeBtn = findViewById(R.id.postLikeBtn);
        sendButton = findViewById(R.id.sendButton);
        messageInput = findViewById(R.id.messageInput);

        postTitle();
        postLikeBtn();
        backBtnProcess();
        sendMessageBtn();
        processdata();
    }

    private void processdata() {
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
                        if (messageData != null) {
                            PostAdapter adapter = new PostAdapter(messageData);
                            postRecview.setAdapter(adapter);
                        }
                    }

                } else {
                    // 處理錯誤
                }
            }

            @Override
            public void onFailure(Call<PostResponseModel> call, Throwable t) {
                // 處理網絡錯誤
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
        backBtn = findViewById(R.id.backBtn); // 获取按钮的引用
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
                                Toast.makeText(PostActivity.this, "Dislike: " + message, Toast.LENGTH_SHORT).show();
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
//                processdata();
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
                            } else {
                                Log.e("soulgo", message);
                                Toast.makeText(PostActivity.this, "Error1: " + message, Toast.LENGTH_SHORT).show();
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
                params.put("Uid", "1");
                params.put("message", messageInput.getText().toString());
                return params;
            }
        };

        Volley.newRequestQueue(this).add(stringRequest);
    }
}