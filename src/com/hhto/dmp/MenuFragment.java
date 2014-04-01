package com.hhto.dmp;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.support.v4.app.Fragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hmhagberg on 13.3.2014.
 */

public class MenuFragment extends Fragment {

    public static MenuFragment newInstance(Integer weekday) {
        MenuFragment mf = new MenuFragment();
        Bundle args = new Bundle();
        args.putInt("WEEKDAY", weekday);
        mf.setArguments(args);
        return mf;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Integer weekday = getArguments().getInt("WEEKDAY");
        List<Restaurant.RestaurantMenu> menuList = DataProvider.getMenuList(weekday);
        View view = inflater.inflate(R.layout.menu_fragment, container, false);
        ListView cardList = (ListView) view.findViewById(R.id.cardList);
        MenuListAdapter adapter = new MenuListAdapter(getActivity(), menuList);
        cardList.setAdapter(adapter);
        return view;
    }
}
