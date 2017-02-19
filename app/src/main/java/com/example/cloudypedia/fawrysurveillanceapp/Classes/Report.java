package com.example.cloudypedia.fawrysurveillanceapp.Classes;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Map;

/**
 * Created by dev3 on 12/15/2016.
 */

public class Report implements Parcelable {


    private String name ;
    private String reportId;
    private String location;
    private String  terminalSerial;
    private String terminalId;
    private String GISLocation;
    private String range;
    private String salesName;
    private String salesEmail;
    private String salesID;
    private String reportUrl;
    private String barcode;
    private Long reportDate;
    private String comment;
    private String Status;
    private String incidentType;
    private String address;
    Map<String, String>  displayNames;

    public Report() {
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    protected Report(Parcel in) {
        name = in.readString();
        reportId = in.readString();
        location = in.readString();
        terminalSerial = in.readString();
        GISLocation = in.readString();
        range = in.readString();
        salesName = in.readString();
        salesEmail = in.readString();
        salesID = in.readString();
        reportUrl = in.readString();
        barcode = in.readString();
        comment = in.readString();
        Status = in.readString();
        incidentType = in.readString();
        reportDate=in.readLong();
        terminalId = in.readString();
        address = in.readString();
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(reportId);
        parcel.writeString(location);
        parcel.writeString(terminalSerial);
        parcel.writeString(GISLocation);
        parcel.writeString(range);
        parcel.writeString(salesName);
        parcel.writeString(salesEmail);
        parcel.writeString(salesID);
        parcel.writeString(reportUrl);
        parcel.writeString(barcode);
        parcel.writeString(comment);
        parcel.writeString(Status);
        parcel.writeString(incidentType);
        parcel.writeLong(reportDate);
        parcel.writeString(terminalId);
        parcel.writeString(address);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTerminalSerial() {
        return terminalSerial;
    }

    public void setTerminalSerial(String terminalSerial) {
        this.terminalSerial = terminalSerial;
    }

    public String getGISLocation() {
        return GISLocation;
    }

    public void setGISLocation(String GISLocation) {
        this.GISLocation = GISLocation;
    }

    public String getRange() {
        return range;
    }

    public void setRange(String range) {
        this.range = range;
    }

    public String getSalesName() {
        return salesName;
    }

    public void setSalesName(String salesName) {
        this.salesName = salesName;
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

    public String getReportUrl() {
        return reportUrl;
    }

    public void setReportUrl(String reportUrl) {
        this.reportUrl = reportUrl;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public Long getReportDate() {
        return reportDate;
    }

    public void setReportDate(Long reportDate) {
        this.reportDate = reportDate;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getIncidentType() {
        return incidentType;
    }

    public void setIncidentType(String incidentType) {
        this.incidentType = incidentType;
    }

    public static Creator<Report> getCREATOR() {
        return CREATOR;
    }

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }
}
