package io.cordova.myapp00d753.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.module.AttendanceModule;

public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.MyViewHolder> {
    ArrayList<AttendanceModule>attendanceInfoList=new ArrayList<>();
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.raw,viewGroup,false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

        if (!attendanceInfoList.get(i).getAttendanceDate().equals("")) {
            myViewHolder.tvDate.setText(attendanceInfoList.get(i).getAttendanceDate());
        }else {
            myViewHolder.tvDate.setText("-");
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
            myViewHolder.tvType.setText(attendanceInfoList.get(i).getAttendanceType());
        }else {
            myViewHolder.tvType.setText("-");
        }

        if (!attendanceInfoList.get(i).getAttendanceReply().equals("")) {
            myViewHolder.tvStatus.setText(attendanceInfoList.get(i).getAttendanceReply());
        }else {
            myViewHolder.tvStatus.setText("-");
        }



    }

    @Override
    public int getItemCount() {
        return attendanceInfoList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate,tvInTime,tvOutTime,tvLocation,tvType,tvStatus;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate=(TextView)itemView.findViewById(R.id.tvDate);
            tvInTime=(TextView)itemView.findViewById(R.id.tvInTime);
            tvOutTime=(TextView)itemView.findViewById(R.id.tvOutTime);
            tvLocation=(TextView)itemView.findViewById(R.id.tvLocation);
            tvType=(TextView)itemView.findViewById(R.id.tvType);
            tvStatus=(TextView)itemView.findViewById(R.id.tvStatus);

        }
    }

    public AttendanceAdapter(ArrayList<AttendanceModule> attendanceInfoList) {
        this.attendanceInfoList = attendanceInfoList;
    }
}
