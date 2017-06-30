package com.example.harshi.sl;

import android.os.AsyncTask;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by harshi on 15/05/17.
 */

public class GetTravelInformation extends AsyncTask<String,String,JSONObject> {
    String apiURl = null;
    JSONObject apiResponse = null;
    public  GetTravelInformation(String apiURl ){
        this.apiURl = apiURl;

        executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
    }


    @Override
    protected JSONObject doInBackground(String... params) {
        try {
            URL url = new URL(apiURl);
            URLConnection urlConn = url.openConnection();
            urlConn.setRequestProperty("Method","GET");
            urlConn.connect();
            BufferedReader bfr = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
            String response = bfr.readLine();
            if ( response != null )
                apiResponse = new JSONObject(response);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        return apiResponse;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(JSONObject s) {
        super.onPostExecute(s);
    }



}
