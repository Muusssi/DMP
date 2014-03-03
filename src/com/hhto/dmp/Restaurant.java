package com.hhto.dmp;

import android.content.Context;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by hmhagberg on 3.3.2014.
 */


public class Restaurant {
    private Context context;
    private String id;

    public Restaurant(Context context, String id) {
        this.context = context;
        this.id = id;
    }

    public RestaurantMenu getMenu(Date date) {
        // TODO: Implement
    }

    RestaurantMenu getMenuFromCache(Date date) {
        try {
            File cacheDir = context.getCacheDir();
            File jsonFile = new File(cacheDir, id);
            BufferedReader br = new BufferedReader(new FileReader(jsonFile));
            StringBuilder jsonString = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null)) {
                jsonString.append(line);
                jsonString.append("\n");
            }
            JSONObject json = new JSONObject(new JSONTokener(jsonString.toString()));

            JSONArray courses = json.getJSONArray("courses");
            JSONObject course;
            int i = 0;
            while ((course = courses.getJSONObject(i)) != null) {
                i++;
                // TODO: Parse course
            }

            // TODO: Handle errors properly
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (JSONException e) {
            System.out.println(e.getMessage());
        }
    }

    class RestaurantMenu {
        private final Restaurant restaurant;
        private final Date date;
        private final ArrayList<Course> courses;

        RestaurantMenu(Restaurant restaurant, Date date, ArrayList<Course> courses) {
            this.restaurant = restaurant;
            this.date = date;
            this.courses = courses;
        }
    }

    class Course {

    }
}
