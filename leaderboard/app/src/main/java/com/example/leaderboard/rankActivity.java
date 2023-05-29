package com.example.leaderboard;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.List;

public class rankActivity extends AppCompatActivity {

    private TextView division, username, integral;

    private List<Ranklist> ranklists;

    @SuppressLint("MissingInflatedId")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        division = findViewById(R.id.txt_division);
        username = findViewById(R.id.txt_username);
        integral = findViewById(R.id.txt_integral);

        ranklists = new ArrayList<>();

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, constants.URL_RANK, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    JSONArray jsonArray = jsonObject.getJSONArray("ranklists");
                    // 循环遍历JSON数组中的每个对象，并将其添加到列表中
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject rankObject = jsonArray.getJSONObject(i);
                        String division = rankObject.getString("division");
                        String username = rankObject.getString("username");
                        Integer integral = rankObject.getInt("integral");
                        ranklists.add(new Ranklist(division, username, integral));
                    }

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
    }

}
