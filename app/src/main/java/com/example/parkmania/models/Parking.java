package com.example.parkmania.models;

import java.io.Serializable;
import java.util.List;

public class Parking implements Serializable {
    private String id;
    private String userId;
    private String buildingCode;
    private long noOfHrs;
    private String carLicensePlateNumber;
    private String suitNumber;
    private String latitude;
    private String longitude;
    private String date;
    private List<Users> userList;
    public Parking() {
    }

    public Parking(String id, String userId, String buildingCode, long noOfHrs, String carLicensePlateNumber, String suitNumber, String latitude, String longitude, String date) {
        this.id = id;
        this.userId = userId;
        this.buildingCode = buildingCode;
        this.noOfHrs = noOfHrs;
        this.carLicensePlateNumber = carLicensePlateNumber;
        this.suitNumber = suitNumber;
        this.latitude = latitude;
        this.longitude = longitude;
        this.date = date;
    }

    public List<Users> getUserList() {
        return userList;
    }

    public void setUserList(List<Users> userList) {
        this.userList = userList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getBuildingCode() {
        return buildingCode;
    }

    public void setBuildingCode(String buildingCode) {
        this.buildingCode = buildingCode;
    }

    public long getNoOfHrs() {
        return noOfHrs;
    }

    public void setNoOfHrs(long noOfHrs) {
        this.noOfHrs = noOfHrs;
    }

    public String getCarLicensePlateNumber() {
        return carLicensePlateNumber;
    }

    public void setCarLicensePlateNumber(String carLicensePlateNumber) {
        this.carLicensePlateNumber = carLicensePlateNumber;
    }

    public String getSuitNumber() {
        return suitNumber;
    }

    public void setSuitNumber(String suitNumber) {
        this.suitNumber = suitNumber;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Parking{" +
                "id='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", buildingCode='" + buildingCode + '\'' +
                ", noOfHrs=" + noOfHrs +
                ", carLicensePlateNumber='" + carLicensePlateNumber + '\'' +
                ", suitNumber='" + suitNumber + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
