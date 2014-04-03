package com.hhto.dmp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.support.v4.app.Fragment;

import java.util.List;

/**
 * A fragment to be used as the content of a tab.
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
        DataProvider.addAdapter(weekday, adapter);
        return view;
    }
}
