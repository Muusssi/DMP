package com.hhto.dmp;


import android.content.Context;
import android.os.AsyncTask;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.MalformedURLException;


/**
 * Created by hmhagberg on 8.3.2014.
 */

public class SodexoRestaurant extends Restaurant {

    public SodexoRestaurant(Context context) {
        super(context, "Sodexo", "141");

    }

    @Override
    void downloadData() {
        Downloader downloader = new Downloader();

        downloader.execute(this);
        /*
        try {
            String testJson = "{\"meta\":{\"week\":13},\"menus\":{\"2014-03-25\":[{\"title_fi\":\"Vettä ja leipää\",\"title_en\":\"Meat kaemae\",\"properties\":\"L, G\",\"desc_fi\":\"\",\"desc_en\":\"\",\"desc_se\":\"\"},{\"title_fi\":\"Munia ja pekonia\",\"title_en\":\"Eggs and bacon\",\"properties\":\"\",\"desc_fi\":\"\",\"desc_en\":\"\",\"desc_se\":\"\"}]}}";
            JSONObject json = new JSONObject(testJson);
            menusOfTheWeek = buildMenuMap(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        */
    }

    class Downloader extends AsyncTask<SodexoRestaurant, Void, String> {

        
        @Override
        protected String doInBackground(SodexoRestaurant... params) {
            //Downloads Json from www.sodexo.fi
            try {
                String url = "http://www.sodexo.fi/ruokalistat/output/daily_json/"+params[0].getId()+"/2014/03/26/fi";
                URL urlObj = new URL(url);

                HttpURLConnection conn = (HttpURLConnection) urlObj.openConnection();

                //Read the response
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                //Response Json String
                String s = response.toString();

                return s;

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


        }
    }
}
