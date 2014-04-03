package com.hhto.dmp;

import android.content.Context;
import android.os.AsyncTask;
import org.json.JSONObject;
import org.json.JSONArray;
import android.util.Log;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by hmhagberg on 8.3.2014.
 */


/**
 * Subclass of Restaurant that represents a Sodexo restaurant and
 * implements the data downloading functionality to the restaurant.
 */
public class SodexoRestaurant extends Restaurant {
    private static final String TAG = "SodexoRestaurant";

    public SodexoRestaurant(Context context) {
        super(context, "Sodexo", "141");
    }

    @Override
    void downloadData() {

        DownloadJsonTask downloader = new DownloadJsonTask();
        downloader.execute(this);

    }

    /**
     * An AsyncTask that downloads the requirered Json data and then parses it.
     */
    class DownloadJsonTask extends AsyncTask<SodexoRestaurant, Void, String[]> {
        SodexoRestaurant restaurant = null;


        /**
         * An AsyncTask method that downloads Json menus from Sodexo.fi
         */
        @Override
        protected String[] doInBackground(SodexoRestaurant... sRestaurants) {


            restaurant = sRestaurants[0];

            //Downloads Json from www.sodexo.fi
            HttpURLConnection connection;
            String urlString;
            URL urlObj;
            String inputLine;
            StringBuffer response;
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

                    urlString = "http://www.sodexo.fi/ruokalistat/output/daily_json/"+sRestaurants[0].getUrlId()+"/"
                            +year+"/"+month +"/"+day+"/fi";
                    urlObj = new URL(urlString);
                    connection = (HttpURLConnection) urlObj.openConnection();
                    connection.setRequestMethod("GET");
                    connection.connect();
                    if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                        return null;
                    }

                    BufferedReader inputStream = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                    response = new StringBuffer();
                    while((inputLine = inputStream.readLine()) != null) {
                        response.append(inputLine);
                    }
                    connection.disconnect();
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

        /**
         * An AsyncTask method that executes after the downloading and parses the downloaded Json
         * to the format this program uses and then saves to the cache and builds the menuMap.
         */
        @Override
        protected void onPostExecute(String[] weeksJson) {

            try {

                if (weeksJson != null) {
                    Calendar cal = Calendar.getInstance();
                    int weekNo = cal.get(Calendar.WEEK_OF_YEAR);

                    int weekDay = cal.get(Calendar.DAY_OF_WEEK);
                    cal.add(Calendar.DATE, -(weekDay-2));

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    String date;

                    JSONObject daysJsonO;
                    JSONObject daysDownloadedJsonO;
                    JSONArray courseArray;
                    JSONObject parsedJson = new JSONObject("{\"meta\":{\"week\":"+weekNo+"},\"menus\":{}}");


                    for (int i=0; i<5; i++) {
                        date = sdf.format(cal.getTime());
                        Log.v(TAG, "-----:"+weeksJson[i]);
                        daysDownloadedJsonO = new JSONObject(weeksJson[i]);

                        courseArray = daysDownloadedJsonO.getJSONArray("courses");
                        daysJsonO = new JSONObject("{\""+date+"\":{}}");
                        daysJsonO.put(date, courseArray);

                        parsedJson.accumulate("menus", daysJsonO);

                        cal.add(Calendar.DATE, 1);
                    }
                    restaurant.menusOfTheWeek = restaurant.buildMenuMap(parsedJson);

                    saveToCache(parsedJson);
                    // Refresh DataProvider and consequently UI
                    DataProvider.refresh();
                }


            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
