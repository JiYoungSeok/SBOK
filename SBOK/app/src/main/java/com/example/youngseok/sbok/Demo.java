package com.example.youngseok.sbok;

public class Demo {
    private String name;
    private double latitude;
    private double longitude;
    private int weather;

    Demo(String name, double latitude, double longitude, int weather) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.weather = weather;
    }

    public String getName() {
        return name;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public int getWeather() {
        return weather;
    }
}
