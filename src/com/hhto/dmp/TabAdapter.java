package com.hhto.dmp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by hmhagberg on 13.3.2014.
 */
public class TabAdapter extends FragmentPagerAdapter {

    public TabAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                return new MenuFragment(DataProvider.monday);
            case 1:
                return new MenuFragment(DataProvider.tuesday);
            case 2:
                return new MenuFragment(DataProvider.wednesday);
            case 3:
                return new MenuFragment(DataProvider.thursday);
            case 4:
                return new MenuFragment(DataProvider.friday);
        }
        return null;
    }

    @Override
    public int getCount() {
        return 5;
    }
}
