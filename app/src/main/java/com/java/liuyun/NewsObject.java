package com.java.liuyun;

import org.json.*;
import org.litepal.crud.LitePalSupport;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class NewsObject extends LitePalSupport {

    String newsID;
    String type;
    String title;
    String content;
    Date publishTime;
    String source;
    String newsURL;
    HashMap<String, String> entityMap;
    ArrayList<String> relatedNewsID;

    public String getNewsID() {
        return newsID;
    }

    public void setNewsID(String newsID) {
        this.newsID = newsID;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(Date publishTime) {
        this.publishTime = publishTime;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getNewsURL() {
        return newsURL;
    }

    public void setNewsURL(String newsURL) {
        this.newsURL = newsURL;
    }

    public HashMap<String, String> getEntityMap() {
        return entityMap;
    }

    public void setEntityMap(HashMap<String, String> entityMap) {
        this.entityMap = entityMap;
    }

    public ArrayList<String> getRelatedNewsID() {
        return relatedNewsID;
    }

    public void setRelatedNewsID(ArrayList<String> relatedNewsID) {
        this.relatedNewsID = relatedNewsID;
    }

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
            String timeStr = jsonData.getString("date");
            LocalDateTime dt = LocalDateTime.parse(timeStr, DateTimeFormatter.RFC_1123_DATE_TIME);
            publishTime = Date.from(dt.atZone(ZoneId.systemDefault()).toInstant());
        }catch (Exception e){
            publishTime = null;
            e.printStackTrace();
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
