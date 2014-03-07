package com.hhto.dmp;

import android.content.Context;
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


public class Restaurant {
    private Context context;
    private final String name;
    private String id;
    private HashMap<Calendar, RestaurantMenu> menusOfTheWeek = new HashMap<Calendar, RestaurantMenu>();

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

    public RestaurantMenu getMenu(Calendar calendar) {
        return menusOfTheWeek.get(calendar);
    }

    boolean createMenus() {
        JSONObject json = buildJsonFromCache();
        // Use short-circuit evaluation to handle menu creation
        return ((json != null && (jsonIsUpToDate(json) && buildMenusFromJson(json))) || downloadMenus());
    }


    /**
     * Check JSONObject holds menu data for current week.
     * @param json JSONObject to be inspected.
     * @return True if object is up-to-date, false otherwise.
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

    boolean downloadMenus() {
        // TODO Implement
        return false;
    }

    /**
     * Build a JSONObject from cached data.
     * @return JSONObject built.
     */
    JSONObject buildJsonFromCache() {
        try {
            File cacheDir = context.getCacheDir();
            File jsonFile = new File(cacheDir, id);
            BufferedReader br = new BufferedReader(new FileReader(jsonFile));
            StringBuilder jsonString = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                jsonString.append(line);
                jsonString.append("\n");
            }
            JSONObject json = new JSONObject(new JSONTokener(jsonString.toString()));
            return json;
        } catch (IOException e) {   // EXCEPT
            return null;
        } catch (JSONException e) {
            return null;
        }
    }

    /**
     * Build RestaurantMenu objects from JSON and insert them to menusOfTheWeek.
     * @param json JSONObject created from cached or downloaded data.
     */
    boolean buildMenusFromJson(JSONObject json) {
        try {
            JSONObject menus = json.getJSONObject("menus");
            Iterator menusIterator = menus.keys();
            JSONArray menuArray;
            JSONObject jsonCourse;
            int i;

            while (menusIterator.hasNext()) {   // Iterate "whole week"
                i = 0;
                String calString = (String) menusIterator.next();
                Calendar menuCal = parseCalendar(calString);
                menuArray = menus.getJSONArray(calString);
                RestaurantMenu menu = new RestaurantMenu(this, menuCal);
                while ((jsonCourse = menuArray.getJSONObject(i)) != null) { // Iterate courses of a single day
                    Course course = new Course(jsonCourse.getString("title_fi"), jsonCourse.getString("title_en"));
                    course.setProperties(jsonCourse.getString("properties"));
                    menu.addCourse(course);
                    i++;
                }
                menusOfTheWeek.put(menuCal, menu);
            }
            return true;
        } catch (JSONException e) {

        } catch (ParseException e) {

        }
        return false;
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
        private Calendar calendar;
        private ArrayList<Course> courses = new ArrayList<Course>();

        RestaurantMenu(Restaurant restaurant, Calendar calendar) {
            this.restaurant = restaurant;
            this.calendar = calendar;
        }

        @Override
        public String toString() {
            StringBuilder collector = new StringBuilder();
            collector.append(name + "\n");
            for (Course course: courses) {
                collector.append(course.toString());
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
        private final String titleFi;
        private final String titleEn;
        private boolean glutenFree = false;     // Property "G"
        private boolean lactoseFree = false;    // Property "L"
        private boolean milkFree = false;       // Property "M"

        Course(String titleFi, String titleEn) {
            this.titleFi = titleFi;
            this.titleEn = titleEn;
        }

        @Override
        public String toString() {
            return titleFi;
        }

        /**
         * Set properties for course. Meaning of properties is as follows:
         * "G" = gluten-free
         * "L" = lactose-free
         * "M" = milk-free
         * @param properties: A single String or an array of Strings containing
         *                  properties for course.
         */
        public void setProperties(String... properties) {
            for (String property: properties) {
                if (property.contains("G")) {
                    glutenFree = true;
                }
                if (property.contains("L") && !property.contains("VL")) {    // Don't label low-lactose as lactose free
                    lactoseFree = true;
                }
                if (property.contains("M")) {
                    milkFree = true;
                }
            }
        }

        public boolean isGlutenFree() {
            return glutenFree;
        }

        public boolean isLactoseFree() {
            return lactoseFree;
        }

        public boolean isMilkFree() {
            return milkFree;
        }
    }
}
