package com.example.leaderboard;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MyrankActivity extends AppCompatActivity {

    private TextView mydivision, myusername, myintegral;

    private List<PersonalRank> myranklists;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mydivision = findViewById(R.id.mydivision);
        myusername = findViewById(R.id.myusername);
        myintegral = findViewById(R.id.myintegral);

        myranklists = new ArrayList<>();

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, constants.URL_MYRANK, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    JSONArray jsonArray = jsonObject.getJSONArray("myranklists");
                    // 循环遍历JSON数组中的每个对象，并将其添加到列表中
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject rankObject = jsonArray.getJSONObject(i);
                        String mydivision = rankObject.getString("division");
                        String myusername = rankObject.getString("username");
                        Integer myintegral = rankObject.getInt("integral");
                        myranklists.add(new PersonalRank(mydivision, myusername, myintegral));
                    }

                    mydivision.setText(myranklists.get(0).getMydivision());
                    myusername.setText(myranklists.get(0).getMyusername());
                    myintegral.setText(myranklists.get(0).getMyintegral());


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
    }
}
