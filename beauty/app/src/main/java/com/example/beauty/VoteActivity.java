package com.example.beauty;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
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
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class VoteActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<BeautyItem> beautyList;
    private ImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote);

        recyclerView = findViewById(R.id.voteRecyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMainActivity();
            }
        });

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

                        beautyList.add(new BeautyItem(name, like, image));
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
}