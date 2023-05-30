package com.example.leaderboard;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RankingActivity extends AppCompatActivity {

    private TextView textViewDivision, textViewUsername, textViewIntegral;

    private LinearLayout rankContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank);

        textViewDivision = findViewById(R.id.mydivision);
        textViewUsername = findViewById(R.id.myusername);
        textViewIntegral = findViewById(R.id.myintegral);

        rankContainer = findViewById(R.id.rank_container);

        ImageButton button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openactivity();
            }
        });

        String nickname = getIntent().getStringExtra("nickname");
        fetchMyRankData(nickname);

        fetchTop100Ranks();
    }

    public void openactivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void fetchMyRankData(String nickname) {
        String url = constants.URL_MYRANK;

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
        String url = constants.URL_RANK;

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject responseJson = new JSONObject(response);
                    JSONArray ranksJsonArray = responseJson.getJSONArray("ranks");
                    int count = ranksJsonArray.length();

                    for (int i = 0; i < count; i++) {
                        JSONObject rankJson = ranksJsonArray.getJSONObject(i);
                        int division = rankJson.getInt("division");
                        String nickname = rankJson.getString("nickname");
                        int points = rankJson.getInt("points");

                        // 動態生成 item_rank.xml 的實例
                        View itemRankView = LayoutInflater.from(RankingActivity.this).inflate(R.layout.item_rank, null);

                        // 找到 item_rank.xml 中的 TextView 元素
                        TextView txtDivision = itemRankView.findViewById(R.id.txt_division);
                        TextView txtUsername = itemRankView.findViewById(R.id.txt_username);
                        TextView txtIntegral = itemRankView.findViewById(R.id.txt_integral);

                        // 設定 TextView 元素的文字內容
                        txtDivision.setText(String.valueOf(division));
                        txtUsername.setText(nickname);
                        txtIntegral.setText(String.valueOf(points));

                        // 將 item_rank.xml 的實例添加到 rankContainer 容器中
                        rankContainer.addView(itemRankView);
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
                });

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }
}
