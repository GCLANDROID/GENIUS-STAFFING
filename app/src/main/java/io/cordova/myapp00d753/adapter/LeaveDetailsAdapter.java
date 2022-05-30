package io.cordova.myapp00d753.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.fragment.DetailsFragment;
import io.cordova.myapp00d753.module.LeaveDetailsModel;


public class LeaveDetailsAdapter extends RecyclerView.Adapter<LeaveDetailsAdapter.MyViewHolder> {
    ArrayList<LeaveDetailsModel>itemList=new ArrayList<>();
    Context context;
    Fragment fContext;
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.leave_details_raw,viewGroup,false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {

        myViewHolder.tvStrtDay.setText(itemList.get(i).getStrtDay());
        myViewHolder.tvEndDay.setText(itemList.get(i).getEndDay());
        myViewHolder.tvValue.setText(itemList.get(i).getValue());
        myViewHolder.tvApprovalDate.setText(itemList.get(i).getApprovedDate());
        myViewHolder.tvRemarks.setText(itemList.get(i).getRemarks());

        //leave name
        myViewHolder.tvReason.setText(itemList.get(i).getReason());


        //reason
        myViewHolder.tvLeaveName.setText(itemList.get(i).getLeaveName());


        //status
        myViewHolder.tvStatus.setText(itemList.get(i).getStatus());

        //approver


            myViewHolder.tvApprovedBy.setText(itemList.get(i).getApprovedDate());



            myViewHolder.tvLeaveApprover.setText("Approve by:");
            myViewHolder.tvLeaveDate.setText("Approve date:");
            myViewHolder.tvLeaveStatus.setText("Approve status:");
            myViewHolder.tvLeaveReason.setText("Reason:");
            myViewHolder.tvLeaveValue.setText("Value:");
            myViewHolder.tvLeaveEndDate.setText("End Date:");
            myViewHolder.tvLeaveStrtDate.setText("Start Date:");
            myViewHolder.tvLeave.setText("Leave Name:");


        myViewHolder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((DetailsFragment)fContext).leaveDelete(itemList.get(i).getId());
            }
        });




    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvLeaveName,tvStrtDay,tvEndDay,tvValue,tvReason,tvStatus,tvApprovalDate ,tvApprovedBy,tvRemarks;
        TextView tvLeaveApprover,tvLeaveDate,tvLeaveStatus,tvLeaveReason,tvLeaveValue,tvLeaveEndDate,tvLeaveStrtDate,tvLeave;
        ImageView imgDelete;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvLeaveName=(TextView)itemView.findViewById(R.id.tvLeaveType);
            tvStrtDay=(TextView)itemView.findViewById(R.id.tvStrtDate);
            tvEndDay=(TextView)itemView.findViewById(R.id.tvEndDate);
            tvValue=(TextView)itemView.findViewById(R.id.tvValue);
            tvReason=(TextView)itemView.findViewById(R.id.tvReason);
            tvStatus=(TextView)itemView.findViewById(R.id.tvStatus);
            tvApprovalDate=(TextView)itemView.findViewById(R.id.tvAppDate);
            tvApprovedBy=(TextView)itemView.findViewById(R.id.tvApprover);
            tvRemarks=(TextView)itemView.findViewById(R.id.tvRemarks);

            tvLeaveApprover=(TextView)itemView.findViewById(R.id.tvLeaveApprover);
            tvLeaveDate=(TextView)itemView.findViewById(R.id.tvLeaveDate);
            tvLeaveStatus=(TextView)itemView.findViewById(R.id.tvLeaveStatus);
            tvLeaveReason=(TextView)itemView.findViewById(R.id.tvLeaveReason);
            tvLeaveValue=(TextView)itemView.findViewById(R.id.tvLeaveValue);
            tvLeaveEndDate=(TextView)itemView.findViewById(R.id.tvLeaveEndDate);
            tvLeaveStrtDate=(TextView)itemView.findViewById(R.id.tvLeaveStrtDate);
            tvLeave=(TextView)itemView.findViewById(R.id.tvLeave);

            imgDelete=(ImageView)itemView.findViewById(R.id.imgDelete);



        }
    }

    public LeaveDetailsAdapter(ArrayList<LeaveDetailsModel> itemList, Context context, Fragment fContext) {
        this.itemList = itemList;
        this.context = context;
        this.fContext = fContext;
    }
}
