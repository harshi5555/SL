package com.example.harshi.sl;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import android.widget.TimePicker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import static com.example.harshi.sl.R.id.home;

public class Main2Activity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener,TimePickerDialog.OnTimeSetListener,NumberPicker.OnValueChangeListener {

    SwipeRefreshLayout swipeRefreshLayout;
    private final String APIKEY_2 = "c58553c52e3546b3b8e7551ccdbf158d";
    private JSONObject apiResponseCallB;
    private ArrayList<String> arrList;
    private boolean bus, metro,train;
    private String siteId = null;
    private RouteInformationAdapter ria;
    private String apiURl;
    private int searchMin = 5;
    Calendar calendar = Calendar.getInstance();
    Date date;
    Button btnSubway ;
    Button btnBuss ;
    Button btnPendel;

    int btnColorActive, btnColorDefault;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-hh.mm");


        TextView time = (TextView) findViewById(R.id.updateTime);
        final String myDate = formatter.format(date);
        ;
        time.setText(getString(R.string.uppdate) + myDate);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolb);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("    Time Suggestion");
            //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            //toolbar.setLogo(R.drawable.ic_action_back);
            toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.primary));
            toolbar.setTitleTextColor(Color.WHITE);
        }


        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);


        btnSubway = (Button) findViewById(R.id.btn_subway);
        btnBuss = (Button) findViewById(R.id.btn_bus);
        btnPendel = (Button) findViewById(R.id.btn_train);
        btnColorActive = ContextCompat.getColor(this, R.color.primary_light);
        arrList = new ArrayList<String>();
        siteId = getIntent().getStringExtra("siteid");
        //bus = getIntent().getBooleanExtra("isBusSelected", false);
        //metro = getIntent().getBooleanExtra("isTrainSelected", false);
        if (!bus && !metro&&!train) {
            bus = true;
            metro = true;
            train=true;

        }
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipelayout);
        swipeRefreshLayout.setOnRefreshListener(this);
        getRouteInformation();
        updateInfo();


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.timepicker, menu);
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.myicon:
                final Dialog d = new Dialog(Main2Activity.this);
                d.setTitle("Välj Tid");

                d.setContentView(R.layout.layout_alert);

                Button btnOk = (Button) d.findViewById(R.id.buttonok);
                Button btnCancel = (Button) d.findViewById(R.id.buttonCancel);

                final NumberPicker np = (NumberPicker) d.findViewById(R.id.numberPicker);
                final String[] displayedValues = new String[11];
                int start = 0;
                for (int i = 0; i < displayedValues.length; i++) {
                    start = start + 5;
                    displayedValues[i] = Integer.toString(start);


                }


                np.setMaxValue(10); // max value 10
                np.setMinValue(0);   // min value 0
                np.setDisplayedValues(displayedValues);
                np.setWrapSelectorWheel(false);
                np.setOnValueChangedListener(this);


                np.setOnValueChangedListener((new NumberPicker.
                        OnValueChangeListener() {
                    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                        searchMin = Integer.decode(displayedValues[picker.getValue()]);
                        System.out.println("Item clicked ............... value " + searchMin);

                    }
                }));
                btnOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getRouteInformation();
                        updateInfo();
                        d.dismiss();
                    }
                });
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        d.dismiss();
                    }
                });

                d.show();
        }
                return true;

        }





    public void updateInfo() {
        ria = new RouteInformationAdapter(this, R.layout.routeinformation);
        ListView listView = (ListView) findViewById(R.id.listViewMain2);
        ria.clear();
        if (arrList.size() > 0) {
            for (String str : arrList) {
                RouteInformation ri = new RouteInformation();
                ri.setCocatOutput(str);
                ria.add(ri);
            }
        } else {
            RouteInformation ri = new RouteInformation();
            if (metro)
                ri.setCocatOutput(getString(R.string.metro));
            else if (bus)
                ri.setCocatOutput(getString(R.string.bus));
            else if (train)
                ri.setCocatOutput(getString(R.string.train_inga));
            else if (!metro && !bus)
                ri.setCocatOutput(getString(R.string.both_bustrain));
            else
                ri.setCocatOutput(getString(R.string.try_again_later));

            ria.add(ri);
        }

        listView.setAdapter(ria);
        ria.notifyDataSetChanged();
    }


    @Override
    public void onBackPressed() {

        super.onBackPressed();
        bus = false;
        metro = false;
        train=false;
    }

    @Override
    public void onRefresh() {

        System.out.println("refresh");
        getRouteInformation();
        updateInfo();


        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    public void getRouteInformation() {
        apiURl = "http://api.sl.se/api2/realtimedeparturesv4.json?key=" + APIKEY_2 + "&siteid=" + siteId + "&timewindow=" + searchMin;
        System.out.println(" API URL : " + apiURl);
        ApiClient apiClient = new ApiClient();
        apiResponseCallB = apiClient.callUrl(apiURl);
        if (arrList.size() > 0) {
            arrList.clear();
        }


        if (apiResponseCallB != null) {
            try {
                JSONObject realTidInfoJObject = apiResponseCallB.getJSONObject("ResponseData");
                Iterator<String> isr = realTidInfoJObject.keys();
                int lineNo = 1;
                while (isr.hasNext()) {
                    String trasType;
                    trasType = isr.next();
                    if (trasType.equalsIgnoreCase("Buses") && bus) {
                        JSONArray lineInfoJArr = realTidInfoJObject.getJSONArray("Buses");
                        for (int l = 0; l < lineInfoJArr.length(); l++) {
                            JSONObject tempObj = lineInfoJArr.getJSONObject(l);
                            arrList.add(tempObj.get("DisplayTime") + "     mot " + tempObj.get("Destination") +
                                    "\n" + tempObj.get("TransportMode")
                                    + " : " + tempObj.get("LineNumber"));
                            lineNo++;

                        }

                    } else if (trasType.equalsIgnoreCase("Metros") && metro) {
                        JSONArray lineInfoJArr = realTidInfoJObject.getJSONArray("Metros");
                        for (int l = 0; l < lineInfoJArr.length(); l++) {
                            JSONObject tempObj = lineInfoJArr.getJSONObject(l);
                            arrList.add(tempObj.get("DisplayTime") + "     mot " + tempObj.get("Destination") +
                                    "\n" + tempObj.get("TransportMode")
                                    + " : " + tempObj.get("LineNumber"));
                            lineNo++;

                        }
                    } else if (trasType.equalsIgnoreCase("Trains") && train) {
                        JSONArray lineInfoJArr = realTidInfoJObject.getJSONArray("Trains");
                        for (int l = 0; l < lineInfoJArr.length(); l++) {
                            JSONObject tempObj = lineInfoJArr.getJSONObject(l);
                            arrList.add(tempObj.get("DisplayTime") + "     mot " + tempObj.get("Destination") +
                                    "\n" + tempObj.get("TransportMode")
                                    + " : " + tempObj.get("LineNumber"));
                            lineNo++;

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


    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        searchMin = minute;
        getRouteInformation();
        updateInfo();
    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

    }

    public void toggleView(View view) {

        switch (view.getId()) {
            case R.id.btn_subway:
                {
                System.out.println("button clicekd");
                btnSubway.setActivated(false);
                btnSubway.setBackgroundColor(btnColorActive);
                btnBuss.setBackgroundColor(btnColorDefault);
                btnPendel.setBackgroundColor(btnColorDefault);
                metro =true;
                bus=false;
                    train=false;
                getRouteInformation();
                updateInfo();
                break;
                }
            case R.id.btn_train: {
                btnSubway.setBackgroundColor(btnColorDefault);
                btnBuss.setBackgroundColor(btnColorDefault);
                btnPendel.setBackgroundColor(btnColorActive);
                metro = false;
                bus = false;
                train=true;
                getRouteInformation();
                updateInfo();
                break;
            }
            case R.id.btn_bus: {
                btnSubway.setBackgroundColor(btnColorDefault);
                btnBuss.setBackgroundColor(btnColorActive);
                btnPendel.setBackgroundColor(btnColorDefault);
                metro = false;
                bus = true;
                train=false;
                getRouteInformation();
                updateInfo();
                break;
            }
            default:{
                //
            }
        }

    }
}







