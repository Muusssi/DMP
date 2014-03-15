package com.hhto.dmp;

import android.content.Context;
import android.content.SharedPreferences;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by tommi on 26.2.2014.
 */
public class DataProvider {
    /**
     * These lists hold menus for corresponding weekday of the current week.
     * DataProvider should update these lists accordingly. (E.g. remove menu
     * if user unchecks the corresponding restaurant).
     */
    public static List<Restaurant.RestaurantMenu> monday = new ArrayList<Restaurant.RestaurantMenu>();
    public static List<Restaurant.RestaurantMenu> tuesday = new ArrayList<Restaurant.RestaurantMenu>();
    public static List<Restaurant.RestaurantMenu> wednesday = new ArrayList<Restaurant.RestaurantMenu>();
    public static List<Restaurant.RestaurantMenu> thursday = new ArrayList<Restaurant.RestaurantMenu>();
    public static List<Restaurant.RestaurantMenu> friday = new ArrayList<Restaurant.RestaurantMenu>();
    static List[] menuLists = {monday, tuesday, wednesday, thursday, friday};
    static int[] weekdays = {Calendar.MONDAY, Calendar.TUESDAY, Calendar.WEDNESDAY, Calendar.THURSDAY, Calendar.FRIDAY};
    static Map<String, Restaurant> restaurants = new HashMap<String, Restaurant>();

    /**
     * Initialize restaurants.
     */
    public static void init(Context context) {
        restaurants.put("sodexo", new SodexoRestaurant(context));
        for (Restaurant restaurant: restaurants.values()) {
            restaurant.init();
        }
    }

    /**
     * Refresh the lists from where tabs fetch display data.
     */
    public static void refresh(SharedPreferences sharedPrefs) {
        Set<String> restaurantIds = sharedPrefs.getStringSet("pref_key_selected_restaurants", null);
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        int weekday;
        List<Restaurant.RestaurantMenu> menuList;

        for (int i = 0; i < 5; i++) {
            weekday = weekdays[i];
            menuList = menuLists[i];
            menuList.clear();
            c.set(Calendar.DAY_OF_WEEK, weekday);
            String date = dateFormat.format(c.getTime());
            for (String id: restaurantIds) {
                Restaurant restaurant = restaurants.get(id);
                Restaurant.RestaurantMenu menu = restaurant.getMenu(date);
                if (menu != null) {
                    menuList.add(menu);
                }
            }
        }

    }

}
