package com.java.liuyun;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

//CategoryGridView类适配器，负责用List<String>管理分类列表信息
public class CategoryAdapter extends BaseAdapter {
    public Context context;
    public List<String> categoryList;
    public boolean isAddition;
    public int deletePos;
    public boolean isVisible;

    public CategoryAdapter(Context context, List<String> categoryList, boolean isAddition) {
        this.context = context;
        this.categoryList = categoryList;
        this.isAddition = isAddition;
        this.deletePos = -1;
        this.isVisible = true;
    }

    @Override
    public int getCount() {return categoryList == null ? 0 : categoryList.size();}

    @Override
    public String getItem(int pos) {
        if (categoryList == null) return null;
        if (categoryList.size() == 0) return null;
        return categoryList.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return pos;
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.category_item_layout, null);
        String text = getItem(pos);
        TextView itemText = (TextView) view.findViewById(R.id.category_item);
        itemText.setText(text);
        //
        return view;
    }

    public void setIsVisible(boolean isVisible) {
        this.isVisible = isVisible;
    }

    public void setCategoryList(List<String> categoryList) {
        this.categoryList = categoryList;
    }
    public List<String> getCategoryList() {
        return categoryList;
    }
    public void addItem(String text) {
        categoryList.add(text);
        notifyDataSetChanged();
    }
    public void setDelete(int pos) {
        deletePos = pos;
        notifyDataSetChanged();
    }
    public void deleteItem() {
        if (deletePos < 0 || categoryList.size() <= deletePos) return;
        categoryList.remove(deletePos);
        deletePos = -1;
        notifyDataSetChanged();
    }

}
