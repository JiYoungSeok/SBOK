package com.example.youngseok.sbok;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.UUID;

public class SettingFragment extends Fragment {

    private static final String TAG = "BLUETOOTH";

    private Context thisContext;

    private SharedPreferences sharedPreferences;

    private SeekBar sb_level1, sb_level2;
    private TextView tv_level1, tv_level2;
    private Button bt_apply;

    Handler h;

    final int RECIEVE_MESSAGE = 1;
    private BluetoothAdapter btAdapter = null;
    private BluetoothSocket btSocket = null;
    private StringBuilder sb = new StringBuilder();
    private ConnectedThread mConnectedThread;

    // SPP UUID service
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    // MAC-address of Bluetooth module
    private static String address = "00:15:83:35:5B:06";

    private int level1, level2;

    public SettingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_setting, container, false);
        thisContext = container.getContext();

        sharedPreferences = thisContext.getSharedPreferences("SensorValue", Context.MODE_PRIVATE);

        btAdapter = BluetoothAdapter.getDefaultAdapter();
        checkBTState();

        level1 = sharedPreferences.getInt("Level1", 50);
        level2 = sharedPreferences.getInt("Level2", 50);
        final boolean isSaved1 = sharedPreferences.getBoolean("Saved1", false);
        final boolean isSaved2 = sharedPreferences.getBoolean("Saved2", false);

        sb_level1 = view.findViewById(R.id.sb_level1);
        sb_level2 = view.findViewById(R.id.sb_level2);

        tv_level1 = view.findViewById(R.id.tv_level1);
        tv_level2 = view.findViewById(R.id.tv_level2);

        bt_apply = view.findViewById(R.id.bt_apply);

        tv_level1.setText(String.valueOf(level1));
        tv_level2.setText(String.valueOf(level2));

        sb_level1.setProgress(level1);
        sb_level2.setProgress(level2);

        sb_level1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                tv_level1.setText(String.valueOf(progress));
                level1 = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if(!isSaved1) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("Level1", level1);
                    editor.putBoolean("Saved1", true);
                    editor.commit();
                } else {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("Level1", level1);
                    editor.apply();
                }
            }
        });
        sb_level2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                tv_level2.setText(String.valueOf(progress));
                level2 = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if(!isSaved2) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("Level2", level2);
                    editor.putBoolean("Saved2", true);
                    editor.commit();
                } else {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("Level2", level2);
                    editor.apply();
                }
            }
        });
        bt_apply.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                mConnectedThread.write("1/" + level1 + "/2/" + level2 + ";");
            }
        });

        return view;
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

    @Override
    public void onResume() {
        super.onResume();

        Log.d(TAG, "...onResume - try connect...");

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

        mConnectedThread = new ConnectedThread(btSocket);
        mConnectedThread.start();
    }

    @Override
    public void onPause() {
        super.onPause();

        Log.d(TAG, "...In onPause()...");

        try     {
            btSocket.close();
        } catch (IOException e2) {
            errorExit("Fatal Error", "In onPause() and failed to close socket." + e2.getMessage() + ".");
        }
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

    private void errorExit(String title, String message){
        Toast.makeText(getContext(), title + " - " + message, Toast.LENGTH_LONG).show();
        getActivity().finish();
    }

    private class ConnectedThread extends Thread {
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
