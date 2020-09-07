package com.java.liuyun;

import org.json.JSONArray;
import org.json.JSONObject;
import org.litepal.LitePal;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

public class NewsListUpdaterThread extends Thread{

    public void run()
    {
        getLatestNews();
    }

    public void getLatestNews()
    {
        try{
            Date currentLatest = LitePal.findFirst(NewsAbstractObject.class).getPublishTime();
            int pageSize = 12;
            String urlHeader = "https://covid-dashboard.aminer.cn/api/events/list?size=" + pageSize + "&page=";
            int page = 1;
            boolean moreNews = true;
            while(moreNews)
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
                if (jsonArray.length() > 0)
                {
                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject jsonNews = jsonArray.getJSONObject(i);
                        NewsAbstractObject newsAbstractObject = new NewsAbstractObject();
                        newsAbstractObject.parseJSON(jsonNews);
                        if (newsAbstractObject.getPublishTime().before(currentLatest))
                        {
                            moreNews = false;
                            break;
                        }
                        if (LitePal.where("NewsID = " + newsAbstractObject.getNewsID()).find(NewsAbstractObject.class) == null)
                        {
                            NewsObject newsObject = new NewsObject();
                            newsAbstractObject.setDetailNews(newsObject);
                            newsObject.save();
                            newsAbstractObject.save();
                        }
                    }
                }
            }
        }catch(Exception ignored){}
    }
}
