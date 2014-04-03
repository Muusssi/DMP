package com.hhto.dmp;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * A data provider that acts as an interface between UI and data.
 */
public class DataProvider {
    private static final String TAG = "DataProvider";

    /**
     * These lists hold menus for corresponding weekday of the current week.
     * DataProvider should update these lists accordingly. (E.g. remove menu
     * if user unchecks the corresponding restaurant). After a list is updated
     * the corresponding list adapter should be notified.
     */
    static List<Restaurant.RestaurantMenu> monday = new ArrayList<Restaurant.RestaurantMenu>();
    static List<Restaurant.RestaurantMenu> tuesday = new ArrayList<Restaurant.RestaurantMenu>();
    static List<Restaurant.RestaurantMenu> wednesday = new ArrayList<Restaurant.RestaurantMenu>();
    static List<Restaurant.RestaurantMenu> thursday = new ArrayList<Restaurant.RestaurantMenu>();
    static List<Restaurant.RestaurantMenu> friday = new ArrayList<Restaurant.RestaurantMenu>();
    static Map<Integer, List<Restaurant.RestaurantMenu>> menuLists = new HashMap<Integer, List<Restaurant.RestaurantMenu>>(5);
    static Map<String, Restaurant> restaurants = new HashMap<String, Restaurant>();
    static Map<Integer, MenuListAdapter> adapters = new HashMap<Integer, MenuListAdapter>(5);
    static SharedPreferences pref = null;

    static {
        menuLists.put(Calendar.MONDAY, monday);
        menuLists.put(Calendar.TUESDAY, tuesday);
        menuLists.put(Calendar.WEDNESDAY, wednesday);
        menuLists.put(Calendar.THURSDAY, thursday);
        menuLists.put(Calendar.FRIDAY, friday);
    }

    /**
     * Create and initialize all supported restaurants and add them to a map.
     */
    public static void init(Context context) {
        Log.d(TAG, "Initializing data provider");
        pref = PreferenceManager.getDefaultSharedPreferences(context);

        // Add here all restaurants
        restaurants.put("sodexo", new SodexoRestaurant(context));
        for (Restaurant restaurant: restaurants.values()) {
            restaurant.init();
        }
    }

    /**
     * Refresh the data model.
     *
     * The method first checks from preferences which restaurants the user has selected. After that
     * it iterates over each day of the current week adding the menu data of the selected restaurants
     * for that day. After the list for one weekday is updated the corresponding list adapter is notified
     * of changed data.
     */
    public static void refresh() {
        Log.d(TAG, "Refreshing data set");
        Set<String> restaurantIds = pref.getStringSet("pref_key_selected_restaurants", new HashSet<String>());
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Integer weekday;
        List<Restaurant.RestaurantMenu> menuList;
        MenuListAdapter adapter;

        for (Map.Entry<Integer, List<Restaurant.RestaurantMenu>> entry: menuLists.entrySet()) {
            weekday = entry.getKey();
            menuList = entry.getValue();
            menuList.clear();
            c.set(Calendar.DAY_OF_WEEK, weekday);
            String date = dateFormat.format(c.getTime());
            Log.d(TAG, "Updating date: " + date);
            for (String id: restaurantIds) {
                Log.d(TAG, "Updating restaurant: " + id);
                Restaurant restaurant = restaurants.get(id);
                if (restaurant != null) {
                    Restaurant.RestaurantMenu menu = restaurant.getMenu(date);
                    if (menu != null) {
                        menuList.add(menu);
                    }
                } else {
                    Log.w(TAG, "Invalid restaurant id: " + id);
                }

            }
            adapter = adapters.get(weekday);
            if (adapter != null) {
                adapter.notifyDataSetChanged();
            }

        }
    }

    public static List<Restaurant.RestaurantMenu> getMenuList(Integer weekday) {
        return menuLists.get(weekday);
    }

    public static void addAdapter(Integer weekday, MenuListAdapter adapter) {
        adapters.put(weekday, adapter);
    }

}
