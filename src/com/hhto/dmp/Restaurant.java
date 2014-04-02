package com.hhto.dmp;

import android.content.Context;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by hmhagberg on 3.3.2014.
 */

/**
 * The base class for various restaurants. The implementation is complete except
 * for data downloading and parsing. This should be implemented using AsyncTask.
 */
public abstract class Restaurant {
    private static final String TAG = "Restaurant";
    Context context;
    String name;
    String urlId;
    HashMap<String, RestaurantMenu> menusOfTheWeek = new HashMap<String, RestaurantMenu>();

    public Restaurant(Context context, String name, String urlId) {
        this.context = context;
        this.name = name;
        this.urlId = urlId;
    }

    public String getName() {
        return name;
    }

    public String getUrlId() {
        return urlId;
    }

    public RestaurantMenu getMenu(String date) {
        return menusOfTheWeek.get(date);
    }

    /**
     * Initialize menus. If data is up to date it is loaded from cache,
     * otherwise it is downloaded from web. This method should be called
     * "immediately" after instantiation.
     */
    void init() {
        JSONObject json = loadFromCache();
        if (json != null && jsonIsUpToDate(json)) {
            menusOfTheWeek = buildMenuMap(json);
        } else {
            downloadData();
        }
    }

    abstract void downloadData();

    /**
     * Check if JSONObject holds menu data for current week.
     */
    boolean jsonIsUpToDate(JSONObject json) {
        try {
            Calendar cal = Calendar.getInstance();
            int week = json.getJSONObject("meta").getInt("week");
            return cal.get(Calendar.WEEK_OF_YEAR) == week;
        } catch (JSONException e) {
            return false;
        }
    }

    /**
     * Build a JSONObject from cached data.
     */
    JSONObject loadFromCache() {
        Log.d(TAG, "Load data from cache.");
        try {
            File cacheDir = context.getCacheDir();
            File cacheFile = new File(cacheDir, urlId + ".json"); // Cache files are identified by restaurant urlId
            BufferedReader reader = new BufferedReader(new FileReader(cacheFile));
            StringBuilder jsonString = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonString.append(line);
                jsonString.append("\n");
            }
            JSONObject json = new JSONObject(new JSONTokener(jsonString.toString()));
            return json;

        } catch (FileNotFoundException e) {
            Log.d(TAG, "Cache file not found.");
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Write a JSONObject to cache.
     */
    void saveToCache(JSONObject json) {
        try {
            Log.d(TAG, "Saving data to cache.");
            File cacheDir = context.getCacheDir();
            File cacheFile = new File(cacheDir, urlId + ".json"); // Cache files are identified by restaurant urlId
            BufferedWriter writer = new BufferedWriter(new FileWriter(cacheFile, false));   // Overwrite cache
            writer.write(json.toString(4));
            writer.close();
            Log.v(TAG, json.toString(4));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /**
     * Build RestaurantMenu objects from JSON and insert them to menusOfTheWeek.
     * @param json JSONObject parsed from cached or downloaded data.
     * @return menuMap A HashMap with dates as keys and RestaurantMenus as values
     */
    HashMap<String, RestaurantMenu> buildMenuMap(JSONObject json) {
        try {
            JSONArray menusArray = json.getJSONArray("menus");
            JSONObject menus = menusArray.getJSONObject(1);

            Iterator menusIterator = menus.keys();
            HashMap<String, RestaurantMenu> menuMap = new HashMap<String, RestaurantMenu>();
            JSONArray menuArray;
            JSONObject jsonCourse;

            while (menusIterator.hasNext()) {   // Iterate "whole week"
                String date = (String) menusIterator.next();
                menuArray = menus.getJSONArray(date);
                RestaurantMenu menu = new RestaurantMenu(this, date);
                for (int i = 0; i < menuArray.length(); i++){ // Iterate courses of a single day
                    jsonCourse = menuArray.getJSONObject(i);
                    Course course = new Course(jsonCourse.getString("title_fi"), jsonCourse.getString("title_en"), jsonCourse.getString("properties"));
                    menu.addCourse(course);
                }
                menuMap.put(date, menu);
            }
            return menuMap;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /*
    HashMap<String, RestaurantMenu> buildMenuMap(JSONObject json) {
        try {
            JSONObject menus = json.getJSONObject("menus");
            Iterator menusIterator = menus.keys();
            HashMap<String, RestaurantMenu> menuMap = new HashMap<String, RestaurantMenu>();
            JSONArray menuArray;
            JSONObject jsonCourse;

            while (menusIterator.hasNext()) {   // Iterate "whole week"
                String date = (String) menusIterator.next();
                menuArray = menus.getJSONArray(date);
                RestaurantMenu menu = new RestaurantMenu(this, date);
                for (int i = 0; i < menuArray.length(); i++){ // Iterate courses of a single day
                    jsonCourse = menuArray.getJSONObject(i);
                    Course course = new Course(jsonCourse.getString("title_fi"), jsonCourse.getString("title_en"), jsonCourse.getString("properties"));
                    menu.addCourse(course);
                }
                menuMap.put(date, menu);
            }
            return menuMap;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    */

    /**
     * Menu of a particular restaurant for a single day.
     */
    class RestaurantMenu {
        private Restaurant restaurant;
        private String date;
        private List<Course> courses = new ArrayList<Course>();

        RestaurantMenu(Restaurant restaurant, String date) {
            this.restaurant = restaurant;
            this.date = date;
        }

        @Override
        public String toString() {
            return restaurant + " " + date;
        }


        public Restaurant getRestaurant() {
            return restaurant;
        }

        public void addCourse(Course course) {
            courses.add(course);
        }

        public List<Course> getCourses() {
            return courses;
        }
    }


    /**
     * A single course of a menu.
     */
    class Course {
        private String titleFi;
        private String titleEn;
        private String properties;

        Course(String titleFi, String titleEn, String properties) {
            this.titleFi = titleFi;
            this.titleEn = titleEn;
            this.properties = properties;
        }

        @Override
        public String toString() {
            return titleEn;
        }

        public String getTitle(String language) {
            if (language.equals("fi")) {
                return titleFi;
            } else {
                return titleEn;
            }
        }

        public String getProperties() {
            return properties;
        }

    }
}
