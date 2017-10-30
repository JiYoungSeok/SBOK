package com.example.youngseok.sbok;

public class RecentDestination {

    private double latitude;
    private double longitude;
    private String name;
    private String addr;

    RecentDestination(double latitude, double longitude, String name, String addr) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
        this.addr = addr;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getName() {
        return name;
    }

    public String getAddr() {
        return addr;
    }

}