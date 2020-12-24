package com.ljkj.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    //x轴坐标对应的数据
    private List<String> xValue = new ArrayList<>();

    private List<String> rvValue = new ArrayList<>();
    //y轴坐标对应的数据
    private List<Integer> yValue = new ArrayList<>();
    //折线对应的数据
    private Map<String, Integer> value = new HashMap<>();
    private ChartView chartView;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideStatusNavigationBar(this);
        setContentView(R.layout.activity_main);
        for (int i = 0; i < 24; i++) {
            xValue.add(i + "");
            value.put(i + "", (int) (Math.random() * 121 + 60));//60--240
        }

        for (int i = 0; i < 10; i++) {
            yValue.add(i * 20);
        }

        chartView = (ChartView) findViewById(R.id.chartview);
        chartView.setValue(value, xValue, yValue);

        recyclerView = findViewById(R.id.rl_content);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);

        for (int i = 0; i < 24; i++) {
            rvValue.add(i + "");
        }

        ChartAdapter chartAdapter = new ChartAdapter(rvValue);
        recyclerView.setAdapter(chartAdapter);

        chartView.setScrollListener(new OnScrollListener() {

            @Override
            public void onScroll(float position) {
                recyclerView.scrollBy((~(int)position)/20, 0);
            }
        });
    }

    public static void hideStatusNavigationBar(Activity activity) {
        if (Build.VERSION.SDK_INT < 16) {
            activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            int uiFlags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY; //hide navigationBar
            activity.getWindow().getDecorView().setSystemUiVisibility(uiFlags);
        }
    }

}
