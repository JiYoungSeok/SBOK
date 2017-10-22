package com.example.youngseok.sbok;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.skp.Tmap.TMapPOIItem;

import java.util.ArrayList;

public class POIBaseAdapter extends BaseAdapter implements AdapterView.OnItemClickListener {
    private ArrayList<TMapPOIItem> mData = null;
    private LayoutInflater mLayoutInflater = null;
    private int selected_position;

    POIBaseAdapter(Context context, ArrayList<TMapPOIItem> data){
        Context mContext = context;
        mData = data;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TMapPOIItem tMapPOIItem = (TMapPOIItem) parent.getItemAtPosition(position);

        EditText et_Dst = (EditText) view.getRootView().findViewById(R.id.et_dst);
        et_Dst.setText(tMapPOIItem.getPOIName());
        NaviFragment.dstPoint = tMapPOIItem.getPOIPoint();
        this.selected_position = position;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.list_view_layout, null);
        }

        TextView tv_Name = (TextView) convertView.findViewById(R.id.tv_name);
        TextView tv_Addr = (TextView) convertView.findViewById(R.id.tv_addr);

        TMapPOIItem poiItem = mData.get(position);

        tv_Name.setText(poiItem.getPOIName());
        tv_Addr.setText(poiItem.getPOIAddress().replace("null", ""));

        NaviFragment.dstPoint = poiItem.getPOIPoint();
        return convertView;
    }
}
