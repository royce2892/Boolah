package com.royce.tripbotify.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.royce.tripbotify.fragment.DiscoverFragment;
import com.royce.tripbotify.fragment.TodayFragment;

public class HomePagerAdapter extends FragmentPagerAdapter {

    public HomePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0)
            return TodayFragment.newInstance();
        else if (position == 1)
            return DiscoverFragment.newInstance(2);
        else
            return DiscoverFragment.newInstance(3);

    }

    @Override
    public int getCount() {
        return 3;
    }

}
