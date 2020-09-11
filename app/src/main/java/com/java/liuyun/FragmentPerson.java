package com.java.liuyun;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FragmentPerson extends Fragment {

    /*
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_person, container, false);
        return view;
    }
     */

    private ExpandableListView expandableListView;
    private List<String> categoryNames, categorySizes;
    private List<ScholarData> scholarInfoListAlive;
    private List<ScholarData> scholarInfoListPassed;
    private List<List<ScholarData>> scholarItems;

    public FragmentPerson() {
        categoryNames = new ArrayList<>();
        categoryNames.add("高关注学者");
        categoryNames.add("追忆学者");
        categorySizes = new ArrayList<>();
        scholarInfoListAlive = new ArrayList<>();
        scholarInfoListPassed = new ArrayList<>();
        scholarItems = new ArrayList<>();
    }

    public void initView() {

        VirusScholarThread virusScholarThread = new VirusScholarThread();
        virusScholarThread.setScholarInfoListAlive(scholarInfoListAlive);
        virusScholarThread.setScholarInfoListPassed(scholarInfoListPassed);
        virusScholarThread.start();
        try {
            virusScholarThread.join();
            categorySizes.add(String.valueOf(scholarInfoListAlive.size()));
            categorySizes.add(String.valueOf(scholarInfoListPassed.size()));
            scholarItems.add(scholarInfoListAlive);
            scholarItems.add(scholarInfoListPassed);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_virus_scholar, container, false);
        initView();
        expandableListView = (ExpandableListView) view.findViewById(R.id.scholar_expandable_list);
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
                ScholarData scholarData = scholarItems.get(groupPosition).get(childPosition); //学者名称：如“钟南山”

                Intent intent = new Intent(getActivity(), ScholarContentActivity.class);
                intent.putExtra("Name", scholarData.name);
                intent.putExtra("Position", scholarData.position);
                intent.putExtra("Affiliation", scholarData.affiliation);
                intent.putExtra("Bio", scholarData.bio);
                intent.putExtra("Edu", scholarData.edu);
                List<String> scholarDataString = new ArrayList<>();
                for(int i = 0; i < scholarData.indices.size(); i++) scholarDataString.add(scholarData.indices.get(i).toString());
                intent.putExtra("Indices", (Serializable) scholarDataString);

                startActivity(intent);
                return true;
            }
        });

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
