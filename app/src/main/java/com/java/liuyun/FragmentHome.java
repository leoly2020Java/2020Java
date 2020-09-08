package com.java.liuyun;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class FragmentHome extends Fragment {

    public List<NewsAbstractObject> newsList;

    public List<String> categoryItemList;
    public String keyWord;

    public TwinklingRefreshLayout twinklingRefreshLayout;
    public RecyclerView recyclerView;

    public FragmentHome() {
        newsList = Collections.synchronizedList(new ArrayList<NewsAbstractObject>());
        categoryItemList = new ArrayList<>();
        categoryItemList.add("news");
        categoryItemList.add("paper");
        keyWord = "";
    }

    public void setCategoryItemList(List<String> categoryItemList) {
        this.categoryItemList = categoryItemList;
        refresh();
    }
    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
        refresh();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    public void refresh() {
        twinklingRefreshLayout.startRefresh();
    }

    /**
     categoryItemList是用户设定的分类标签列表（最多包含"news"和"paper"）
     keyWord是用户查询的关键字，例如“病毒”
     TODO:Adapter类用于把多个类包装成一个List，并处理点击这个List里面的元素发生的事件
     TODO:实现一个Adapter类，维护一个List<NewsObject>，Adapter类内部维护点击新闻对应的跳转事件，跳转至TextActivity类，跳转时传入新闻的标题、时间、来源、正文内容或URL，在TextActivity类里面画个xml显示这些内容
     TODO:完成本类的onCreateView函数，实现twinklingRefreshLayout的setOnRefreshListener函数（维护出上拉和下拉对应的事件）
     */

    public void flush() {
        int pageSize = 12;
        newsList.clear();

        NewsListUpdaterThread newsListUpdaterThread = new NewsListUpdaterThread();
        newsListUpdaterThread.start();
        try{
            newsListUpdaterThread.join();
            newsList.addAll(LitePal.where("title like ?", "%" + keyWord + "%")
                    .order("publishTime desc").limit(pageSize).find(NewsAbstractObject.class));
            //newsList.addAll(LitePal.where("title like ? and type in (?)", "%" + keyWord + "%", "'" + String.join("','", categoryItemList) + "'")
                    //.order("publishTime desc").limit(pageSize).find(NewsAbstractObject.class));
            newsList.sort(new SortByTimeDesc());
        }catch(Exception e){
            e.printStackTrace();
        }
        while (newsList.size() == 0)
        {
            NewsListRetrieverThread newsListRetrieverThread = new NewsListRetrieverThread();
            newsListRetrieverThread.start();
            try{
                newsListRetrieverThread.join();
                System.out.println("join retriever complete");
                newsList.addAll(LitePal.where("title like ?", "%" + keyWord + "%")
                        .order("publishTime desc").limit(pageSize).find(NewsAbstractObject.class));
                //newsList.addAll(LitePal.where("title like ? and type in (?)", "%" + keyWord + "%", "\"" + String.join("\",\"", categoryItemList) + "\"")
                        //.order("publishTime desc").limit(pageSize).find(NewsAbstractObject.class));
                newsList.sort(new SortByTimeDesc());
                System.out.println("second add to news list complete");
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        if (newsList.size() < pageSize)
        {
            long earliestInList = newsList.get(newsList.size() - 1).getPublishTime().getTime();
            NewsListRetrieverThread newsListRetrieverThread = new NewsListRetrieverThread();
            newsListRetrieverThread.start();
            try{
                newsListRetrieverThread.join();
                newsList.addAll(LitePal.where("publishTime < ? and title like ? and type in (?)", Long.toString(earliestInList), "%" + keyWord + "%", "\"" + String.join("\",\"", categoryItemList) + "\"")
                        .order("publishTime desc").limit(pageSize - newsList.size()).find(NewsAbstractObject.class));
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        Toast.makeText(getActivity(), "flush: newsList.size() = "+newsList.size(), Toast.LENGTH_SHORT).show(); //Debug

    }

    public void loadMore() {
        int pageSize = 12;


        long earliestInList = newsList.get(newsList.size() - 1).getPublishTime().getTime();
        newsList.addAll(LitePal.where("publishTime < ? and title like ? and type in (?)", Long.toString(earliestInList), "%" + keyWord + "%", "\"" + String.join("\",\"", categoryItemList) + "\"")
                .limit(pageSize).find(NewsAbstractObject.class));
        newsList.sort(new SortByTimeDesc());
        if (newsList.size() < pageSize)
        {
            NewsListRetrieverThread newsListRetrieverThread = new NewsListRetrieverThread();
            newsListRetrieverThread.start();
            try{
                newsListRetrieverThread.join();
                earliestInList = newsList.get(newsList.size() - 1).getPublishTime().getTime();
                newsList.addAll(LitePal.where("publishTime < ? and title like ? and type in (?)", Long.toString(earliestInList), "%" + keyWord + "%", "\"" + String.join("\",\"", categoryItemList) + "\"")
                        .order("publishTime desc").limit(pageSize - newsList.size()).find(NewsAbstractObject.class));
                newsList.sort(new SortByTimeDesc());
            }catch(Exception ignored){}
        }


        Toast.makeText(getActivity(), "loadMore: newsList.size() = "+newsList.size(), Toast.LENGTH_SHORT).show(); //Debug

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_home, container, false);
        twinklingRefreshLayout = (TwinklingRefreshLayout) view.findViewById(R.id.fragment_home_layout);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        LinearLayoutManager llm =new LinearLayoutManager((MainActivity)getActivity());
        llm.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(llm);
        recyclerView.addItemDecoration(new DividerItemDecoration((MainActivity)getActivity(),RecyclerView.VERTICAL));

        //TODO:adapter = new NewsRecyclerViewAdapter(getActivity(),newsList);
        //TODO:recyclerView.setAdapter(adapter);

        twinklingRefreshLayout.startRefresh();
        //实现setOnRefreshListener
        twinklingRefreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            //下拉刷新最新新闻
            @Override
            public void onRefresh(final TwinklingRefreshLayout twinklingRefreshLayout) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        flush(); //重新加载新闻
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {}
                        //TODO:adapter.notifyDataSetchanged();
                        twinklingRefreshLayout.finishRefreshing();
                    }
                }, 1000);
            }

            //上拉获取更多新闻
            @Override
            public void onLoadMore(final TwinklingRefreshLayout twinklingRefreshLayout) {
                Toast.makeText(getActivity(), "onLoadMore", Toast.LENGTH_SHORT).show(); //Debug
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadMore(); //获取更多新闻
                        try {
                            Thread.sleep(1000);
                        }catch (Exception e){}
                        //TODO:adapter.notifyDataSetChanged();
                        twinklingRefreshLayout.finishLoadmore();
                    }
                }, 1000);
            }
        });

        return view;
    }

}
