package com.example.soulgo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;import android.media.MediaPlayer;
import android.net.Uri;import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;import android.widget.VideoView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
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

    private GoogleSignInClient gsc;
    private MediaPlayer mediaPlayer;
    private VideoView videoView;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Objects.requireNonNull(getSupportActionBar()).hide();

        setupGoogleSignIn();
        initializeUI();
        setupButtonListeners();
    }

    private void setupGoogleSignIn() {
        GoogleSignInOptions gso;
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this, gso);
    }

    private void initializeUI() {
        mediaPlayer = MediaPlayer.create(this, R.raw.beep);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        videoView = findViewById(R.id.viewVideo);
        imageView = findViewById(R.id.image);
        setupVideoView();
    }

    private void setupVideoView() {
        String videoPath = "android.resource://" + getPackageName() + "/" + R.raw.login_video;
        Uri uri = Uri.parse(videoPath);
        videoView.setVideoURI(uri);
        videoView.setOnPreparedListener(mp -> {
            mp.start();
            imageView.setVisibility(View.GONE);
        });
        videoView.setOnCompletionListener(MediaPlayer::start);
    }

    private void setupButtonListeners() {
        ImageView googleBtn = findViewById(R.id.button);
        googleBtn.setOnClickListener(view -> {
            signIn();
            playButtonClickSound();
        });
    }

    private void signIn() {
        Intent signInIntent = gsc.getSignInIntent();
        startActivityForResult(signInIntent, 1000);
    }

    private void playButtonClickSound() {
        if (mediaPlayer != null) {
            mediaPlayer.start();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000) {
            handleGoogleSignInResult(data);
        }
    }

    private void handleGoogleSignInResult(Intent data) {
        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);
            if (account != null) {
                String personName = account.getDisplayName();
                String personEmail = account.getEmail();
                registerUser(personName, personEmail);
                navigateToSecondActivity();
            }
        } catch (ApiException e) {
            Toast.makeText(getApplicationContext(), "發生錯誤", Toast.LENGTH_SHORT).show();
        }
    }

    private void registerUser(String personName, String personEmail) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_REGISTER,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        Toast.makeText(getApplicationContext(), jsonObject.getString("訊息"), Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("username", personName);
                params.put("gmail", personEmail);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void navigateToSecondActivity() {
        finish();
        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
        startActivity(intent);
    }
}
