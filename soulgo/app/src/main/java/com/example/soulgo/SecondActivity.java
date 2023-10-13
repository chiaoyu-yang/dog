package com.example.soulgo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;import com.android.volley.RequestQueue;import com.android.volley.Response;import com.android.volley.VolleyError;import com.android.volley.toolbox.StringRequest;import com.android.volley.toolbox.Volley;import com.bumptech.glide.Glide;import com.example.soulgo.Book.BookActivity;
import com.example.soulgo.Quiz.QuizActivity;
import com.example.soulgo.Rank.RankingActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import org.json.JSONArray;import org.json.JSONException;import org.json.JSONObject;import java.util.HashMap;import java.util.Map;
import java.util.Objects;

public class SecondActivity extends AppCompatActivity{
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    TextView userName, myusername;
    ImageButton image;
    ImageView imageview;

    String imageUrl;


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        Objects.requireNonNull(getSupportActionBar()).hide();

        userName = findViewById(R.id.userName);
        myusername = findViewById(R.id.myusername);
        image = findViewById(R.id.imageButton);
        imageview = findViewById(R.id.imageView);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this,gso);

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            String personEmail = acct.getEmail();

            String url = Constants.URL_MYNICKNAME;

            final Map<String, String> params = new HashMap<>();
            params.put("gmail", personEmail);

            final StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject responseJson = new JSONObject(response);

                        JSONArray myNicknameArray = responseJson.getJSONArray("myNickname");

                        if (myNicknameArray.length() > 0) {
                            JSONObject nicknameObject = myNicknameArray.getJSONObject(0);
                            String nickname = nicknameObject.getString("Nickname");
                            String uid = nicknameObject.getString("Uid");
                            imageUrl = nicknameObject.getString("userimage");

                            userName.setText(nickname); // 將值設置到userName的TextView
                            myusername.setText(uid); // 將值設置到myusername的TextView

                            Glide.with(SecondActivity.this) // 使用當前活動的上下文
                                    .load("http://140.131.114.145/Android/112_dog/setting/" + imageUrl) // 加載圖片的 URL
                                    .error(R.drawable.error_image) // 加載失敗時顯示的圖片（可選）
                                    .into(imageview); // 加載圖片到 ImageView 中
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



        final LinearLayout startRank = findViewById(R.id.startRankBtn);
        final LinearLayout startBook = findViewById(R.id.startBookBtn);
        final LinearLayout startBtn = findViewById(R.id.startQuizBtn);
        final ImageButton startimageBtn = findViewById(R.id.imageButton);
        final ImageButton startpublish = findViewById(R.id.imageButton4);

    startBtn.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            TextView textViewNickname = findViewById(R.id.myusername);
            String nickname = textViewNickname.getText().toString().trim();
            Intent intent = new Intent(SecondActivity.this, QuizActivity.class);
            intent.putExtra("nickname", nickname);
            startActivity(intent);
          }
        });

    startRank.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            TextView textViewNickname = findViewById(R.id.userName);
            String nickname = textViewNickname.getText().toString().trim();
            Intent intent = new Intent(SecondActivity.this, RankingActivity.class);
            intent.putExtra("nickname", nickname);
            startActivity(intent);
          }
        });
        startBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SecondActivity.this, BookActivity.class);
                startActivity(intent);

            }
        });


        startimageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView textViewNickname = findViewById(R.id.userName);
                String nickname = textViewNickname.getText().toString().trim();
                Intent intent = new Intent(SecondActivity.this,SettingActivity.class);
                intent.putExtra("nickname", nickname);
                intent.putExtra("imageUrl", imageUrl);
                startActivity(intent);

            }
        });

        startpublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView textViewNickname = findViewById(R.id.myusername);
                String nickname = textViewNickname.getText().toString().trim();
                Intent intent = new Intent(SecondActivity.this,PublishActivity.class);
                intent.putExtra("nickname", nickname);
                startActivity(intent);

            }
        });


    }
    
}
