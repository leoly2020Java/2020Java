package com.java.liuyun;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView navigationView;
    private MainViewPager mainViewPager;
    private int[] fragmentId = {0, 1, 2}; //底部导航栏每个元素给一个编号

    public MainActivity() {
        super();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //处理主界面（一个MainViewPager）
        mainViewPager = (MainViewPager)findViewById(R.id.mainViewPager);
        mainViewPager.setActivity(this);
        //没有SetAdapter!!!
        mainViewPager.setCurrentItem(fragmentId[0]);

        //处理底部导航栏
        navigationView = findViewById(R.id.navView);
        navigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.navigation_home:
                                mainViewPager.setCurrentItem(fragmentId[0]);
                                Toast.makeText(getApplicationContext(), "000 "+fragmentId[0]+mainViewPager.getCurrentItem(), Toast.LENGTH_SHORT).show(); //Debug
                                break;
                            case R.id.navigation_virus_information:
                                mainViewPager.setCurrentItem(fragmentId[1]);
                                Toast.makeText(getApplicationContext(), "111 "+fragmentId[1]+mainViewPager.getCurrentItem(), Toast.LENGTH_SHORT).show(); //Debug
                                break;
                            case R.id.navigation_person:
                                mainViewPager.setCurrentItem(fragmentId[2]);
                                Toast.makeText(getApplicationContext(), "222 "+fragmentId[2]+mainViewPager.getCurrentItem(), Toast.LENGTH_SHORT).show(); //Debug
                                break;
                        }
                        return true;
                    }
                }
        );
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    //创建顶部菜单栏
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_menu, menu);
        return true;
    }

    //根据底部菜单栏的状态，设置顶部菜单栏每个按钮是否可见
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        int id = mainViewPager.getCurrentItem();
        Toast.makeText(getApplicationContext(), "menu "+id, Toast.LENGTH_SHORT).show(); //Debug
        switch (id) {
            case 0:
                menu.findItem(R.id.menu_search).setVisible(true);
                menu.findItem(R.id.menu_classification).setVisible(true);
                break;
            case 1:
                menu.findItem(R.id.menu_search).setVisible(false);
                menu.findItem(R.id.menu_classification).setVisible(false);
                break;
            case 2:
                menu.findItem(R.id.menu_search).setVisible(false);
                menu.findItem(R.id.menu_classification).setVisible(false);
                break;
            default: //异常状态
                menu.findItem(R.id.menu_search).setVisible(true);
                menu.findItem(R.id.menu_classification).setVisible(false);
                break;
        }
        return super.onPrepareOptionsMenu(menu);
    }

    //顶部菜单栏每个按钮对应的事件
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.menu_search:
                break;
            case R.id.menu_classification:
                break;
            default:
                break;
        }
        return true;
    }
}