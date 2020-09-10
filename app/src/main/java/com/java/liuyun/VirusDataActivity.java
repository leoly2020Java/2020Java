package com.java.liuyun;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.ListAdapter;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;

import org.json.*;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class VirusDataActivity extends AppCompatActivity {

    BarChart worldChart;
    BarChart chinaChart;
    Description description;

    List<String> worldNames = new ArrayList<>(); //国家名称
    List<Integer> worldAmounts = new ArrayList<>(); //感染人数
    List<Integer> worldDeaths = new ArrayList<>(); //死亡人数
    List<String> chinaNames = new ArrayList<>(); //中国省份名称
    List<Integer> chinaAmounts = new ArrayList<>(); //中国省份感染人数
    List<Integer> chinaDeaths = new ArrayList<>(); //中国省份死亡人数

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_virus_data);
        worldChart = findViewById(R.id.world_chart);
        chinaChart = findViewById(R.id.china_chart);

        VirusDataThread virusDataThread = new VirusDataThread();
        virusDataThread.setWorldNames(worldNames);
        virusDataThread.setWorldAmounts(worldAmounts);
        virusDataThread.setWorldDeaths(worldDeaths);
        virusDataThread.setChinaNames(chinaNames);
        virusDataThread.setChinaAmounts(chinaAmounts);
        virusDataThread.setChinaDeaths(chinaDeaths);
        virusDataThread.start();
        try {
            virusDataThread.join();
        } catch (Exception e) {
            e.printStackTrace();
        }

        initWorldChart();
        initChinaChart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void initWorldChart() {
        worldChart.setDrawValueAboveBar(true);  //设置所有的数值在图形的上面,而不是图形上
        worldChart.setTouchEnabled(true);  //进制触控
        worldChart.setScaleEnabled(true); //设置能否缩放
        worldChart.setPinchZoom(true);  //设置true支持两个指头向X、Y轴的缩放，如果为false，只能支持X或者Y轴的当方向缩放
        worldChart.setDrawBarShadow(false);  //设置阴影
        worldChart.setDrawGridBackground(false);  //设置背景是否网格显示
        description = worldChart.getDescription();
        description.setText("");
        worldChart.setDescription(description); //不描述

        //X轴的数据格式
        XAxis xAxis = worldChart.getXAxis();
        xAxis.setValueFormatter(new ChartDataFormatter(worldNames));
        //设置位置
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //设置是否绘制网格线
        xAxis.setDrawGridLines(false);
        worldChart.getAxisLeft().setDrawGridLines(false);
        // barChart.animateY(2500);
        //设置X轴文字居中对齐
        xAxis.setCenterAxisLabels(false);
        //X轴最小间距
        xAxis.setGranularity(1f);

        //Y轴的数据格式
        YAxis axisLeft = worldChart.getAxisLeft();
        //axisLeft.setValueFormatter(new MyFormatter2());
        worldChart.animateY(2500);
        //设置Y轴刻度的最大值
        axisLeft.setAxisMinValue(0);
        axisLeft.setAxisMaxValue(7000000);
        worldChart.getAxisRight().setEnabled(false);

        //设置数据
        setWorldData(worldAmounts, worldDeaths);
    }
    //在这里获取疫情数据
    private void setWorldData(List<Integer> amounts, List<Integer> deaths) {
        ArrayList<BarEntry> yVals1 = new ArrayList<>();
        ArrayList<BarEntry> yVals2 = new ArrayList<>();

        for (int i = 0; i < amounts.size(); i++) yVals1.add(new BarEntry(i + 1, amounts.get(i)));
        for (int i = 0; i < amounts.size(); i++) yVals2.add(new BarEntry(i + 1, deaths.get(i)));

        BarDataSet set1, set2;
        set1 = new BarDataSet(yVals1, "累计确诊人数");
        set2 = new BarDataSet(yVals2, "累计死亡人数");
        //设置多彩 也可以单一颜色
        set1.setColor(Color.parseColor("#4169E1"));
        set1.setDrawValues(false);
        set2.setColor(Color.parseColor("#666666"));
        set2.setDrawValues(false);
        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);
        dataSets.add(set2);
        BarData data = new BarData(dataSets);

        worldChart.setData(data);
        worldChart.setFitBars(true);

        //设置文字的大小
        set1.setValueTextSize(10f);
        worldChart.invalidate();
        //设置每条柱子的宽度
        data.setBarWidth(0.5f);

        for (IDataSet set : worldChart.getData().getDataSets())
            set.setDrawValues(!set.isDrawValuesEnabled());
        worldChart.invalidate();
        worldChart.setAutoScaleMinMaxEnabled(!worldChart.isAutoScaleMinMaxEnabled());
        worldChart.notifyDataSetChanged();
        worldChart.invalidate();

    }

    public void initChinaChart() {
        chinaChart.setDrawValueAboveBar(true);  //设置所有的数值在图形的上面,而不是图形上
        chinaChart.setTouchEnabled(true);  //进制触控
        chinaChart.setScaleEnabled(true); //设置能否缩放
        chinaChart.setPinchZoom(true);  //设置true支持两个指头向X、Y轴的缩放，如果为false，只能支持X或者Y轴的当方向缩放
        chinaChart.setDrawBarShadow(false);  //设置阴影
        chinaChart.setDrawGridBackground(false);  //设置背景是否网格显示
        description = chinaChart.getDescription();
        description.setText("");
        worldChart.setDescription(description); //不描述

        //X轴的数据格式
        XAxis xAxis = chinaChart.getXAxis();
        xAxis.setValueFormatter(new ChartDataFormatter(chinaNames));
        //设置位置
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //设置是否绘制网格线
        xAxis.setDrawGridLines(false);
        chinaChart.getAxisLeft().setDrawGridLines(false);
        // barChart.animateY(2500);
        //设置X轴文字居中对齐
        xAxis.setCenterAxisLabels(false);
        //X轴最小间距
        xAxis.setGranularity(1f);

        //Y轴的数据格式
        YAxis axisLeft = chinaChart.getAxisLeft();
        //axisLeft.setValueFormatter(new MyFormatter2());
        chinaChart.animateY(2500);
        //设置Y轴刻度的最大值
        axisLeft.setAxisMinValue(0);
        axisLeft.setAxisMaxValue(100000);
        chinaChart.getAxisRight().setEnabled(false);

        //设置数据
        setChinaData(chinaAmounts, chinaDeaths);
    }
    //在这里获取疫情数据
    private void setChinaData(List<Integer> amounts, List<Integer> deaths) {
        ArrayList<BarEntry> yVals1 = new ArrayList<>();
        ArrayList<BarEntry> yVals2 = new ArrayList<>();
        for (int i = 0; i < amounts.size(); i++) yVals1.add(new BarEntry(i + 1, amounts.get(i)));
        for (int i = 0; i < deaths.size(); i++) yVals2.add(new BarEntry(i + 1, deaths.get(i)));

        BarDataSet set1, set2;
        set1 = new BarDataSet(yVals1, "累计确诊人数");
        set2 = new BarDataSet(yVals2, "累计死亡人数");
        //设置多彩 也可以单一颜色
        set1.setColor(Color.parseColor("#4169E1"));
        set1.setDrawValues(false);
        set2.setColor(Color.parseColor("#666666"));
        set2.setDrawValues(false);
        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);
        dataSets.add(set2);
        BarData data = new BarData(dataSets);
        chinaChart.setData(data);
        chinaChart.setFitBars(true);
        //设置文字的大小
        set1.setValueTextSize(10f);
        //设置每条柱子的宽度
        data.setBarWidth(0.5f);
        chinaChart.invalidate();

        for (IDataSet set : chinaChart.getData().getDataSets())
            set.setDrawValues(!set.isDrawValuesEnabled());
        chinaChart.invalidate();
        chinaChart.setAutoScaleMinMaxEnabled(!chinaChart.isAutoScaleMinMaxEnabled());
        chinaChart.notifyDataSetChanged();
        chinaChart.invalidate();

    }

}



class VirusDataThread extends Thread{

    public void setWorldNames(List<String> worldNames) {
        this.worldNames = worldNames;
    }

    public void setWorldAmounts(List<Integer> worldAmounts) {
        this.worldAmounts = worldAmounts;
    }

    public void setWorldDeaths(List<Integer> worldDeaths) {
        this.worldDeaths = worldDeaths;
    }

    public void setChinaNames(List<String> chinaNames) {
        this.chinaNames = chinaNames;
    }

    public void setChinaAmounts(List<Integer> chinaAmounts) {
        this.chinaAmounts = chinaAmounts;
    }

    public void setChinaDeaths(List<Integer> chinaDeaths) {
        this.chinaDeaths = chinaDeaths;
    }

    List<String> worldNames;
    List<Integer> worldAmounts;
    List<Integer> worldDeaths;
    List<String> chinaNames;
    List<Integer> chinaAmounts;
    List<Integer> chinaDeaths;
    List<Triple> worldInfo;
    List<Triple> chinaInfo;

    public void run()
    {
        worldNames.clear();
        worldAmounts.clear();
        worldDeaths.clear();
        chinaNames.clear();
        chinaAmounts.clear();
        chinaDeaths.clear();
        worldInfo = new ArrayList<>();
        chinaInfo = new ArrayList<>();
        try {
            URL url = new URL("https://covid-dashboard.aminer.cn/api/dist/epidemic.json");
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder stringBuffer = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line);
            }
            bufferedReader.close();
            inputStreamReader.close();
            inputStream.close();
            JSONObject json = new JSONObject(stringBuffer.toString());
            for (Iterator<String> it = json.keys(); it.hasNext(); ) {
                String location = it.next();
                JSONArray virusData = json.getJSONObject(location).getJSONArray("data");
                JSONArray latestVirusData = virusData.getJSONArray(virusData.length() - 1);
                if (!location.contains("|")) {
                    Triple triple = new Triple();
                    triple.location = location;
                    triple.amount = latestVirusData.getInt(0);
                    triple.death = latestVirusData.getInt(3);
                    worldInfo.add(triple);
                }
                else if (location.startsWith("China|") & (location.length() <= 6 | !location.substring(6).contains("|"))) {
                    Triple triple = new Triple();
                    triple.location = location.substring(6);
                    triple.amount = latestVirusData.getInt(0);
                    triple.death = latestVirusData.getInt(3);
                    chinaInfo.add(triple);
                }
            }
            chinaInfo.sort(new Comparator<Triple>() {
                @Override
                public int compare(Triple t1, Triple t2) {
                    return t2.amount - t1.amount;
                }
            });
            worldInfo.sort(new Comparator<Triple>() {
                @Override
                public int compare(Triple t1, Triple t2) {
                    return t2.amount - t1.amount;
                }
            });
            worldInfo.remove(0);
            for (int i = 0; i < 10; i++)
            {
                worldNames.add(worldInfo.get(i).location);
                worldAmounts.add(worldInfo.get(i).amount);
                worldDeaths.add(worldInfo.get(i).death);
                chinaNames.add(worldInfo.get(i).location);
                chinaAmounts.add(worldInfo.get(i).amount);
                chinaDeaths.add(worldInfo.get(i).death);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class Triple
{
    public String location;
    public int amount;
    public int death;
}