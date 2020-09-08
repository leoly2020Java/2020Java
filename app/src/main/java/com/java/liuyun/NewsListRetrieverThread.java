package com.java.liuyun;

import org.json.JSONArray;
import org.json.JSONObject;
import org.litepal.LitePal;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class NewsListRetrieverThread extends Thread{

    public void run()
    {
        int newsCount = LitePal.count(NewsAbstractObject.class);
        if (newsCount > 0)
        {
            getMoreNews(300);
        }
        else
        {
            getInitNews(300);
        }
    }

    public void getInitNews(int moreAmount)
    {
        try{
            String urlHeader = "https://covid-dashboard.aminer.cn/api/events/list?size=" + moreAmount + "&page=1";
            URL url = new URL(urlHeader);
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
            for (int i = 0; i < jsonArray.length(); i++)
            {
                JSONObject jsonNews = jsonArray.getJSONObject(i);
                NewsAbstractObject newsAbstractObject = new NewsAbstractObject();
                newsAbstractObject.parseJSON(jsonNews);
                NewsObject newsObject = new NewsObject();
                newsAbstractObject.setDetailNews(newsObject);
                newsObject.save();
                newsAbstractObject.save();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void getMoreNews(int moreAmount)
    {
        try{
            int newsCount = LitePal.count(NewsAbstractObject.class);
            int pageSize = 20;
            String urlHeader = "https://covid-dashboard.aminer.cn/api/events/list?size=" + pageSize + "&page=";
            int page = (newsCount + 1) / pageSize;
            while(moreAmount != 0)
            {
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
                for (int i = 0; i < jsonArray.length(); i++)
                {
                    JSONObject jsonNews = jsonArray.getJSONObject(i);
                    NewsAbstractObject newsAbstractObject = new NewsAbstractObject();
                    newsAbstractObject.parseJSON(jsonNews);
                    if (LitePal.where("newsID = ?", newsAbstractObject.getNewsID()).find(NewsAbstractObject.class) == null)
                    {
                        NewsObject newsObject = new NewsObject();
                        newsAbstractObject.setDetailNews(newsObject);
                        newsObject.save();
                        newsAbstractObject.save();
                        moreAmount--;
                        if (moreAmount == 0)
                        {
                            break;
                        }
                    }
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
