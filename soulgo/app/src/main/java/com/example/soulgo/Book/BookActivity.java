package com.example.soulgo.Book;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.soulgo.Constants;
import com.example.soulgo.HomeActivity;
import com.example.soulgo.R;
import com.example.soulgo.Setting.Beep;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class BookActivity extends AppCompatActivity {
    private RecyclerView recview;
    private List<BookModel> data;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);
        Objects.requireNonNull(getSupportActionBar()).hide();

        recview = findViewById(R.id.recview);
        recview.setLayoutManager(new GridLayoutManager(this, 3));

        Intent intent = getIntent();
        uid = intent.getStringExtra("uid");

        ImageButton to_home = findViewById(R.id.back);

        to_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openactivity();
                Beep.playBeepSound(getApplicationContext());
            }
        });

        processdata();
        setupSearch();

    }

    public void openactivity() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

    private void processdata() {
        TextView loadingText = findViewById(R.id.loadingText);
        loadingText.setVisibility(View.VISIBLE);

        StringRequest request =
                new StringRequest(
                        Constants.URL_Booklist,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                GsonBuilder builder = new GsonBuilder();
                                Gson gson = builder.create();
                                data = Arrays.asList(gson.fromJson(response, BookModel[].class));

                                // 检查是否有足够的数据
                                if (data.size() >= 3) {
                                    // 设置top1_text、top1_button的文本和图像
                                    ((TextView) findViewById(R.id.top1_text)).setText(data.get(0).getName());
                                    Glide.with(BookActivity.this)
                                            .load(
                                                    "http://140.131.114.145/Android/112_dog/beauty_top_season_history/" + data.get(0).getImage())
                                            .error(R.drawable.error_image)
                                            .into((ImageView) findViewById(R.id.top1_button));

                                    // 设置top2_text、top2_button的文本和图像
                                    ((TextView) findViewById(R.id.top2_text)).setText(data.get(1).getName());
                                    Glide.with(BookActivity.this)
                                            .load(
                                                    "http://140.131.114.145/Android/112_dog/beauty_top_season_history/" + data.get(1).getImage())
                                            .error(R.drawable.error_image)
                                            .into((ImageView) findViewById(R.id.top2_button));

                                    // 设置top3_text、top3_button的文本和图像
                                    ((TextView) findViewById(R.id.top3_text)).setText(data.get(2).getName());
                                    Glide.with(BookActivity.this)
                                            .load(
                                                    "http://140.131.114.145/Android/112_dog/beauty_top_season_history/" + data.get(2).getImage())
                                            .error(R.drawable.error_image)
                                            .into((ImageView) findViewById(R.id.top3_button));
                                }

                                // 设置RecyclerView的Adapter
                                BookAdapter adapter = new BookAdapter(data);
                                recview.setAdapter(adapter);

                                adapter.setItemClickListener(new BookAdapter.ItemClickListener() {
                                    @Override
                                    public void onItemClick(String bookId) {
                                        Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
                                        intent.putExtra("bookId", bookId);
                                        intent.putExtra("uid", uid);
                                        startActivity(intent);
                                    }
                                });

                                loadingText.setVisibility(View.GONE);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                                loadingText.setVisibility(View.GONE);
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
                Beep.playBeepSound(getApplicationContext());
            }
        });
    }


    private void performSearch(String query) {
        // 使用 Java 8 Stream API 尋找符合搜尋關鍵字的項目
        List<BookModel> searchResults = data.stream()
                .filter(book -> book.getName().contains(query) ||
                        book.getName().contains(((TextView) findViewById(R.id.top1_text)).getText()) ||
                        book.getName().contains(((TextView) findViewById(R.id.top2_text)).getText()) ||
                        book.getName().contains(((TextView) findViewById(R.id.top3_text)).getText()))
                .collect(Collectors.toList());

        // 將符合搜尋結果的項目傳遞給適配器
        BookAdapter adapter = new BookAdapter(searchResults);
        recview.setAdapter(adapter);
    }


}