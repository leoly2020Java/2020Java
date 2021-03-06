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
import java.util.List;

public class NewsListUpdaterThread extends Thread{

    public List<NewsAbstractObject> getAddedNewsList() {
        return addedNewsList;
    }

    public void setAddedNewsList(List<NewsAbstractObject> addedNewsList) {
        this.addedNewsList = addedNewsList;
    }

    private List<NewsAbstractObject> addedNewsList;

    public void run()
    {
        int newsCount = LitePal.count(NewsAbstractObject.class);
        if (newsCount > 0)
        {
            getLatestNews();
        }
        else
        {
            getInitNews(200);
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
                addedNewsList.add(newsAbstractObject);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void getLatestNews()
    {
        try{
            Date currentLatest = LitePal.order("publishTime desc").findFirst(NewsAbstractObject.class).getPublishTime();
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
                        if (LitePal.where("newsID = ?", newsAbstractObject.getNewsID()).find(NewsAbstractObject.class) == null)
                        {
                            addedNewsList.add(newsAbstractObject);
                        }
                    }
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
