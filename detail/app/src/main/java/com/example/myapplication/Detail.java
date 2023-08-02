package com.example.myapplication;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.example.soulgo.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class Detail extends AppCompatActivity {
        TextView name;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            name = findViewById(R.id.name);

        }

    private String url;
    final StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            try {
                JSONObject responseJson = new JSONObject(response);

                JSONArray myNicknameArray = responseJson.getJSONArray("myNickname");

                if (myNicknameArray.length() > 0) {
                    JSONObject nicknameObject = myNicknameArray.getJSONObject(0);
                    String Name = nicknameObject.getString("name");
//                    String uid = nicknameObject.getString("Uid");

                    name.setText(Name); // 將值設置到userName的TextView
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

}
