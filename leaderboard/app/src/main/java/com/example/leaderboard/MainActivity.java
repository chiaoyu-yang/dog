package com.example.leaderboard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;

import java.util.List;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 在活動中找到 RecyclerView 元素
        RecyclerView recyclerView = findViewById(R.id.recycler_view);

        // 設置 RecyclerView 的佈局管理器
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // 創建包含資料的 List
        List<Ranklist> ranklists = new ArrayList<>();  // 假設您已經從 rankList.php 獲取到 Rank 物件的列表

        // 創建 RecyclerView 的適配器並設置資料
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(ranklists);
        recyclerView.setAdapter(adapter);
    }
}
