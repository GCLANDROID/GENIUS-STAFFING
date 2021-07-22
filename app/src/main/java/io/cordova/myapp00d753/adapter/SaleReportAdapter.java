package io.cordova.myapp00d753.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.module.SaleModel;
import io.cordova.myapp00d753.module.SaleQTDModel;

public class SaleReportAdapter extends RecyclerView.Adapter<SaleReportAdapter.MyViewHolder> {
    ArrayList<SaleModel> itemList =new ArrayList<>();


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.duplicate_sale_report_raw,viewGroup,false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

        myViewHolder.tvDate.setText(itemList.get(i).getDate());
        myViewHolder.tvModel.setText(itemList.get(i).getCategory());
        myViewHolder.tvName.setText(itemList.get(i).getName());
        myViewHolder.tvToken.setText(itemList.get(i).getToke());
        myViewHolder.tvAppStatus.setText(itemList.get(i).getAppStatus());
        myViewHolder.tvMob.setText(itemList.get(i).getPhn());










    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate,tvModel,tvName,tvToken,tvAppStatus,tvMob;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate=(TextView)itemView.findViewById(R.id.tvDate);
            tvModel=(TextView)itemView.findViewById(R.id.tvModel);
            tvName=(TextView)itemView.findViewById(R.id.tvName);
            tvToken=(TextView)itemView.findViewById(R.id.tvToken);
            tvAppStatus=(TextView)itemView.findViewById(R.id.tvAppStatus);
            tvMob=(TextView)itemView.findViewById(R.id.tvMob);





        }
    }

    public SaleReportAdapter(ArrayList<SaleModel> itemList) {
        this.itemList = itemList;
    }
}
