package com.royce.tripbotify.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.royce.tripbotify.fragment.DiscoverFragment;
import com.royce.tripbotify.fragment.MyWorldFragment;
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
            return MyWorldFragment.newInstance();

    }

    @Override
    public int getCount() {
        return 3;
    }

}
