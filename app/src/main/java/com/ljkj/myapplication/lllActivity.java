package com.ljkj.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

/**
 * 文件名：lllActivity
 * 作者：Turbo
 * 时间： 12/24/20 3:31 PM
 * 蚁穴虽小，溃之千里。
 */
public class lllActivity extends AppCompatActivity {
    //x轴坐标对应的数据
    //y轴坐标对应的数据
    final ArrayList<Float> value = new ArrayList<>();
    final ArrayList<String> mon = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_sdfsdfsdfsf);

        final HorizontalLineView indicatorView = (HorizontalLineView) findViewById(R.id.sl);
        for (int i = 0; i <= 7; i++) {
            value.add((float) ((int) (Math.random() * 1000)));
            mon.add(0, i + "");
        }
        final int[] flag = {12};
        indicatorView
                .value(value)
                .xvalue(mon)
//                .listener(new HorizontalLineView.OnSelectedChangedListener() {
//                    @Override
//                    public void onChanged(int position) {
//                        Log.v("3699", position + "");
//                        mSelectedTv.setText("" + value.get(position));
//                    }
//                })
                .setIOnScrollStateListener(new HorizontalLineView.IOnScrollStateListener() {


            @Override
            public void onScrolling(int firstposition) {

            }

            @Override
            public void onScrollStop(int firstposition) {

            }

            @Override
            public void onScrollbottom() {
                Toast.makeText(lllActivity.this, "加载更多", Toast.LENGTH_SHORT).show();
                for (int i = flag[0]; i < flag[0] + 2; i++) {
                    value.add(0, (float) ((int) (Math.random() * 1000)));
                    mon.add(0, i + "");
                }
                flag[0] += 3;
                indicatorView.value(value).xvalue(mon);
                Log.d("3699onScrollbottom", value.size() + "          " + flag[0]);
            }
        });

    }

}
