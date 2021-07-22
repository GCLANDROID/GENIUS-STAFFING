package io.cordova.myapp00d753.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;



import java.util.List;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.module.OfflineSaleModel;

public class OfflineAttenAdapter extends ArrayAdapter<OfflineSaleModel> {
    private List<OfflineSaleModel> saleList;

    //context object
    private Context context;

    //constructor
    public OfflineAttenAdapter(Context context, int resource, List<OfflineSaleModel> saleList) {
        super(context, resource, saleList);
        this.context = context;
        this.saleList = saleList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //getting the layoutinflater
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //getting listview itmes
        View listViewItem = inflater.inflate(R.layout.raw, null, true);
        TextView tvInTime = (TextView) listViewItem.findViewById(R.id.tvInTime);
        TextView tvDate=(TextView)listViewItem.findViewById(R.id.tvDate);
        TextView tvOutTime=(TextView)listViewItem.findViewById(R.id.tvOutTime);
        TextView tvLocation=(TextView)listViewItem.findViewById(R.id.tvLocation);


        //getting the current name
        OfflineSaleModel name = saleList.get(position);

        //setting the name to textview
        tvInTime.setText(name.getDate());
        tvDate.setText(name.getDate());
        tvOutTime.setText(name.getDate());
        tvLocation.setText(name.getAddress());

        return listViewItem;
    }
}
