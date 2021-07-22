package io.cordova.myapp00d753.adapter;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.activity.AttenApprovalActivity;
import io.cordova.myapp00d753.module.AttendanceApprovalModule;

public class AttendanceApprovalAdapter extends RecyclerView.Adapter<AttendanceApprovalAdapter.MyViewHolder> {
    ArrayList<AttendanceApprovalModule>attendanceInfoList=new ArrayList<>();
    Context context;
    boolean isSelectedAll,isAll;
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.approvalraw,viewGroup,false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {
        final AttendanceApprovalModule attandanceModel = attendanceInfoList.get(i);

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

        myViewHolder.tvEmpId.setText(attendanceInfoList.get(i).getEmpId());
        myViewHolder.tvEmpName.setText(attendanceInfoList.get(i).getEmpName());

        if (isAll) {
            if (isSelectedAll) {
                myViewHolder.imgLike.setVisibility(View.VISIBLE);

            } else {
                myViewHolder.imgLike.setVisibility(View.GONE);
            }
        }else {
            if (attendanceInfoList.get(i).isSelected()){
                myViewHolder.imgLike.setVisibility(View.VISIBLE);
            }else {
                myViewHolder.imgLike.setVisibility(View.GONE);
            }
            myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    attandanceModel.setSelected(!attandanceModel.isSelected());
                    // holder.view.setBackgroundColor(attandanceModel.isSelected() ? Color.CYAN : Color.WHITE);

                    if (attandanceModel.isSelected()) {

                        myViewHolder.imgLike.setVisibility(View.VISIBLE);
                        attendanceInfoList.get(i).setSelected(true);
                        notifyDataSetChanged();

                        ((AttenApprovalActivity) context).updateAttendanceStatus(i, true );



                    } else {
                        myViewHolder.imgLike.setVisibility(View.GONE);
                        ((AttenApprovalActivity) context).updateAttendanceStatus(i, false);
                        attendanceInfoList.get(i).setSelected(false);
                        notifyDataSetChanged();
                    }

                }
            });
        }





    }

    @Override
    public int getItemCount() {
        return attendanceInfoList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate,tvInTime,tvOutTime,tvLocation,tvReply,tvAttendanceType,tvEmpId,tvEmpName;
        ImageView imgLike;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate=(TextView)itemView.findViewById(R.id.tvDate);
            tvInTime=(TextView)itemView.findViewById(R.id.tvInTime);
            tvOutTime=(TextView)itemView.findViewById(R.id.tvOutTime);
            tvLocation=(TextView)itemView.findViewById(R.id.tvLocation);
            tvAttendanceType=(TextView)itemView.findViewById(R.id.tvAttendancetype);
            tvEmpId=(TextView)itemView.findViewById(R.id.tvEmpId);
            tvEmpName=(TextView)itemView.findViewById(R.id.tvEmpName);


            imgLike=(ImageView) itemView.findViewById(R.id.imgLike);
        }
    }

    public AttendanceApprovalAdapter(ArrayList<AttendanceApprovalModule> attendanceInfoList, Context context) {
        this.attendanceInfoList = attendanceInfoList;
        this.context = context;
    }

    public void selectAll(){
        isSelectedAll=true;
        isAll=true;
        notifyDataSetChanged();
    }
    public void unselectall(){
        isSelectedAll=false;
        isAll=false;
        notifyDataSetChanged();
    }
}
