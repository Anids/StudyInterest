package com.swust.stylezz.studyinteret.ui;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    private FragmentManager fragmentManager;
    private List<Fragment> fragments;
    public ViewPagerAdapter(FragmentManager fm, List<Fragment> mFragments) {
        super(fm);
        this.fragments = mFragments;
        this.fragmentManager = fm;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
