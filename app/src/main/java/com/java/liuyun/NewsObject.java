package com.java.liuyun;

import org.json.*;

import java.text.SimpleDateFormat;
import java.util.*;

public class NewsObject {
    String newsID;
    String type;
    String title;
    String content;
    String category;
    Date publishTime;
    String language;
    String source;
    String newsURL;
    HashMap<String, String> entityMap;
    ArrayList<String> relatedNewsID;

    public NewsObject()
    {
        super();
    }

    void parseJSON(JSONObject jsonData)
    {
        try{
            newsID = jsonData.getString("_id");
        }catch (Exception e){
            newsID = "";
        }

        try{
            type = jsonData.getString("type");
        }catch (Exception e){
            type = "";
        }

        try{
            title = jsonData.getString("title");
        }catch (Exception e){
            title = "";
        }

        try{
            content = jsonData.getString("content");
        }catch (Exception e){
            content = "";
        }

        try{
            category = jsonData.getString("category");
        }catch (Exception e){
            category = "";
        }

        try{
            String timeStr = jsonData.getString("publishTime");
            SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
            publishTime = ft.parse(timeStr);
        }catch (Exception e){
            publishTime = null;
        }

        try{
            language = jsonData.getString("language");
        }catch (Exception e){
            language = "zh-CN";
        }

        try{
            source = jsonData.getString("source");
        }catch (Exception e){
            source = "";
        }

        try{
            newsURL = jsonData.getString("url");
        }catch (Exception e){
            newsURL = "";
        }

        try{
            entityMap = new HashMap<String, String>();
            JSONArray entityArray = jsonData.getJSONArray("entities");
            for (int i = 0; i < entityArray.length(); i++)
            {
                JSONObject entityWithURL = entityArray.getJSONObject(i);
                entityMap.put(entityWithURL.getString("label"), entityWithURL.getString("url"));
            }
        }catch (Exception e) {
            entityMap = new HashMap<String, String>();
        }

        try{
            relatedNewsID = new ArrayList<String>();
            JSONArray relatedNewsArray = jsonData.getJSONArray("related_events");
            for (int i = 0; i < relatedNewsArray.length(); i++)
            {
                JSONObject relatedNewsWithScore = relatedNewsArray.getJSONObject(i);
                relatedNewsID.add(relatedNewsWithScore.getString("id"));
            }
        }catch (Exception e) {
            relatedNewsID = new ArrayList<String>();
        }
    }

}
