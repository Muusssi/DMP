package com.hhto.dmp;


import android.content.Context;
import android.os.AsyncTask;

/**
 * Created by hmhagberg on 8.3.2014.
 */

public class SodexoRestaurant extends Restaurant {
    Downloader downloader;

    public SodexoRestaurant(Context context) {
        super(context);
        downloader = new Downloader();
    }

    @Override
    void downloadData() {
        downloader.execute(this);
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
