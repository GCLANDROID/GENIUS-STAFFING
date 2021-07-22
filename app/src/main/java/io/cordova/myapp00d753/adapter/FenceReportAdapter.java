package io.cordova.myapp00d753.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.module.ReportModel;

public class FenceReportAdapter extends RecyclerView.Adapter<FenceReportAdapter.MyViewHolder> {
    ArrayList<ReportModel>itemList=new ArrayList<>();
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.report_item_raw, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.tvAddress.setText(itemList.get(i).getAddress());
        myViewHolder.tvType.setText(itemList.get(i).getFenceType());
        myViewHolder.tvTime.setText(itemList.get(i).getLogTime());
        myViewHolder.tvDate.setText(itemList.get(i).getLogDate());

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvAddress,tvType,tvTime,tvDate;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAddress=(TextView)itemView.findViewById(R.id.tvAddress);
            tvTime=(TextView)itemView.findViewById(R.id.tvTime);
            tvType=(TextView)itemView.findViewById(R.id.tvType);
            tvDate=(TextView)itemView.findViewById(R.id.tvDate);
        }
    }

    public FenceReportAdapter(ArrayList<ReportModel> itemList) {
        this.itemList = itemList;
    }
}
