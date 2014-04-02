package com.hhto.dmp;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

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
    static List<Restaurant.RestaurantMenu> monday = new ArrayList<Restaurant.RestaurantMenu>();
    static List<Restaurant.RestaurantMenu> tuesday = new ArrayList<Restaurant.RestaurantMenu>();
    static List<Restaurant.RestaurantMenu> wednesday = new ArrayList<Restaurant.RestaurantMenu>();
    static List<Restaurant.RestaurantMenu> thursday = new ArrayList<Restaurant.RestaurantMenu>();
    static List<Restaurant.RestaurantMenu> friday = new ArrayList<Restaurant.RestaurantMenu>();
    static Map<Integer, List<Restaurant.RestaurantMenu>> menuLists = new HashMap<Integer, List<Restaurant.RestaurantMenu>>(5);
    static Map<String, Restaurant> restaurants = new HashMap<String, Restaurant>();

    static {
        menuLists.put(Calendar.MONDAY, monday);
        menuLists.put(Calendar.TUESDAY, tuesday);
        menuLists.put(Calendar.WEDNESDAY, wednesday);
        menuLists.put(Calendar.THURSDAY, thursday);
        menuLists.put(Calendar.FRIDAY, friday);
    }

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
    public static void refresh(Context context) {
        System.out.println("------REFRESH---------------------------------");
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        Set<String> restaurantIds = pref.getStringSet("pref_key_selected_restaurants", new HashSet<String>());
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Integer weekday;
        List<Restaurant.RestaurantMenu> menuList;

        for (Map.Entry<Integer, List<Restaurant.RestaurantMenu>> entry: menuLists.entrySet()) {
            weekday = entry.getKey();
            menuList = entry.getValue();
            menuList.clear();
            c.set(Calendar.DAY_OF_WEEK, weekday);
            String date = dateFormat.format(c.getTime());
            System.out.println("------"+date+"---------------------------------");
            for (String id: restaurantIds) {
                System.out.println("------ID:"+id+"---------------------------------");
                Restaurant restaurant = restaurants.get(id);
                Restaurant.RestaurantMenu menu = restaurant.getMenu(date);
                if (menu != null) {
                    menuList.add(menu);
                }
            }
        }
    }

    public static List<Restaurant.RestaurantMenu> getMenuList(Integer weekday) {
        return menuLists.get(weekday);
    }

}
