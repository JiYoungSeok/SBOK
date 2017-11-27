package com.example.youngseok.sbok;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.skp.Tmap.TMapData;
import com.skp.Tmap.TMapMarkerItem;
import com.skp.Tmap.TMapPoint;
import com.skp.Tmap.TMapPolyLine;
import com.skp.Tmap.TMapView;

import java.util.LinkedList;
import java.util.Queue;

public class DemoMapFragment extends Fragment {

    private static String mApiKey = "e3ec92b4-d0be-396c-a3f6-1df217dec94a";
    private static TMapView tMapView;
    private static TMapData tMapData;
    private static TMapPoint startPoint;
    private static TMapPoint endPoint;

    private Queue<Demo> KookminQueue = new LinkedList<>();
    private DemoDBManager demoDBManager = null;

    private SensorManager mSensorManager = null;
    private SensorEventListener mAccLis;
    private Sensor mAccelometerSensor = null;

    private Context thisContext;

    private FrameLayout ll_tMap;
    private TextView tv_curAddr;
    private ImageView iv_curWeather;

    private double lat, lon;
    private String name, addr;
    private int weatherCode;


    private boolean isRun = true;
    private boolean isInit = true;

    private double angleXZ, angleYZ;
    private double initAngle, realAngle;

    private class AccelometerListener implements SensorEventListener {

        @Override
        public void onSensorChanged(SensorEvent event) {

            double accX = event.values[0];
            double accY = event.values[1];
            double accZ = event.values[2];

            angleXZ = Math.atan2(accX,  accZ) * 180/Math.PI;
            angleYZ = Math.atan2(accY,  accZ) * 180/Math.PI;
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    }

    public DemoMapFragment() {
        //Basic Construction
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_demo_map, container, false);
        thisContext = container.getContext();

        demoDBManager = new DemoDBManager(thisContext, "demo.db", null, 1);

        ll_tMap = view.findViewById(R.id.ll_tMap);
        tv_curAddr = view.findViewById(R.id.tv_curAddr);
        iv_curWeather = view.findViewById(R.id.iv_curWeather);

        tMapView = new TMapView(thisContext);
        tMapData = new TMapData();

        demoDBManager.demoKookmin(KookminQueue);

//        drawPolyLine();
        startSubThreads();

        initMap();
        ll_tMap.addView(tMapView);

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isRun = false;
    }

    @Override
    public void onPause() {
        super.onPause();
        isRun = false;
    }

//    private void drawPolyLine() {
//        if(NaviFragment.startLat != 0.0 && NaviFragment.startLon != 0.0
//                && NaviFragment.dstLat != 0.0 && NaviFragment.dstLon != 0.0) {
//            startPoint = new TMapPoint(NaviFragment.startLat, NaviFragment.startLon);
//            endPoint = new TMapPoint(NaviFragment.dstLat, NaviFragment.dstLon);
//
//            tMapData.findPathData(startPoint, endPoint, new TMapData.FindPathDataListenerCallback() {
//                @Override
//                public void onFindPathData(TMapPolyLine tMapPolyLine) {
//                    tMapPolyLine.setLineWidth(12);
//                    tMapView.addTMapPath(tMapPolyLine);
//                }
//            });
//        }
//    }

    private void initMap() {
        double initLat, initLon;

        initLat = 37.6018;
        initLon = 127.0407;

        tMapView.setSKPMapApiKey(mApiKey);
        tMapView.setCenterPoint(initLon, initLat);
        tMapView.setZoomLevel(12);
    }

    private void drawCurMarker() {
        if(lat != 0.0 || lon != 0.0) {
            TMapPoint tMapPoint = new TMapPoint(lat, lon);
            TMapMarkerItem tItem = new TMapMarkerItem();

            tItem.setTMapPoint(tMapPoint);

            Bitmap bitmap = BitmapFactory.decodeResource(thisContext.getResources(),R.drawable.car);
            tItem.setIcon(bitmap);

            tMapView.addMarkerItem("현재위치", tItem);

            tMapView.setCenterPoint(lon, lat, false);
            tMapView.setZoomLevel(20);
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
//                        mSensorManager.registerListener(mAccLis, mAccelometerSensor, SensorManager.SENSOR_DELAY_UI);

                        if(isInit) {
                            initAngle = angleYZ;
                            realAngle = 0;
                            isInit = false;
                        } else {
                            realAngle = angleYZ - initAngle;
                        }

                        Log.e("REAL ANGLE", "Real Angle : " + realAngle);

//                        mSensorManager.unregisterListener(mAccLis);

                        Thread.sleep(1000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    android.os.Handler mainHandler = new android.os.Handler() {
        public void handleMessage(Message msg) {
            drawCurMarker();

            tMapData.convertGpsToAddress(lat, lon, new TMapData.ConvertGPSToAddressListenerCallback() {
                @Override
                public void onConvertToGPSToAddress(String s) {
                    addr = s;
                }
            });

            tv_curAddr.setText(addr);

            switch(weatherCode) {
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

            while(!KookminQueue.isEmpty()) {
                Message msg = Message.obtain();
                mainHandler.sendMessage(msg);

                try {
                    name = KookminQueue.peek().getName();
                    lat = KookminQueue.peek().getLatitude();
                    lon = KookminQueue.peek().getLongitude();
                    weatherCode = KookminQueue.peek().getWeather();

                    KookminQueue.remove();

                    Thread.sleep(1000);
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
