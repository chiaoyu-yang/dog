package com.example.soulgo.Beauty;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.soulgo.Constants;
import com.example.soulgo.HomeActivity;
import com.example.soulgo.R;
import com.example.soulgo.Setting.Beep;
import com.example.soulgo.Setting.SettingDireationsActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class BeautyActivity extends AppCompatActivity {
    private ImageButton vote,image,to_home;
    private String nickname;
    private TextView textViewUsername;
    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beauty);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mediaPlayer = MediaPlayer.create(this, R.raw.beep);


        textViewUsername = findViewById(R.id.myusername);
        nickname = getIntent().getStringExtra("nickname");
        textViewUsername.setText(nickname);
        to_home = findViewById(R.id.to_home);
        vote = findViewById(R.id.vote);
        image = findViewById(R.id.image);

        ImageButton button2 = findViewById(R.id.circle_help);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openactivity2();
                Beep.playBeepSound(getApplicationContext());
            }
        });

        to_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openHome();
                Beep.playBeepSound(getApplicationContext());
            }
        });

        // 初始化vote按鈕
        vote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openVoteActivity();
                Beep.playBeepSound(getApplicationContext());
            }
        });

        // 初始化image按鈕
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openVotePublish();
                Beep.playBeepSound(getApplicationContext());
            }
        });

        fetchTopBeautyData();
    }

    public void openactivity2() {
        Intent intent = new Intent(this, BeautyDireationsActivity.class);
        startActivity(intent);
    }

    private void openHome() {
    Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

    public void openVoteActivity() {
    Intent intent = new Intent(this, VoteActivity.class);
        intent.putExtra("nickname", nickname);
        startActivity(intent);
    }

    public void openVotePublish() {
        TextView textViewNickname = findViewById(R.id.myusername);
        String nickname = textViewNickname.getText().toString().trim();
        Intent intent = new Intent(this, VotePublish.class);
        intent.putExtra("nickname", nickname);
        startActivity(intent);
    }

    private void fetchTopBeautyData() {
    StringRequest request =
        new StringRequest(
            Request.Method.POST,
            Constants.URL_TOPBEAUTY,
            new Response.Listener<String>() {
              @Override
              public void onResponse(String response) {
                try {
                  // 將 JSON 字串轉換為 JSONObject
                  JSONObject jsonResponse = new JSONObject(response);

                  // 獲取 "topbeautys" 陣列
                  JSONArray topBeautysArray = jsonResponse.getJSONArray("topbeautys");

                  // 創建一個 List 來保存 TopBeauty 物件
                  List<TopBeauty> topBeautyList = new ArrayList<>();

                  // 迭代 "topbeautys" 陣列，創建 TopBeauty 物件並添加到列表中
                  for (int i = 0; i < topBeautysArray.length(); i++) {
                    JSONObject topBeautyJson = topBeautysArray.getJSONObject(i);
                    TopBeauty topBeauty = new TopBeauty();
                    topBeauty.setImage(topBeautyJson.getString("image"));
                    topBeauty.setName(topBeautyJson.getString("name"));
                    topBeautyList.add(topBeauty);
                  }

                  // 更新 UI，使用 topBeautyList 中的資料
                  updateUI(topBeautyList);

                } catch (JSONException e) {
                  e.printStackTrace();
                }
              }
            },
            new Response.ErrorListener() {
              @Override
              public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error loading image", Toast.LENGTH_LONG)
                    .show();
              }
            });

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(request);
    }

    private void updateUI(List<TopBeauty> topBeautyList) {
        // 更新第一組資料
        TopBeauty firstTopBeauty = topBeautyList.get(0);
        updateUI(R.id.beautyone, R.id.nameone, firstTopBeauty);

        // 更新第二組資料
        TopBeauty secondTopBeauty = topBeautyList.get(1);
        updateUI(R.id.beautytwo, R.id.nametwo, secondTopBeauty);

        // 更新第三組資料
        TopBeauty thirdTopBeauty = topBeautyList.get(2);
        updateUI(R.id.beautythree, R.id.namethree, thirdTopBeauty);
    }

    // 這是原始的 updateUI 方法，不需要再更改
    private void updateUI(int imageResourceId, int nameResourceId, TopBeauty topBeauty) {
        String imageUrl = topBeauty.getImage();

        String name = topBeauty.getName();

        ImageView imageView = findViewById(imageResourceId);
        String fullImageUrl = "http://140.131.114.145/Android/112_dog/beauty/" + imageUrl; // 修改圖片 URL
        Glide.with(BeautyActivity.this)
                .load(fullImageUrl)
                .fitCenter() // 自適應大小
                .error(R.drawable.error_image)
                .into(imageView);

        TextView nameTextView = findViewById(nameResourceId);

        nameTextView.setText(name);
    }

}
