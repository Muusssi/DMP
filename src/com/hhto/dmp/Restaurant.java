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
    private String id;
    private HashMap<Calendar, RestaurantMenu> menusOfTheWeek = new HashMap<Calendar, RestaurantMenu>();

    public Restaurant(Context context, String id) {
        this.context = context;
        this.id = id;
    }

    public RestaurantMenu getMenu(Calendar calendar) {
        return menusOfTheWeek.get(calendar);
    }

    void createMenus() {
        try {
            JSONObject json = buildJsonFromCache();
            if (jsonIsUpToDate(json)) {
                buildMenusFromJson(json);
            } else {
                downloadMenus();    // TODO: Implement
            }
        } catch (JSONException e) { // TODO: Handle errors properly
            System.out.println(e.getMessage());
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (ParseException e) {
            System.out.println(e.getMessage());
        }
    }


    /**
     * Check JSONObject holds menu data for current week.
     * @param json JSONObject to be inspected.
     * @return True if object is up-to-date, false otherwise.
     * @throws JSONException
     */
    boolean jsonIsUpToDate(JSONObject json) throws JSONException{
        Calendar today = Calendar.getInstance();
        int week = json.getJSONObject("meta").getInt("week");
        return today.WEEK_OF_YEAR == week;
    }


    /**
     * Build a JSONObject from cached data.
     * @return JSONObject built.
     * @throws IOException
     * @throws JSONException
     */
    JSONObject buildJsonFromCache() throws IOException, JSONException{
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
    }

    /**
     * Build RestaurantMenu objects from JSON and insert them to menusOfTheWeek.
     * @param json JSONObject created from cached or downloaded data.
     * @throws ParseException thrown when date in JSON is invalid.
     * @throws JSONException thrown when parsing JSON fails.
     */
    void buildMenusFromJson(JSONObject json) throws ParseException, JSONException{

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
            for (Course course: courses) {
                collector.append(course.toString());
                collector.append("\n");
            }
            return collector.toString();
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
