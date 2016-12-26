package com.example.cloudypedia.fawrysurveillanceapp.Classes;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by dev3 on 12/15/2016.
 */

public class Report implements Parcelable {

    private String name ;
    private String location;
    private String  merchantID;
    private String GISLocation;
    private String range ;
    private String salesName;
    private String salesEmail;
    private String salesID;
    private Bitmap reportImage;

    protected Report(Parcel in) {
        name = in.readString();
        location = in.readString();
        merchantID = in.readString();
        GISLocation = in.readString();
        range = in.readString();
        salesName = in.readString();
        salesEmail = in.readString();
        salesID = in.readString();
        reportImage = in.readParcelable(Bitmap.class.getClassLoader());
        terminalID = in.readString();
    }

    public Report() {
    }

    public static final Creator<Report> CREATOR = new Creator<Report>() {
        @Override
        public Report createFromParcel(Parcel in) {
            return new Report(in);
        }

        @Override
        public Report[] newArray(int size) {
            return new Report[size];
        }
    };

    public String getSalesName() {
        return salesName;
    }

    public void setSalesName(String salesName) {
        this.salesName = salesName;
    }

    public String getMerchantID() {
        return merchantID;
    }

    public void setMerchantID(String merchantID) {
        this.merchantID = merchantID;
    }

    public String getRange() {
        return range;
    }

    public void setRange(String range) {
        this.range = range;
    }

    public String getGISLocation() {
        return GISLocation;
    }

    public void setGISLocation(String GISLocation) {
        this.GISLocation = GISLocation;
    }

    public String getSalesEmail() {
        return salesEmail;
    }

    public void setSalesEmail(String salesEmail) {
        this.salesEmail = salesEmail;
    }

    public String getSalesID() {
        return salesID;
    }

    public void setSalesID(String salesID) {
        this.salesID = salesID;
    }

    public String getTerminalID() {
        return terminalID;
    }

    public void setTerminalID(String terminalID) {
        this.terminalID = terminalID;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    private String terminalID;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public Bitmap getReportImage() {
        return reportImage;
    }

    public void setReportImage(Bitmap reportImage) {
        this.reportImage = reportImage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(location);
        parcel.writeString(merchantID);
        parcel.writeString(GISLocation);
        parcel.writeString(range);
        parcel.writeString(salesName);
        parcel.writeString(salesEmail);
        parcel.writeString(salesID);
        parcel.writeParcelable(reportImage, i);
        parcel.writeString(terminalID);
    }
}