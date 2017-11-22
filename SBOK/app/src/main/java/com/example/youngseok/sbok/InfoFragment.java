package com.example.youngseok.sbok;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.icu.util.Calendar;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.widget.TextView;

public class InfoFragment extends Fragment {

    private Context thisContext;

    private SensorManager mSensorManager = null;

    private SensorEventListener mAccLis;
    private Sensor mAccelometerSensor = null;

    private LocationManager locationManager;

    private SunAltitude sunAltitude;
    private SunSet sunSet;

    private TextView tv_XZ, tv_YZ, tv_date, tv_time;
    private TextView tv_latitude, tv_longitude;
    private TextView tv_altitude09, tv_altitude12, tv_altitude15, tv_altitude18, tv_altitude;
    private TextView tv_sunRise, tv_sunSet;

    private double latitude, longitude;
    private int year, month, date, convertDate;
    private int hour, min, sec, convertTime;

    public InfoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_info, container, false);
        thisContext = container.getContext();

        mSensorManager = (SensorManager) thisContext.getSystemService(Context.SENSOR_SERVICE);

        //Using the Accelometer
        mAccelometerSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mAccLis = new AccelometerListener();

        sunAltitude = new SunAltitude();
        sunSet = new SunSet();

        Calendar today;
        today = Calendar.getInstance();
        year = today.get(Calendar.YEAR);
        month = today.get(Calendar.MONTH);
        date = today.get(Calendar.DAY_OF_MONTH);

        hour = today.get(Calendar.HOUR_OF_DAY);
        min = today.get(Calendar.MINUTE);
        sec = today.get(Calendar.SECOND);

        convertDate = year * 10000 + (month + 1) * 100 + date;
        convertTime = hour * 10000 + min * 100 + sec;

        tv_XZ = view.findViewById(R.id.tv_XZ);
        tv_YZ = view.findViewById(R.id.tv_YZ);
        tv_date = view.findViewById(R.id.tv_date);
        tv_time = view.findViewById(R.id.tv_time);

        tv_latitude = view.findViewById(R.id.tv_latitude);
        tv_longitude = view.findViewById(R.id.tv_longitude);

        tv_altitude09 = view.findViewById(R.id.tv_altitude09);
        tv_altitude12 = view.findViewById(R.id.tv_altitude12);
        tv_altitude15 = view.findViewById(R.id.tv_altitude15);
        tv_altitude18 = view.findViewById(R.id.tv_altitude18);
        tv_altitude = view.findViewById(R.id.tv_altitude);

        tv_sunRise = view.findViewById(R.id.tv_sunRise);
        tv_sunSet = view.findViewById(R.id.tv_sunSet);

        view.findViewById(R.id.bt_start).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                startLocationService();

                new Thread() {
                    public void run() {
                        try {
                            sunAltitude.getAltitudeInfo(convertDate, latitude, longitude);
                        } catch(Exception e) {
                            e.printStackTrace();
                        }

                    }
                }.start();

                new Thread() {
                    public void run() {
                        try {
                            sunSet.getInfo(latitude, longitude, convertDate);
                        } catch(Exception e) {
                            e.printStackTrace();
                        }
                    }
                }.start();

                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        mSensorManager.registerListener(mAccLis, mAccelometerSensor, SensorManager.SENSOR_DELAY_UI);
                        break;

                    case MotionEvent.ACTION_UP:
                        mSensorManager.unregisterListener(mAccLis);
                        break;

                }

                tv_date.setText("날짜 : " + Integer.toString(convertDate));
                tv_time.setText("시간 : " + Integer.toString(convertTime));

                if(latitude != 0.0 && longitude != 0.0) {
                    tv_latitude.setText("위도 : " + Double.toString(latitude));
                    tv_longitude.setText("경도 : " + Double.toString(longitude));
                } else {
                    tv_latitude.setText("위도 확인중");
                    tv_longitude.setText("경도 확인중");
                }

                tv_altitude09.setText("9시고도 : " + sunAltitude.getAltitude09());
                tv_altitude12.setText("12시고도 : " + sunAltitude.getAltitude12());
                tv_altitude15.setText("15시고도 : " + sunAltitude.getAltitude15());
                tv_altitude18.setText("18시고도 : " + sunAltitude.getAltitude18());

                int sunSetTime = Integer.parseInt(sunSet.getSunSet());
                int sunRiseTime = Integer.parseInt(sunSet.getSunRise());

                if(sunSetTime != 0 && sunRiseTime != 0) tv_altitude.setText("현재고도 : " + sunAltitude.calcAltitude(sunRiseTime, sunSetTime, convertTime));

                tv_sunRise.setText("일출시간 : " + sunSet.getSunRise());
                tv_sunSet.setText("일몰시간 : " + sunSet.getSunSet());

                return false;
            }
        });


        return view;
    }

    @Override
    public void onPause(){
        super.onPause();
        Log.e("LOG", "onPause()");
        mSensorManager.unregisterListener(mAccLis);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.e("LOG", "onDestroy()");
        mSensorManager.unregisterListener(mAccLis);
    }


    private class AccelometerListener implements SensorEventListener {

        @Override
        public void onSensorChanged(SensorEvent event) {

            double accX = event.values[0];
            double accY = event.values[1];
            double accZ = event.values[2];

            double angleXZ = Math.atan2(accX,  accZ) * 180/Math.PI;
            double angleYZ = Math.atan2(accY,  accZ) * 180/Math.PI;

            tv_XZ.setText("XZ축 기울기 : " + Double.toString(angleXZ));
            tv_YZ.setText("YZ축 기울기 : " + Double.toString(angleYZ));

            Log.e("LOG", "ACCELOMETER           [X]:" + String.format("%.4f", event.values[0])
                    + "           [Y]:" + String.format("%.4f", event.values[1])
                    + "           [Z]:" + String.format("%.4f", event.values[2])
                    + "           [angleXZ]: " + String.format("%.4f", angleXZ)
                    + "           [angleYZ]: " + String.format("%.4f", angleYZ));

        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    }

    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {
        }

        @Override
        public void onProviderEnabled(String s) {
        }

        @Override
        public void onProviderDisabled(String s) {
        }
    };

    private void startLocationService () {
        locationManager = (LocationManager) thisContext.getSystemService(Context.LOCATION_SERVICE);

        long minTime = 1000 * 5;
        float minDistance = 10;

        if (ActivityCompat.checkSelfPermission(thisContext, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(thisContext, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime, minDistance, mLocationListener);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, minTime, minDistance, mLocationListener);
    }
}
