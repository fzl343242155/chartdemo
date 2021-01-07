package com.ljkj.myapplication;

import android.app.Application;

import me.jessyan.autosize.AutoSizeConfig;
import me.jessyan.autosize.unit.Subunits;

/**
 * 文件名：SampleApplication
 * 作者：Turbo
 * 时间： 1/7/21 5:20 PM
 * 蚁穴虽小，溃之千里。
 */
public class SampleApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // 初始化适配
        AutoSizeConfig.getInstance().setExcludeFontScale(false).getUnitsManager()
                .setSupportDP(false)
                .setSupportSP(false)
                .setSupportSubunits(Subunits.MM);
    }
}
