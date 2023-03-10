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
import io.cordova.myapp00d753.utility.Util;

public class DaywiseAttendanceCalenderAdapter extends RecyclerView.Adapter<DaywiseAttendanceCalenderAdapter.MyViewHolder> {
    ArrayList<AttendanceCalenderModel> itemList = new ArrayList<>();

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.daywise_attendance_calendar, viewGroup, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        if (itemList.get(i).getStatus().equalsIgnoreCase("Approved")) {
            myViewHolder.llStatus.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#F205E33C")));
            myViewHolder.tvTiming.setTextColor(Color.parseColor("#F2000000"));
        } else if (itemList.get(i).getStatus().equalsIgnoreCase("Reject")) {

            myViewHolder.llStatus.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#F2D80606")));
            myViewHolder.tvTiming.setTextColor(Color.parseColor("#F2000000"));

        } else if (itemList.get(i).getStatus().equalsIgnoreCase("Approval Pending")) {

            myViewHolder.llStatus.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#F2066EF1")));
            myViewHolder.tvTiming.setTextColor(Color.parseColor("#F2000000"));

        } else {

        }

        myViewHolder.tvStatus.setText(itemList.get(i).getStatus());
        myViewHolder.tvDay.setText(Util.changeAnyDateFormat(itemList.get(i).getDate(), "dd MMM yy", "dd"));
        myViewHolder.tvTiming.setText(itemList.get(i).getTime());


    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvDay, tvTiming, tvStatus;
        LinearLayout llStatus;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDay = (TextView) itemView.findViewById(R.id.tvDay);
            tvTiming = (TextView) itemView.findViewById(R.id.tvTiming);
            tvStatus = (TextView) itemView.findViewById(R.id.tvStatus);

            llStatus = (LinearLayout) itemView.findViewById(R.id.llStatus);

        }
    }

    public DaywiseAttendanceCalenderAdapter(ArrayList<AttendanceCalenderModel> itemList) {
        this.itemList = itemList;
    }
}
