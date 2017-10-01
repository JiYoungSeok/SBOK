package com.example.youngseok.sbok;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Fragment fragment;

    public static boolean isBluetoothOn = false;
    public static boolean isGpsOn = false;

    private final BroadcastReceiver bluetoothReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
                switch (state) {
                    case BluetoothAdapter.STATE_OFF:
                        isBluetoothOn = false;
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        isBluetoothOn = false;
                        break;
                    case BluetoothAdapter.STATE_ON:
                        isBluetoothOn = true;
                        break;
                    case BluetoothAdapter.STATE_TURNING_ON:
                        isBluetoothOn = true;
                        break;
                }
            }
        }
    };

    private final BroadcastReceiver gpsReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().matches("android.location.PROVIDERS_CHANGED")) {
                final LocationManager manager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE );

                if(!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    isGpsOn = false;
                } else {
                    isGpsOn = true;
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();

        if(adapter != null) {
            if (!adapter.isEnabled()) isBluetoothOn = false;
            else isBluetoothOn = true;
        }


        final LocationManager manager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
        if(!manager.isProviderEnabled((LocationManager.GPS_PROVIDER))) isGpsOn = false;
        else isGpsOn = true;

        IntentFilter bluetoothFilter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        IntentFilter gpsFilter = new IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION);
        registerReceiver(bluetoothReceiver, bluetoothFilter);
        registerReceiver(gpsReceiver, gpsFilter);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        fragment = new MapFragment() ;
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_layout, fragment).commit() ;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        fragment = null;

        if (id == R.id.nav_map) {
            fragment = new MapFragment();
        } else if (id == R.id.nav_navi) {
            fragment = new NaviFragment();
        } else if (id == R.id.nav_extra1) {

        } else if (id == R.id.nav_extra2) {

        }

        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_layout, fragment).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
