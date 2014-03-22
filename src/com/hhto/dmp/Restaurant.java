package com.hhto.dmp;

import android.content.Context;
import android.os.AsyncTask;
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
    Context context;
    String name;
    String id;
    HashMap<String, RestaurantMenu> menusOfTheWeek = new HashMap<String, RestaurantMenu>();

    public Restaurant(Context context, String name, String id) {
        this.context = context;
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
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
     * Check JSONObject holds menu data for current week.
     * @param json JSONObject to be inspected.
     * @return true if object is up-to-date, false otherwise.
     */
    boolean jsonIsUpToDate(JSONObject json) {
        try {
            Calendar today = Calendar.getInstance();
            int week = json.getJSONObject("meta").getInt("week");
            return today.WEEK_OF_YEAR == week;
        } catch (JSONException e) {
            return false;
        }
    }

    /**
     * Build a JSONObject from cached data.
     * @return JSONObject built.
     */
    JSONObject loadFromCache() {
        try {
            File cacheDir = context.getCacheDir();
            File cacheFile = new File(cacheDir, id + ".json"); // Cache files are identified by restaurant id
            BufferedReader reader = new BufferedReader(new FileReader(cacheFile));
            StringBuilder jsonString = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonString.append(line);
                jsonString.append("\n");
            }
            JSONObject json = new JSONObject(new JSONTokener(jsonString.toString()));
            return json;
        } catch (IOException e) {   // EXCEPT
            e.printStackTrace();
            return null;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Write a JSONObject to cache.
     * @param json
     */
    void saveToCache(JSONObject json) {
        try {
            File cacheDir = context.getCacheDir();
            File cacheFile = new File(cacheDir, id + ".json"); // Cache files are identified by restaurant id
            BufferedWriter writer = new BufferedWriter(new FileWriter(cacheFile, false));   // Overwrite cache
            writer.write(json.toString(4));
        } catch (IOException e) {   // EXCEPT
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
    HashMap<String , RestaurantMenu> buildMenuMap(JSONObject json) {
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
        } catch (JSONException e) { // EXCEPT
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Parse String to Calendar. Required format is yyyy-MM-dd.
     * @param calString String to be parsed.
     * @return Resulting Calendar.
     * @throws ParseException
     */
    static Calendar parseCalendar(String calString) throws ParseException{
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = formatter.parse(calString);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }


    /**
     * Menu of a particular restaurant for a single day.
     */
    class RestaurantMenu {
        private Restaurant restaurant;
        private String date;
        private ArrayList<Course> courses = new ArrayList<Course>();

        RestaurantMenu(Restaurant restaurant, String date) {
            this.restaurant = restaurant;
            this.date = date;
        }

        @Override
        public String toString() {
            return restaurant + " " + date;
        }

        public String format(String language, Boolean showProperties) {
            StringBuilder collector = new StringBuilder();
            for (Course course: courses) {
                collector.append(course.format(language, showProperties));
                collector.append("\n");
            }
            return collector.toString();
        }

        public Restaurant getRestaurant() {
            return restaurant;
        }

        public void addCourse(Course course) {
            courses.add(course);
        }

        public ArrayList<Course> getCourses() {
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

        public String format(String language, Boolean showProperties) {
            StringBuilder collector = new StringBuilder();
            if (language.equals("fi")) {
                collector.append(titleFi);
            } else {
                collector.append(titleEn);
            }
            if (showProperties) {
                collector.append(" ");
                collector.append(properties);
            }
            return collector.toString();
        }


    }
}
