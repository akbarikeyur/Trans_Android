package com.trans.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import java.util.ArrayList;

public class SearchPagerAdapter extends FragmentPagerAdapter {


    Context context;
    int size;
    ArrayList<String> strings;

    public SearchPagerAdapter(Context context, FragmentManager fm, ArrayList<String> strings) {
        super(fm);
        this.context = context;
        this.strings = strings;
    }

    @Override
    public Fragment getItem(int i) {
        return new Fragment();

    }

    @Override
    public int getCount() {
        return strings.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return strings.get(position);
    }
}
