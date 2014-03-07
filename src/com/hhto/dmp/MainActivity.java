package com.hhto.dmp;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

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
        Calendar today = Calendar.getInstance();
        DataProvider dataProvider = new DataProvider();
        List<Restaurant.RestaurantMenu> menuList = dataProvider.getMenus(today, "id1", "id2");
        menuListView.setAdapter(new MenuListAdapter(this, menuList));
    }
}
