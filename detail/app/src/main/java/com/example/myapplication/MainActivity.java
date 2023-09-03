package com.example.myapplication;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private TextView name;
    private EditText editTiltle, editDetail;
    private android.widget.LinearLayout LinearLayout, LinearLayout2;
    private Button submit;
    private boolean isTextEmpty = true;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fetchDetail();
        createDetail();
    }

    private void fetchDetail() {
        String url = Constants.URL_DETAIL;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.has("Bid")) {
                                String id = response.getString("Bid");
                                String userName = response.getString("name");
                                name.setText(userName);
                            } else {
                                name.setText("can not find");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                        Log.d("SoulGo", "Error: " + error.getMessage());
                        name.setText("Error: " + error.getMessage());
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }

    private void createDetail() {
        ImageButton editButton = findViewById(R.id.imageButton2);
        LinearLayout = findViewById(R.id.LinearLayout);
        LinearLayout2 = findViewById(R.id.LinearLayout2);
        submit = findViewById(R.id.submit);
        name = findViewById(R.id.name);

        editTiltle = findViewById(R.id.editTitle);
        String getEditTiltle = editTiltle.getText().toString();
        editDetail = findViewById(R.id.editDetail);

        LinearLayout2.setVisibility(View.GONE);

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isTextEmpty) {
                    isTextEmpty = false;
                    LinearLayout.setVisibility(View.GONE);
                    LinearLayout2.setVisibility(View.VISIBLE);
                } else {
                    isTextEmpty = true;
                    LinearLayout.setVisibility(View.VISIBLE);
                    LinearLayout2.setVisibility(View.GONE);
                }
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout.setVisibility(View.VISIBLE);
                LinearLayout2.setVisibility(View.GONE);
            }
        });
    }

    private void fetchupdate() {
        editTiltle = findViewById(R.id.editTitle);
        String getEditTiltle = editTiltle.getText().toString();

        String url = Constants.URL_DETAIL;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // 在這裡處理 PHP 腳本的回應
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // 在這裡處理錯誤
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("desc", getEditTiltle); // 將使用者輸入傳送到 PHP 腳本
                return params;
            }
        };

        requestQueue.add(stringRequest);
    }


}