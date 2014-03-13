package com.hhto.dmp;

import android.content.Context;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by tommi on 26.2.2014.
 */
public class DataProvider {
    /**
     * These lists hold menus for corresponding weekday of the current week.
     * DataProvider should update these lists accordingly. (E.g. remove menu
     * if user uncheck the corresponding restaurant).
     */
    public static List<Restaurant.RestaurantMenu> MONDAY = new ArrayList<Restaurant.RestaurantMenu>();
    public static List<Restaurant.RestaurantMenu> TUESDAY = new ArrayList<Restaurant.RestaurantMenu>();
    public static List<Restaurant.RestaurantMenu> WEDNESDAY = new ArrayList<Restaurant.RestaurantMenu>();
    public static List<Restaurant.RestaurantMenu> THURSDAY = new ArrayList<Restaurant.RestaurantMenu>();
    public static List<Restaurant.RestaurantMenu> FRIDAY = new ArrayList<Restaurant.RestaurantMenu>();

    public DataProvider(Context context) {
        SodexoRestaurant sodexo = new SodexoRestaurant(context);
        sodexo.initMenus();
        FRIDAY.add(sodexo.getMenu("2014-03-05"));

    }

}
