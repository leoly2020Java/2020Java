package com.java.liuyun;

import android.content.Intent;
import android.os.Bundle;
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
import java.util.Iterator;
import java.util.List;

public class VirusAtlasActivity extends AppCompatActivity {

    List<String> entityInfo = new ArrayList<>();
    List<String> relationTitle = new ArrayList<>();
    List<String> relationDescription = new ArrayList<>();
    List<Boolean> relationDirection = new ArrayList<>();
    List<String> attributeTitle = new ArrayList<>();
    List<String> attributeDescription = new ArrayList<>();
    List<String> relatedEntities = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_virus_atlas);
        SearchView searchView = (SearchView) findViewById(R.id.search_view_atlas);

        searchView.ClickSearch(new SearchCallBack() {
            @Override
            public void SearchAction(String keyWord) {
                //打开新的Activity，显示疫情图谱
                Toast.makeText(getApplicationContext(), "Altas search: "+keyWord, Toast.LENGTH_SHORT).show(); //Debug

                VirusAtlasThread virusAtlasThread = new VirusAtlasThread();
                virusAtlasThread.setKeyWord(keyWord);
                virusAtlasThread.setEntityInfo(entityInfo);
                virusAtlasThread.setRelationTitle(relationTitle);
                virusAtlasThread.setRelationDescription(relationDescription);
                virusAtlasThread.setRelationDirection(relationDirection);
                virusAtlasThread.setAttributeTitle(attributeTitle);
                virusAtlasThread.setAttributeDescription(attributeDescription);
                virusAtlasThread.setRelatedEntities(relatedEntities);
                virusAtlasThread.start();
                try {
                    virusAtlasThread.join();
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
                
                Intent intent = new Intent(VirusAtlasActivity.this, AtlasSpecificActivity.class);

                intent.putExtra("Name", entityInfo.get(0));
                intent.putExtra("Description", entityInfo.get(1));
                intent.putExtra("RelationTitle", (Serializable) relationTitle);
                intent.putExtra("RelationDescripton", (Serializable) relationDescription);
                intent.putExtra("AttributeTitle", (Serializable) attributeTitle);
                intent.putExtra("AttributeDescription", (Serializable) attributeDescription);
                intent.putExtra("RelatedWord", (Serializable) relatedEntities);
                
                startActivity(intent);
            }
        });

        searchView.ClickBack(new BackCallBack() {
            @Override
            public void BackAction() {
                finish();
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}

class VirusAtlasThread extends Thread{
    List<String> entityInfo;
    List<String> relationTitle;
    List<String> relationDescription;
    List<Boolean> relationDirection;
    List<String> attributeTitle;
    List<String> attributeDescription;
    String keyWord;
    List<String> relatedEntities;

    public void setEntityInfo(List<String> entityInfo) {
        this.entityInfo = entityInfo;
    }

    public void setRelationTitle(List<String> relationTitle) {
        this.relationTitle = relationTitle;
    }

    public void setRelationDescription(List<String> relationDescription) {
        this.relationDescription = relationDescription;
    }

    public void setRelationDirection(List<Boolean> relationDirection) {
        this.relationDirection = relationDirection;
    }

    public void setAttributeTitle(List<String> attributeTitle) {
        this.attributeTitle = attributeTitle;
    }

    public void setAttributeDescription(List<String> attributeDescription) {
        this.attributeDescription = attributeDescription;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    public void setRelatedEntities(List<String> relatedEntities) {
        this.relatedEntities = relatedEntities;
    }

    public void run()
    {
        relationTitle.clear();
        relationDescription.clear();
        relationDirection.clear();
        attributeTitle.clear();
        attributeDescription.clear();
        relatedEntities.clear();
        try{
            URL url = new URL("https://innovaapi.aminer.cn/covid/api/v1/pneumonia/entityquery?entity=" + keyWord);
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
            JSONArray jsonData = json.getJSONArray("data");
            for (int i = 1; i < jsonData.length(); i++)
            {
                relatedEntities.add(jsonData.getJSONObject(i).getString("label"));
            }
            JSONObject jsonEntity = jsonData.getJSONObject(0);
            entityInfo.add(jsonEntity.getString("label"));
            JSONObject jsonInfo = jsonEntity.getJSONObject("abstractInfo");
            entityInfo.add(jsonInfo.getString("enwiki"));
            if (entityInfo.get(1).equals(""))
            {
                entityInfo.remove(1);
                entityInfo.add(jsonInfo.getString("baidu"));
            }
            if (entityInfo.get(1).equals(""))
            {
                entityInfo.remove(1);
                entityInfo.add(jsonInfo.getString("zhwiki"));
            }
            JSONObject jsonCOVID = jsonInfo.getJSONObject("COVID");
            JSONObject jsonProperties = jsonCOVID.getJSONObject("properties");
            for (Iterator<String> it = jsonProperties.keys(); it.hasNext(); ) {
                String item = it.next();
                attributeTitle.add(item);
                attributeDescription.add(jsonProperties.getString(item));
            }
            JSONArray jsonRelations = jsonCOVID.getJSONArray("relations");
            for (int i = 0; i < jsonRelations.length(); i++)
            {
                relationTitle.add(jsonRelations.getJSONObject(i).getString("label"));
                relationDescription.add(jsonRelations.getJSONObject(i).getString("relation"));
                relationDirection.add(jsonRelations.getJSONObject(i).getBoolean("forward"));
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }
}