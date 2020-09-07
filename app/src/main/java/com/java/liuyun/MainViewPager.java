package com.java.liuyun;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

public class MainViewPager extends ViewPager {

    private Context context;
    private AppCompatActivity activity;

    public MainViewPager(Context context) {
        super(context);
        this.context = context;
    }
    public MainViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public void setActivity(AppCompatActivity activity) {
        this.activity = activity;
    }

    //把点击事件下放到子View中
    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }

}
