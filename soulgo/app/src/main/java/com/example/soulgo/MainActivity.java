package com.example.soulgo;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.AnimatorSet;import android.animation.ObjectAnimator;import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.animation.DecelerateInterpolator;import android.view.animation.LinearInterpolator;import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.soulgo.Setting.BackgroundMusicService;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    MediaPlayer mediaPlayer;
    private ImageView soulS, soulO, soulU, soulL, goG, goO;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Objects.requireNonNull(getSupportActionBar()).hide();



        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this, gso);


        // 初始化MediaPlayer
        mediaPlayer = MediaPlayer.create(this, R.raw.beep);
        setupButtonListeners();

        // 初始化ImageView
        soulS = findViewById(R.id.soul_s);
        soulO = findViewById(R.id.soul_o);
        soulU = findViewById(R.id.soul_u);
        soulL = findViewById(R.id.soul_l);
        goG = findViewById(R.id.go_g);
        goO = findViewById(R.id.go_o);

        ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(soulS, "scaleX", 0.3f, 1f);
        ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(soulS, "scaleY", 0.3f, 1f);

        ObjectAnimator rotationAnimator = ObjectAnimator.ofFloat(soulO, "rotation", 0f, 360f);

        ObjectAnimator translationXAnimator = ObjectAnimator.ofFloat(soulU, "translationX", -200f, 0f);
        ObjectAnimator translationYAnimator = ObjectAnimator.ofFloat(soulU, "translationY", -200f, 0f);

        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(soulL, "alpha", 0f, 1f);

        ObjectAnimator scaleXAnimatorGo = ObjectAnimator.ofFloat(goG, "scaleX", 0.2f, 1f);
        ObjectAnimator scaleYAnimatorGo = ObjectAnimator.ofFloat(goG, "scaleY", 0.2f, 1f);

        ObjectAnimator translationXAnimatorGo = ObjectAnimator.ofFloat(goO, "translationX", -200f, 0f);
        ObjectAnimator translationYAnimatorGo = ObjectAnimator.ofFloat(goO, "translationY", -200f, 0f);



        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(scaleXAnimator, scaleYAnimator, rotationAnimator, translationXAnimator, translationYAnimator, alphaAnimator, scaleXAnimatorGo, scaleYAnimatorGo, translationXAnimatorGo, translationYAnimatorGo);
        animatorSet.setDuration(2000);
        animatorSet.start();

        // 执行动画
        animateImageView(soulO, 1500, 0);
        animateImageView(goO, 1500, 0);

        Intent serviceIntent = new Intent(MainActivity.this, BackgroundMusicService.class);
        startService(serviceIntent);
    }

    private void setupButtonListeners() {
        ImageView googleBtn = findViewById(R.id.button);

        // 按下googleBtn啟動登入畫面
        googleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // 启动服务播放音乐
                signIn();
                playButtonClickSound();
            }
        });
    }

    private void playButtonClickSound() {
        if (mediaPlayer != null) {
            mediaPlayer.start();
        }
    }

    void signIn() {
        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
        startActivity(intent);
//      Intent signInIntent = gsc.getSignInIntent();
//        startActivityForResult(signInIntent, 1000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1000) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if (account != null) {
                    String personName = account.getDisplayName();
                    String personEmail = account.getEmail();

                    StringRequest stringRequest = new StringRequest(Request.Method.POST,
                            Constants.URL_REGISTER,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        JSONObject jsonObject = new JSONObject(response);
                                        Toast.makeText(getApplicationContext(), jsonObject.getString("訊息"), Toast.LENGTH_LONG).show();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();
                            params.put("username", personName);
                            params.put("gmail", personEmail);
                            return params;
                        }
                    };

                    RequestQueue requestQueue = Volley.newRequestQueue(this);
                    requestQueue.add(stringRequest);
                }

                navigateToSecondActivity();
            } catch (ApiException e) {
                Toast.makeText(getApplicationContext(), "發生錯誤", Toast.LENGTH_SHORT).show();
            }
        }
    }





    void navigateToSecondActivity() {
        finish();
        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
        startActivity(intent);
    }

    private void animateImageView(ImageView imageView, long duration, long delay) {
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(imageView, "scaleX", 0.9f, 1f);
        scaleX.setDuration(duration);
        scaleX.setInterpolator(new DecelerateInterpolator());
        scaleX.setStartDelay(delay);
        scaleX.setRepeatMode(ObjectAnimator.REVERSE);
        scaleX.setRepeatCount(ObjectAnimator.INFINITE);

        ObjectAnimator scaleY = ObjectAnimator.ofFloat(imageView, "scaleY", 0.9f, 1f);
        scaleY.setDuration(duration);
        scaleY.setInterpolator(new DecelerateInterpolator());
        scaleY.setStartDelay(delay);
        scaleY.setRepeatMode(ObjectAnimator.REVERSE);
        scaleY.setRepeatCount(ObjectAnimator.INFINITE);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(scaleX, scaleY);
        animatorSet.start();
    }
}
