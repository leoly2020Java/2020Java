package com.java.liuyun;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;
import java.util.List;

public class CategoryActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private CategoryGridView addGridView, deleteGridView;
    private List<String> addList, deleteList;
    private CategoryAdapter addAdapter, deleteAdapter;
    private Button button;

    public void initView() {
        addList = getIntent().getStringArrayListExtra("AddList");
        addGridView = (CategoryGridView) findViewById(R.id.addition_grid_view);
        addAdapter = new CategoryAdapter(this, addList, true);
        addGridView.setAdapter(addAdapter);
        addGridView.setOnItemClickListener(this);

        deleteList = getIntent().getStringArrayListExtra("DeleteList");
        deleteGridView = (CategoryGridView) findViewById(R.id.deletion_grid_view);
        deleteAdapter = new CategoryAdapter(this, deleteList, false);
        deleteGridView.setAdapter(deleteAdapter);
        deleteGridView.setOnItemClickListener(this);
    }
    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_category);
        initView();
        button = (Button) findViewById(R.id.category_confirm);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("AddList", (Serializable) addList);
                intent.putExtra("DeleteList", (Serializable) deleteList);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    //动画
    private void Animation(View view, int[] startLocation, int[] endLocation, final String moveChannel, final GridView clickGridView, final boolean isAddition) {
        //获取动画起始坐标信息
        int[] initLocation = new int[2];
        view.getLocationInWindow(initLocation);
        final ViewGroup moveViewGroup = getMoveViewGroup();
        final View moveView = getMoveView(moveViewGroup, view, initLocation);
        //创建并配置动画
        TranslateAnimation moveAnimation = new TranslateAnimation(startLocation[0], endLocation[0], startLocation[1], endLocation[1]);
        moveAnimation.setDuration(200L);
        AnimationSet moveAnimationSet = new AnimationSet(true);
        moveAnimationSet.setFillAfter(false);
        moveAnimationSet.addAnimation(moveAnimation);
        moveView.startAnimation(moveAnimationSet);

        moveAnimationSet.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                moveViewGroup.removeView(moveView);
                if (isAddition) {
                    deleteAdapter.setIsVisible(true);
                    deleteAdapter.notifyDataSetChanged();
                    addAdapter.deleteItem();
                } else {
                    addAdapter.setIsVisible(true);
                    addAdapter.notifyDataSetChanged();
                    deleteAdapter.deleteItem();
                }
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int pos, long id) {
        final ImageView moveImageView = getView(view);
        switch (parent.getId()) {
            case R.id.addition_grid_view:
                if (moveImageView != null) {
                    TextView newTextView = (TextView) view.findViewById(R.id.category_item);
                    final int[] startLocation = new int[2];
                    newTextView.getLocationInWindow(startLocation);
                    final String text = ((CategoryAdapter) parent.getAdapter()).getItem(pos);
                    deleteAdapter.setIsVisible(false);
                    deleteAdapter.addItem(text);
                    //开一个子线程运行动画
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            try {
                                int[] endLocation = new int[2];
                                deleteGridView.getChildAt(deleteGridView.getLastVisiblePosition()).getLocationInWindow(endLocation);
                                Animation(moveImageView, startLocation, endLocation, text, addGridView, true);
                                addAdapter.setDelete(pos);
                            } catch (Exception localException) {}
                        }
                    }, 100L);
                }

                break;
            case R.id.deletion_grid_view:
                if (moveImageView != null) {
                    TextView newTextView = (TextView) view.findViewById(R.id.category_item);
                    final int[] startLocation = new int[2];
                    newTextView.getLocationInWindow(startLocation);
                    final String text = ((CategoryAdapter) parent.getAdapter()).getItem(pos);
                    addAdapter.setIsVisible(false);
                    addAdapter.addItem(text);
                    //开一个子线程运行动画
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            try {
                                int[] endLocation = new int[2];
                                addGridView.getChildAt(addGridView.getLastVisiblePosition()).getLocationInWindow(endLocation);
                                Animation(moveImageView, startLocation, endLocation, text, deleteGridView,false);
                                deleteAdapter.setDelete(pos);
                            } catch (Exception localException) {}
                        }
                    }, 100L);
                }
                break;
            default:
                break;
        }
    }

    private ImageView getView(View view) {
        view.destroyDrawingCache();
        view.setDrawingCacheEnabled(true);
        Bitmap cache = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);
        ImageView imageView = new ImageView(this);
        imageView.setImageBitmap(cache);
        return imageView;
    }

    private View getMoveView(ViewGroup viewGroup, View view, int[] initLocation) {
        int x = initLocation[0];
        int y = initLocation[1];
        viewGroup.addView(view);
        LinearLayout.LayoutParams mLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mLayoutParams.leftMargin = x;
        mLayoutParams.topMargin = y;
        view.setLayoutParams(mLayoutParams);
        return view;
    }

    private ViewGroup getMoveViewGroup() {
        ViewGroup moveViewGroup = (ViewGroup) getWindow().getDecorView();
        LinearLayout moveLinearLayout = new LinearLayout(this);
        moveLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        moveViewGroup.addView(moveLinearLayout);
        return moveLinearLayout;
    }

}
