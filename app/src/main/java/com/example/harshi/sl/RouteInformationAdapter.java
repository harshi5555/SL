package com.example.harshi.sl;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by harshi on 05/05/17.
 */

public class RouteInformationAdapter extends ArrayAdapter {
    List list = new ArrayList<>();
    public RouteInformationAdapter(Context context, int resource) {
        super(context, resource);
    }

    public void add(RouteInformation object) {
        super.add(object);
        list.add(object);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Nullable
    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row;
        RouteInforHolder routeInforHolder = null;
        row = convertView;
        if(row == null){
            LayoutInflater layoutInflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
         row = layoutInflater.inflate(R.layout.routeinformation,parent,false);
            routeInforHolder = new RouteInforHolder();
            routeInforHolder.routeInformation =(TextView)row.findViewById(R.id.routeInformation);
            row.setTag(routeInforHolder);
        }
        else {
            routeInforHolder=(RouteInforHolder)row.getTag();

        }

        RouteInformation routeInformation = (RouteInformation) this.getItem(position);
        routeInforHolder.routeInformation.setText(routeInformation.getCocatOutput());
        return row;
    }

    static  class RouteInforHolder{
        TextView routeInformation;
    }

    public void clear(){
        list.clear();

    }
}
