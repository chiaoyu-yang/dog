package com.example.leaderboard;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivityRank();
            }
        });
    }

    public void openActivityRank() {
        TextView textViewNickname = findViewById(R.id.nickname);
        String nickname = textViewNickname.getText().toString().trim();

        Intent intent = new Intent(this, RankingActivity.class);
        intent.putExtra("nickname", nickname);
        startActivity(intent);
    }
}
