package com.example.beauty;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VoteActivity extends AppCompatActivity implements VoteAdapter.LikeClickListener {

    private RecyclerView recyclerView;
    private ImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote);

        recyclerView = findViewById(R.id.voteRecyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        back = findViewById(R.id.back);
        back.setOnClickListener(view -> openMainActivity());

        fetchAllBeauty();
    }

    public void openMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    private void fetchAllBeauty() {
        StringRequest request = new StringRequest(Request.Method.POST, constants.URL_BEAUTY, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject responseJson = new JSONObject(response);
                    JSONArray voteJsonArray = responseJson.getJSONArray("beautys");
                    int count = voteJsonArray.length();

                    List<BeautyItem> beautyList = new ArrayList<>();

                    for (int i = 0; i < count; i++) {
                        JSONObject voteJson = voteJsonArray.getJSONObject(i);
                        String name = voteJson.getString("name");
                        int like = voteJson.getInt("like");
                        String image = voteJson.getString("image");
                        int beauty_id = voteJson.getInt("beauty_id");

                        beautyList.add(new BeautyItem(name, like, image, beauty_id));
                    }

                    RecyclerView recyclerView = findViewById(R.id.voteRecyclerView);
                    recyclerView.setLayoutManager(new GridLayoutManager(VoteActivity.this, 2));

                    VoteAdapter voteAdapter = new VoteAdapter(beautyList);
                    recyclerView.setAdapter(voteAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        });

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }

    @Override
    public void onLikeClick(BeautyItem beautyItem) {
        // 在這裡實現向伺服器發送 "like" 更新的請求
        // 使用 Volley 或其他 HTTP 請求庫來向伺服器發送請求，更新 "like" 計數
        // 您需要根據您的伺服器端點來實現請求和處理回應

        // 更新 UI
        updateLikeCount(beautyItem);
    }

    private void updateLikeCount(BeautyItem beautyItem) {
        // 實現向伺服器發送 "like" 更新的請求
        // 並更新伺服器上的 "like" 計數
        StringRequest updateRequest = new StringRequest(Request.Method.POST, constants.URL_UPDATE_LIKE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // 處理成功更新的回應，您可以在此處添加任何必要的邏輯
                // 更新伺服器上的 "like" 計數
                // 更新 UI
                // ...
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                // 如果更新失敗，您可以在此處進行錯誤處理
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("beauty_id", String.valueOf(beautyItem.getBeautyId()));
                params.put("like", String.valueOf(beautyItem.getLike()));
                // 如果您需要在請求中傳遞其他參數，請在此處添加

                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(updateRequest);
    }
}