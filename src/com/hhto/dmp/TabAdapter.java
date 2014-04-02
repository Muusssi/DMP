package com.hhto.dmp;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;

import java.util.Calendar;

/**
 * Created by hmhagberg on 13.3.2014.
 */
public class TabAdapter extends FragmentPagerAdapter {
    private static final String TAG = "FragmentPagerAdapter";

    public TabAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        Log.v(TAG, "Creating tab number " + i);
        switch (i) {
            case 0:
                return MenuFragment.newInstance(Calendar.MONDAY);
            case 1:
                return MenuFragment.newInstance(Calendar.TUESDAY);
            case 2:
                return MenuFragment.newInstance(Calendar.WEDNESDAY);
            case 3:
                return MenuFragment.newInstance(Calendar.THURSDAY);
            case 4:
                return MenuFragment.newInstance(Calendar.FRIDAY);
        }
        return null;
    }

    @Override
    public int getCount() {
        return 5;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
