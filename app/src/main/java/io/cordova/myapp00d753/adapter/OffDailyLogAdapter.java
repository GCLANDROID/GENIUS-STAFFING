package io.cordova.myapp00d753.adapter;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.module.OfflineDailyModel;


public class OffDailyLogAdapter extends RecyclerView.Adapter<OffDailyLogAdapter.MyViewHolder> {
    ArrayList<OfflineDailyModel>activityList=new ArrayList<>();
    Context context;
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.offline_daily_raw,viewGroup,false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int i) {
        myViewHolder.tvDate.setText(activityList.get(i).getDate());
        myViewHolder.tvInTime.setText(activityList.get(i).getInTime());
        myViewHolder.tvOutTime.setText(activityList.get(i).getOutTime());
        myViewHolder.tvInLocation.setText(activityList.get(i).getInLocation());
        myViewHolder.tvOutLocation.setText(activityList.get(i).getOutLocation());
        myViewHolder.tvRemarksIn.setText(activityList.get(i).getInRemarks());
        myViewHolder.tvRemarksOut.setText(activityList.get(i).getOutRemarks());






        if (activityList.get(i).getInRemarks().equals("")){
            myViewHolder.llInRemark.setVisibility(View.GONE);
        }else {
            myViewHolder.llInRemark.setVisibility(View.VISIBLE);
            myViewHolder.tvRemarksIn.setText(activityList.get(i).getInRemarks());
        }

        if (activityList.get(i).getOutRemarks().equals("")){
            myViewHolder.llOutRemark.setVisibility(View.GONE);
        }else {
            myViewHolder.llOutRemark.setVisibility(View.VISIBLE);
            myViewHolder.tvRemarksOut.setText(activityList.get(i).getOutRemarks());
        }

        if (activityList.get(i).getOutTime().equals("")){
            myViewHolder.llOutTime.setVisibility(View.GONE);
        }else {
            myViewHolder.llOutTime.setVisibility(View.VISIBLE);
        }

        if (activityList.get(i).getOutLocation().equals("")){
            myViewHolder.llOutLocation.setVisibility(View.GONE);
        }else {
            myViewHolder.llOutLocation.setVisibility(View.VISIBLE);
        }



    }

    @Override
    public int getItemCount() {
        return activityList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate,tvInTime,tvOutTime,tvInLocation,tvOutLocation,tvRemarksIn,tvRemarksOut;
        LinearLayout llInRemark,llOutRemark,llOutTime,llOutLocation;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate=(TextView)itemView.findViewById(R.id.tvDate);
            tvInTime=(TextView)itemView.findViewById(R.id.tvInTime);
            tvOutTime=(TextView)itemView.findViewById(R.id.tvOutTime);
            tvInLocation=(TextView)itemView.findViewById(R.id.tvInLocation);
            tvOutLocation=(TextView)itemView.findViewById(R.id.tvOutLocation);
            tvRemarksIn=(TextView)itemView.findViewById(R.id.tvRemarksIn);
            tvRemarksOut=(TextView)itemView.findViewById(R.id.tvRemarksOut);
            llInRemark=(LinearLayout)itemView.findViewById(R.id.llInRemraks);
            llOutRemark=(LinearLayout)itemView.findViewById(R.id.llOutRemraks);
            llOutTime=(LinearLayout)itemView.findViewById(R.id.llOutTime);
            llOutLocation=(LinearLayout)itemView.findViewById(R.id.llOutLocation);



        }
    }

    public OffDailyLogAdapter(ArrayList<OfflineDailyModel> activityList, Context context) {
        this.activityList = activityList;
        this.context = context;
    }
}
