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

    String atlasName;
    String atlasDescription;
    List<String> relationTitle;
    List<String> relationDescription;
    List<Boolean> relationDirection;
    List<String> attributeTitle;
    List<String> attributeDescription;
    List<String> relatedEntities;

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
                virusAtlasThread.setAtlasName(atlasName);
                virusAtlasThread.setAtlasDescription(atlasDescription);
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

                intent.putExtra("Name", atlasName);
                intent.putExtra("Description", atlasDescription);
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
    String atlasName;
    String atlasDescription;
    List<String> relationTitle;
    List<String> relationDescription;
    List<Boolean> relationDirection;
    List<String> attributeTitle;
    List<String> attributeDescription;
    String keyWord;
    List<String> relatedEntities;

    public void setAtlasName(String atlasName) {
        this.atlasName = atlasName;
    }

    public void setAtlasDescription(String atlasDescription) {
        this.atlasDescription = atlasDescription;
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
            atlasName = jsonEntity.getString("label");
            JSONObject jsonInfo = jsonEntity.getJSONObject("abstractInfo");
            atlasDescription = jsonInfo.getString("enwiki");
            if (atlasDescription.equals(""))
            {
                atlasDescription = jsonInfo.getString("baidu");
            }
            if (atlasDescription.equals(""))
            {
                atlasDescription = jsonInfo.getString("zhwiki");
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