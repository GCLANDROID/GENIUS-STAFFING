package io.cordova.myapp00d753.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;



import io.cordova.myapp00d753.R;

/**
 * Created by kiit on 13-04-2018.
 */

public class SpinnerAdapter extends BaseAdapter {


    Context context;
    String[] spItem;
    LayoutInflater inflter;
    @Override
    public int getCount() {
        return spItem.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.spinner_raw, null);

        TextView tvSpItem = (TextView) view.findViewById(R.id.tvSpItem);
        tvSpItem.setText(spItem[i]);
        return view;
    }

    public SpinnerAdapter(Context context, String[] aircrafttype) {
        this.context = context;
        this.spItem = aircrafttype;
        inflter = (LayoutInflater.from(context));
    }
}
