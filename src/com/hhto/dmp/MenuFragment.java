package com.hhto.dmp;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.support.v4.app.Fragment;

import java.util.List;

/**
 * Created by hmhagberg on 13.3.2014.
 */
public class MenuFragment extends Fragment {
    private List<Restaurant.RestaurantMenu> menuList;

    public MenuFragment(List<Restaurant.RestaurantMenu> menuList) {
        this.menuList = menuList;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.menu_fragment, container, false);
        ListView cardList = (ListView) view.findViewById(R.id.cardList);
        cardList.setAdapter(new MenuListAdapter(getActivity(), menuList));
        return view;
    }
}
