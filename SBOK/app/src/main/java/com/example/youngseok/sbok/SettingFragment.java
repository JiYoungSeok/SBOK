package com.example.youngseok.sbok;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    private int level1, level2;

    public SettingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_setting, container, false);
        thisContext = container.getContext();

        sharedPreferences = thisContext.getSharedPreferences("SensorValue", Context.MODE_PRIVATE);

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
                MapFragment.mConnectedThread.write("c/1/" + level1 + "/2/" + level2 + ";");
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
