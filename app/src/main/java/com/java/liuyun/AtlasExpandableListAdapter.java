package com.java.liuyun;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.List;

public class AtlasExpandableListAdapter extends BaseExpandableListAdapter {
    private Context mcontext;

    public List<String> groupString;
    public List<String> groupSize;
    public List<List<AtlasData>> childString;

    public AtlasExpandableListAdapter(List<String> groupString, List<String> groupSize, List<List<AtlasData>> childString) {
        this.groupString = groupString;
        this.childString = childString;
        this.groupSize = groupSize;
    }

    @Override
    // 获取分组的个数
    public int getGroupCount() {
        return groupString.size();
    }

    //获取指定分组中的子选项的个数
    @Override
    public int getChildrenCount(int groupPosition) {
        return childString.get(groupPosition).size();
    }

    //        获取指定的分组数据
    @Override
    public Object getGroup(int groupPosition) {
        return groupString.get(groupPosition);
    }

    //获取指定分组中的指定子选项数据
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return childString.get(groupPosition).get(childPosition);
    }

    //获取指定分组的ID, 这个ID必须是唯一的
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    //获取子选项的ID, 这个ID必须是唯一的
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    //分组和子选项是否持有稳定的ID, 就是说底层数据的改变会不会影响到它们
    @Override
    public boolean hasStableIds() {
        return true;
    }
    /**
     *
     * 获取显示指定组的视图对象
     *
     * @param groupPosition 组位置
     * @param isExpanded 该组是展开状态还是伸缩状态
     * @param convertView 重用已有的视图对象
     * @param parent 返回的视图对象始终依附于的视图组
     */
// 获取显示指定分组的视图
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder groupViewHolder;
        if (convertView == null){
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.virus_atlas_category_layout,parent,false);
            groupViewHolder = new GroupViewHolder();
            groupViewHolder.title = (TextView) convertView.findViewById(R.id.atlas_category_name);
            groupViewHolder.descr = (TextView) convertView.findViewById(R.id.atlas_category_size);
            convertView.setTag(groupViewHolder);
        }else {
            groupViewHolder = (GroupViewHolder)convertView.getTag();
        }
        groupViewHolder.title.setText(groupString.get(groupPosition));
        groupViewHolder.descr.setText(groupSize.get(groupPosition));
        return convertView;
    }
    /**
     *
     * 获取一个视图对象，显示指定组中的指定子元素数据。
     *
     * @param groupPosition 组位置
     * @param childPosition 子元素位置
     * @param isLastChild 子元素是否处于组中的最后一个
     * @param convertView 重用已有的视图(View)对象
     * @param parent 返回的视图(View)对象始终依附于的视图组
     * @return
     * @see android.widget.ExpandableListAdapter#getChildView(int, int, boolean, android.view.View,
     *      android.view.ViewGroup)
     */

    //取得显示给定分组给定子位置的数据用的视图
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder childViewHolder;
        if (convertView==null){
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.virus_atlas_item_layout,parent,false);
            childViewHolder = new ChildViewHolder();
            childViewHolder.title = (TextView)convertView.findViewById(R.id.atlas_item_name);
            childViewHolder.descr = (TextView)convertView.findViewById(R.id.atlas_item_description);
            convertView.setTag(childViewHolder);

        }else {
            childViewHolder = (ChildViewHolder) convertView.getTag();
        }
        childViewHolder.title.setText(childString.get(groupPosition).get(childPosition).title);
        childViewHolder.descr.setText(childString.get(groupPosition).get(childPosition).descr);
        return convertView;
    }

    //指定位置上的子元素是否可选中
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    static class GroupViewHolder {
        TextView title;
        TextView descr;
    }

    static class ChildViewHolder {
        TextView title;
        TextView descr;
    }
}
