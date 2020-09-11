package com.java.liuyun;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView navigationView;
    private MainViewPager mainViewPager;
    private int[] fragmentId = {0, 1, 2}; //底部导航栏每个元素给一个编号
    private FragmentHome fragmentHome;
    private FragmentVirusInformation fragmentVirusInformation;
    private FragmentPerson fragmentPerson;
    private List<Fragment> fragmentList;

    int SEARCH_RESULT_CODE = 1;
    int CATEGORY_RESULT_CODE = 2;

    //分类标签列表
    private List<String> categoryAddList, categoryDeleteList;

    public MainActivity() {
        super();
        /*
        System.out.println("get events begin");
        GetEvents getEvents = new GetEvents();
        getEvents.solve();
        System.out.println("get events end");
         */
        categoryAddList = new ArrayList<>();
        categoryDeleteList = new ArrayList<>();
        categoryAddList.add("news");
        categoryAddList.add("paper");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //加载底部三个Fragment
        fragmentHome = new FragmentHome();
        fragmentVirusInformation = new FragmentVirusInformation();
        fragmentPerson = new FragmentPerson();
        fragmentList = new ArrayList<>();
        fragmentList.add(fragmentHome);
        fragmentList.add(fragmentVirusInformation);
        fragmentList.add(fragmentPerson);
        FragmentManager fragmentManager= getSupportFragmentManager();
        MainFragmentPagerAdapter mainFragmentPagerAdapter = new MainFragmentPagerAdapter(fragmentList, fragmentManager);

        //处理主界面（一个MainViewPager）
        mainViewPager = (MainViewPager)findViewById(R.id.mainViewPager);
        mainViewPager.setActivity(this);
        mainViewPager.setAdapter(mainFragmentPagerAdapter);
        mainViewPager.setCurrentItem(fragmentId[0]);

        //处理底部导航栏
        final MainActivity mainActivity = this;
        navigationView = findViewById(R.id.navView);
        navigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.navigation_home:
                                mainViewPager.setCurrentItem(fragmentId[0]);
                                break;
                            case R.id.navigation_virus_information:
                                mainViewPager.setCurrentItem(fragmentId[1]);
                                break;
                            case R.id.navigation_person:
                                mainViewPager.setCurrentItem(fragmentId[2]);
                                break;
                        }
                        mainActivity.invalidateOptionsMenu(); //调用onPrepareOptionsMenu，实现动态修改上边栏
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
                intent = new Intent(MainActivity.this, SearchActivity.class);
                startActivityForResult(intent, SEARCH_RESULT_CODE);
                break;
            case R.id.menu_classification:
                intent = new Intent(MainActivity.this, CategoryActivity.class);
                intent.putExtra("AddList", (Serializable) categoryAddList);
                intent.putExtra("DeleteList", (Serializable) categoryDeleteList);
                startActivityForResult(intent, CATEGORY_RESULT_CODE);
                break;
            default:
                break;
        }
        return true;
    }

    //处理其它Activity（搜索，修改分类列表）的返回结果
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (data == null) return;
        if (requestCode == SEARCH_RESULT_CODE) {
            String keyWord = data.getStringExtra("KeyWord");
            fragmentHome.setKeyWord(keyWord);
        }
        if (requestCode == CATEGORY_RESULT_CODE) {
            categoryAddList = data.getStringArrayListExtra("AddList");
            categoryDeleteList = data.getStringArrayListExtra("DeleteList");
            fragmentHome.setCategoryItemList(categoryAddList);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}