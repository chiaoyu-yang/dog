package com.example.soulgo;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import java.util.HashMap;
import java.util.Map;

public class SettingActivity extends AppCompatActivity {
    private EditText editNickname;

    private ImageButton paw;
    private ImageButton paw2;
    //new 2
    private static final String PREFS_NAME = "ImagePrefs";
    private static final String PREF_IMAGE_URI = "ImageUri";

    //new2
    private ImageView iv_pick_image;
    private ActivityResultLauncher<String> mGetContent;
    private SharedPreferences sharedPreferences;
    //new 3
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        paw = findViewById(R.id.to_home);
        paw2 = findViewById(R.id.logout);
        iv_pick_image = findViewById(R.id.iv_pick_image);

        Button button2 = findViewById(R.id.button2);
        // 初始化 SharedPreferences new3
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        String nickname = getIntent().getStringExtra("nickname");

        editNickname = findViewById(R.id.editNickname);
        editNickname.setText(nickname);


        // 恢复保存的图像 URI
        String savedUri = sharedPreferences.getString(PREF_IMAGE_URI, null);
        if (savedUri != null) {
            imageUri = Uri.parse(savedUri);
            iv_pick_image.setImageURI(imageUri);
        }

        paw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openHome();
            }
        });

        paw2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openHome2();
            }
        });

        iv_pick_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mGetContent.launch("image/*");
            }
        });

        mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {

                Intent intent = new Intent(SettingActivity.this,CropperActivity.class);
                intent.putExtra("DATA", result.toString());
                startActivityForResult(intent,101);

            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newNickname = editNickname.getText().toString();
                String oldNickname = getIntent().getStringExtra("nickname");

                postNicknameToBackend(oldNickname, newNickname);
            }
        });

    }

    private void postNicknameToBackend(String oldNickname, String newNickname) {
        StringRequest stringRequest =
                new StringRequest(
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



    //回到主畫面
    private void openHome() {
        Intent intent = new Intent(this,SecondActivity.class);
        startActivity(intent);
    }

    private void openHome2() {
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }


}