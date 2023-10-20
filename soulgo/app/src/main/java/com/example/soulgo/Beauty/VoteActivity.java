package com.example.soulgo.Beauty;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.soulgo.Constants;import com.example.soulgo.R;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

    @Override
    protected void onResume() {
        super.onResume();
        fetchAllBeauty();
    }

    public void openMainActivity() {
    Intent intent = new Intent(this, BeautyActivity.class);
        startActivity(intent);
    }
    private void fetchAllBeauty() {
    StringRequest request =
        new StringRequest(
            Request.Method.POST,
            Constants.URL_BEAUTY,
            response -> {
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

                VoteAdapter voteAdapter = new VoteAdapter(beautyList, this, this);
                recyclerView.setAdapter(voteAdapter);

              } catch (JSONException e) {
                e.printStackTrace();
              }
            },
            error ->
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG)
                    .show());

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }

    @Override
    public void onLikeClick(BeautyItem beautyItem) {
        // 更新 UI
        updateLikeCount(beautyItem);
    }

    private void updateLikeCount(BeautyItem beautyItem) {
        StringRequest updateRequest = new StringRequest(Request.Method.POST, Constants.URL_UPDATE_LIKE, response -> {
        }, error -> {
            Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
            // 如果更新失敗，您可以在此處進行錯誤處理
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("beauty_id", String.valueOf(beautyItem.getBeautyId()));
                params.put("like", String.valueOf(beautyItem.getLike()));

                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(updateRequest);
    }
}
