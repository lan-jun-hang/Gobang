package com.example.gobang;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.gobang.chessview.MyView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn_start;
    private MyView chessView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_start = findViewById(R.id.bt_start);
        chessView = findViewById(R.id.custom_chess_main);

        btn_start.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_start:
                chessView.Restart();
        }
    }
}