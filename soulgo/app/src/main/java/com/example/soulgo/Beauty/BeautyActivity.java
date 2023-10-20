package com.example.soulgo.Beauty;

import android.content.Intent;
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
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class BeautyActivity extends AppCompatActivity {
    private ImageButton vote,image,back;
    private ImageButton[] myImageButtons = new ImageButton[3];

    private String nickname;
    private TextView textViewUsername;
    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beauty);
        mediaPlayer = MediaPlayer.create(this, R.raw.beep);

        Window window = BeautyActivity.this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(BeautyActivity.this, R.color.white));

        // 初始化 ImageButton，假設您的按鈕在布局中的 ID 分別是 myImageButton1、myImageButton2、myImageButton3
        myImageButtons[0] = findViewById(R.id.myImageButton1);
        myImageButtons[1] = findViewById(R.id.myImageButton2);
        myImageButtons[2] = findViewById(R.id.myImageButton3);

        textViewUsername = findViewById(R.id.myusername);
        nickname = getIntent().getStringExtra("nickname");
        textViewUsername.setText(nickname);

        // 設置按鈕的點擊事件處理程序
        for (int i = 0; i < myImageButtons.length; i++) {
            setButtonClickHandler(myImageButtons[i]);
        }
        setupButtonListeners();

    }

    private void openHome() {
    Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }
    private void setupButtonListeners() {
        back = findViewById(R.id.back);
        vote = findViewById(R.id.vote);
        image = findViewById(R.id.image);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openHome();
                playButtonClickSound();
            }
        });

        // 初始化vote按鈕
        vote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openVoteActivity();
                playButtonClickSound();
            }
        });

        // 初始化image按鈕
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openVotePublish();
                playButtonClickSound();
            }
        });
    }
    private void setButtonClickHandler(final ImageButton button) {
        button.setOnClickListener(new View.OnClickListener() {
            boolean isImage1 = true; // 初始圖片為 image1

            @Override
            public void onClick(View v) {
                // 點擊事件處理程序
                if (isImage1) {
                    button.setImageResource(R.drawable.keji); // 切換為第二張圖片
                } else {
                    button.setImageResource(R.drawable.keji_none); // 切換回第一張圖片
                }

                // 切換圖片狀態
                isImage1 = !isImage1;
            }
        });



        fetchTopBeautyData();
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
                    topBeauty.setLike(topBeautyJson.getInt("like"));
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
        updateUI(R.id.beautyone, R.id.nameone, R.id.likeone, firstTopBeauty);

        // 更新第二組資料
        TopBeauty secondTopBeauty = topBeautyList.get(1);
        updateUI(R.id.beautytwo, R.id.nametwo, R.id.liketwo, secondTopBeauty);

        // 更新第三組資料
        TopBeauty thirdTopBeauty = topBeautyList.get(2);
        updateUI(R.id.beautythree, R.id.namethree, R.id.likethree, thirdTopBeauty);
    }

    // 這是原始的 updateUI 方法，不需要再更改
    private void updateUI(int imageResourceId, int nameResourceId, int likeResourceId, TopBeauty topBeauty) {
        String imageUrl = topBeauty.getImage();

        String name = topBeauty.getName();
        int like = topBeauty.getLike();

        ImageView imageView = findViewById(imageResourceId);
        String fullImageUrl = "http://140.131.114.145/Android/112_dog/beauty/" + imageUrl; // 修改圖片 URL
        Glide.with(BeautyActivity.this)
                .load(fullImageUrl)
                .fitCenter() // 自適應大小
                .error(R.drawable.error_image)
                .into(imageView);

        TextView nameTextView = findViewById(nameResourceId);
        TextView likeTextView = findViewById(likeResourceId);

        nameTextView.setText(name);
        likeTextView.setText(String.valueOf(like));
    }
    private void playButtonClickSound() {
        if (mediaPlayer != null) {
            mediaPlayer.start();
        }
    }

}