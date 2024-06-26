package io.cordova.myapp00d753.activity.metso.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.module.AttendanceModule;

public class MetsoAttendanceReportAdapter extends RecyclerView.Adapter<MetsoAttendanceReportAdapter.MyViewHolder>{
    Context context;
    ArrayList<AttendanceModule> attendanceInfoList;

    public MetsoAttendanceReportAdapter(Context context, ArrayList<AttendanceModule> attendabceInfiList) {
        this.context = context;
        this.attendanceInfoList = attendabceInfiList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.metso_attendance_report_row,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position) {
        if (!attendanceInfoList.get(position).getAttendanceDate().equals("")) {
            myViewHolder.tvDate.setText(attendanceInfoList.get(position).getAttendanceDate());
        }else {
            myViewHolder.tvDate.setText("-");
        }
        if (!attendanceInfoList.get(position).getAttendanceInTime().equals("")) {
            myViewHolder.tvInTime.setText(attendanceInfoList.get(position).getAttendanceInTime());
        }else {
            myViewHolder.tvInTime.setText("-");
        }
        if (!attendanceInfoList.get(position).getAttendanceOutTime().equals("")) {
            myViewHolder.tvOutTime.setText(attendanceInfoList.get(position).getAttendanceOutTime());
        }
        else {
            myViewHolder.tvOutTime.setText("-");
        }
        if (!attendanceInfoList.get(position).getAttendanceLocation().equals("")) {
            myViewHolder.tvAddress.setText(attendanceInfoList.get(position).getAttendanceLocation());
        }else {
            myViewHolder.tvAddress.setText("-");
        }

        if (!attendanceInfoList.get(position).getAttendanceType().equals("")) {
            myViewHolder.tvType.setText(attendanceInfoList.get(position).getAttendanceType());
        }else {
            myViewHolder.tvType.setText("-");
        }

        if (!attendanceInfoList.get(position).getAttendanceReply().equals("")) {
            myViewHolder.tvStatus.setText(attendanceInfoList.get(position).getAttendanceReply());
        }else {
            myViewHolder.tvStatus.setText("-");
        }

        if (!attendanceInfoList.get(position).getShift().equals("")) {
            myViewHolder.tvShift.setText(attendanceInfoList.get(position).getShift());
        }else {
            myViewHolder.tvShift.setText("-");
        }

        if (!attendanceInfoList.get(position).getLocation().equals("")) {
            myViewHolder.tvLocation.setText(attendanceInfoList.get(position).getLocation());
        }else {
            myViewHolder.tvLocation.setText("-");
        }

    }

    @Override
    public int getItemCount() {
        return attendanceInfoList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tvInTime,tvOutTime,tvType,tvStatus,tvShift,tvLocation,tvAddress,tvDate;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvInTime = itemView.findViewById(R.id.tvInTime);
            tvOutTime = itemView.findViewById(R.id.tvOutTime);
            tvType = itemView.findViewById(R.id.tvType);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvShift = itemView.findViewById(R.id.tvShift);
            tvLocation = itemView.findViewById(R.id.tvLocation);
            tvAddress = itemView.findViewById(R.id.tvAddress);
            tvDate = itemView.findViewById(R.id.tvDate);
        }
    }
}
