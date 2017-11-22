package com.example.youngseok.sbok;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SunSet {
    private String apiKey = "zEXz1laEFGakic3NTzJMvTamHZKkwVFqQGSV5p7cvQra00bq2cWvX4rAC3c7WNH3mNxCdOlMqrcm%2BN1Zlu6lhw%3D%3D";

    private String sunRise = "0";
    private String sunSet = "0";

    private String urlAddress;
    private String protocol = "GET";
    private URL url;
    private BufferedReader br;
    private HttpURLConnection conn;

    public String getSunRise() {
            return sunRise;
    }

    public String getSunSet() {
            return sunSet;
    }

    public void getInfo(double latitude, double longitude, int date) throws Exception {
        if(latitude == 0.0 || longitude == 0.0 || date == 0) {
            return;
        }

        urlAddress = "http://apis.data.go.kr/B090041/openapi/service/RiseSetInfoService/getLCRiseSetInfo" +
                "?longitude=" + longitude +
                "&latitude=" + latitude +
                "&locdate=" + date +
                "&dnYn=Y" +
                "&ServiceKey=" + apiKey;

        url = new URL(urlAddress);
        conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod(protocol);
        br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "MS949"));

        String line = br.readLine();

        Log.d("REQUEST MESSAGE : ", urlAddress);
        Log.d("RESPONSE MESSAGE : ", line);

        int startIndex = line.indexOf("<sunrise>");
        int endIndex = line.indexOf("</sunrise>");
        sunRise = line.substring(startIndex + 9, endIndex);

        startIndex = line.indexOf("<sunset>");
        endIndex = line.indexOf("</sunset>");
        sunSet = line.substring(startIndex + 8, endIndex);

        Log.d("SUNRISE : ", sunRise);
        Log.d("SUNSET : ", sunSet);
    }
}
