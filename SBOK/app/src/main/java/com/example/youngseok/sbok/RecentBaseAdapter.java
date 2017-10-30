package com.example.youngseok.sbok;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.skp.Tmap.TMapPoint;

import java.util.ArrayList;

public class RecentBaseAdapter extends BaseAdapter implements AdapterView.OnItemClickListener {

    private ArrayList<RecentDestination> mData = null;
    private LayoutInflater mLayoutInflater = null;

    RecentBaseAdapter(Context context, ArrayList<RecentDestination> data) {
        Context mContext = context;
        mData = data;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        RecentDestination recentDestination = (RecentDestination) parent.getItemAtPosition(position);

        TMapPoint tMapPoint = new TMapPoint(recentDestination.getLatitude(), recentDestination.getLongitude());

        EditText et_Dst = view.getRootView().findViewById(R.id.et_dst);
        et_Dst.setText(recentDestination.getName());
        NaviFragment.dstPoint = tMapPoint;
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
            convertView = mLayoutInflater.inflate(R.layout.list_view_recent_layout, null);
        }

        TextView tv_RecentName = convertView.findViewById(R.id.tv_recentName);
        TextView tv_RecentAddr = convertView.findViewById(R.id.tv_recentAddr);

        RecentDestination recentDestination = mData.get(position);

        tv_RecentName.setText(recentDestination.getName());
        tv_RecentAddr.setText(recentDestination.getAddr());

        return convertView;
    }
}
