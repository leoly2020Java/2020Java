package com.java.liuyun;

import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class ScholarContentActivity extends AppCompatActivity {

    private TextView name;
    private TextView position;
    private TextView affiliation;
    private TextView bio;
    private TextView edu;

    private ExpandableListView expandableListView;
    private List<String> categoryNames, categorySizes;
    private List<List<String>> contentItems;
    private List<String> items, pri;

    public ScholarContentActivity() {
        categoryNames = new ArrayList<>();
        categorySizes = new ArrayList<>();
        contentItems = new ArrayList<>();
        categoryNames.add("Indices");
    }

    public void initView() {
        name = (TextView) findViewById(R.id.scholar_content_name);
        position = (TextView) findViewById(R.id.scholar_content_position);
        affiliation = (TextView) findViewById(R.id.scholar_content_affiliation);
        bio = (TextView) findViewById(R.id.scholar_content_bio);
        edu = (TextView) findViewById(R.id.scholar_content_edu);
        name.setText(getIntent().getStringExtra("Name"));
        position.setText(getIntent().getStringExtra("Position"));
        affiliation.setText(getIntent().getStringExtra("Affiliation"));
        bio.setText(getIntent().getStringExtra("Bio"));
        edu.setText(getIntent().getStringExtra("Edu"));
        pri = getIntent().getStringArrayListExtra("Indices");
        items = new ArrayList<>();
        items.add("activity: " + pri.get(0));
        items.add("citations: " + pri.get(1));
        items.add("diversity: " + pri.get(2));
        items.add("gindex: " + pri.get(3));
        items.add("hindex: " + pri.get(4));
        categorySizes.add(Integer.toString(items.size()));
        contentItems.add(items);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scholar_content);
        initView();
        expandableListView = (ExpandableListView) findViewById(R.id.scholar_content_expandable_list);
        expandableListView.setAdapter(new ScholarContentExpandableListAdapter(categoryNames, categorySizes, contentItems));
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
