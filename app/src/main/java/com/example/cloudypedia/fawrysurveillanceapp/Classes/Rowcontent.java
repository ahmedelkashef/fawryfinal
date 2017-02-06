package com.example.cloudypedia.fawrysurveillanceapp.Classes;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by dev3 on 1/31/2017.
 */

public class Rowcontent implements Parcelable {
    private String Date ;
    private String Location ;
    private String ID;


    protected Rowcontent(Parcel in) {
        Date = in.readString();
        Location = in.readString();
        ID = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Date);
        dest.writeString(Location);
        dest.writeString(ID);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Rowcontent> CREATOR = new Creator<Rowcontent>() {
        @Override
        public Rowcontent createFromParcel(Parcel in) {
            return new Rowcontent(in);
        }

        @Override
        public Rowcontent[] newArray(int size) {
            return new Rowcontent[size];
        }
    };

    public String getDate() {
        return Date;
    }

    public Rowcontent(String date, String location, String Id) {
        Date = date;
        Location = location;
        ID = Id;
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
    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

}
