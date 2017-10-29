package com.example.youngseok.sbok;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SunAltitude {
    private String apiKey = "plYFpPp6rK%2FSi2MUsTfBgHyofM8ZWmwUPGtuXtYtBhFeAoxxLQq%2FTR4NhxIcVUZBeTtVJL2S%2Bn58XZ1ARyp5Yw%3D%3D";

    private String altitude09, altitude12, altitude15, altitude18;
    private String azimuth09, azimuth12, azimuth15, azimuth18;
    private String altitudeMeridian;

    private String urlAddress;
    private String protocol = "GET";
    private URL url;
    private BufferedReader br;
    private HttpURLConnection conn;

    public String getAltitude09() { return altitude09; }
    public String getAltitude12() { return altitude12; }
    public String getAltitude15() { return altitude15; }
    public String getAltitude18() { return altitude18; }

    public String getAzimuth09() { return azimuth09; }
    public String getAzimuth12() { return azimuth12; }
    public String getAzimuth15() { return azimuth15; }
    public String getAzimuth18() { return azimuth18; }

    public String getAltitudeMeridian() { return altitudeMeridian; }

    public void getAltitudeInfo(int date, double latitude, double longitude) throws Exception {
        if (date == 0 || latitude == 0.0 || longitude == 0.0) return;

        urlAddress = "http://apis.data.go.kr/B090041/openapi/service/SrAltudeInfoService/getLCSrAltudeInfo" +
                "?locdate=" + date +
                "&latitude=" + latitude +
                "&longitude=" + longitude +
                "&dnYn=Y" +
                "&ServiceKey=" + apiKey;

        url = new URL(urlAddress);
        conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod(protocol);
        br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "MS949"));

        String line = br.readLine();

        Log.d("REQUEST MESSAGE : ", urlAddress);
        Log.d("RESPONSE MESSAGE : ", line);

        int startIndex = line.indexOf("<altitude_09>");
        int endIndex = line.indexOf("</altitude_09>");
        altitude09 = line.substring(startIndex + 13, endIndex);

        startIndex = line.indexOf("<altitude_12>");
        endIndex = line.indexOf("</altitude_12>");
        altitude12 = line.substring(startIndex + 13, endIndex);

        startIndex = line.indexOf("<altitude_15>");
        endIndex = line.indexOf("</altitude_15>");
        altitude15 = line.substring(startIndex + 13, endIndex);

        startIndex = line.indexOf("<altitude_18>");
        endIndex = line.indexOf("</altitude_18>");
        altitude18 = line.substring(startIndex + 13, endIndex);

        startIndex = line.indexOf("<azimuth_09>");
        endIndex = line.indexOf("</azimuth_09>");
        azimuth09 = line.substring(startIndex + 12, endIndex);

        startIndex = line.indexOf("<azimuth_12>");
        endIndex = line.indexOf("</azimuth_12>");
        azimuth12 = line.substring(startIndex + 12, endIndex);

        startIndex = line.indexOf("<azimuth_15>");
        endIndex = line.indexOf("</azimuth_15>");
        azimuth15 = line.substring(startIndex + 12, endIndex);

        startIndex = line.indexOf("<azimuth_18>");
        endIndex = line.indexOf("</azimuth_18>");
        azimuth18 = line.substring(startIndex + 12, endIndex);

        startIndex = line.indexOf("<altitudeMeridian>");
        endIndex = line.indexOf("</altitudeMeridian>");
        altitudeMeridian = line.substring(startIndex + 18, endIndex);

        Log.d("ALTITUDE09 : ", altitude09);
        Log.d("ALTITUDE12 : ", altitude12);
        Log.d("ALTITUDE15 : ", altitude15);
        Log.d("ALTITUDE18 : ", altitude18);

        Log.d("AZIMUTH09 : ", azimuth09);
        Log.d("AZIMUTH12 : ", azimuth12);
        Log.d("AZIMUTH15 : ", azimuth15);
        Log.d("AZIMUTH18 : ", azimuth18);

        Log.d("ALTITUDE_MERIDIAN : ", altitudeMeridian);
    }
}
