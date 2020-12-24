package com.ljkj.myapplication;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainNewActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    //    private BrokenLineView mBrokenLineView;
    MyLineChartView chartView;
    List<String> xValues;   //x轴数据集合
    List<Integer> yValues;  //y轴数据集合

    private List<String> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideStatusNavigationBar(this);
        setContentView(R.layout.activity_main_new);
        list = new ArrayList<>();
        for (int i = 0; i < 23; i++) {
            list.add(i + "");
        }

        float bronkenData[] = new float[24];
        for (int i = 0; i < bronkenData.length; i++) {
            bronkenData[i] = (float) (Math.random() * 121 + 60);
        }

//        mBrokenLineView = findViewById(R.id.blv_content);
//        mBrokenLineView.setTextY(0, 200, 20, "");
//        mBrokenLineView.setTextX(list);
//        mBrokenLineView.setBrokenData(bronkenData);


        chartView = (MyLineChartView) findViewById(R.id.lcv_content);
        xValues = new ArrayList<>();
        yValues = new ArrayList<>();


        for (int i = 0; i < 23; i++) {
            xValues.add(i + "");
        }


        for (int i = 0; i < 10; i++) {
            yValues.add(i * 20);
        }


        // xy轴集合自己添加数据
        chartView.setXValues(xValues);
        chartView.setYValues(yValues);

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
