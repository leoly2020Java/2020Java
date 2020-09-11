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
    public NewsRecyclerViewAdapter adapter;

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

    public void flush() {
        int pageSize = 12;
        newsList.clear();

        List<NewsAbstractObject> addedNewsList = new ArrayList<>();
        NewsListUpdaterThread newsListUpdaterThread = new NewsListUpdaterThread();
        newsListUpdaterThread.setAddedNewsList(addedNewsList);
        newsListUpdaterThread.start();
        try{
            newsListUpdaterThread.join();
            LitePal.saveAll(addedNewsList);
            if (categoryItemList.size() == 1)
            {
                newsList.addAll(LitePal.where("title like ? and type like ?", "%" + keyWord + "%", categoryItemList.get(0))
                        .order("publishTime desc").limit(pageSize).find(NewsAbstractObject.class));
            }
            else
            {
                newsList.addAll(LitePal.where("title like ?", "%" + keyWord + "%")
                        .order("publishTime desc").limit(pageSize).find(NewsAbstractObject.class));
            }
            newsList.sort(new SortByTimeDesc());
        }catch(Exception ignored){}
        if (newsList.size() == 0)
        {
            NewsListRetrieverThread newsListRetrieverThread = new NewsListRetrieverThread();
            addedNewsList = new ArrayList<>();
            newsListRetrieverThread.setAddedNewsList(addedNewsList);
            newsListRetrieverThread.setCategoryItemList(categoryItemList);
            newsListRetrieverThread.start();
            try{
                newsListRetrieverThread.join();
                LitePal.saveAll(addedNewsList);
                if (categoryItemList.size() == 1)
                {
                    newsList.addAll(LitePal.where("title like ? and type like ?", "%" + keyWord + "%", categoryItemList.get(0))
                            .order("publishTime desc").limit(pageSize).find(NewsAbstractObject.class));
                }
                else
                {
                    newsList.addAll(LitePal.where("title like ?", "%" + keyWord + "%")
                            .order("publishTime desc").limit(pageSize).find(NewsAbstractObject.class));
                }
                newsList.sort(new SortByTimeDesc());
            }catch(Exception ignored){}
        }

    }

    public void loadMore() {
        int pageSize = 12;
        int origSize = newsList.size();
        if (origSize == 0)
        {
            flush();
            return;
        }

        long earliestInList = newsList.get(newsList.size() - 1).getPublishTime().getTime();
        if (categoryItemList.size() == 1)
        {
            newsList.addAll(LitePal.where("publishTime < ? and title like ? and type like ?", Long.toString(earliestInList), "%" + keyWord + "%", categoryItemList.get(0))
                    .order("publishTime desc").limit(pageSize).find(NewsAbstractObject.class));
        }
        else
        {
            newsList.addAll(LitePal.where("publishTime < ? and title like ?", Long.toString(earliestInList), "%" + keyWord + "%")
                    .order("publishTime desc").limit(pageSize).find(NewsAbstractObject.class));
        }
        newsList.sort(new SortByTimeDesc());
        if (newsList.size() < origSize + pageSize)
        {
            NewsListRetrieverThread newsListRetrieverThread = new NewsListRetrieverThread();
            List<NewsAbstractObject> addedNewsList = new ArrayList<>();
            newsListRetrieverThread.setAddedNewsList(addedNewsList);
            newsListRetrieverThread.start();
            try{
                newsListRetrieverThread.join();
                LitePal.saveAll(addedNewsList);
                earliestInList = newsList.get(newsList.size() - 1).getPublishTime().getTime();
                if (categoryItemList.size() == 1)
                {
                    newsList.addAll(LitePal.where("publishTime < ? and title like ? and type like ?", Long.toString(earliestInList), "%" + keyWord + "%", categoryItemList.get(0))
                            .order("publishTime desc").limit(pageSize).find(NewsAbstractObject.class));
                }
                else
                {
                    newsList.addAll(LitePal.where("publishTime < ? and title like ?", Long.toString(earliestInList), "%" + keyWord + "%")
                            .order("publishTime desc").limit(pageSize).find(NewsAbstractObject.class));
                }
                newsList.sort(new SortByTimeDesc());
            }catch(Exception ignored){}
        }

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

        adapter = new NewsRecyclerViewAdapter(getActivity(),newsList);
        recyclerView.setAdapter(adapter);

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
                        adapter.notifyDataSetChanged();
                        twinklingRefreshLayout.finishRefreshing();
                    }
                }, 1000);
            }

            //上拉获取更多新闻
            @Override
            public void onLoadMore(final TwinklingRefreshLayout twinklingRefreshLayout) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadMore(); //获取更多新闻
                        try {
                            Thread.sleep(1000);
                        }catch (Exception e){}
                        adapter.notifyDataSetChanged();
                        twinklingRefreshLayout.finishLoadmore();
                    }
                }, 1000);
            }
        });

        return view;
    }

}
