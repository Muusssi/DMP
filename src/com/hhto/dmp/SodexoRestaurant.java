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

public class SodexoRestaurant extends Restaurant {
    private static final String TAG = "SodexoRestaurant";

    public SodexoRestaurant(Context context) {
        super(context, "Sodexo", "141");
    }

    @Override
    void downloadData() {

        DownloadJsonTask downloader = new DownloadJsonTask();
        downloader.execute(this);

        /*
        try {
            String testJson = "{\"menus\":[{},{\"2014-03-31\":[{\"desc_fi\":\"\",\"desc_se\":\"\",\"title_en\":\"Oven baked sausage and mashed potatoes\",\"title_fi\":\"Juustoinen uunimakkara ja perunamuusi\",\"properties\":\"VL\",\"desc_en\":\"\"},{\"desc_fi\":\"\",\"desc_se\":\"\",\"title_en\":\"Saithe with salsa sauce\",\"title_fi\":\"Salsagratinoitua seitä\",\"properties\":\"G, M\",\"desc_en\":\"\"},{\"desc_fi\":\"\",\"desc_se\":\"\",\"title_en\":\"Vegetable stew with nuts and tofu\",\"title_fi\":\"Tofua omena-cashewpähkinäkastikkeessa\",\"properties\":\"G, VL\",\"desc_en\":\"\"},{\"desc_fi\":\"\",\"desc_se\":\"\",\"title_en\":\"Tomato soup with garlic\",\"title_fi\":\"Tomaatti-valkosipulikeitto\",\"properties\":\"G, VL\",\"desc_en\":\"\"},{\"desc_fi\":\"\",\"desc_se\":\"\",\"title_en\":\"Greek salad\",\"title_fi\":\"Kreikkalainen salaatti\",\"properties\":\"G, VL\",\"desc_en\":\"\"}]},{\"2014-04-01\":[{\"desc_fi\":\"\",\"desc_se\":\"\",\"title_en\":\"Beef stew Greek style\",\"title_fi\":\"Kreikkalainen härkäpata\",\"properties\":\"G, M\",\"price\":\"6,80 \\/ 5,80 \\/ 2,60\",\"desc_en\":\"\"},{\"desc_fi\":\"\",\"desc_se\":\"\",\"title_en\":\"Shrimp curry\",\"title_fi\":\"Katkarapucurry\",\"properties\":\"VL\",\"price\":\"6,80 \\/ 5,80 \\/ 2,60\",\"desc_en\":\"\"},{\"desc_fi\":\"\",\"desc_se\":\"\",\"title_en\":\"Chicken soup with noodles\",\"title_fi\":\"Broiler-nuudelikeitto\",\"properties\":\"M\",\"price\":\"6,80 \\/ 5,80 \\/ 2,60\",\"desc_en\":\"\"},{\"desc_fi\":\"\",\"desc_se\":\"\",\"title_en\":\"Spinach pasta with feta\",\"title_fi\":\"Feta-pinaattipasta\",\"properties\":\"VL\",\"price\":\"6,80 \\/ 5,80 \\/ 2,60\",\"desc_en\":\"\"},{\"desc_fi\":\"\",\"desc_se\":\"\",\"title_en\":\"Salad with chili marinated shrimps\",\"title_fi\":\"Chili-katkarapusalaatti\",\"properties\":\"G, M\",\"price\":\"6,80 \\/ 5,80 \\/ 2,60\",\"desc_en\":\"\"}]},{\"2014-04-02\":[{\"desc_fi\":\"\",\"desc_se\":\"\",\"title_en\":\"Chicken nuggets and chili mayonnaise\",\"title_fi\":\"Broilernugetit ja chilimajoneesi\",\"properties\":\"L\",\"price\":\"6,80 \\/ 5,80 \\/ 2,60\",\"desc_en\":\"\"},{\"desc_fi\":\"\",\"desc_se\":\"\",\"title_en\":\"Vegetable croquettes and chili mayonnaise\",\"title_fi\":\"Kasviskroketit ja chilimajoneesi\",\"properties\":\"L\",\"price\":\"6,80 \\/ 5,80 \\/ 2,60\",\"desc_en\":\"\"},{\"desc_fi\":\"\",\"desc_se\":\"\",\"title_en\":\"Carrot pure' soup with smoked cheese\",\"title_fi\":\"Savujuusto-porkkanasosekeitto\",\"properties\":\"VL\",\"price\":\"6,80 \\/ 5,80 \\/ 2,60\",\"desc_en\":\"\"},{\"desc_fi\":\"\",\"desc_se\":\"\",\"title_en\":\"Chicken salad with nuts\",\"title_fi\":\"Broiler-pähkinäsalaatti\",\"properties\":\"G, M\",\"price\":\"6,80 \\/ 5,80 \\/ 2,60\",\"desc_en\":\"\"},{\"desc_fi\":\"\",\"desc_se\":\"\",\"title_en\":\"Overdone pork and mustrad sauce\",\"title_fi\":\"Ylikypsää porsaanpaistia ja sinappikastike\",\"properties\":\"G, L\",\"price\":\"8,30 \\/ 8,20 \\/ 4,95\",\"desc_en\":\"\"}]},{\"2014-04-03\":[{\"desc_fi\":\"\",\"desc_se\":\"\",\"title_en\":\"Lasagne\",\"title_fi\":\"Lasagne\",\"properties\":\"VL\",\"price\":\"6,80 \\/ 5,80 \\/ 2,60\",\"desc_en\":\"\"},{\"desc_fi\":\"\",\"desc_se\":\"\",\"title_en\":\"Pea soup and pancake\",\"title_fi\":\"Hernekeitto ja pannukakku\",\"properties\":\"VL\",\"price\":\"6,80 \\/ 5,80 \\/ 2,60\",\"desc_en\":\"\"},{\"desc_fi\":\"\",\"desc_se\":\"\",\"title_en\":\"Beans dal makhani\",\"title_fi\":\"Papuja dal makhani\",\"properties\":\"G, M\",\"price\":\"6,80 \\/ 5,80 \\/ 2,60\",\"desc_en\":\"\"},{\"desc_fi\":\"\",\"desc_se\":\"\",\"title_en\":\"Salad with grilled salmon\",\"title_fi\":\"Salaatti pariloidusta lohesta\",\"properties\":\"G, M\",\"price\":\"6,80 \\/ 5,80 \\/ 2,60\",\"desc_en\":\"\"}]},{\"2014-04-04\":[{\"desc_fi\":\"\",\"desc_se\":\"\",\"title_en\":\"Hamburger seaks and mashed potatoes\",\"title_fi\":\"Jauhelihapihvit ja perunamuusi\",\"properties\":\"VL\",\"price\":\"6,80 \\/ 5,80 \\/ 2,60\",\"desc_en\":\"\"},{\"desc_fi\":\"\",\"desc_se\":\"\",\"title_en\":\"Vegetable tortilla\",\"title_fi\":\"Kasvistortilla\",\"properties\":\"L\",\"price\":\"6,80 \\/ 5,80 \\/ 2,60\",\"desc_en\":\"\"},{\"desc_fi\":\"\",\"desc_se\":\"\",\"title_en\":\"Parsnip pure' soup and pasty\",\"title_fi\":\"Palsternakkasosekeitto ja kasvispasteija\",\"properties\":\"VL\",\"price\":\"6,80 \\/ 5,80 \\/ 2,60\",\"desc_en\":\"\"},{\"desc_fi\":\"\",\"desc_se\":\"\",\"title_en\":\"Caesarsalad with bacon\",\"title_fi\":\"Pekoni-caesarsalaatti\",\"properties\":\"VL\",\"price\":\"6,80 \\/ 5,80 \\/ 2,60\",\"desc_en\":\"\"},{\"desc_fi\":\"\",\"desc_se\":\"\",\"title_en\":\"Grilled salmon and remoulade sauce\",\"title_fi\":\"Grillattua lohta ja remouladekastike\",\"properties\":\"VL\",\"price\":\"8,30 \\/ 8,20 \\/ 4,95\",\"desc_en\":\"\"}]}],\"meta\":{\"week”:14}}";

            int i =12;
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

                    urlString = "http://www.sodexo.fi/ruokalistat/output/daily_json/"+sRestaurants[0].getUrlId()+"/"
                            +year+"/"+month +"/"+day+"/fi";
                    Log.d(TAG, ":::Haetaan:" + urlString);
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
                    Log.v(TAG, "::::::::Luettiin"+response.toString());

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
                    Log.v(TAG, "#####1#"+parsedJson.toString());


                    for (int i=0; i<5; i++) {
                        date = sdf.format(cal.getTime());
                        Log.v(TAG, "-----:"+weeksJson[i]);
                        daysDownloadedJsonO = new JSONObject(weeksJson[i]);

                        courseArray = daysDownloadedJsonO.getJSONArray("courses");
                        Log.v(TAG, "#####Array#" + courseArray.toString());

                        daysJsonO = new JSONObject("{\""+date+"\":{}}");
                        daysJsonO.put(date, courseArray);
                        Log.v(TAG, "#####2#" + daysJsonO.toString());

                        parsedJson.accumulate("menus", daysJsonO);

                        Log.v(TAG, "#####3#"+parsedJson.toString());

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
