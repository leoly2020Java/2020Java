package com.java.liuyun;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class VirusScholarActivity extends AppCompatActivity {

    private ExpandableListView expandableListView;
    private List<String> categoryNames, categorySizes;
    private List<List<AtlasData>> scholarItems;
    private List<AtlasData> item;

    public VirusScholarActivity() {
        categoryNames = new ArrayList<>();
        categoryNames.add("高关注学者");
        categoryNames.add("追忆学者");
        categorySizes = new ArrayList<>();
        scholarItems = new ArrayList<>();
    }

    public void initView() {

        //TODO:实现知疫学者列表，将以下数据补充完整
        // categorySize:高关注学者和追忆学者的数量
        // scholarItems:每个学者的姓名（必要）和简介（可适当写写）

        categorySizes.add("3");
        categorySizes.add("2");
        item = new ArrayList<>();
        item.add(new AtlasData("钟南山1", "描述1"));
        item.add(new AtlasData("钟南山2", "描述2"));
        item.add(new AtlasData("钟南山3", "描述3"));
        scholarItems.add(item);
        item = new ArrayList<>();
        item.add(new AtlasData("追忆1", "描述4"));
        item.add(new AtlasData("追忆2", "描述5"));
        scholarItems.add(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_virus_scholar);
        initView();
        expandableListView = (ExpandableListView) findViewById(R.id.scholar_expandable_list);
        expandableListView.setAdapter(new ScholarExpandableListAdapter(categoryNames, categorySizes, scholarItems));
        //设置分组的监听
        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return false;
            }
        });
        //设置子项布局监听
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                //TODO:根据子项学者名称，搜索对应的学者信息，用intent打开新的Activity显示这些内容
                String scholarName = scholarItems.get(groupPosition).get(childPosition).title; //学者名称：如“钟南山”
                Toast.makeText(getApplicationContext(), "Scholar name: "+scholarName, Toast.LENGTH_SHORT).show(); //Debug
                //TODO:查询学者相关信息，传入intent中，实现ScholarContentActivity类以及对应的activity_scholar_content.xml
                // 通过调用这个类，展示学者的详细信息
                //Intent intent = new Intent(VirusScholarActivity.this, ScholarContentActivity.class);
                //intent.putExtra(); //传一些信息
                //startActivity(intent);
                return true;
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
