package com.example.soulgo.Beauty;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;import java.util.Map;
import java.util.Objects;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.soulgo.Constants;
import com.example.soulgo.R;
import com.example.soulgo.Setting.Beep;
import com.github.dhaval2404.imagepicker.ImagePicker;

import org.json.JSONException;
import org.json.JSONObject;

public class VotePublish extends AppCompatActivity {

    private static final int REQUEST_IMAGE_PICKER = 1;
    private TextView textViewUsername, textViewCount;
    private EditText uploadTitleEditText;
    private String base64EncodedImage, nickname;
    private ImageButton btnUpload;
    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beauty_publish);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mediaPlayer = MediaPlayer.create(this, R.raw.beep);

        textViewUsername = findViewById(R.id.myusername);
        uploadTitleEditText = findViewById(R.id.upload_title);
        textViewCount = findViewById(R.id.textViewCount);
        btnUpload = findViewById(R.id.btnUpload);

        nickname = getIntent().getStringExtra("nickname");
        textViewUsername.setText(nickname);

        findViewById(R.id.to_home).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openHome();
                Beep.playBeepSound(getApplicationContext());
            }
        });

        findViewById(R.id.ic_outline_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {pickImage();}
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnUpload.setEnabled(false);
                uploadData();
                Beep.playBeepSound(getApplicationContext());
                btnUpload.setEnabled(true);
            }
        });

        uploadTitleEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                scrollToView(view);
            }
        });


        uploadTitleEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                updateTextCount(charSequence, uploadTitleEditText, 10, textViewCount);
            }

            @Override
            public void afterTextChanged(Editable editable) {}
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

    private void pickImage() {
        ImagePicker.with(this)
                .crop(315, 370)
                .compress(1024)
                .maxResultSize(1080, 1080)
                .start(REQUEST_IMAGE_PICKER);
    }

    private void scrollToView(View view) {
        ScrollView scrollView = findViewById(R.id.scrollView);
        scrollView.scrollTo(0, (int) view.getY());
    }

    private void updateTextCount(CharSequence charSequence, EditText editText, int maxLength, TextView textView) {
        int currentCount = charSequence.length();
        textView.setText(currentCount + "/" + maxLength);

        if (currentCount > maxLength) {
            editText.setText(charSequence.subSequence(0, maxLength));
            editText.setSelection(maxLength);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_PICKER && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            if (selectedImageUri != null) {
                try {
                    Bitmap selectedBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    selectedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                    byte[] imageBytes = byteArrayOutputStream.toByteArray();
                    base64EncodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);

                    ImageView clickToUploadImg = findViewById(R.id.clickToUploadImg);
                    clickToUploadImg.setImageURI(selectedImageUri);
                    findViewById(R.id.ic_outline_add_container).setVisibility(View.GONE);
                    findViewById(R.id.upload).setVisibility(View.GONE);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void uploadData() {
        ImageButton icOutlineAdd = findViewById(R.id.ic_outline_add);
        TextView uploadText = findViewById(R.id.upload);
        String title = uploadTitleEditText.getText().toString().trim();

        if (icOutlineAdd.getVisibility() == View.VISIBLE && uploadText.getVisibility() == View.VISIBLE) {
            Toast.makeText(this, "请选取一张图片", Toast.LENGTH_SHORT).show();
        }  else if (title.isEmpty()) {
            Toast.makeText(this, "請填寫標題", Toast.LENGTH_SHORT).show();
        }  else {
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            StringRequest stringRequest = new StringRequest(
                    Request.Method.POST,
                    Constants.URL_BEAUTY_PUBLISH,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("UploadResponse", response); // 添加这行输出
                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                boolean error = jsonResponse.getBoolean("error");
                                if (!error) {
                                    Toast.makeText(com.example.soulgo.Beauty.VotePublish.this, "成功上傳", Toast.LENGTH_SHORT).show();
                                    uploadTitleEditText.setText("");
                                    ImageView clickToUploadImg = findViewById(R.id.clickToUploadImg);
                                    clickToUploadImg.setImageResource(R.drawable.publish_1);
                                    findViewById(R.id.ic_outline_add_container).setVisibility(View.VISIBLE);
                                    findViewById(R.id.upload).setVisibility(View.VISIBLE);
                                } else {
                                    String errorMessage = jsonResponse.getString("message");
                                    Toast.makeText(com.example.soulgo.Beauty.VotePublish.this, "上傳失敗：" + errorMessage, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(com.example.soulgo.Beauty.VotePublish.this, "上傳失敗，請再試一次", Toast.LENGTH_SHORT).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            String message = error.getMessage();
                            if (error.networkResponse != null) {
                                message = "錯誤代碼：" + error.networkResponse.statusCode;
                            }
                            Log.e("UploadError", "上傳資料失敗：" + message); // 加入這行日誌以獲取額外的資訊

                            // 進一步詳細印出 Volley 錯誤信息
                            if (error instanceof NetworkError) {
                                Log.e("VolleyError", "NetworkError: " + error.getMessage());
                            } else if (error instanceof ServerError) {
                                Log.e("VolleyError", "ServerError: " + error.getMessage());
                            } else if (error instanceof AuthFailureError) {
                                Log.e("VolleyError", "AuthFailureError: " + error.getMessage());
                            } else if (error instanceof ParseError) {
                                Log.e("VolleyError", "ParseError: " + error.getMessage());
                            } else if (error instanceof NoConnectionError) {
                                Log.e("VolleyError", "NoConnectionError: " + error.getMessage());
                            } else if (error instanceof TimeoutError) {
                                Log.e("VolleyError", "TimeoutError: " + error.getMessage());
                            } else {
                                Log.e("VolleyError", "UnexpectedError: " + error.getMessage());
                            }

                            Toast.makeText(VotePublish.this, "網路不穩，請再試一次：", Toast.LENGTH_SHORT).show();
                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("uid", nickname);
                    params.put("img", base64EncodedImage);
                    params.put("title", title);
                    return params;
                }
            };
            // 設定超時時間為60秒
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    60000, // 設定超時時間為60秒
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            ));

            requestQueue.add(stringRequest);
        }
    }

    private void openHome() {
        Intent intent = new Intent(this, VoteActivity.class);
        startActivity(intent);
    }

    public void openactivity2() {
        Intent intent = new Intent(this, VotePublishDireation.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        super.onDestroy();
    }

}

