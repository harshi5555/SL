package com.example.harshi.sl;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;

public class Main2Activity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        RouteInformationAdapter ria = new RouteInformationAdapter(this,R.layout.routeinformation);
        ListView listView = (ListView) findViewById(R.id.listViewMain2) ;
        listView.setAdapter(ria);
        Intent intent = getIntent();
        ArrayList<String> arrList ;

        //retrieve array list and set to adapter
        arrList = intent.getStringArrayListExtra("arrList");
        if(arrList.size() > 0 ) {
            for (String str : arrList) {
                RouteInformation ri = new RouteInformation();
                ri.setCocatOutput(str);
                System.out.println(str);
                ria.add(ri);
            }
        } else{
            RouteInformation ri = new RouteInformation();
            ri.setCocatOutput("No busses trains available within 10 minute");
            ria.add(ri);
        }

        ria.notifyDataSetChanged();



    }





}
