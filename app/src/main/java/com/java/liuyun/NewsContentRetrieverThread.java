package com.java.liuyun;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class NewsContentRetrieverThread extends Thread {

    public void run()
    {

    }

    public void getNewsContent(NewsAbstractObject newsAbstractObject)
    {
        try{
            String newsID = newsAbstractObject.getNewsID();
            String urlHeader = "https://covid-dashboard.aminer.cn/api/event/";
            URL url = new URL(urlHeader + newsID);
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
            JSONObject jsonNews = json.getJSONObject("data");
            NewsObject newsObject = newsAbstractObject.getDetailNews();
            newsObject.parseJSON(jsonNews);
            newsObject.save();
        }catch(Exception ignored){}

    }
}
