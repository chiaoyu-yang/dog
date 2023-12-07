package com.example.soulgo.Book;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.soulgo.Constants;
import com.example.soulgo.R;
import com.example.soulgo.Rank.RankDirectionsActivity;
import com.example.soulgo.Setting.Beep;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DetailActivity extends AppCompatActivity {

    private TextView name, textView, detailContent, submit, lastName;
    private String Bid, Uid;
    private EditText editDetail;
    private android.widget.LinearLayout nullLayout, editLayout, detailLayout, containerLayout;
    private boolean isTextEmpty = true;
    private ImageButton editButton, backBtn, warnBtn;
    private RequestQueue requestQueue;
    private ImageView dog_image;

    private ViewGroup.MarginLayoutParams containerLayoutParam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        requestQueue = Volley.newRequestQueue(this);

        editButton = findViewById(R.id.imageButton2);
        containerLayout = findViewById(R.id.containerLayout);
        containerLayoutParam = (ViewGroup.MarginLayoutParams) containerLayout.getLayoutParams();

        nullLayout = findViewById(R.id.nullLayout);
        editLayout = findViewById(R.id.editLayout);
        detailLayout = findViewById(R.id.detailLayout);
        submit = findViewById(R.id.submit);
        name = findViewById(R.id.name);
        textView = findViewById(R.id.textView);
        editDetail = findViewById(R.id.editDetail);
        detailContent = findViewById(R.id.detailContent);
        dog_image = findViewById(R.id.imageView2);
        backBtn = findViewById(R.id.back);
        lastName = findViewById(R.id.lastName);
        warnBtn = findViewById(R.id.warnBtn);

        Intent intent = getIntent();
        Bid = intent.getStringExtra("bookId");
        Uid = intent.getStringExtra("uid");

        fetchDetail();
        updateDetail();
        backBtn();
        warnAction();

        nullLayout.setVisibility(View.GONE);
        detailLayout.setVisibility(View.GONE);
        editLayout.setVisibility(View.GONE);
        if (detailContent.getText().toString() == "null") {
            nullLayout.setVisibility(View.VISIBLE);
        } else {
            detailLayout.setVisibility(View.VISIBLE);
        }

        ImageButton button2 = findViewById(R.id.circle_help);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openactivity2();
                Beep.playBeepSound(getApplicationContext());
            }
        });
    }

    public void openactivity2() {
        Intent intent = new Intent(this, DetailDireationsActivity.class);
        startActivity(intent);
    }

    private void warnAction() {
        warnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DetailActivity.this);

                alertDialogBuilder.setTitle("檢舉");
                alertDialogBuilder.setMessage("檢舉此內容原因:");

                final EditText input = new EditText(DetailActivity.this);
                alertDialogBuilder.setView(input);

                alertDialogBuilder.setPositiveButton("送出", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String warningMessage = input.getText().toString();
                        if (!warningMessage.isEmpty())  {
                            reportAction(warningMessage);
                        } else {
                            Toast.makeText(DetailActivity.this, "請輸入檢舉此內容原因", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });
    }

    private void reportAction(String warningMessage) {
        String url = Constants.URL_DETAIL_REPORT;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("SoulGooo", "成功: " + response);
                        Toast.makeText(DetailActivity.this, "已檢舉", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        name.setText("Error: " + error.getMessage());
                        Log.d("SoulGooo", "Error: " + error.getMessage());
                        Toast.makeText(DetailActivity.this, "失敗" + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("Uid", Uid);
                params.put("Bid", Bid);
                params.put("reason", warningMessage);
                return params;
            }
        };

        requestQueue.add(stringRequest);
    }

    private void fetchDetail() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://140.131.114.145/Android/v1/detail.php/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);

        Call<DetailResponseModel> call = apiService.getDataById(Bid);
        call.enqueue(new Callback<DetailResponseModel>() {
            @Override
            public void onResponse(Call<DetailResponseModel> call, retrofit2.Response<DetailResponseModel> response) {
                if (response.isSuccessful()) {
                    DetailResponseModel detailResponse = response.body();

                    if (detailResponse != null && !detailResponse.isError()) {
                        List<DetailModel> detailList = detailResponse.getData();

                        if (!detailList.isEmpty()) {
                            DetailModel detail = detailList.get(0);

                            name.setText(detail.getName());
                            detailContent.setText(detail.getDesc());
                            editDetail.setText(detail.getDesc());
                            textView.setText(detail.getManege_desc());
                            lastName.setText("最後編輯人: " + detail.getNickname());

                            String imageUrl = detail.getImage();
                            Glide.with(getApplicationContext()) // 使用當前活動的上下文
                                    .load("http://140.131.114.145/Android/112_dog/books/" + imageUrl) // 加載圖片的 URL
                                    .error(R.drawable.error_image) // 加載失敗時顯示的圖片（可選）
                                    .into(dog_image);
                        } else {
                            Toast.makeText(DetailActivity.this, "No data in the list", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(DetailActivity.this, "Error: " + detailResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(DetailActivity.this, "Error in response", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DetailResponseModel> call, Throwable t) {
                Toast.makeText(DetailActivity.this, "Connection Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void closeKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(submit.getWindowToken(), 0);
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

                Beep.playBeepSound(getApplicationContext());
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchUpdate();
                closeKeyboard();
                cancelEdit();
                Beep.playBeepSound(getApplicationContext());
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
        String getEditDetail = editDetail.getText().toString();
        String url = Constants.URL_UPDATE_DETAIL;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("SoulGooo", "成功: " + response);
                        Toast.makeText(DetailActivity.this, "新增成功！", Toast.LENGTH_SHORT).show();
                        fetchDetail();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        name.setText("Error: " + error.getMessage());
                        Log.d("SoulGooo", "Error: " + error.getMessage());
                        Toast.makeText(DetailActivity.this, "失敗" + error.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("desc", getEditDetail);
                params.put("Bid", Bid);
                params.put("update_id", Uid);
                return params;
            }
        };

        requestQueue.add(stringRequest);
    }

    private void backBtn() {
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), BookActivity.class);
                startActivity(intent);
                Beep.playBeepSound(getApplicationContext());
            }
        });
    }
}