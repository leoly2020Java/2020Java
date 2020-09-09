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
    Date publishTime;
    String content;
    String source;
    String newsURL;

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
    }

}
