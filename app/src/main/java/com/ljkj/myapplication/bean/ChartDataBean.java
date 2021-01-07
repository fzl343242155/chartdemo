package com.ljkj.myapplication.bean;

/**
 * 文件名：ChartDataBean
 * 作者：Turbo
 * 时间： 1/2/21 7:20 PM
 * 蚁穴虽小，溃之千里。
 */
public class ChartDataBean {

    private int value;
    private  boolean fromUserTouch ;

    public boolean isFromUserTouch() {
        return fromUserTouch;
    }

    public void setFromUserTouch(boolean fromUserTouch) {
        this.fromUserTouch = fromUserTouch;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
