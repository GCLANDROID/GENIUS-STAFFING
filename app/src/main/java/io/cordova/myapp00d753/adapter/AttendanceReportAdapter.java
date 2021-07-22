package io.cordova.myapp00d753.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.module.AttendancereportModule;

public class AttendanceReportAdapter extends RecyclerView.Adapter<AttendanceReportAdapter.MyViewHolder> {
    ArrayList<AttendancereportModule>attendanceInfoList=new ArrayList<>();
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.report_raw,viewGroup,false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

        if (!attendanceInfoList.get(i).getAttendanceDate().equals("")) {
            myViewHolder.tvDate.setText(attendanceInfoList.get(i).getAttendanceDate());
        }else {
            myViewHolder.tvDate.setText("");
        }
        if (!attendanceInfoList.get(i).getAttendanceInTime().equals("")) {

            myViewHolder.tvInTime.setText(attendanceInfoList.get(i).getAttendanceInTime());
        }else {
            myViewHolder.tvInTime.setText("-");
        }
        if (!attendanceInfoList.get(i).getAttendanceOutTime().equals("")) {
            myViewHolder.tvOutTime.setText(attendanceInfoList.get(i).getAttendanceOutTime());
        }
        else {
            myViewHolder.tvOutTime.setText("-");
        }
        if (!attendanceInfoList.get(i).getAttendanceLocation().equals("")) {
            myViewHolder.tvLocation.setText(attendanceInfoList.get(i).getAttendanceLocation());
        }else {
            myViewHolder.tvLocation.setText("-");
        }

        if (!attendanceInfoList.get(i).getAttendanceType().equals("")) {
            myViewHolder.tvAttendanceType.setText(attendanceInfoList.get(i).getAttendanceType());
        }else {
            myViewHolder.tvAttendanceType.setText("-");
        }

        myViewHolder.tvEmpName.setText(attendanceInfoList.get(i).getEmpName());
        myViewHolder.tvStatus.setText(attendanceInfoList.get(i).getAttendanceReply());

    }

    @Override
    public int getItemCount() {
        return attendanceInfoList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate,tvInTime,tvOutTime,tvLocation,tvAttendanceType,tvEmpName,tvStatus;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate=(TextView)itemView.findViewById(R.id.tvDate);
            tvInTime=(TextView)itemView.findViewById(R.id.tvInTime);
            tvOutTime=(TextView)itemView.findViewById(R.id.tvOutTime);
            tvLocation=(TextView)itemView.findViewById(R.id.tvLocation);
            tvAttendanceType=(TextView)itemView.findViewById(R.id.tvAttendancetype);
            tvEmpName=(TextView)itemView.findViewById(R.id.tvEmpName);
            tvStatus=(TextView)itemView.findViewById(R.id.tvStatus);
        }
    }

    public AttendanceReportAdapter(ArrayList<AttendancereportModule> attendanceInfoList) {
        this.attendanceInfoList = attendanceInfoList;
    }
}
