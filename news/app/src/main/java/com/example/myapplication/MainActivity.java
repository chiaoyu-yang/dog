package com.example.myapplication;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private TextView name, textView, detailContent;
    private String id;
    private EditText editDetail;
    private android.widget.LinearLayout nullLayout, editLayout, detailLayout, containerLayout;
    private Button submit, cancel;
    private boolean isTextEmpty = true;
    private ImageButton editButton;
    private RequestQueue requestQueue;
    private ImageView dog_image;

    private ViewGroup.MarginLayoutParams containerLayoutParam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestQueue = Volley.newRequestQueue(this);

        editButton = findViewById(R.id.imageButton2);
        containerLayout = findViewById(R.id.containerLayout);
        containerLayoutParam = (ViewGroup.MarginLayoutParams) containerLayout.getLayoutParams();

        nullLayout = findViewById(R.id.nullLayout);
        editLayout = findViewById(R.id.editLayout);
        detailLayout = findViewById(R.id.detailLayout);
        submit = findViewById(R.id.submit);
        cancel = findViewById(R.id.cancel);
        name = findViewById(R.id.name);
        textView = findViewById(R.id.textView);
        editDetail = findViewById(R.id.editDetail);
        detailContent = findViewById(R.id.detailContent);
        dog_image = findViewById(R.id.imageView2);

        fetchDetail();
        updateDetail();

        nullLayout.setVisibility(View.GONE);
        detailLayout.setVisibility(View.GONE);
        editLayout.setVisibility(View.GONE);
        if (detailContent.getText().toString() == "null") {
            nullLayout.setVisibility(View.VISIBLE);
        } else {
            detailLayout.setVisibility(View.VISIBLE);
        }
    }

    private void fetchDetail() {
        String url = Constants.URL_DETAIL;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.has("Bid")) {
                                id = response.getString("Bid");
                                String dog_name = response.getString("name");
                                String manege_desc = response.getString("manege_desc");
                                String desc = response.getString("desc");
                                String imageUrl = response.getString("image");
                                name.setText(dog_name);
                                textView.setText(manege_desc);
                                detailContent.setText(desc);
                                editDetail.setText(desc);

                                Glide.with(MainActivity.this) // 使用當前活動的上下文
                                        .load("http://140.131.114.145/Android/112_dog/books/" + imageUrl) // 加載圖片的 URL
                                        .error(R.drawable.error_image) // 加載失敗時顯示的圖片（可選）
                                        .into(dog_image); // 加載圖片到 ImageView 中
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

    private void updateDetail() {
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchDetail();

                if (isTextEmpty) {
                    isTextEmpty = false;
                    dog_image.setVisibility(View.GONE);
                    nullLayout.setVisibility(View.GONE);
                    detailLayout.setVisibility(View.GONE);
                    editLayout.setVisibility(View.VISIBLE);

                    containerLayoutParam.topMargin = 0;

                } else {
                    isTextEmpty = true;
                    cancelEdit();
                }
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchUpdate();
                cancelEdit();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelEdit();
            }
        });
    }

    private void cancelEdit() {
        if (detailContent.getText().toString() == "null") {
            nullLayout.setVisibility(View.VISIBLE);
        } else {
            detailLayout.setVisibility(View.VISIBLE);
        }
        editLayout.setVisibility(View.GONE);
        dog_image.setVisibility(View.VISIBLE);

        containerLayoutParam.topMargin = 50;

        fetchDetail();
    }

    private void fetchUpdate() {
        // 找到編輯文本框
        String getEditDetail = editDetail.getText().toString();

        // 定義 PHP 腳本的 URL
        String url = Constants.URL_UPDATE_DETAIL;

        // 創建 StringRequest 對象
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("SoulGo", "成功: ");
                        Toast.makeText(MainActivity.this, "新增成功！", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        name.setText("Error: " + error.getMessage());
                        Log.d("SoulGo", "Error: " + error.getMessage());
                        Toast.makeText(MainActivity.this, "失敗" + error.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                // 創建包含要發送的參數的映射
                Map<String, String> params = new HashMap<>();
                params.put("desc", getEditDetail);
                params.put("Bid", id);
                params.put("update_id", id);// 將使用者輸入傳送到 PHP 腳本
                return params;
            }
        };

        // 將請求添加到 requestQueue（假設您已經初始化了 requestQueue）
        requestQueue.add(stringRequest);
    }



}