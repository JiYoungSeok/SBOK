package com.example.youngseok.sbok;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class SettingFragment extends Fragment {

    private Context thisContext;

    private SharedPreferences sharedPreferences;

    private SeekBar sb_level1, sb_level2;
    private TextView tv_level1, tv_level2;

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

        tv_level1.setText(String.valueOf(level1));
        tv_level2.setText(String.valueOf(level2));

        sb_level1.setProgress(level1);
        sb_level2.setProgress(level2);

//        ViewTreeObserver vto = sb_level1.getViewTreeObserver();
//        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
//            public boolean onPreDraw() {
//                Resources res = getResources();
//                Drawable thumb = res.getDrawable(R.drawable.thumb);
//                int h = (int)(sb_level1.getMeasuredHeight() * 1.5); // 8 * 1.5 = 12
//                int w = h;
//                Bitmap bmpOrg = ((BitmapDrawable)thumb).getBitmap();
//                Bitmap bmpScaled = Bitmap.createScaledBitmap(bmpOrg, w, h, true);
//                Drawable newThumb = new BitmapDrawable(res, bmpScaled);
//                newThumb.setBounds(0, 0, newThumb.getIntrinsicWidth(), newThumb.getIntrinsicHeight());
//                sb_level1.setThumb(newThumb);
//                sb_level2.setThumb(newThumb);
//
//                sb_level1.getViewTreeObserver().removeOnPreDrawListener(this);
//                sb_level2.getViewTreeObserver().removeOnPreDrawListener(this);
//
//                return true;
//            }
//        });

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
        return view;
    }
}
