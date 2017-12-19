package com.example.youngseok.sbok;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.skp.Tmap.TMapData;
import com.skp.Tmap.TMapMarkerItem;
import com.skp.Tmap.TMapPoint;
import com.skp.Tmap.TMapView;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class DemoMapFragment extends Fragment {

    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mConditionRef = mDatabase.child("State");

    private static String mApiKey = "e3ec92b4-d0be-396c-a3f6-1df217dec94a";
    private static TMapView tMapView;
    private static TMapData tMapData;

    private Queue<Demo> KookminQueue = new LinkedList<>();
    private Stack<Demo> KookminStack = new Stack<>();
    private Stack<Demo> KookminStack2 = new Stack<>();
    private DemoDBManager demoDBManager = null;

    private Context thisContext;

    private FrameLayout ll_tMap;
    private TextView tv_curAddr, tv_sunWarning;
    private ImageView iv_curWeather;

    private double lat, lon;
    private String name, addr;
    private int weatherCode;

    private int count = 0;
    private boolean flag = false;

    private long state;

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
        tv_sunWarning = view.findViewById(R.id.tv_sunWarning);
        iv_curWeather = view.findViewById(R.id.iv_curWeather);

        tMapView = new TMapView(thisContext);
        tMapData = new TMapData();

        demoDBManager.demoKookmin(KookminQueue);

        startSubThreads();

        initMap();
        ll_tMap.addView(tMapView);

        mConditionRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                state = dataSnapshot.getValue(Long.class);
                Log.e("STATE", Long.toString(state));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

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

            while(true) {
                Message msg = Message.obtain();
                mainHandler.sendMessage(msg);

                try {

                    if(!flag) {
                        name = KookminQueue.peek().getName();
                        lat = KookminQueue.peek().getLatitude();
                        lon = KookminQueue.peek().getLongitude();
                        weatherCode = KookminQueue.peek().getWeather();

                        KookminQueue.remove();
                        KookminStack.push(new Demo(name, lat, lon, weatherCode));

                        if(KookminQueue.isEmpty()) {
                            flag = true;
                        }
                    } else if(flag && count % 2 == 0) {
                        name = KookminStack.peek().getName();
                        lat = KookminStack.peek().getLatitude();
                        lon = KookminStack.peek().getLongitude();
                        weatherCode = KookminStack.peek().getWeather();

                        KookminStack.pop();
                        KookminStack2.push(new Demo(name, lat, lon, weatherCode));

                        if(KookminStack.isEmpty()) {
                            count++;
                        }
                    } else if(flag && count % 2 != 0) {
                        name = KookminStack2.peek().getName();
                        lat = KookminStack2.peek().getLatitude();
                        lon = KookminStack2.peek().getLongitude();
                        weatherCode = KookminStack2.peek().getWeather();

                        KookminStack2.pop();
                        KookminStack.push(new Demo(name, lat, lon, weatherCode));

                        if(KookminStack2.isEmpty()) {
                            count++;
                        }
                    }

                    if(state == 0) {
                        tv_sunWarning.setText("보통");
                        tv_sunWarning.setTextColor(Color.parseColor("#00FF00"));
                        MapFragment.mConnectedThread.write("s/0");
                    } else if(state == 1) {
                        tv_sunWarning.setText("위험");
                        tv_sunWarning.setTextColor(Color.parseColor("#FF0000"));
                        MapFragment.mConnectedThread.write("s/1");

                    }

                    Thread.sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
    }
}
