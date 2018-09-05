package io.cordova.myapp00d753.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;



import java.util.ArrayList;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.activity.AttenApprovalActivity;
import io.cordova.myapp00d753.module.AttendanceApprovalModule;

public class AttendanceApprovalAdapter extends RecyclerView.Adapter<AttendanceApprovalAdapter.MyViewHolder> {
    ArrayList<AttendanceApprovalModule>attendanceInfoList=new ArrayList<>();
    Context context;
    @NonNull
    @Override
    public AttendanceApprovalAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.approval_raw,viewGroup,false);

        return new AttendanceApprovalAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final AttendanceApprovalAdapter.MyViewHolder myViewHolder, final int i) {
        final AttendanceApprovalModule attandanceModel = attendanceInfoList.get(i);

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

        myViewHolder.tvEmpId.setText(attendanceInfoList.get(i).getEmpId());
        myViewHolder.tvEmpName.setText(attendanceInfoList.get(i).getEmpName());
        myViewHolder.tvAttId.setText(attendanceInfoList.get(i).getAttId());

        if (attendanceInfoList.get(i).isSelected()){
            myViewHolder.imgLike.setVisibility(View.VISIBLE);
        }else {
            myViewHolder.imgLike.setVisibility(View.GONE);
        }
        myViewHolder.llMain.setOnClickListener(new View.OnClickListener() {
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

    @Override
    public int getItemCount() {
        return attendanceInfoList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate,tvInTime,tvOutTime,tvLocation,tvReply,tvAttendanceType,tvEmpId,tvEmpName,tvAttId;
        ImageView imgLike;
        LinearLayout llMain;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate=(TextView)itemView.findViewById(R.id.tvDate);
            tvInTime=(TextView)itemView.findViewById(R.id.tvInTime);
            tvOutTime=(TextView)itemView.findViewById(R.id.tvOutTime);
            tvLocation=(TextView)itemView.findViewById(R.id.tvLocation);
            tvReply=(TextView)itemView.findViewById(R.id.tvReply);
            tvAttendanceType=(TextView)itemView.findViewById(R.id.tvAttendancetype);
            tvEmpId=(TextView)itemView.findViewById(R.id.tvEmpId);
            tvEmpName=(TextView)itemView.findViewById(R.id.tvEmpName);
            tvAttId=(TextView)itemView.findViewById(R.id.tvAttId);

            llMain=(LinearLayout)itemView.findViewById(R.id.llMain);
            imgLike=(ImageView) itemView.findViewById(R.id.imgLike);
        }
    }

    public AttendanceApprovalAdapter(ArrayList<AttendanceApprovalModule> attendanceInfoList, Context context) {
        this.attendanceInfoList = attendanceInfoList;
        this.context = context;
    }
}
