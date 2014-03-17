package com.hhto.dmp;


import android.content.Context;
import android.os.AsyncTask;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by hmhagberg on 8.3.2014.
 */

public class SodexoRestaurant extends Restaurant {

    public SodexoRestaurant(Context context) {
        super(context, "Sodexo", "Sodexo");
        // downloader = new Downloader();
    }

    @Override
    void downloadData() {
        // downloader.execute(this);
        try {
            String testJson = "{\"meta\":{\"week\":12},\"menus\":{\"2014-03-17\":[{\"title_fi\":\"Vettä ja leipää\",\"title_en\":\"Meat kaemae\",\"properties\":\"L, G\",\"desc_fi\":\"\",\"desc_en\":\"\",\"desc_se\":\"\"},{\"title_fi\":\"Munia ja pekonia\",\"title_en\":\"Eggs and bacon\",\"properties\":\"\",\"desc_fi\":\"\",\"desc_en\":\"\",\"desc_se\":\"\"}]}}";
            JSONObject json = new JSONObject(testJson);
            menusOfTheWeek = buildMenuMap(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    class Downloader extends AsyncTask<SodexoRestaurant, Void, String> {

        @Override
        protected String doInBackground(SodexoRestaurant... params) {
            // TODO: Write HTTP GET request
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            // TODO: Parse downloaded JSON
        }
    }
}
