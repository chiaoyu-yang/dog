package com.example.soulgo.Setting;

import static com.example.soulgo.Constants.URL_setting_nickname;

import android.Manifest;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;import androidx.appcompat.app.AppCompatActivity;
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
import com.example.soulgo.MainActivity;
import com.example.soulgo.R;
import com.example.soulgo.Rank.RankDirectionsActivity;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;import java.util.Map;import java.util.Objects;

public class SettingActivity extends AppCompatActivity{

    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    ImageView imageView;
    String base64EncodedImage, nickname, imageUrl, Uid;
    EditText editNickname;

    AudioManager audioManager;
    SeekBar volumeSeekBar,gameSeekBar;

    ImageButton button;

    String[] permissions = new String[]{
            Manifest.permission.POST_NOTIFICATIONS
    };

    boolean permission_post_notification = false;
    boolean isUploadExecuted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        button = findViewById(R.id.button2);

        button.setOnClickListener(view -> {
            if(!permission_post_notification){
                requestPermissionNotification();
            } else {
                Toast.makeText(this, "已授予通知許可..", Toast.LENGTH_SHORT).show();
                setReminder();

            }
        });

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this, gso);

        imageView = findViewById(R.id.iv_pick_image);
        editNickname = findViewById(R.id.editNickname);

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefsFile", MODE_PRIVATE);
        nickname = sharedPreferences.getString("nickname", "");
        imageUrl = sharedPreferences.getString("imageUrl", "");
        Uid = sharedPreferences.getString("uid", "");
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

        ImageButton to_home = findViewById(R.id.back);
        ImageButton logout = findViewById(R.id.logout);

        editNickname.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                Rect r = new Rect();
                editNickname.getWindowVisibleDisplayFrame(r);

                int screenHeight = editNickname.getRootView().getHeight();

                int keypadHeight = screenHeight - r.bottom;

                // 如果鍵盤高度小於一定閾值，即鍵盤被收起
                if (keypadHeight < screenHeight * 0.15) {
                    // 確保上一次的 uploadNickname() 已經執行過，避免重複執行
                    if (!isUploadExecuted) {
                        String oldNickname = editNickname.getText().toString();
                        if (!oldNickname.equals(nickname)) {
                            uploadNickname();

                            // 在 uploadNickname() 调用之后隐藏光标
                            editNickname.clearFocus();
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(editNickname.getWindowToken(), 0);

                            isUploadExecuted = true; // 設置標誌為已執行
                        }
                    }
                } else {
                    // 鍵盤沒有被收起，重置標誌
                    isUploadExecuted = false;
                }

                return true;
            }
        });

        to_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openactivity();
                Beep.playBeepSound(getApplicationContext());
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getSharedPreferences("MyPrefsFile", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("nickname", "");
                editor.putString("uid", "");
                editor.putString("imageUrl", "");
                editor.apply();
                signOut(); // 呼叫登出方法
                Beep.playBeepSound(getApplicationContext());
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
                    }

                });
            }
        });


        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);

        volumeSeekBar = findViewById(R.id.bg_seekbar);
        gameSeekBar = findViewById(R.id.game_seekbar);

        // 設置音量SeekBar的最大值
        volumeSeekBar.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));

        // 設置游戏音效SeekBar的最大值
        gameSeekBar.setMax(100);

        // 載入保存的音量設置
        loadVolumeSettings();

        volumeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // 处理进度改变事件
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // 用户开始拖动SeekBar时触发的事件
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // 用户停止拖动SeekBar时触发的事件
                saveVolumeProgress(seekBar.getProgress(), "background_music_volume");
                float volume = (float) seekBar.getProgress() / seekBar.getMax();
                BackgroundMusicService.setVolume(volume);
            }
        });

        gameSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // 处理进度改变事件
                saveVolumeProgress(progress, "sound_effect_volume");
                float volume = (float) progress / seekBar.getMax();
                Beep.setVolume(volume);
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

        ImageButton button2 = findViewById(R.id.circle_help);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openactivity2();
                Beep.playBeepSound(getApplicationContext());
            }
        });

    }

    private void loadVolumeSettings() {
        SharedPreferences preferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);

        // 检查是否是首次启动应用，如果是，则使用默认值，否则使用保存的值
        boolean isFirstRun = preferences.getBoolean("is_first_run", true);

        // 如果是首次启动应用，则使用默认值为50
        int backgroundMusicVolume = isFirstRun ? 50 : preferences.getInt("background_music_volume", 50);
        int gameVolume = isFirstRun ? 50 : preferences.getInt("sound_effect_volume", 50);

        // 设置音量SeekBar的初始位置
        volumeSeekBar.setProgress(backgroundMusicVolume);
        gameSeekBar.setProgress(gameVolume);

        // 更新相应的音量
        float backgroundVolume = (float) backgroundMusicVolume / volumeSeekBar.getMax();
        BackgroundMusicService.setVolume(backgroundVolume);

        float gameEffectVolume = (float) gameVolume / gameSeekBar.getMax();
        Beep.setVolume(gameEffectVolume);

        // 如果是首次启动应用，将标志设置为false
        if (isFirstRun) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("is_first_run", false);
            editor.apply();
        }
    }

    private void saveVolumeProgress(int progress, String key) {
        SharedPreferences preferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(key, progress);
        editor.apply();
    }
    public void openactivity2() {
        Intent intent = new Intent(this, SettingDireationsActivity.class);
        startActivity(intent);
    }

    //~~~~~ step2
    public void requestPermissionNotification() {
        if (ContextCompat.checkSelfPermission(SettingActivity.this, permissions[0]) == PackageManager.PERMISSION_GRANTED) {
            permission_post_notification = true;
            setReminder();
        } else {
            // 請求權限
            requestPermissionLauncherNotification.launch(permissions[0]);
        }
    }

    //~~~~~ step3
    private ActivityResultLauncher<String> requestPermissionLauncherNotification =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    permission_post_notification = true;
                    setReminder();
                } else {
                    permission_post_notification = false;
                    showPermissionDialog("Notification Permission");
                }
            });

    //~~~~~ step4
    public void showPermissionDialog(String permission_desc) {
        new AlertDialog.Builder(SettingActivity.this)
                .setTitle("權限提醒")
                .setMessage(permission_desc)
                .setPositiveButton("允許權限", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        requestPermissionLauncherNotification.launch(permissions[0]);
                        dialogInterface.dismiss();
                    }
                })
                .setNegativeButton("退出", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .show();
    }

    //~~~~~ step5
    private void setReminder() {
        Toast.makeText(this, "提醒已開啟!", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(SettingActivity.this, ReminderBroadcast.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(SettingActivity.this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        Calendar currentTime = Calendar.getInstance();

        Calendar alarmTime = Calendar.getInstance();
        alarmTime.set(Calendar.HOUR_OF_DAY, 22);
        alarmTime.set(Calendar.MINUTE, 15);
        alarmTime.set(Calendar.SECOND, 0);

        if (currentTime.after(alarmTime)) {
            alarmTime.add(Calendar.DAY_OF_YEAR, 1); // 將提醒時間增加一天
        }

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarmTime.getTimeInMillis(), pendingIntent);
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
        uploadImage();

    }

    public void openactivity() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

    private void uploadImage() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.URL_setting_image,
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
                                String imagea = jsonResponse.getString("userimage");
                                // Toast.makeText(SettingActivity.this, message, Toast.LENGTH_SHORT).show();

                                SharedPreferences sharedPreferences = getSharedPreferences("MyPrefsFile", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("imageUrl", imagea);
                                editor.apply(); // 使用apply()方法保存更改，提高性能
                            } else {
                                // 操作失败，显示错误消息
                                Toast.makeText(SettingActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
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
                params.put("Uid", Uid); // 参数名与后端接口定义一致
                params.put("img", base64EncodedImage);
                return params;
            }
        };

        requestQueue.add(stringRequest);
    }

    private void uploadNickname() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String newNickname = editNickname.getText().toString();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_setting_nickname,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("UploadResponse", response);
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean error = jsonResponse.getBoolean("error");

                            if (!error) {
                                SharedPreferences sharedPreferences = getSharedPreferences("MyPrefsFile", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("nickname", newNickname);
                                editor.apply();
                                Toast.makeText(SettingActivity.this, "暱稱更新成功", Toast.LENGTH_SHORT).show();
                                // 可以在这里进行其他处理，例如更新 UI
                            } else {
                                Toast.makeText(SettingActivity.this, "暱稱重覆", Toast.LENGTH_SHORT).show();
                            }

                            // 在這裡處理回調邏輯
                            // 例如，更新 UI 或顯示訊息給使用者
                        } catch (JSONException e) {
                            // JSON 解析錯誤
                            e.printStackTrace();
                            Toast.makeText(SettingActivity.this, "暱稱重覆，請修改暱稱", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // 請求失敗
                        error.printStackTrace();
                        // 在這裡處理錯誤，例如顯示錯誤訊息給使用者
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                // 注意：Uid 的值應該在這裡被設定
                params.put("Uid", Uid);
                params.put("newNickname", newNickname);
                return params;
            }
        };

        requestQueue.add(stringRequest);
    }
}