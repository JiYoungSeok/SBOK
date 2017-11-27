package com.example.youngseok.sbok;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SunAltitude {
    private String apiKey = "zEXz1laEFGakic3NTzJMvTamHZKkwVFqQGSV5p7cvQra00bq2cWvX4rAC3c7WNH3mNxCdOlMqrcm%2BN1Zlu6lhw%3D%3D";

    private String altitude09, altitude12, altitude15, altitude18;

    private String urlAddress;
    private String protocol = "GET";
    private URL url;
    private BufferedReader br;
    private HttpURLConnection conn;

    public String getAltitude09() { return altitude09; }
    public String getAltitude12() { return altitude12; }
    public String getAltitude15() { return altitude15; }
    public String getAltitude18() { return altitude18; }

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
        altitude09 = line.substring(startIndex + 13, startIndex + 15);

        startIndex = line.indexOf("<altitude_12>");
        altitude12 = line.substring(startIndex + 13, startIndex + 15);

        startIndex = line.indexOf("<altitude_15>");
        altitude15 = line.substring(startIndex + 13, startIndex + 15);

        startIndex = line.indexOf("<altitude_18>");
        altitude18 = line.substring(startIndex + 13, startIndex + 15);
    }

    public int calcAltitude(int sunRise, int sunSet, int curTime) {
        int section = 0;
        int altitude = 0;
        double temp;
        boolean isWinter;

        if(sunSet > 180000) isWinter = false;
        else isWinter = true;

        if(sunRise <= curTime && curTime < 90000) section = 1;
        else if(90000 <= curTime && curTime < 120000) section = 2;
        else if(120000 <= curTime && curTime < 150000) section = 3;
        else if(150000 <= curTime && curTime < sunSet && isWinter) section = 4;
        else if(150000 <= curTime && curTime < 180000 && !isWinter) section = 4;
        else if(180000 <= curTime && curTime < sunSet && !isWinter) section = 5;

        int tempTime = (curTime / 10000) * 3600 + ((curTime % 10000) / 100) * 60 + curTime % 100;
        curTime = tempTime;

        tempTime = (sunRise / 10000) * 3600 + ((sunRise % 10000) / 100) * 60 + sunRise % 100;
        sunRise = tempTime;

        tempTime = (sunSet / 10000) * 3600 + ((sunSet % 10000) / 100) * 60 + sunSet % 100;
        sunSet = tempTime;

        switch(section) {
            case 0:
                break;

            case 1:
                temp = Double.parseDouble(altitude09) / (9 * 3600 - sunRise);
                altitude = (int) (temp * (curTime - sunRise));
                break;

            case 2:
                temp = (Double.parseDouble(altitude12) - Integer.parseInt(altitude09)) / (3 * 3600);
                altitude = (int) (temp * (curTime - 9 * 3600)) + Integer.parseInt(altitude09);
                break;

            case 3:
                temp = (Double.parseDouble(altitude15) - Integer.parseInt(altitude12)) / (3 * 3600);
                altitude = (int) (temp * (curTime - 12 * 3600)) + Integer.parseInt(altitude12);
                break;

            case 4:
                if(isWinter) {
                    temp = (Double.parseDouble(altitude15) / (sunSet - 15 * 3600));
                    altitude = (int) (temp * (sunSet - curTime)) + Integer.parseInt(altitude15);
                } else {
                    temp = (Double.parseDouble(altitude18) - Integer.parseInt(altitude15)) / (3 * 3600);
                    altitude = (int) (temp * (curTime - 15 * 3600)) + Integer.parseInt(altitude15);
                }
                break;

            case 5:
                temp = Double.parseDouble(altitude18) / (sunSet - 18 * 3600);
                altitude = (int) (temp * (curTime - 18 * 3600)) + Integer.parseInt(altitude18);
                break;
        }

        return altitude;
    }
}
