package io.cordova.myapp00d753.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.module.MulFenceConfigModel;


public class MultipleConfigAdapter extends RecyclerView.Adapter<MultipleConfigAdapter.MyViewHolder> {
    ArrayList<MulFenceConfigModel>configList=new ArrayList<>();

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.multiple_fencing_config_raw,viewGroup,false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.tvCreatedOn.setText(configList.get(i).getCraetdOn());
        myViewHolder.tvAddress.setText(configList.get(i).getFencePoint());
        myViewHolder.tvLocation.setText(configList.get(i).getAddress());

    }

    @Override
    public int getItemCount() {
        return configList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvCreatedOn,tvAddress,tvLocation;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCreatedOn=(TextView)itemView.findViewById(R.id.tvDate);
            tvAddress=(TextView)itemView.findViewById(R.id.tvAddress);
            tvLocation=(TextView)itemView.findViewById(R.id.tvLocation);

        }
    }

    public MultipleConfigAdapter(ArrayList<MulFenceConfigModel> configList) {
        this.configList = configList;
    }
}
