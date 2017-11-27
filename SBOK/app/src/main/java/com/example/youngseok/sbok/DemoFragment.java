package com.example.youngseok.sbok;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class DemoFragment extends Fragment {

    private Context thisContext;

    private Button bt_kookmin, bt_dbInit, bt_dbDelete;

    private DemoDBManager demoDBManager = null;

    public DemoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_demo, container, false);
        thisContext = container.getContext();

        demoDBManager = new DemoDBManager(thisContext, "demo.db", null, 1);

        bt_kookmin = view.findViewById(R.id.bt_kookmin);
        bt_dbInit = view.findViewById(R.id.bt_dbInit);
        bt_dbDelete = view.findViewById(R.id.bt_dbDelete);

        bt_kookmin.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new DemoMapFragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_layout, fragment).commit();
            }
        });

        bt_dbInit.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                demoDBManager.init();
            }
        });

        bt_dbDelete.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                demoDBManager.delete();
            }
        });

        return view;
    }
}
