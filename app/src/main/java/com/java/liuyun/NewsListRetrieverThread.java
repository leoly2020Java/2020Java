package com.java.liuyun;

import org.json.JSONArray;
import org.json.JSONObject;
import org.litepal.LitePal;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class NewsListRetrieverThread extends Thread{

    private List<String> categoryItemList;

    public List<NewsAbstractObject> getAddedNewsList() {
        return addedNewsList;
    }

    public void setAddedNewsList(List<NewsAbstractObject> addedNewsList) {
        this.addedNewsList = addedNewsList;
    }

    private List<NewsAbstractObject> addedNewsList;

    public void setCategoryItemList(List<String> categoryItemList) {
        this.categoryItemList = categoryItemList;
    }

    public void run()
    {
        int newsCount = LitePal.count(NewsAbstractObject.class);
        if (newsCount > 0)
        {
            getMoreNews(200);
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

    public void getMoreNews(int moreAmount)
    {
        try{
            int pageSize = 200;
            String urlHeader = "https://covid-dashboard.aminer.cn/api/events/list?size=" + pageSize + "&";
            if (categoryItemList.size() == 1)
            {
                urlHeader += "type=" + categoryItemList.get(0) + "&";
            }
            urlHeader += "page=";
            int page = 1;
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
                    if (LitePal.where("newsID = ?", newsAbstractObject.getNewsID()).find(NewsAbstractObject.class).size() == 0)
                    {
                        addedNewsList.add(newsAbstractObject);
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
