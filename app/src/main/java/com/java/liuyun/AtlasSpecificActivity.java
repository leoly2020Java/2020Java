package com.java.liuyun;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AtlasSpecificActivity extends AppCompatActivity {

    private TextView name;
    private TextView description;
    private ExpandableListView expandableListView;
    private List<String> categoryNames, categorySizes;
    private List<List<AtlasData>> altasItems; //改成AtlasData类（包含两个字符串）

    public AtlasSpecificActivity() {
        categoryNames = new ArrayList<>();
        categoryNames.add("关系");
        categoryNames.add("属性");
        categoryNames.add("相关图谱");
    }

    public void initView() {
        name = (TextView) findViewById(R.id.atlas_name);
        description = (TextView) findViewById(R.id.atlas_description);
        name.setText(getIntent().getStringExtra("Name"));
        description.setText(getIntent().getStringExtra("Description"));
        //接受传过来的4个list<String>，获取atlasItem的信息
        List<String> s1, s2, s3, s4, s5;
        s1 = getIntent().getStringArrayListExtra("RelationTitle");
        s2 = getIntent().getStringArrayListExtra("RelationDescripton");
        s3 = getIntent().getStringArrayListExtra("AttributeTitle");
        s4 = getIntent().getStringArrayListExtra("AttributeDescription");
        //获取相关图谱
        s5 = getIntent().getStringArrayListExtra("RelatedWord");
        categorySizes = new ArrayList<>();
        categorySizes.add(Integer.toString(s1.size()));
        categorySizes.add(Integer.toString(s3.size()));
        categorySizes.add(Integer.toString(s5.size()));
        List<AtlasData> data1 = new ArrayList<>(), data2 = new ArrayList<>(), data3 = new ArrayList<>();
        for (int i = 0; i < s1.size(); i++) data1.add(new AtlasData(s1.get(i), s2.get(i)));
        for (int i = 0; i < s3.size(); i++) data2.add(new AtlasData(s3.get(i), s4.get(i)));
        for (int i = 0; i < s5.size(); i++) data3.add(new AtlasData(s5.get(i), ""));
        altasItems = new ArrayList<>();
        altasItems.add(data1);
        altasItems.add(data2);
        altasItems.add(data3);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atlas_specific);
        initView();
        expandableListView = (ExpandableListView) findViewById(R.id.atlas_expandable_list);
        expandableListView.setAdapter(new AtlasExpandableListAdapter(categoryNames, categorySizes, altasItems));
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
                return true;
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}