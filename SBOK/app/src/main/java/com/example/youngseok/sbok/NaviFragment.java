package com.example.youngseok.sbok;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.skp.Tmap.TMapData;
import com.skp.Tmap.TMapPOIItem;
import com.skp.Tmap.TMapPoint;

import java.util.ArrayList;

public class NaviFragment extends Fragment {

    private static TMapData tMapData = null;
    public static TMapPoint dstPoint = null;
    public static String destinationAddr = null;

    private ArrayList <RecentDestination> recentDestinationList = new ArrayList<>();
    private RecentDBManager recentDBManager = null;

    private Context thisContext;

    private POIBaseAdapter poiAdapter;
    private LocationManager locationManager;

    private String srcAddr;
    private String dstAddr;

    private double startLat, startLon;
    private double dstLat, dstLon;

    private TextView tv_src, tv_classification;
    private EditText et_dst;

    private ListView lv_poi;

    private Button bt_src;
    private Button bt_dst;
    private Button bt_findPath;

    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            startLat = location.getLatitude();
            startLon = location.getLongitude();
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

    private void startLocationService() {
        locationManager = (LocationManager) thisContext.getSystemService(Context.LOCATION_SERVICE);

        long minTime = 1000 * 5;
        float minDistance = 10;

        if (ActivityCompat.checkSelfPermission(thisContext, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(thisContext, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime, minDistance, mLocationListener);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, minTime, minDistance, mLocationListener);
    }

    public NaviFragment() {
        //Basic Construction
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_navi, container, false);
        thisContext = container.getContext();

        recentDBManager = new RecentDBManager(thisContext, "recent.db", null, 1);
//        recentDBManager.delete();

        tv_src = view.findViewById(R.id.tv_src);
        tv_classification = view.findViewById(R.id.tv_classification);
        et_dst = view.findViewById(R.id.et_dst);

        lv_poi = view.findViewById(R.id.lv_poi);

        bt_src = view.findViewById(R.id.bt_src);
        bt_dst = view.findViewById(R.id.bt_dst);
        bt_findPath = view.findViewById(R.id.bt_findPath);

        tMapData = new TMapData();
        makeList();

        RecentBaseAdapter recentAdapter = new RecentBaseAdapter(thisContext, recentDestinationList);
        lv_poi.setAdapter(recentAdapter);
        lv_poi.setOnItemClickListener(recentAdapter);

        bt_src.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                startLocationService();

                if(startLat != 0.0 || startLon != 0.0) {
                    tMapData.convertGpsToAddress(startLat, startLon, new TMapData.ConvertGPSToAddressListenerCallback() {
                        @Override
                        public void onConvertToGPSToAddress(String s) {
                            srcAddr = s;
                        }
                    });

                    tv_src.setText(srcAddr);
                } else {
                    Toast.makeText(thisContext, "위치 확인 중\n잠시 후 다시 시도하세요", Toast.LENGTH_SHORT).show();
                }
            }
        });
        bt_dst.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_classification.setText("검색 결과");
                dstAddr = et_dst.getText().toString();
                getPOI(dstAddr);
            }
        });
        bt_findPath.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(et_dst.getText().toString().equals("")) {

                } else {
                    dstLat = dstPoint.getLatitude();
                    dstLon = dstPoint.getLongitude();

                    recentDBManager.insert(dstPoint.getLatitude(), dstPoint.getLongitude(), et_dst.getText().toString(), destinationAddr);

                    Toast.makeText(thisContext, dstLat + " " + dstLon, Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    public void getPOI(String strData) {
        TMapData tMapData = new TMapData();

        tMapData.findAllPOI(strData, new TMapData.FindAllPOIListenerCallback() {
            @Override
            public void onFindAllPOI(ArrayList<TMapPOIItem> poiItem) {
                poiAdapter = new POIBaseAdapter(thisContext, poiItem);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                lv_poi.setAdapter(poiAdapter);
                                lv_poi.setOnItemClickListener(poiAdapter);
                            }
                        });
                    }
                }).start();
            }
        });
    }

    public void makeList() {
        recentDestinationList.clear();
        recentDBManager.showList(recentDestinationList);
    }
}