package com.ljkj.myapplication.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.ljkj.myapplication.utils.ImmersionBarUtils;

/**
 * 文件名：BaseActivity
 * 作者：Turbo
 * 时间： 1/7/21 5:32 PM
 * 蚁穴虽小，溃之千里。
 */
public class BaseActivity extends FragmentActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //设置透明状态栏
        ImmersionBarUtils.initBaseBar(this);
    }
}
