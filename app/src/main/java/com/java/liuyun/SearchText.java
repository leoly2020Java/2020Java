package com.java.liuyun;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.appcompat.widget.AppCompatEditText;

public class SearchText extends AppCompatEditText {

    //搜索图标，清空图标
    private Drawable searchIcon, clearIcon;

    //初始化搜索文本框
    public void init() {
        searchIcon = getContext().getDrawable(R.drawable.search);
        clearIcon = getContext().getDrawable(R.drawable.clear);
        //在搜索框左侧设置搜索图标
        setCompoundDrawablesWithIntrinsicBounds(searchIcon, null, null, null);
    }
    public SearchText(Context context) {
        super(context);
        init();
    }
    public SearchText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    public SearchText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    //确定是否显示清空图标：输入内容变化或焦点变化时应当处理这个问题
    public void setClearIconVisible(boolean flag) {
        setCompoundDrawablesWithIntrinsicBounds(searchIcon, null, flag ? clearIcon : null, null);
    }
    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        //获得焦点且文本存在时显示，否则不显示
        setClearIconVisible(hasFocus() && text.length() > 0);
    }
    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        //获得焦点且文本存在时显示，否则不显示
        setClearIconVisible(focused && length() > 0);
    }

    //当用户点击“清空图标”的区域时，清空搜索框，根据用户手指抬起的位置判定
    public boolean in(Drawable drawable, MotionEvent event) {
        if (drawable == null) return false;
        int Right = getWidth() - getPaddingRight();
        int Left = Right - drawable.getBounds().width();
        return Left <= event.getX() && event.getX() <= Right;
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                if (in(clearIcon, event)) setText("");
                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }

}