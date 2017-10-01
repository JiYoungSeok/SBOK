package com.example.youngseok.sbok;



import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Weather {
    private String wApiKey1 = "9eebd1d0-4f68-3e07-b44b-4454c48e9c61";
    private String wApiKey2 = "9d45f4cc-2dbc-38c6-8378-88f1132fffe9";

    private String urlAddress;
    private String protocol = "GET";
    private URL url;
    private BufferedReader br;
    private HttpURLConnection conn;

    private String weatherCode;
    private int curWeather = -1;

    private static int order = 0;

    public int getCurWeather() {
        return curWeather;
    }

    public void webData(double cLatitude, double cLongitude) throws Exception {
        if(cLatitude == 0.0 && cLongitude == 0.0) {
            return;
        }

        switch(order) {
            case 0:
                urlAddress = "http://apis.skplanetx.com/weather/current/minutely?version=1&lat=" + cLatitude + "&lon=" + cLongitude + "&appKey=" + wApiKey1;
                order = 1;
                break;

            case 1:
                urlAddress = "http://apis.skplanetx.com/weather/current/minutely?version=1&lat=" + cLatitude + "&lon=" + cLongitude + "&appKey=" + wApiKey2;
                order = 0;
                break;

            default:
                break;
        }

        url = new URL(urlAddress);
        conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod(protocol);
        br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "MS949"));

        String line = br.readLine();
        int index = line.indexOf("\"sky\":{\"code\":");
        weatherCode = line.substring(index+15, index+22);

        Log.d("JSON SEND MESSAGE", urlAddress);
        Log.d("JSON RECEIVE MESSAGE", line);

        transCodeToName();
    }

    public void transCodeToName() {
        switch(weatherCode) {
            case "SKY_A01" : curWeather = 0; break; // 맑음
            case "SKY_A02" : curWeather = 1; break; // 구름
            case "SKY_A03" : curWeather = 1; break; // 구름
            case "SKY_A04" : curWeather = 2; break; // 비
            case "SKY_A05" : curWeather = 3; break; // 눈
            case "SKY_A06" : curWeather = 4; break; // 눈비
            case "SKY_A07" : curWeather = 5; break; // 흐림
            case "SKY_A08" : curWeather = 2; break; // 비
            case "SKY_A09" : curWeather = 3; break; // 눈
            case "SKY_A10" : curWeather = 4; break; // 눈비
            case "SKY_A11" : curWeather = 6; break; // 번개
            case "SKY_A12" : curWeather = 6; break; // 번개
            case "SKY_A13" : curWeather = 6; break; // 번개
            case "SKY_A14" : curWeather = 6; break; // 번개
        }
    }
}
