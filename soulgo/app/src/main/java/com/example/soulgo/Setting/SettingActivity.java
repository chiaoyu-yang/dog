package com.example.soulgo.Setting;

import android.content.Intent;import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;import android.os.Bundle;import android.provider.MediaStore;import android.util.Base64;import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.soulgo.Constants;
import com.example.soulgo.HomeActivity;
import com.example.soulgo.MainActivity;
import com.example.soulgo.R;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;import java.io.IOException;import java.util.HashMap;import java.util.Map;import java.util.Objects;

public class SettingActivity extends AppCompatActivity{

    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    ImageView imageView;
    String base64EncodedImage, nickname, imageUrl;
    EditText editNickname;
    MediaPlayer mediaPlayer;
    AudioManager audioManager;
    SeekBar volumeSeekBar;
    SeekBar gameSeekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        Objects.requireNonNull(getSupportActionBar()).hide();

        mediaPlayer = MediaPlayer.create(this, R.raw.beep);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this, gso);

        imageView = findViewById(R.id.iv_pick_image);

        editNickname = findViewById(R.id.editNickname);
        nickname = getIntent().getStringExtra("nickname");
        imageUrl = getIntent().getStringExtra("imageUrl");
        editNickname.setText(nickname);

        Glide.with(SettingActivity.this) // 使用當前活動的上下文
                .load("http://140.131.114.145/Android/112_dog/setting/" + imageUrl) // 加載圖片的 URL
                .error(R.drawable.error_image) // 加載失敗時顯示的圖片（可選）
                .into(imageView); // 加載圖片到 ImageView 中

        base64EncodedImage = "0";



        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.with(SettingActivity.this)
                        .cropSquare()	    			//Crop image(Optional), Check Customization for more option
                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            }
        });
        setupButtonListeners();

        mediaPlayer = MediaPlayer.create(this, R.raw.beep);
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        volumeSeekBar = findViewById(R.id.bg_seekbar);
        gameSeekBar = findViewById(R.id.game_seekbar);

        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

        volumeSeekBar.setMax(maxVolume);
        volumeSeekBar.setProgress(currentVolume);

        gameSeekBar.setMax(maxVolume);
        gameSeekBar.setProgress(currentVolume);

        volumeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (seekBar.getId() == R.id.bg_seekbar) {
                    // 如果是 bg_seekbar 被改變
                    if (fromUser) {
                        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
                        float volume = (float) progress / seekBar.getMax();
                        BackgroundMusicService.setVolume(volume);
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // 用户开始拖动SeekBar时触发的事件
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // 用户停止拖动SeekBar时触发的事件
            }
        });

        gameSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (seekBar.getId() == R.id.game_seekbar) {
                    // 如果是 game_seekbar 被改變
                    if (fromUser) {
                        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
                        float volume = (float) progress / seekBar.getMax();
                        Beep.setBeep(volume);
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // 用户开始拖动SeekBar时触发的事件
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // 用户停止拖动SeekBar时触发的事件
            }
        });
    }

    private void setupButtonListeners() {
        ImageButton to_home = findViewById(R.id.back);
        ImageButton logout = findViewById(R.id.logout);

        to_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openactivity();
                uploadData();
                playButtonClickSound();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut(); // 呼叫登出方法
            }

            private void signOut() {
                gsc.signOut().addOnCompleteListener(SettingActivity.this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // 登出成功，執行登出後的操作，例如導航到登入畫面
                        navigateToMainActivity();
                    }

                    private void navigateToMainActivity() {
                        Intent intent = new Intent(SettingActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish(); // 結束當前的活動
                        playButtonClickSound();
                    }

                });
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,@Nullable Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        Uri uri = data.getData();
        imageView.setImageURI(uri);

        try {
            Bitmap selectedBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            selectedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            byte[] imageBytes = byteArrayOutputStream.toByteArray();
            base64EncodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void openactivity() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

    private void uploadData() {
        String newNickname = editNickname.getText().toString();

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.URL_setting,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // 服务器响应成功
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean error = jsonResponse.getBoolean("error");
                            String message = jsonResponse.getString("message");

                            if (!error) {
                                // 操作成功
                                Toast.makeText(SettingActivity.this, message, Toast.LENGTH_SHORT).show();
                            } else {
                                // 操作失败，显示错误消息
                                Toast.makeText(SettingActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            // JSON解析错误，显示默认错误消息
                            Toast.makeText(SettingActivity.this, "暱稱重複，请重试", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // 网络请求错误，显示错误消息
                        Toast.makeText(SettingActivity.this, "网络请求失败，请检查网络连接", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("oldNickname", nickname); // 参数名与后端接口定义一致
                params.put("newNickname", newNickname); // 参数名与后端接口定义一致
                params.put("img", base64EncodedImage);
                return params;
            }
        };

        requestQueue.add(stringRequest);
    }

    private void playButtonClickSound() {
        if (mediaPlayer != null) {
            mediaPlayer.start();
        }
    }
}
