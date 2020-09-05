package com.java.liuyun;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

public class MainFragmentPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragmentList;
    private FragmentManager fragmentManager;

    public MainFragmentPagerAdapter (List<Fragment> fragmentList, FragmentManager fragmentManager) {
        super(fragmentManager);
        this.fragmentList = fragmentList;
        this.fragmentManager = fragmentManager;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }
}
