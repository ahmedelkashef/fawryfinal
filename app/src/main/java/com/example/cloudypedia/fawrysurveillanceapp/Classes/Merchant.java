package com.example.cloudypedia.fawrysurveillanceapp.Classes;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by dev3 on 12/19/2016.
 */

public class Merchant implements Parcelable{

    private double longitude;
    private double latitude;
    private String name;
    private String address;
    private String phone;
    private String terminalID;
    private String merchantType;

    public Merchant( double latitude,double longitude, String name) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
    }

    protected Merchant(Parcel in) {
        longitude = in.readDouble();
        latitude = in.readDouble();
        name = in.readString();
        address = in.readString();
        phone = in.readString();
        terminalID = in.readString();
        merchantType = in.readString();
    }

    public Merchant() {
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getTerminalID() {
        return terminalID;
    }

    public void setTerminalID(String terminalID) {
        this.terminalID = terminalID;
    }

    public String getMerchantType() {
        return merchantType;
    }

    public void setMerchantType(String merchantType) {
        this.merchantType = merchantType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeDouble(longitude);
        parcel.writeDouble(latitude);
        parcel.writeString(name);
        parcel.writeString(address);
        parcel.writeString(phone);
        parcel.writeString(terminalID);
        parcel.writeString(merchantType);
    }
    public static final Creator<Merchant> CREATOR = new Creator<Merchant>() {
        @Override
        public Merchant createFromParcel(Parcel in) {
            return new Merchant(in);
        }

        @Override
        public Merchant[] newArray(int size) {
            return new Merchant[size];
        }
    };
}
