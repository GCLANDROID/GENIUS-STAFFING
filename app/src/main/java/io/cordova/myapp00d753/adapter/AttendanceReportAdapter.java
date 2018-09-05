package io.cordova.myapp00d753.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;



import java.util.ArrayList;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.module.AttendancereportModule;

public class AttendanceReportAdapter extends RecyclerView.Adapter<AttendanceReportAdapter.MyViewHolder> {
    ArrayList<AttendancereportModule>attendanceInfoList=new ArrayList<>();
    @NonNull
    @Override
    public AttendanceReportAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.attendancereport_raw,viewGroup,false);

        return new AttendanceReportAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AttendanceReportAdapter.MyViewHolder myViewHolder, int i) {

        if (!attendanceInfoList.get(i).getAttendanceDate().equals("")) {
            myViewHolder.tvDate.setText(attendanceInfoList.get(i).getAttendanceDate());
        }else {
            myViewHolder.tvDate.setText("N/A");
        }
        if (!attendanceInfoList.get(i).getAttendanceInTime().equals("")) {

            myViewHolder.tvInTime.setText(attendanceInfoList.get(i).getAttendanceInTime());
        }else {
            myViewHolder.tvInTime.setText("N/A");
        }
        if (!attendanceInfoList.get(i).getAttendanceOutTime().equals("")) {
            myViewHolder.tvOutTime.setText(attendanceInfoList.get(i).getAttendanceOutTime());
        }
        else {
            myViewHolder.tvOutTime.setText("N/A");
        }
        if (!attendanceInfoList.get(i).getAttendanceLocation().equals("")) {
            myViewHolder.tvLocation.setText(attendanceInfoList.get(i).getAttendanceLocation());
        }else {
            myViewHolder.tvLocation.setText("N/A");
        }
        if (!attendanceInfoList.get(i).getAttendanceReply().equals("")) {
            myViewHolder.tvReply.setText(attendanceInfoList.get(i).getAttendanceReply());
        }else {
            myViewHolder.tvReply.setText("N/A");
        }
        if (!attendanceInfoList.get(i).getAttendanceType().equals("")) {
            myViewHolder.tvAttendanceType.setText(attendanceInfoList.get(i).getAttendanceType());
        }else {
            myViewHolder.tvAttendanceType.setText("N/A");
        }

        myViewHolder.tvEmpName.setText(attendanceInfoList.get(i).getEmpName());

    }

    @Override
    public int getItemCount() {
        return attendanceInfoList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate,tvInTime,tvOutTime,tvLocation,tvReply,tvAttendanceType,tvEmpName;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate=(TextView)itemView.findViewById(R.id.tvDate);
            tvInTime=(TextView)itemView.findViewById(R.id.tvInTime);
            tvOutTime=(TextView)itemView.findViewById(R.id.tvOutTime);
            tvLocation=(TextView)itemView.findViewById(R.id.tvLocation);
            tvReply=(TextView)itemView.findViewById(R.id.tvReply);
            tvAttendanceType=(TextView)itemView.findViewById(R.id.tvAttendancetype);
            tvEmpName=(TextView)itemView.findViewById(R.id.tvEmpName);
        }
    }

    public AttendanceReportAdapter(ArrayList<AttendancereportModule> attendanceInfoList) {
        this.attendanceInfoList = attendanceInfoList;
    }
}
