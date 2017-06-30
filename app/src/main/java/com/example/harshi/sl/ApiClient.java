package com.example.harshi.sl;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by harshi on 15/05/17.
 */

public class ApiClient {
    public JSONObject callUrl(String apiURl){
        JSONObject apiResponse = null;
        GetTravelInformation gti = new GetTravelInformation(apiURl);
        //gti.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
        try {
            apiResponse = gti.get();
        }catch(Exception e)
        {
            System.out.println(e.getMessage());
            apiResponse = null;
        }

        return apiResponse;
    }



}
