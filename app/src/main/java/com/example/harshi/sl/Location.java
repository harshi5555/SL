package com.example.harshi.sl;

/**
 * Created by harshi on 05/05/17.
 */

public class Location {
    private String station;
    private String time;
    private String siteId;


    public Location(String station,String siteId) {
        this.setStation(station);
        this.setSiteId(siteId);

    }

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }
}
