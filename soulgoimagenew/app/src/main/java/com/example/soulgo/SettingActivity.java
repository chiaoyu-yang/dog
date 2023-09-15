package com.example.soulgo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingActivity extends AppCompatActivity {

    private EditText editNickname;
    private ImageButton paw;
    private ImageButton paw2;
    private AudioManager audioManager;
    private SeekBar volumeSeekBar;
    private SeekBar gameSeekBar;
    private MediaPlayer mediaPlayer;
    private  int uid;
    private String base64EncodedImage;
    private ImageView image;
    private static final int GALLERY_REQUEST_CODE = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);



        String nickname = getIntent().getStringExtra("nickname");
        editNickname = findViewById(R.id.editNickname);
        editNickname.setText(nickname);

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
                if (fromUser) {
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
                    float volume = (float) progress / seekBar.getMax();
                    BackgroundMusicService.setVolume(volume);
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
                if (fromUser) {
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
                    float volume = (float) progress / seekBar.getMax();
                    Beep.setBeep(volume);
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

        setupButtonListeners();

        image = findViewById(R.id.iv_pick_image);

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });
    }

    private void setupButtonListeners() {
        paw = findViewById(R.id.to_home);
        paw2 = findViewById(R.id.logout);
        Button button2 = findViewById(R.id.button2);

        paw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openHome();
                playButtonClickSound();
            }
        });

        paw2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openHome2();
                playButtonClickSound();
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newNickname = editNickname.getText().toString();
                String oldNickname = getIntent().getStringExtra("nickname");
                postNicknameToBackend(oldNickname, newNickname);
                playButtonClickSound();
            }
        });

        // 添加上传按钮的点击监听器
    }

    private void playButtonClickSound() {
        if (mediaPlayer != null) {
            mediaPlayer.start();
        }
    }

    private void postNicknameToBackend(String oldNickname, String newNickname) {
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.URL_setting,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // 请求成功的处理逻辑
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // 请求失败的处理逻辑
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("oldNickname", oldNickname); // 参数名与后端接口定义一致
                params.put("newNickname", newNickname); // 参数名与后端接口定义一致
                // 可以添加其他参数
                return params;
            }
        };
        // 将请求添加到请求队列
        Volley.newRequestQueue(this).add(stringRequest);
    }

    private void openHome() {
        Intent intent = new Intent(this, SecondActivity.class);
        startActivity(intent);
    }

    private void openHome2() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            try {
                Bitmap selectedBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);

                // 将Bitmap转换为Base64字符串
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                selectedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                byte[] imageBytes = byteArrayOutputStream.toByteArray();
                base64EncodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);

                int targetWidth = getResources().getDimensionPixelSize(R.dimen.image_width);
                int targetHeight = getResources().getDimensionPixelSize(R.dimen.image_height);
                Bitmap resizedBitmap = Bitmap.createScaledBitmap(selectedBitmap, targetWidth, targetHeight, false);
                image.setImageBitmap(resizedBitmap);

                // 调用上传数据的方法
                uploadData();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void uploadData() {
        // 获取EditText中的昵称
        EditText editNickname = findViewById(R.id.editNickname);
        String nickname = editNickname.getText().toString();

        // 创建一个StringRequest
        StringRequest stringRequest =
                new StringRequest(
                        Request.Method.POST,
                        Constants.URL_UploadImg,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                // 在这里处理成功上传的响应
                                // 提示用户成功上传
                                Toast.makeText(SettingActivity.this, "成功上传", Toast.LENGTH_SHORT).show();
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // 在这里处理上传失败的情况
                                // 提示用户上传失败
                                Toast.makeText(SettingActivity.this, "上传失败：" + error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() {
                        // 将三个值作为参数传递
                        Map<String, String> params = new HashMap<>();
                        //params.put("uid", String.valueOf(uid));
                        params.put("userimage", base64EncodedImage);
                        params.put("nickname", nickname); // 使用EditText中的昵称

                        return params;
                    }
                };
        // 初始化 RequestQueue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        // 将StringRequest添加到RequestQueue中
        requestQueue.add(stringRequest);
    }


}
