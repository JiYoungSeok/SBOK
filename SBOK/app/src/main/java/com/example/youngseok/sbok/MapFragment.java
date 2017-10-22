package com.example.youngseok.sbok;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.skp.Tmap.TMapData;
import com.skp.Tmap.TMapGpsManager;
import com.skp.Tmap.TMapMarkerItem;
import com.skp.Tmap.TMapPoint;
import com.skp.Tmap.TMapView;

public class MapFragment extends Fragment implements TMapGpsManager.onLocationChangedCallback {

    private static String mApiKey = "e3ec92b4-d0be-396c-a3f6-1df217dec94a";
    private static TMapView tMapView;
    private static TMapData tMapData;
    private static Weather weather = new Weather();

    private Context thisContext;

    private LocationManager locationManager;

    private FrameLayout ll_tMap;
    private ImageButton bt_auto;
    private TextView tv_curAddr;
    private ImageView iv_curWeather;
    private ImageView iv_bluetooth, iv_gps;

    private double curLat;
    private double curLon;

    private String curAddr;

    private boolean isRun = false;

    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            curLat = location.getLatitude();
            curLon = location.getLongitude();
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

    @Override
    public void onLocationChange(Location location) {
        tMapView.setLocationPoint(location.getLongitude(), location.getLatitude());
    }

    public MapFragment() {
        //Basic Construction
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_map, container, false);
        thisContext = container.getContext();

        ll_tMap = view.findViewById(R.id.ll_tMap);
        bt_auto = view.findViewById(R.id.bt_auto);
        tv_curAddr = view.findViewById(R.id.tv_curAddr);
        iv_curWeather = view.findViewById(R.id.iv_curWeather);
        iv_bluetooth = view.findViewById(R.id.iv_bluetooth);
        iv_gps = view.findViewById(R.id.iv_gps);

        detectChangeThreads();

        tMapView = new TMapView(thisContext);
        tMapData = new TMapData();

        initMap();
        ll_tMap.addView(tMapView);

        bt_auto.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isRun) {
                    isRun = true;
                    startSubThreads();
                    bt_auto.setImageResource(R.drawable.switch_on);
                } else {
                    isRun = false;
                    bt_auto.setImageResource(R.drawable.switch_off);
                }
            };
        });

        return view;
    }

    private void initMap() {
        double initLat = 37.6018;
        double initLon = 127.0407;

        tMapView.setSKPMapApiKey(mApiKey);
        tMapView.setCenterPoint(initLon, initLat);
        tMapView.setZoomLevel(12);
    }

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

    private void drawCurMarker() {
        if(curLat != 0.0 || curLon != 0.0) {
            TMapPoint tMapPoint = new TMapPoint(curLat, curLon);
            TMapMarkerItem tItem = new TMapMarkerItem();

            tItem.setTMapPoint(tMapPoint);

            Bitmap bitmap = BitmapFactory.decodeResource(thisContext.getResources(),R.drawable.car);
            tItem.setIcon(bitmap);

            tMapView.addMarkerItem("현재위치", tItem);

            tMapView.setCenterPoint(curLon, curLat, false);
            tMapView.setZoomLevel(15);
        }
    }

    public void startAutoThread() {
        MyRunnable myRunnable = new MyRunnable();
        Thread myThread = new Thread(myRunnable);
        myThread.setDaemon(true);
        myThread.start();
    }

    public void startSubThreads() {
        startAutoThread();

        new Thread() {
            public void run() {
                while (isRun) {
                    try {
                        weather.webData(curLat, curLon);
                        Thread.sleep(2000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    public void detectChangeThreads() {
        new Thread() {
            public void run() {
                while (true) {
                    try {
                        if(MainActivity.isBluetoothOn) iv_bluetooth.setImageResource(R.drawable.bluetooth_on);
                        else iv_bluetooth.setImageResource(R.drawable.bluetooth_off);

                        if(MainActivity.isGpsOn) iv_gps.setImageResource(R.drawable.gps_on);
                        else iv_gps.setImageResource(R.drawable.gps_off);

                        Thread.sleep(300);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    android.os.Handler mainHandler = new android.os.Handler() {
        public void handleMessage(Message msg) {
            startLocationService();
            drawCurMarker();

            tMapData.convertGpsToAddress(curLat, curLon, new TMapData.ConvertGPSToAddressListenerCallback() {
                @Override
                public void onConvertToGPSToAddress(String s) {
                    curAddr = s;
                }
            });

            tv_curAddr.setText(curAddr);

            switch(weather.getCurWeather()) {
                case 0: iv_curWeather.setImageResource(R.drawable.sunny); break;
                case 1: iv_curWeather.setImageResource(R.drawable.cloudy); break;
                case 2: iv_curWeather.setImageResource(R.drawable.rainy); break;
                case 3: iv_curWeather.setImageResource(R.drawable.snowy); break;
                case 4: iv_curWeather.setImageResource(R.drawable.snowrain); break;
                case 5: iv_curWeather.setImageResource(R.drawable.blur); break;
                case 6: iv_curWeather.setImageResource(R.drawable.lightning); break;
            }
        }
    };

    private class MyRunnable implements Runnable {
        @Override
        public void run() {

            while(isRun) {
                Message msg = Message.obtain();
                mainHandler.sendMessage(msg);

                try {
                    Thread.sleep(2000);
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
