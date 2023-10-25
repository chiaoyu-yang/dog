package com.example.soulgo.News;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
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
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class NewsActivity extends AppCompatActivity {
    private RecyclerView newsRecview;
    private List<NewsModel> data;
    private String uid, nickname;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        Objects.requireNonNull(getSupportActionBar()).hide();

        mediaPlayer = MediaPlayer.create(this, R.raw.beep);

        newsRecview = findViewById(R.id.newsRecview);
        newsRecview.setLayoutManager(new GridLayoutManager(this, 2));

        Intent intent = getIntent();
        uid = intent.getStringExtra("uid");
        nickname = intent.getStringExtra("nickname");

        processdata();
        setupButtonListeners();
    }
    private void setupButtonListeners() {
        ImageButton back = findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openactivity();
                playButtonClickSound();
            }
        });
    }

    private void processdata() {
        StringRequest request = new StringRequest(Constants.URL_News,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        GsonBuilder builder = new GsonBuilder();
                        Gson gson = builder.create();
                        data = Arrays.asList(gson.fromJson(response, NewsModel[].class));

                        NewsAdapter adapter = new NewsAdapter(data);
                        newsRecview.setAdapter(adapter);

                        adapter.setItemClickListener(new NewsAdapter.ItemClickListener() {
                            @Override
                            public void onItemClick(String newsId) {
                                Intent intent = new Intent(getApplicationContext(), PostActivity.class);
                                intent.putExtra("newsId", newsId);
                                intent.putExtra("uid", uid);
                                intent.putExtra("nickname", nickname);
                                startActivity(intent);
                            }
                        });
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                    }
                });

                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                queue.add(request);
    }

    private void openactivity() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

    private void playButtonClickSound() {
        if (mediaPlayer != null) {
            mediaPlayer.start();
        }
    }
}