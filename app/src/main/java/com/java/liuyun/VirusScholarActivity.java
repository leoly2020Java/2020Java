package com.java.liuyun;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class VirusScholarActivity extends AppCompatActivity {

    private ExpandableListView expandableListView;
    private List<String> categoryNames, categorySizes;
    private List<ScholarData> scholarInfoListAlive;
    private List<ScholarData> scholarInfoListPassed;
    private List<List<ScholarData>> scholarItems;

    public VirusScholarActivity() {
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
                ScholarData scholarData = scholarItems.get(groupPosition).get(childPosition); //学者名称：如“钟南山”

                Intent intent = new Intent(VirusScholarActivity.this, ScholarContentActivity.class);
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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}

class VirusScholarThread extends Thread{
    List<ScholarData> scholarInfoListAlive;
    List<ScholarData> scholarInfoListPassed;

    public void setScholarInfoListAlive(List<ScholarData> scholarInfoListAlive) {
        this.scholarInfoListAlive = scholarInfoListAlive;
    }

    public void setScholarInfoListPassed(List<ScholarData> scholarInfoListPassed) {
        this.scholarInfoListPassed = scholarInfoListPassed;
    }

    public void run()
    {
        scholarInfoListAlive.clear();
        scholarInfoListPassed.clear();
        try {
            URL url = new URL("https://innovaapi.aminer.cn/predictor/api/v1/valhalla/highlight/get_ncov_expers_list?v=2");
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder stringBuffer = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line);
            }
            bufferedReader.close();
            inputStreamReader.close();
            inputStream.close();
            JSONObject json = new JSONObject(stringBuffer.toString());
            JSONArray jsonArray = json.getJSONArray("data");
            for (int i = 0; i < jsonArray.length() - 1; i++)
            {
                ScholarData scholarInfo = new ScholarData();
                JSONObject jsonScholar = jsonArray.getJSONObject(i);
                JSONObject jsonProfile = jsonScholar.getJSONObject("profile");
                String zhAttempt = jsonScholar.getString("name_zh");
                if (zhAttempt.equals(""))
                {
                    zhAttempt = jsonScholar.getString("name");
                }
                scholarInfo.setName(zhAttempt);
                try{
                    scholarInfo.setPosition(jsonProfile.getString("position"));
                }catch (Exception e) {
                    scholarInfo.setPosition("");
                }
                try{
                    scholarInfo.setAffiliation(jsonProfile.getString("affiliation"));
                }catch (Exception e) {
                    scholarInfo.setAffiliation("");
                }
                try{
                    scholarInfo.setBio(jsonProfile.getString("bio"));
                }catch (Exception e) {
                    scholarInfo.setBio("");
                }

                try{
                    scholarInfo.setEdu(jsonProfile.getString("edu"));
                }catch (Exception e) {
                    scholarInfo.setEdu("");
                }

                JSONObject jsonIndice = jsonScholar.getJSONObject("indices");
                List<Double> indices = new ArrayList<>();
                indices.add(jsonIndice.getDouble("activity"));
                indices.add(jsonIndice.getDouble("citations"));
                indices.add(jsonIndice.getDouble("diversity"));
                indices.add(jsonIndice.getDouble("gindex"));
                indices.add(jsonIndice.getDouble("hindex"));
                scholarInfo.setIndices(indices);

                if (jsonScholar.getBoolean("is_passedaway"))
                {
                    scholarInfoListPassed.add(scholarInfo);
                }
                else
                {
                    scholarInfoListAlive.add(scholarInfo);
                }
            }
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}

class ScholarData{
    String name;
    String position;
    String affiliation;
    String bio;
    String edu;
    List<Double> indices;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getAffiliation() {
        return affiliation;
    }

    public void setAffiliation(String affiliation) {
        this.affiliation = affiliation;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getEdu() {
        return edu;
    }

    public void setEdu(String edu) {
        this.edu = edu;
    }

    public List<Double> getIndices() {
        return indices;
    }

    public void setIndices(List<Double> indices) {
        this.indices = indices;
    }
}
