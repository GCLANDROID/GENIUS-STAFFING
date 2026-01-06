package io.cordova.myapp00d753.activity.NEW.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.activity.NEW.model.AttendanceReportModel;
import io.cordova.myapp00d753.module.AttendanceModule;

public class NEW_AttendanceAdapter extends RecyclerView.Adapter<NEW_AttendanceAdapter.MyViewHolder> {
    ArrayList<AttendanceReportModel>attendanceInfoList;

    public NEW_AttendanceAdapter(ArrayList<AttendanceReportModel> attendanceInfoList) {
        this.attendanceInfoList = attendanceInfoList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.new_attendance_report_row,viewGroup,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

        if (!attendanceInfoList.get(i).getDate().equals("")) {
            myViewHolder.tvDate.setText(attendanceInfoList.get(i).getDate());
        }else {
            myViewHolder.tvDate.setText("-");
        }
        if (!attendanceInfoList.get(i).getIntime().equals("")) {
            myViewHolder.tvInTime.setText(attendanceInfoList.get(i).getIntime());
        }else {
            myViewHolder.tvInTime.setText("--");
        }
        if (!attendanceInfoList.get(i).getOutTime().equals("")) {
            myViewHolder.tvOutTime.setText(attendanceInfoList.get(i).getOutTime());
        } else {
            myViewHolder.tvOutTime.setText("--");
        }

        if (!attendanceInfoList.get(i).getDayType().equals("null")) {
            myViewHolder.tvType.setText(attendanceInfoList.get(i).getDayType());
        }else {
            myViewHolder.tvType.setText("--");
        }

        if (!attendanceInfoList.get(i).getWorkingShift().equals("null")) {
            myViewHolder.tvShift.setText(attendanceInfoList.get(i).getWorkingShift());
        }else {
            myViewHolder.tvShift.setText("--");
        }

        if (!attendanceInfoList.get(i).getApprovalStatus().equals("null")) {
            myViewHolder.tvStatus.setText(attendanceInfoList.get(i).getApprovalStatus());
        }else {
            myViewHolder.tvStatus.setText("--");
        }

        if (!attendanceInfoList.get(i).getInAddress().equals("null")) {
            myViewHolder.tvInAddress.setText(attendanceInfoList.get(i).getInAddress());
        }else {
            myViewHolder.tvInAddress.setText("--");
        }

        if (!attendanceInfoList.get(i).getOutAddress().equals("null") ) {
            myViewHolder.tvOutAddress.setText(attendanceInfoList.get(i).getOutAddress());
        }else {
            myViewHolder.tvOutAddress.setText("--");
        }

        if (!attendanceInfoList.get(i).getLocation().equals("null") ) {
            myViewHolder.tvLocation.setText(attendanceInfoList.get(i).getLocation());
        }else {
            myViewHolder.tvLocation.setText("--");
        }

    }

    @Override
    public int getItemCount() {
        return attendanceInfoList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate,tvInTime,tvOutTime,tvLocation,tvType,tvStatus,tvOutAddress,tvInAddress,tvShift;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate=(TextView)itemView.findViewById(R.id.tvDate);
            tvInTime=(TextView)itemView.findViewById(R.id.tvInTime);
            tvOutTime=(TextView)itemView.findViewById(R.id.tvOutTime);
            tvLocation=(TextView)itemView.findViewById(R.id.tvLocation);
            tvType=(TextView)itemView.findViewById(R.id.tvType);
            tvStatus=(TextView)itemView.findViewById(R.id.tvStatus);
            tvOutAddress=(TextView) itemView.findViewById(R.id.tvOutAddress);
            tvInAddress=(TextView) itemView.findViewById(R.id.tvInAddress);
            tvShift=(TextView) itemView.findViewById(R.id.tvShift);
        }
    }
}
