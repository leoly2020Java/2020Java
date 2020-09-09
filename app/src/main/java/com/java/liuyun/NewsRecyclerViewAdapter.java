package com.java.liuyun;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NewsRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private Context context;
    private List<NewsAbstractObject> newsList;
    NewsRecyclerViewAdapter(Context context, List<NewsAbstractObject> newsList){
        this.context = context;
        this.newsList = newsList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(context);
        return new NewsViewHolder(inflater.inflate(R.layout.news_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position)
    {
        MainActivity mainActivity = (MainActivity)context;
        NewsViewHolder newsViewHolder = (NewsViewHolder) viewHolder;
        newsViewHolder.newsAbstractObject = newsList.get(position);
        newsViewHolder.title.setText(newsViewHolder.newsAbstractObject.getTitle());
        newsViewHolder.title.setTextColor(context.getColor(R.color.Blue));
        //newsViewHolder.title.setOnClickListener(new View.OnClickListener(){});
        newsViewHolder.publishTime.setText(DateUtils.getRelativeTimeSpanString(newsViewHolder.newsAbstractObject.getPublishTime().getTime()));
        newsViewHolder.type.setText(newsViewHolder.newsAbstractObject.getType());
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }
}

class NewsViewHolder extends RecyclerView.ViewHolder
{
    TextView title;
    TextView publishTime;
    TextView type;
    NewsAbstractObject newsAbstractObject;
    NewsViewHolder(View itemView)
    {
        super(itemView);
        title = (TextView) itemView.findViewById(R.id.newsTitle);
        publishTime = (TextView) itemView.findViewById(R.id.newsPublishTime);
        type = (TextView) itemView.findViewById(R.id.newsType);
    }
}