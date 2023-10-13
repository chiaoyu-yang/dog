package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Arrays;
import java.util.List;

public class NewsActivity extends AppCompatActivity {
    private RecyclerView newsRecview;
    private List<NewsModel> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        Window window = NewsActivity.this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(NewsActivity.this, R.color.white));

        newsRecview = findViewById(R.id.newsRecview);
        newsRecview.setLayoutManager(new GridLayoutManager(this, 2));

        ImageButton to_home = findViewById(R.id.to_home);

        to_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openactivity();
            }
        });

        processdata();
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
        //                        Toast.makeText(getApplicationContext(), "NewsId " + newsId, Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(getApplicationContext(), PostActivity.class);
                                intent.putExtra(PostActivity.NEWS_ID_KEY, newsId);
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
        Intent intent = new Intent(this, PostActivity.class);
        startActivity(intent);
    }
}