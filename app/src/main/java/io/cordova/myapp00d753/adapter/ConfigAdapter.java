package io.cordova.myapp00d753.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.module.ConfigReportModel;


public class ConfigAdapter extends RecyclerView.Adapter<ConfigAdapter.MyViewHolder> {
    ArrayList<ConfigReportModel>configList=new ArrayList<>();

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.config_report_raw,viewGroup,false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.tvCreatedOn.setText(configList.get(i).getCreatedOn());
        myViewHolder.tvAddress.setText(configList.get(i).getAddress());
        myViewHolder.tvLocation.setText(configList.get(i).getLocationName());
        myViewHolder.tvSLat.setText(configList.get(i).getsLat());
        myViewHolder.tvSLong.setText(configList.get(i).getsLong());
        myViewHolder.tvRadius.setText(configList.get(i).getEndPoint());


    }

    @Override
    public int getItemCount() {
        return configList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvCreatedOn,tvAddress,tvLocation,tvSLat,tvSLong,tvRadius;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCreatedOn=(TextView)itemView.findViewById(R.id.tvDate);
            tvAddress=(TextView)itemView.findViewById(R.id.tvAddress);
            tvLocation=(TextView)itemView.findViewById(R.id.tvLocation);

            tvSLat=(TextView)itemView.findViewById(R.id.tvSlat);
            tvSLong=(TextView)itemView.findViewById(R.id.tvSlong);
            tvRadius=(TextView)itemView.findViewById(R.id.tvRadius);

        }
    }

    public ConfigAdapter(ArrayList<ConfigReportModel> configList) {
        this.configList = configList;
    }
}
