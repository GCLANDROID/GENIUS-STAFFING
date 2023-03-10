package io.cordova.myapp00d753.adapter;


import android.content.res.ColorStateList;
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
        if (itemList.get(i).getStatus().equalsIgnoreCase("Approved")) {
            myViewHolder.llStatus.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#F205E33C")));
            myViewHolder.tvTime.setTextColor(Color.parseColor("#FFFFFF"));
        } else if (itemList.get(i).getStatus().equalsIgnoreCase("Reject")) {

            myViewHolder.llStatus.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#F2D80606")));
            myViewHolder.tvTime.setTextColor(Color.parseColor("#FFFFFF"));

        } else if (itemList.get(i).getStatus().equalsIgnoreCase("Approval Pending")) {

            myViewHolder.llStatus.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#F2066EF1")));
            myViewHolder.tvTime.setTextColor(Color.parseColor("#FFFFFF"));

        } else {
            myViewHolder.llStatus.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#F2DFDFDF")));
            myViewHolder.tvTime.setTextColor(Color.parseColor("#FFFFFF"));
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
        LinearLayout llStatus;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate=(TextView) itemView.findViewById(R.id.tvDate);
            tvDay=(TextView) itemView.findViewById(R.id.tvDay);
            tvTime=(TextView) itemView.findViewById(R.id.tvTime);
            tvStatus=(TextView)itemView.findViewById(R.id.tvStatus);

            llStatus=(LinearLayout) itemView.findViewById(R.id.llStatus);

        }
    }

    public AttendanceCalenderAdapter(ArrayList<AttendanceCalenderModel> itemList) {
        this.itemList = itemList;
    }
}
