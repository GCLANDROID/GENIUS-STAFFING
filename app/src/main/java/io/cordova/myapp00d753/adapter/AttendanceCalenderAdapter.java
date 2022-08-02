package io.cordova.myapp00d753.adapter;


import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.module.AttendanceCalenderModel;
import io.cordova.myapp00d753.module.AttendanceModule;

public class AttendanceCalenderAdapter extends RecyclerView.Adapter<AttendanceCalenderAdapter.MyViewHolder> {
    ArrayList<AttendanceCalenderModel>itemList=new ArrayList<>();
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.attendance_calender_raw,viewGroup,false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        if (itemList.get(i).getStatus().equalsIgnoreCase("Approved")){
            myViewHolder.llMain.setBackgroundColor(Color.parseColor("#F202B32E"));
        }else if (itemList.get(i).getStatus().equalsIgnoreCase("Reject")){
            myViewHolder.llMain.setBackgroundColor(Color.parseColor("#F2B61111"));
        }else if (itemList.get(i).getStatus().equalsIgnoreCase("Approval Pending")){
            myViewHolder.llMain.setBackgroundColor(Color.parseColor("#f29b9105"));
        }else {
            myViewHolder.llMain.setBackgroundColor(Color.parseColor("#4f8888"));
        }

        myViewHolder.tvStatus.setText(itemList.get(i).getStatus());
        myViewHolder.tvDay.setText(itemList.get(i).getDay());
        myViewHolder.tvDate.setText(itemList.get(i).getDate());
        myViewHolder.tvTime.setText(itemList.get(i).getTime());




    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvDay,tvDate,tvTime,tvStatus;
        LinearLayout llMain;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate=(TextView) itemView.findViewById(R.id.tvDate);
            tvDay=(TextView) itemView.findViewById(R.id.tvDay);
            tvTime=(TextView) itemView.findViewById(R.id.tvTime);
            tvStatus=(TextView)itemView.findViewById(R.id.tvStatus);

            llMain=(LinearLayout) itemView.findViewById(R.id.llMain);

        }
    }

    public AttendanceCalenderAdapter(ArrayList<AttendanceCalenderModel> itemList) {
        this.itemList = itemList;
    }
}
