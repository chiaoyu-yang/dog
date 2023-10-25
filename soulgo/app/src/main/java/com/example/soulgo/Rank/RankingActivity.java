package com.example.soulgo.Rank;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.soulgo.Constants;import com.example.soulgo.R;
import com.example.soulgo.HomeActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class RankingActivity extends AppCompatActivity {

    private TextView textViewDivision, textViewUsername, textViewIntegral;
    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank);
        Objects.requireNonNull(getSupportActionBar()).hide();

        mediaPlayer = MediaPlayer.create(this, R.raw.beep);

        textViewDivision = findViewById(R.id.mydivision);
        textViewUsername = findViewById(R.id.myusername);
        textViewIntegral = findViewById(R.id.myintegral);

        String nickname = getIntent().getStringExtra("nickname");
        fetchMyRankData(nickname);

        fetchTop100Ranks();
        setupButtonListeners();

    }

    private void setupButtonListeners() {
        ImageButton button = findViewById(R.id.back);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openactivity();
                playButtonClickSound();
            }
        });
    }

    public void openactivity() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

    private void fetchMyRankData(String nickname) {
        String url = Constants.URL_MYRANK;

        final Map<String, String> params = new HashMap<>();
        params.put("nickname", nickname);

        final StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject responseJson = new JSONObject(response);
                    JSONArray myRanksJsonArray = responseJson.getJSONArray("myranks");

                    if (myRanksJsonArray.length() > 0) {
                        JSONObject myRankJson = myRanksJsonArray.getJSONObject(0);
                        int division = myRankJson.getInt("division");
                        String nickname = myRankJson.getString("nickname");
                        int points = myRankJson.getInt("points");

                        textViewDivision.setText(String.valueOf(division));
                        textViewUsername.setText(nickname);
                        textViewIntegral.setText(String.valueOf(points));
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

    private void fetchTop100Ranks() {
        String url = Constants.URL_RANK;

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject responseJson = new JSONObject(response);
                    JSONArray ranksJsonArray = responseJson.getJSONArray("ranks");
                    int count = ranksJsonArray.length();

                    List<RankItem> rankList = new ArrayList<>();

                    for (int i = 0; i < count; i++) {
                        JSONObject rankJson = ranksJsonArray.getJSONObject(i);
                        int division = rankJson.getInt("division");
                        String nickname = rankJson.getString("nickname");
                        int points = rankJson.getInt("points");

                        rankList.add(new RankItem(division, nickname, points));
                    }

                    RecyclerView recyclerView = findViewById(R.id.rankRecyclerView);
                    recyclerView.setLayoutManager(new LinearLayoutManager(RankingActivity.this));

                    RankAdapter rankAdapter = new RankAdapter(rankList);
                    recyclerView.setAdapter(rankAdapter);

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

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }

    private void playButtonClickSound() {
        if (mediaPlayer != null) {
            mediaPlayer.start();
        }
    }

}

