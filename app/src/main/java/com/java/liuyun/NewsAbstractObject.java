package com.java.liuyun;

import org.json.*;
import org.litepal.crud.LitePalSupport;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class NewsAbstractObject extends LitePalSupport {

    String newsID;
    String type;
    String title;
    Date publishTime;
    NewsObject detailNews;

    public NewsObject getDetailNews() {
        return detailNews;
    }

    public void setDetailNews(NewsObject detailNews) {
        this.detailNews = detailNews;
    }

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

    public Date getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(Date publishTime) {
        this.publishTime = publishTime;
    }

    public NewsAbstractObject()
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
            String timeStr = jsonData.getString("date");
            LocalDateTime dt = LocalDateTime.parse(timeStr, DateTimeFormatter.RFC_1123_DATE_TIME);
            publishTime = Date.from(dt.atZone(ZoneId.systemDefault()).toInstant());
        }catch (Exception e){
            publishTime = null;
            e.printStackTrace();
        }

    }

}

class SortByTimeDesc implements Comparator<NewsAbstractObject>
{
    public int compare(NewsAbstractObject a, NewsAbstractObject b)
    {
        if (a.getPublishTime().before(b.getPublishTime()))
        {
            return 1;
        }
        else if (a.getPublishTime().after(b.getPublishTime()))
        {
            return -1;
        }
        else
        {
            return 0;
        }
    }
}