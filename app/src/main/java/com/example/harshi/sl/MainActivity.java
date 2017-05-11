package com.example.harshi.sl;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;


import java.io.BufferedReader;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;


public class MainActivity extends AppCompatActivity {
    LocationAdapter locationAdapter;
    private String apiURl = null;
    private JSONObject apiResponse = null;
    private JSONObject apiResponseCallA = null;
    private JSONObject apiResponseCallB = null;
    private String searchLocationStr=null;
    private final String APIKEY_1 = "e561a9f413074449b04f55c99e099d2b";
    private final String APIKEY_2 = "c58553c52e3546b3b8e7551ccdbf158d";
    ArrayList<String> arrList = new ArrayList<>();

    ListView listView;
    public static final  String myLocation="savekey";
    SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        locationAdapter = new LocationAdapter(this,R.layout.station_list);
        listView = (ListView)findViewById(R.id.listViewResult);
        listView.setAdapter(locationAdapter);
        Button myBtn = (Button)findViewById(R.id.myButton);
        final TextView searchLocation = (TextView)findViewById(R.id.searchLocation) ;
        sharedPreferences = getSharedPreferences("prefs", Context.MODE_PRIVATE);

        if(sharedPreferences.contains(myLocation)){
            searchLocation.setText(sharedPreferences.getString(myLocation,""));
        }


        myBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locationAdapter.clear();
                locationAdapter.notifyDataSetChanged();
                searchLocationStr = searchLocation.getText().toString();
               // SharedPreferences sharedPreferences = getSharedPreferences("prefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(myLocation,searchLocationStr);
                editor.commit();

                if(searchLocationStr != null) {
                    callAPI1();
                }


            }
        });



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Location selectedLocation = (Location) listView.getItemAtPosition(position);
                callAPI2(selectedLocation.getSiteId());
                Intent intent =new Intent(MainActivity.this,Main2Activity.class);
                intent.putStringArrayListExtra("arrList",arrList);
                startActivity(intent);
            }
        });


    }

    public void callAPI2(String siteId){
        String apiURl = "http://api.sl.se/api2/realtimedeparturesv4.json?key=" + APIKEY_2 + "&siteid=" + siteId + "&timewindow=10";
        GetTravelInformationInner gtiRealTime = new GetTravelInformationInner(apiURl);
        gtiRealTime.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);;
        try {
            gtiRealTime.get();
        }catch(Exception e)
        {
            System.out.println(e.getMessage());
        }

        apiResponseCallB = apiResponse;
        //clear the array list
        if(arrList.size() > 0) {
            arrList.clear();
        }
        if (apiResponseCallB != null) {
            try {
                //System.out.println(apiResponse.get("ResponseData").toString());
                JSONObject realTidInfoJObject = apiResponseCallB.getJSONObject("ResponseData");
                Iterator<String> isr = realTidInfoJObject.keys();
                while ( isr.hasNext()){
                    String trasType;
                    trasType = isr.next();
                     if(trasType.equals("Buses")) {
                        JSONArray lineInfoJArr = realTidInfoJObject.getJSONArray("Buses");
                        for (int l = 0; l < lineInfoJArr.length(); l++) {
                            JSONObject tempObj = lineInfoJArr.getJSONObject(l);
                            arrList.add(tempObj.get("DisplayTime")
                                    + ":" + tempObj.get("TransportMode")
                                    + ":" + "LineNumber : "  + tempObj.get("LineNumber")
                                    + ":" + tempObj.get("Destination"));
                        }

                    }
                    else if(trasType.equals("Metros")) {
                        JSONArray lineInfoJArr = realTidInfoJObject.getJSONArray("Metros");
                        for (int l = 0; l < lineInfoJArr.length(); l++) {
                            JSONObject tempObj = lineInfoJArr.getJSONObject(l);
                            arrList.add(tempObj.get("DisplayTime")
                                    + ":" + tempObj.get("TransportMode")
                                    + ":" + "LineNumber : "  + tempObj.get("LineNumber")
                                    + ":" + tempObj.get("Destination"));
                        }
                    }
                }

            } catch (JSONException e) {
                System.out.println(e);
            }


        } else {
            System.out.println("No Response");
        }

    }

    public void callAPI1(){

        apiURl = "http://api.sl.se/api2/typeahead.json?key="+ APIKEY_1 +"&searchstring=" + searchLocationStr +"&stationsonly=true";
        GetTravelInformationInner gti = new GetTravelInformationInner(apiURl);
        gti.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
        try {
            gti.get();
        }catch(Exception e)
        {
            System.out.println(e.getMessage());
        }
        apiResponseCallA = apiResponse;

        if (apiResponseCallA != null) {
            try {
                JSONArray sites = apiResponseCallA.getJSONArray("ResponseData");
                for (int i = 0; i < sites.length(); i++) {
                    JSONObject site = sites.getJSONObject(i);
                    Location location = new Location(site.getString("Name"),site.getString("SiteId"));
                    locationAdapter.add(location);
                }

            } catch (JSONException e) {
                System.out.println(e);
            }

        }

    }

    public class GetTravelInformationInner extends AsyncTask<String,String,String> {
        String apiURl = null;
        public  GetTravelInformationInner(String apiURl ){
            this.apiURl = apiURl;
        }
        @Override
        protected String doInBackground(String... params) {
            try {
                URL url = new URL(apiURl);
                URLConnection urlConn = (HttpURLConnection)url.openConnection();
                urlConn.setRequestProperty("Method","GET");
                urlConn.connect();
                BufferedReader bfr = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
                String response = bfr.readLine();
                if ( response != null )
                    apiResponse = new JSONObject(response);
            }catch(Exception e){
                System.out.println(e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }


    }




}