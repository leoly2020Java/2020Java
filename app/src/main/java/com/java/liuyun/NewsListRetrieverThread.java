package com.java.liuyun;

import android.os.Message;

import org.json.JSONArray;
import org.json.JSONObject;
import org.litepal.LitePal;
import org.litepal.crud.LitePalSupport;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

public class NewsListRetrieverThread extends Thread{

    public void getNews()
    {
        try{
            boolean moreNews = true;
            String urlHeader = "https://covid-dashboard.aminer.cn/api/events/list?page=";
            int page = 1;
            while(moreNews)
            {
                moreNews = false;
                URL url = new URL(urlHeader + page);
                page++;
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                StringBuilder stringBuffer = new StringBuilder();
                String line;
                while((line = bufferedReader.readLine()) != null)
                {
                    stringBuffer.append(line);
                }
                bufferedReader.close();
                inputStreamReader.close();
                inputStream.close();
                JSONObject json = new JSONObject(stringBuffer.toString());
                JSONArray jsonArray = json.getJSONArray("data");
                if (jsonArray.length() > 0)
                {
                    moreNews = true;
                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject jsonNews = jsonArray.getJSONObject(i);
                        NewsAbstractObject newsAbstractObject = new NewsAbstractObject();
                        newsAbstractObject.parseJSON(jsonNews);
                        newsAbstractObject.save();
                    }
                }
            }
        }catch(Exception ignored){}
    }

    public void getNews(Date currentLatest)
    {
        try{
            boolean moreNews = true;
            String urlHeader = "https://covid-dashboard.aminer.cn/api/events/list?page=";
            int page = 1;
            while(moreNews)
            {
                moreNews = false;
                URL url = new URL(urlHeader + page);
                page++;
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                StringBuilder stringBuffer = new StringBuilder();
                String line;
                while((line = bufferedReader.readLine()) != null)
                {
                    stringBuffer.append(line);
                }
                bufferedReader.close();
                inputStreamReader.close();
                inputStream.close();
                JSONObject json = new JSONObject(stringBuffer.toString());
                JSONArray jsonArray = json.getJSONArray("data");
                if (jsonArray.length() > 0)
                {
                    moreNews = true;
                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject jsonNews = jsonArray.getJSONObject(i);
                        NewsAbstractObject newsAbstractObject = new NewsAbstractObject();
                        newsAbstractObject.parseJSON(jsonNews);
                        if (newsAbstractObject.getPublishTime().after(currentLatest))
                        {
                            newsAbstractObject.save();
                        }
                    }
                }
            }
        }catch(Exception ignored){}
    }
}
