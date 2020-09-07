package com.java.liuyun;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FragmentHome extends Fragment {

    public int page; //当前加载了page页新闻（上拉page-1次）
    public List<NewsObject> newsList; //【重要】新闻列表

    public List<String> categoryItemList;
    public String keyWord;

    public TwinklingRefreshLayout twinklingRefreshLayout;
    public RecyclerView recyclerView;

    public FragmentHome() {
        page = 1;
        newsList = Collections.synchronizedList(new ArrayList<NewsObject>());
        categoryItemList = new ArrayList<>();
        categoryItemList.add("news");
        categoryItemList.add("paper");
        keyWord = "";
    }

    public void setPage(int page) {
        this.page = page;
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
     TODO:完成flush()函数，表示重置本页面（类似于刚打开APP时做的事情，从第1页开始重新加载）
     TODO:类似地完成loadMore()函数
     TODO:完成本类的onCreateView函数，实现twinklingRefreshLayout的setOnRefreshListener函数（维护出上拉和下拉对应的事件）
     */

    public void flush() {
        //重新开始加载第1页新闻
        setPage(1);
        newsList.clear();

        //根据categoryItemList和keyWord，查询一定条数的新闻（例如12条）
        //得到新的newsList，长度为12
        //其它要做的事情...

    }

    public void loadMore() {
        setPage(this.page + 1);

        //再加载12条新闻，更新newsList
        //其它要做的事情...

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_home, container, false);
        twinklingRefreshLayout = (TwinklingRefreshLayout) view.findViewById(R.id.fragment_home_layout);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

        twinklingRefreshLayout.startRefresh();
        //实现setOnRefreshListener

        return view;
    }

}
