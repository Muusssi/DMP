package com.hhto.dmp;


import android.content.Context;
import android.os.AsyncTask;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.MalformedURLException;
import java.util.*;


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

    class DownloadJsonTask extends AsyncTask<SodexoRestaurant, Void, String> {
        SodexoRestaurant restaurant = null;
        
        @Override
        protected String doInBackground(SodexoRestaurant... sRestaurants) {


            restaurant = sRestaurants[0];

            //Downloads Json from www.sodexo.fi
            HttpURLConnection connection = null;
            String[] responseBuffer = null;

            try {

                // TODO Get the time parameters right
                String urlString = "http://www.sodexo.fi/ruokalistat/output/daily_json/"+sRestaurants[0].getId()+"/2014/03/31/fi";

                URL urlObj = new URL(urlString);
                connection = (HttpURLConnection) urlObj.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();
                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    return null; // Possibly needs error handling
                }

                BufferedReader inputStream = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();
                while((inputLine = inputStream.readLine()) != null) {
                    response.append(inputLine);
                }

                connection.disconnect();
                String sResponse = response.toString();

                return sResponse;

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
        protected void onPostExecute(String s) {
            // TODO Parse downloaded JSON
            try {

                String testJson = "{\"meta\":{\"week\":14},\"menus\":{\"2014-03-31\":[{\"title_fi\":\"Vettä ja leipää\",\"title_en\":\"Meat kaemae\",\"properties\":\"L, G\",\"desc_fi\":\"\",\"desc_en\":\"\",\"desc_se\":\"\"},{\"title_fi\":\"Munia ja pekonia\",\"title_en\":\"Eggs and bacon\",\"properties\":\"\",\"desc_fi\":\"\",\"desc_en\":\"\",\"desc_se\":\"\"}]}}";
                JSONObject json = new JSONObject(testJson);
                restaurant.menusOfTheWeek = restaurant.buildMenuMap(json);



            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
