package com.example.soulgo.News;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.soulgo.Constants;
import com.example.soulgo.HomeActivity;
import com.example.soulgo.R;
import com.example.soulgo.Setting.Beep;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NewsActivity extends AppCompatActivity {
    private RecyclerView newsRecview;
    private List<NewsModel> data;
    private String uid, nickname;

    private RadioGroup radioGroup;
    private RadioButton radioNear;
    private boolean sort=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        radioGroup = findViewById(R.id.radioGroup);
        radioNear = findViewById(R.id.radio_near);
        radioNear.setChecked(true);
        radio();

        newsRecview = findViewById(R.id.newsRecview);
        newsRecview.setLayoutManager(new GridLayoutManager(this, 2));

        Intent intent = getIntent();
        uid = intent.getStringExtra("uid");
        nickname = intent.getStringExtra("nickname");

        processdata(sort);

        ImageButton back = findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openactivity();
                Beep.playBeepSound(getApplicationContext());
            }
        });

    }

    private void radio() {
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // 根據選中的 RadioButton 做相應的處理
                RadioGroup radioGroup = findViewById(R.id.radioGroup);

                RadioButton selectedRadioButton = findViewById(checkedId);
                if (selectedRadioButton != null) {
                    String selectedText = selectedRadioButton.getText().toString();
                    Toast.makeText(getApplicationContext(), selectedText, Toast.LENGTH_SHORT).show();

                    // 根據選擇執行其他操作
                    if ("由新至舊".equals(selectedText)) {
                        sort = true;
                    } else if ("由舊至新".equals(selectedText)) {
                        // 执行 Option 2 的操作
                        sort = false;
                    }

                    processdata(sort);
                }
            }
        });
    }

    private void processdata(Boolean sort) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://140.131.114.145/Android/v1/") // 請替換為實際的 API 基本 URL
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        NewsApiService apiService = retrofit.create(NewsApiService.class);

        Call<NewsResponseModel> call = apiService.getDataBySort(sort);

        call.enqueue(new Callback<NewsResponseModel>() {
            @Override
            public void onResponse(Call<NewsResponseModel> call, retrofit2.Response<NewsResponseModel> response) {
                if (response.isSuccessful()) {
                    NewsResponseModel data = response.body();
                    List<NewsModel> newsData = data.getData();

                    if (data.isError()) {
                        Log.e("soulgo", data.getMessage());
                    } else {
                        NewsAdapter adapter = new NewsAdapter(NewsActivity.this, newsData);
                        newsRecview.setAdapter(adapter);

                        adapter.setItemClickListener(new NewsAdapter.ItemClickListener() {
                            @Override
                            public void onItemClick(String newsId) {
                                // 處理點擊事件
                                Intent intent = new Intent(getApplicationContext(), PostActivity.class);
                                intent.putExtra("newsId", newsId);
                                intent.putExtra("uid", uid);
                                intent.putExtra("nickname", nickname);
                                startActivity(intent);
                                Beep.playBeepSound(getApplicationContext());
                            }
                        });
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<NewsResponseModel> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "網路錯誤", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openactivity() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);

    }


}