package com.hhto.dmp;


import android.content.Context;
import android.os.AsyncTask;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * Created by hmhagberg on 8.3.2014.
 */

public class SodexoRestaurant extends Restaurant {

    public SodexoRestaurant(Context context) {
        super(context, "Sodexo", "141");

    }

    @Override
    void downloadData() {

        DownloadJsonTask downloader = new DownloadJsonTask();
        downloader.execute(this);

        /*
        try {
            String testJson = "{\"meta\":{\"week\":14},\"menus\":{\"2014-03-31\":[{\"title_fi\":\"Vettä ja leipää\",\"title_en\":\"Meat kaemae\",\"properties\":\"L, G\",\"desc_fi\":\"\",\"desc_en\":\"\",\"desc_se\":\"\"},{\"title_fi\":\"Munia ja pekonia\",\"title_en\":\"Eggs and bacon\",\"properties\":\"\",\"desc_fi\":\"\",\"desc_en\":\"\",\"desc_se\":\"\"}]}}";
            JSONObject json = new JSONObject(testJson);
            menusOfTheWeek = buildMenuMap(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        */

    }

    class DownloadJsonTask extends AsyncTask<SodexoRestaurant, Void, String[]> {
        SodexoRestaurant restaurant = null;
        
        @Override
        protected String[] doInBackground(SodexoRestaurant... sRestaurants) {


            restaurant = sRestaurants[0];

            //Downloads Json from www.sodexo.fi
            HttpURLConnection connection;
            String urlString;
            URL urlObj;
            String inputLine;
            StringBuffer response = null;
            String[] weeksJson = new String[5];

            Calendar cal = Calendar.getInstance();
            int weekDay = cal.get(Calendar.DAY_OF_WEEK);
            cal.add(Calendar.DATE, -(weekDay-2));
            int year;
            int month;
            int day;

            try {

                //Get json for each the 5 days of current week
                for (int i = 0; i<5; i++) {
                    year = cal.get(Calendar.YEAR);
                    month = cal.get(Calendar.MONTH) +1; //Months start from 0
                    day = cal.get(Calendar.DAY_OF_MONTH);

                    urlString = "http://www.sodexo.fi/ruokalistat/output/daily_json/"+sRestaurants[0].getId()+"/"
                            +year+"/"+month +"/"+day+"/fi";
                    System.out.println(":::Haetaan:"+urlString);
                    urlObj = new URL(urlString);
                    connection = (HttpURLConnection) urlObj.openConnection();
                    connection.setRequestMethod("GET");
                    connection.connect();
                    if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                        return null; // Possibly needs error handling
                    }

                    BufferedReader inputStream = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                    response = new StringBuffer();
                    while((inputLine = inputStream.readLine()) != null) {
                        response.append(inputLine);
                    }
                    connection.disconnect();
                    System.out.println("::::::::Luettiin"+response.toString());

                    cal.add(Calendar.DATE, 1);
                    weeksJson[i] = response.toString();
                }

                return weeksJson;

            }
            catch (MalformedURLException e) {
                e.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String[] weeksJson) {
            // TODO Parse downloaded JSON

            try {

                if (weeksJson != null) {
                    Calendar cal = Calendar.getInstance();
                    int weekNo = cal.get(Calendar.WEEK_OF_YEAR);

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    String date = sdf.format(new Date());

                    JSONObject daysJsonO;
                    JSONObject daysDownloadedJsonO;
                    JSONObject parsedJson = new JSONObject("{\"meta\":{\"week\":"+weekNo+"},\"menus\":{}}");
                    System.out.println("#####1#"+parsedJson.toString());


                    for (int i=0; i<5; i++) {
                        System.out.println("-----:"+weeksJson[i]);
                        daysDownloadedJsonO = new JSONObject(weeksJson[i]);
                        daysDownloadedJsonO = daysDownloadedJsonO.getJSONObject("courses");
                        System.out.println(daysDownloadedJsonO.toString());
                        daysJsonO = new JSONObject("{\""+date+"\":{}}");
                        daysJsonO.accumulate(date, daysDownloadedJsonO);
                        System.out.println("#####2#"+daysJsonO.toString());
                        parsedJson.accumulate("menus", daysJsonO);
                        System.out.println("#####3#"+parsedJson.toString());

                    }
                }

                /*
                String testJson = "{\"meta\":{\"week\":14},\"menus\":{\"2014-03-31\":[{\"title_fi\":\"Vettä ja leipää\",\"title_en\":\"Meat kaemae\",\"properties\":\"L, G\",\"desc_fi\":\"\",\"desc_en\":\"\",\"desc_se\":\"\"},{\"title_fi\":\"Munia ja pekonia\",\"title_en\":\"Eggs and bacon\",\"properties\":\"\",\"desc_fi\":\"\",\"desc_en\":\"\",\"desc_se\":\"\"}]}}";
                JSONObject json = new JSONObject(testJson);
                restaurant.menusOfTheWeek = restaurant.buildMenuMap(json);
                */


            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
