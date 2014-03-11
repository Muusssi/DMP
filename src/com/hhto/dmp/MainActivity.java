package com.hhto.dmp;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        ListView menuListView = (ListView) findViewById(R.id.menuList);
        SodexoRestaurant sodexo = new SodexoRestaurant(this);
        sodexo.initMenus();
        List<Restaurant.RestaurantMenu> menuList = new ArrayList<Restaurant.RestaurantMenu>();
        menuList.add(sodexo.getMenu("2014-03-05"));
        menuListView.setAdapter(new MenuListAdapter(this, menuList));
    }
}
