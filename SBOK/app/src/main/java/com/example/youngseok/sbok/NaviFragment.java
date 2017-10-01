package com.example.youngseok.sbok;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;

public class NaviFragment extends Fragment {

    public NaviFragment() {
        //Basic Construction
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_navi, container, false);


        return view;
    }
}
