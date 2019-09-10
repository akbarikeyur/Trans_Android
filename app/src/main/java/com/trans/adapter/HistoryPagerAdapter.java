package com.trans.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.trans.fragment.BookingHistoryFragment;

import java.util.ArrayList;

public class HistoryPagerAdapter extends FragmentPagerAdapter {


    Context context;
    ArrayList<String> strings;
    public BookingHistoryFragment upComingHistoryFragment, pastHistoryFragment, cancelledHistoryFragment;

    public HistoryPagerAdapter(Context context, FragmentManager fm, ArrayList<String> strings) {
        super(fm);
        this.context = context;
        this.strings = strings;
    }

    @Override
    public Fragment getItem(int i) {
        if (i == 0) {
            if (upComingHistoryFragment == null)
                upComingHistoryFragment = new BookingHistoryFragment(i);
            return upComingHistoryFragment;
        } else if (i == 1) {
            if (pastHistoryFragment == null)
                pastHistoryFragment = new BookingHistoryFragment(i);
            return pastHistoryFragment;
        } else {
            if (cancelledHistoryFragment == null)
                cancelledHistoryFragment = new BookingHistoryFragment(i);
            return cancelledHistoryFragment;
        }

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
