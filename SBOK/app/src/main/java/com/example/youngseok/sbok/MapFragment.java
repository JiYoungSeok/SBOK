package com.example.youngseok.sbok;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.icu.util.Calendar;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.skp.Tmap.TMapData;
import com.skp.Tmap.TMapMarkerItem;
import com.skp.Tmap.TMapPoint;
import com.skp.Tmap.TMapPolyLine;
import com.skp.Tmap.TMapView;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.UUID;

public class MapFragment extends Fragment {

    public static int flag = 0;

    private static final String TAG = "BLUETOOTH";

    private static String mApiKey = "e3ec92b4-d0be-396c-a3f6-1df217dec94a";
    private static TMapView tMapView;
    private static TMapData tMapData;
    private static TMapPoint startPoint;
    private static TMapPoint endPoint;
    private static Weather weather = new Weather();

    final int RECIEVE_MESSAGE = 1;
    private BluetoothAdapter btAdapter = null;
    private BluetoothSocket btSocket = null;
    private StringBuilder sb = new StringBuilder();
    public static ConnectedThread mConnectedThread;

    private SensorManager mSensorManager = null;
    private SensorEventListener mAccLis;
    private Sensor mAccelometerSensor = null;

    Handler h;

    private Context thisContext;

    private SunAltitude sunAltitude;
    private SunSet sunSet;

    private LocationManager locationManager;

    private FrameLayout ll_tMap;
    private ImageButton bt_auto;
    private TextView tv_curAddr, tv_sunWarning;
    private ImageView iv_curWeather;

    public static double curLat;
    public static double curLon;

    public static String curAddr;

    private boolean isRun = true;
    private boolean isInit = true;

    private int year, month, date, convertDate;
    private int hour, min, sec, convertTime;
    private double angleXZ, angleYZ;
    private double initAngle, realAngle, betweenAngle;

    private int count = 0;

    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    private static String address = "00:15:83:35:5B:06";

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

    private void startLocationService () {
        locationManager = (LocationManager) thisContext.getSystemService(Context.LOCATION_SERVICE);

        long minTime = 1000 * 5;
        float minDistance = 10;

        if (ActivityCompat.checkSelfPermission(thisContext, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(thisContext, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime, minDistance, mLocationListener);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, minTime, minDistance, mLocationListener);
    }

    public MapFragment() {
        //Basic Construction
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_map, container, false);
        thisContext = container.getContext();

        btAdapter = BluetoothAdapter.getDefaultAdapter();
        checkBTState();

        mSensorManager = (SensorManager) thisContext.getSystemService(Context.SENSOR_SERVICE);

        //Using the Accelometer
        mAccelometerSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mAccLis = new MapFragment.AccelometerListener();

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

        ll_tMap = view.findViewById(R.id.ll_tMap);
        bt_auto = view.findViewById(R.id.bt_auto);
        tv_curAddr = view.findViewById(R.id.tv_curAddr);
        tv_sunWarning = view.findViewById(R.id.tv_sunWarning);
        iv_curWeather = view.findViewById(R.id.iv_curWeather);

        tMapView = new TMapView(thisContext);
        tMapData = new TMapData();

        drawPolyLine();

        initMap();
        ll_tMap.addView(tMapView);
        startSubThreads();

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

    @Override
    public void onResume() {
        super.onResume();

        Log.d(TAG, "...onResume - try connect...");

        if(flag == 0) {
            BluetoothDevice device = btAdapter.getRemoteDevice(address);

            try {
                btSocket = createBluetoothSocket(device);
            } catch (IOException e) {
                errorExit("Fatal Error", "In onResume() and socket create failed: " + e.getMessage() + ".");
            }

            btAdapter.cancelDiscovery();

            Log.d(TAG, "...Connecting...");
            try {
                btSocket.connect();
                Log.d(TAG, "....Connection ok...");
            } catch (IOException e) {
                try {
                    btSocket.close();
                } catch (IOException e2) {
                    errorExit("Fatal Error", "In onResume() and unable to close socket during connection failure" + e2.getMessage() + ".");
                }
            }

            Log.d(TAG, "...Create Socket...");

            mConnectedThread = new MapFragment.ConnectedThread(btSocket);
            mConnectedThread.start();
            flag = 1;
        }
    }

    private void errorExit(String title, String message){
        Toast.makeText(getContext(), title + " - " + message, Toast.LENGTH_LONG).show();
        getActivity().finish();
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

        Log.d(TAG, "...In onPause()...");
    }

    private void drawPolyLine() {
        if(NaviFragment.startLat != 0.0 && NaviFragment.startLon != 0.0
                && NaviFragment.dstLat != 0.0 && NaviFragment.dstLon != 0.0) {
            startPoint = new TMapPoint(NaviFragment.startLat, NaviFragment.startLon);
            endPoint = new TMapPoint(NaviFragment.dstLat, NaviFragment.dstLon);

            tMapData.findPathData(startPoint, endPoint, new TMapData.FindPathDataListenerCallback() {
                @Override
                public void onFindPathData(TMapPolyLine tMapPolyLine) {
                    tMapPolyLine.setLineWidth(12);
                    tMapView.addTMapPath(tMapPolyLine);
                }
            });
        }
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
                        mSensorManager.registerListener(mAccLis, mAccelometerSensor, SensorManager.SENSOR_DELAY_UI);

                        sunAltitude.getAltitudeInfo(convertDate, curLat, curLon);
                        sunSet.getInfo(curLat, curLon, convertDate);
                        weather.webData(curLat, curLon);

                        if(isInit) {
                            realAngle = 0;

                            count++;

                            if(count == 10) {
                                initAngle = angleYZ;
                                isInit = false;
                            }
                            Log.e("INIT ANGLE", "Init Angle : " + initAngle);
                        } else {
                            realAngle = angleYZ - initAngle;
                        }

                        Log.e("ANGLE YZ", "Angle YZ : " + angleYZ);
                        Log.e("INIT ANGLE", "Init Angle : " + initAngle);
                        Log.e("REAL ANGLE", "Real Angle : " + realAngle);

                        betweenAngle = 100;

                        int sunSetTime = Integer.parseInt(sunSet.getSunSet());
                        int sunRiseTime = Integer.parseInt(sunSet.getSunRise());

                        if(sunSetTime != 0 && sunRiseTime != 0) {
                            betweenAngle = sunAltitude.calcAltitude(sunRiseTime, sunSetTime, convertTime) - realAngle;
                            Log.e("BETWEEN ANGLE", "between angle : " + betweenAngle);
                            Log.e("CURRENT_ALTITUDE", "현재고도 : " + sunAltitude.calcAltitude(sunRiseTime, sunSetTime, convertTime));
                        }

                        mSensorManager.unregisterListener(mAccLis);

                        if(betweenAngle < 15 && weather.getCurWeather() == 0) {
                            mConnectedThread.write("s/1;");
                        } else {
                            mConnectedThread.write("s/0;");
                        }

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
            startLocationService();
            drawCurMarker();

            tMapData.convertGpsToAddress(curLat, curLon, new TMapData.ConvertGPSToAddressListenerCallback() {
                @Override
                public void onConvertToGPSToAddress(String s) {
                    curAddr = s;
                }
            });

            tv_curAddr.setText(curAddr);

            if(Integer.parseInt(sunSet.getSunRise()) < convertTime && convertTime < Integer.parseInt(sunSet.getSunSet()) &&
                    0 < betweenAngle && betweenAngle < 15 &&
                    weather.getCurWeather() == 0) {
                tv_sunWarning.setText("위험");
                tv_sunWarning.setTextColor(Color.parseColor("#FF0000"));
            } else if((betweenAngle < 0 || betweenAngle > 15)) {
                tv_sunWarning.setText("보통");
                tv_sunWarning.setTextColor(Color.parseColor("#00FF00"));
            } else if(weather.getCurWeather() != 0) {
                tv_sunWarning.setText("보통");
                tv_sunWarning.setTextColor(Color.parseColor("#00FF00"));
            } else if(convertTime < Integer.parseInt(sunSet.getSunRise()) || convertTime > Integer.parseInt(sunSet.getSunSet())) {
                tv_sunWarning.setText("보통");
                tv_sunWarning.setTextColor(Color.parseColor("#00FF00"));
            }

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

    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {
        if(Build.VERSION.SDK_INT >= 10){
            try {
                final Method m = device.getClass().getMethod("createInsecureRfcommSocketToServiceRecord", new Class[] { UUID.class });
                return (BluetoothSocket) m.invoke(device, MY_UUID);
            } catch (Exception e) {
                Log.e(TAG, "Could not create Insecure RFComm Connection",e);
            }
        }
        return  device.createRfcommSocketToServiceRecord(MY_UUID);
    }

    private void checkBTState() {
        if(btAdapter==null) {
            errorExit("Fatal Error", "Bluetooth not support");
        } else {
            if (btAdapter.isEnabled()) {
                Log.d(TAG, "...Bluetooth ON...");
            } else {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, 1);
            }
        }
    }

    public class ConnectedThread extends Thread {
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) { }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            byte[] buffer = new byte[256];
            int bytes;

            while (true) {
                try {
                    bytes = mmInStream.read(buffer);
                    h.obtainMessage(RECIEVE_MESSAGE, bytes, -1, buffer).sendToTarget();
                } catch (IOException e) {
                    break;
                }
            }
        }

        public void write(String message) {
            Log.d(TAG, "...Data to send: " + message + "...");
            byte[] msgBuffer = message.getBytes();
            try {
                mmOutStream.write(msgBuffer);
            } catch (IOException e) {
                Log.d(TAG, "...Error data send: " + e.getMessage() + "...");
            }
        }
    }
}
