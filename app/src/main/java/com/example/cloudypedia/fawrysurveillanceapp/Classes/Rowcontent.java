package com.example.cloudypedia.fawrysurveillanceapp.Classes;

/**
 * Created by dev3 on 1/31/2017.
 */

public class Rowcontent {
    private String Date ;
    private String Location ;

    public String getDate() {
        return Date;
    }

    public Rowcontent(String date, String location) {
        Date = date;
        Location = location;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }
}
