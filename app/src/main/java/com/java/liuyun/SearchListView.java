package com.java.liuyun;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class SearchListView extends ListView {

    public SearchListView(Context context) {
        super(context);
    }
    public SearchListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public SearchListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    //将高度限制设置为无穷大，以适配ScrollView
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
