package io.cordova.myapp00d753.activity.NEW.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.activity.NEW.AllApplicationViewActivity;
import io.cordova.myapp00d753.activity.NEW.model.AllApplicationViewModel;

public class AllApplicationViewAdapter extends RecyclerView.Adapter<AllApplicationViewAdapter.MyViewHolder>{
    Context context;
    ArrayList<AllApplicationViewModel> applicationViewList;

    public AllApplicationViewAdapter(Context context, ArrayList<AllApplicationViewModel> applicationViewList) {
        this.context = context;
        this.applicationViewList = applicationViewList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_application_view_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.tvAdjustmentType.setText(applicationViewList.get(position).appliedType);
        holder.tvApplicationDate.setText(applicationViewList.get(position).applicationDate);
        holder.tvAppliedDate.setText(applicationViewList.get(position).appliedDate);
        holder.tvReferralDate.setText(applicationViewList.get(position).refDate);
        holder.tvInTime.setText(applicationViewList.get(position).intime);
        holder.tvOutTime.setText(applicationViewList.get(position).outtime);
        holder.tvReason.setText(applicationViewList.get(position).reason);
        //holder.tvClientName.setText(applicationViewList.get(position).);
        holder.tvWorkPlace.setText(applicationViewList.get(position).selectedWorkPlace);
        holder.tvApprover.setText(applicationViewList.get(position).selectedApprover);
        holder.tvStatus.setText(applicationViewList.get(position).currentApprovalStatus);
        holder.tvAprovalDetails.setText(applicationViewList.get(position).approvalDetails);
        holder.tvFStatus.setText(applicationViewList.get(position).finalApprovalStatus);
        if(applicationViewList.get(position).AllowDelete == 1){
            holder.imgDelete.setVisibility(View.VISIBLE);
        } else {
            holder.imgDelete.setVisibility(View.GONE);
        }
        Log.e("log", "AdjType: "+applicationViewList.get(position).AdjType);
        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(applicationViewList.get(position).AdjType.equalsIgnoreCase("H")
                        || applicationViewList.get(position).AdjType.equalsIgnoreCase("OH")
                        || applicationViewList.get(position).AdjType.equalsIgnoreCase("WO")){
                    ((AllApplicationViewActivity) context).Delete_H_OH_WO(applicationViewList.get(position).AdjApplicationID);
                } else if(applicationViewList.get(position).AdjType.equalsIgnoreCase("OD")
                        || applicationViewList.get(position).AdjType.equalsIgnoreCase("CO")
                        || applicationViewList.get(position).AdjType.equalsIgnoreCase("WFH")){
                    ((AllApplicationViewActivity) context).Delete_OD_CO_WFH(applicationViewList.get(position).appliedType,applicationViewList.get(position).AdjApplicationID);
                } else if(applicationViewList.get(position).AdjType.equalsIgnoreCase("Reg")){
                    ((AllApplicationViewActivity) context).DeleteRegularisation(applicationViewList.get(position).AdjApplicationID,
                            applicationViewList.get(position).RegApplicationDID,applicationViewList.get(position).RegApplicationMID);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return applicationViewList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tvAdjustmentType,tvApplicationDate,tvStrtDate,tvEndDate,tvInTime,tvOutTime,tvClientName,tvWorkPlace,tvReason,
                RefDate,tvStatus,tvFStatus,tvApprover,tvAprovalDetails,tvAppliedDate,tvReferralDate;
        ImageView imgDelete;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAdjustmentType = itemView.findViewById(R.id.tvAdjustmentType);
            tvApplicationDate = itemView.findViewById(R.id.tvApplicationDate);
            tvStrtDate = itemView.findViewById(R.id.tvStrtDate);
            tvEndDate = itemView.findViewById(R.id.tvEndDate);
            tvInTime = itemView.findViewById(R.id.tvInTime);
            tvOutTime = itemView.findViewById(R.id.tvOutTime);
            tvClientName = itemView.findViewById(R.id.tvClientName);
            tvWorkPlace = itemView.findViewById(R.id.tvWorkPlace);
            tvReason = itemView.findViewById(R.id.tvReason);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvFStatus = itemView.findViewById(R.id.tvFStatus);
            tvApprover = itemView.findViewById(R.id.tvApprover);
            tvAprovalDetails = itemView.findViewById(R.id.tvAprovalDetails);
            tvAppliedDate = itemView.findViewById(R.id.tvAppliedDate);
            tvReferralDate = itemView.findViewById(R.id.tvReferralDate);
            imgDelete = itemView.findViewById(R.id.imgDelete);

        }
    }
}
