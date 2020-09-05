package com.java.liuyun;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

public class MainViewPager extends ViewPager {

    private Context context;
    private AppCompatActivity activity;

    public MainViewPager(Context context) {
        super(context);
        this.context = context;
    }
    public MainViewPager(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.context = context;
    }
    public void setActivity(AppCompatActivity activity) {
        this.activity = activity;
    }
}
