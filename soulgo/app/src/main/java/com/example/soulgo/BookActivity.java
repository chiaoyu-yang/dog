package com.example.soulgo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class BookActivity extends AppCompatActivity {
    private RecyclerView recview;
    private List<BookModel> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);

        recview = findViewById(R.id.recview);
        recview.setLayoutManager(new GridLayoutManager(this, 3));

        ImageButton to_home = findViewById(R.id.to_home);

        to_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openactivity();
            }
        });

        processdata();
        setupSearch();
    }

    public void openactivity() {
        Intent intent = new Intent(this, SecondActivity.class);
        startActivity(intent);
    }

    private void processdata() {
        StringRequest request = new StringRequest(Constants.URL_Booklist, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                GsonBuilder builder = new GsonBuilder();
                Gson gson = builder.create();
                data = Arrays.asList(gson.fromJson(response, BookModel[].class));

                BookAdapter adapter = new BookAdapter(data);
                recview.setAdapter(adapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        });

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(request);
    }

    private void setupSearch() {
        EditText searchbar = findViewById(R.id.searchbar);
        searchbar.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String query = textView.getText().toString();
                    performSearch(query);
                    return true;
                }
                return false;
            }
        });

        View searchButton = findViewById(R.id.searchbutton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String query = searchbar.getText().toString();
                performSearch(query);
            }
        });
    }

    private void performSearch(String query) {
        // 使用 Java 8 Stream API 尋找符合搜尋關鍵字的項目
        List<BookModel> searchResults = data.stream()
                .filter(book -> book.getName().contains(query))
                .collect(Collectors.toList());

        // 將符合搜尋結果的項目傳遞給適配器
        BookAdapter adapter = new BookAdapter(searchResults);
        recview.setAdapter(adapter);
    }
}
