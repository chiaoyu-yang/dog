package com.example.soulgo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
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
import android.view.View;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class PublishActivity extends AppCompatActivity {

    private TextView textViewUsername;
    private ImageButton back;
    private EditText uploadTitleEditText, uploadContentEditText;
    private TextView textViewCount, textViewOverlay;
    private String base64EncodedImage,nickname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish);

        textViewUsername = findViewById(R.id.myusername);
        ImageView imageView = findViewById(R.id.ic_outline_add);
        ImageButton button = findViewById(R.id.btnUpload);
        uploadTitleEditText = findViewById(R.id.upload_title);
        uploadContentEditText = findViewById(R.id.upload_content_rec);
        ScrollView scrollView = findViewById(R.id.scrollView);
        textViewCount = findViewById(R.id.textViewCount);
        textViewOverlay = findViewById(R.id.textViewOverlay);

        nickname = getIntent().getStringExtra("nickname");
        textViewUsername.setText(nickname);

        back = findViewById(R.id.to_home);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openHome();
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });

        uploadTitleEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    scrollView.scrollTo(0, (int) view.getY());
                }
            }
        });

        uploadContentEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    scrollView.scrollTo(0, (int) view.getY());
                }
            }
        });

        uploadTitleEditText.addTextChangedListener(
            new TextWatcher() {
              @Override
              public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

              @Override
              public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // 更新文字數量
                int currentCount = charSequence.length();
                textViewCount.setText(currentCount + "/10");

                // 限制文字數量不超過10個字
                if (currentCount > 10) {
                  uploadTitleEditText.setText(charSequence.subSequence(0, 10));
                  uploadTitleEditText.setSelection(10); // 移動光標到末尾
                }
              }

              @Override
              public void afterTextChanged(Editable editable) {}
            });

        uploadContentEditText.addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        // 更新文字數量
                        int currentCount = charSequence.length();
                        textViewOverlay.setText(currentCount + "/50");

                        // 限制文字數量不超過10個字
                        if (currentCount > 50) {
                            uploadContentEditText.setText(charSequence.subSequence(0, 50));
                            uploadContentEditText.setSelection(50); // 移動光標到末尾
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {}
                });
    }

    private void openHome() {
        Intent intent = new Intent(this, SecondActivity.class);
        startActivity(intent);
    }

    private static final int GALLERY_REQUEST_CODE = 123;

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
                ImageView imageView = findViewById(R.id.clickToUploadImg);
                imageView.setImageBitmap(resizedBitmap);
                findViewById(R.id.ic_outline_add_container).setVisibility(View.GONE);
                findViewById(R.id.upload).setVisibility(View.GONE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public void uploadData(View view) {
        // 检查是否ic_outline_add和upload仍然可见
        ImageButton icOutlineAdd = findViewById(R.id.ic_outline_add);
        TextView uploadText = findViewById(R.id.upload);

        // 取得使用者輸入的標題和內文
        String title = uploadTitleEditText.getText().toString().trim();
        String content = uploadContentEditText.getText().toString().trim();

        if (icOutlineAdd.getVisibility() == View.VISIBLE && uploadText.getVisibility() == View.VISIBLE) {
            // ic_outline_add和upload仍然可见，显示提示
            Toast.makeText(this, "请选取一张图片", Toast.LENGTH_SHORT).show();
            return;
        }else if (title.isEmpty() && content.isEmpty()) {
            // 提示用戶填寫標題和內容
            Toast.makeText(this, "請填寫標題和內容", Toast.LENGTH_SHORT).show();
        } else if (title.isEmpty()) {
            // 提示用戶填寫標題
            Toast.makeText(this, "請填寫標題", Toast.LENGTH_SHORT).show();
        } else if (content.isEmpty()) {
            // 提示用戶填寫內容
            Toast.makeText(this, "請填寫內容", Toast.LENGTH_SHORT).show();
        } else {
            RequestQueue requestQueue = Volley.newRequestQueue(this);


          // 创建一个StringRequest
          StringRequest stringRequest =
              new StringRequest(
                  Request.Method.POST,
                      Constants.URL_publish,
                  new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                      // 在这里处理成功上传的响应
                      // 提示用戶成功上傳
                      Toast.makeText(PublishActivity.this, "成功上傳", Toast.LENGTH_SHORT).show();

                      // 清空輸入的標題和內容
                      uploadTitleEditText.setText("");
                      uploadContentEditText.setText("");

                      // 恢复图片和按钮的可见性状态
                      ImageView clickToUploadImg = findViewById(R.id.clickToUploadImg);
                      clickToUploadImg.setImageResource(R.drawable.publish_1);
                      findViewById(R.id.ic_outline_add_container).setVisibility(View.VISIBLE);
                      findViewById(R.id.upload).setVisibility(View.VISIBLE);
                    }
                  },
                  new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                      // 在这里处理上传失败的情况
                      // 提示用户上传失败
                      Toast.makeText(PublishActivity.this, "上传失败", Toast.LENGTH_SHORT).show();
                    }
                  }) {
                @Override
                protected Map<String, String> getParams() {
                  // 将四个值作为参数传递
                  Map<String, String> params = new HashMap<>();
                  params.put("uid", nickname);
                  params.put("img", base64EncodedImage);
                  params.put("title", title);
                  params.put("content", content);

                  return params;
                }
              };

            // 将StringRequest添加到RequestQueue中
            requestQueue.add(stringRequest);
        }
    }



}
