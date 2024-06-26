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
import io.cordova.myapp00d753.module.CompffViewModel;

public class CompOffViewAdapter extends RecyclerView.Adapter<CompOffViewAdapter.MyViewHolder> {
    ArrayList<CompffViewModel>itemList=new ArrayList<>();
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.compff_view_row,viewGroup,false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.tvReferalDate.setText(itemList.get(i).getCom_ref_date());
        myViewHolder.tvEffectDate.setText(itemList.get(i).getGatePassDate());
        myViewHolder.tvMode.setText(itemList.get(i).getAdjday());
        myViewHolder.tvReason.setText(itemList.get(i).getRemarks());
        myViewHolder.tvApplicationDate.setText(itemList.get(i).getApplicationDate());
        myViewHolder.tvApprovalStatus.setText(itemList.get(i).getApprovalStatus());
        myViewHolder.tvApplicationType.setText(itemList.get(i).getGatePassType());




    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvReferalDate,tvEffectDate,tvMode,tvReason,tvApplicationDate,tvApprovalStatus,tvApplicationType;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvReferalDate=(TextView)itemView.findViewById(R.id.tvReferalDate);
            tvEffectDate=(TextView)itemView.findViewById(R.id.tvEffectDate);
            tvMode=(TextView)itemView.findViewById(R.id.tvMode);
            tvReason=(TextView)itemView.findViewById(R.id.tvReason);
            tvApplicationDate=(TextView) itemView.findViewById(R.id.tvApplicationDate);
            tvApprovalStatus=(TextView) itemView.findViewById(R.id.tvApprovalStatus);
            tvApplicationType=(TextView) itemView.findViewById(R.id.tvApplicationType);
        }
    }

    public CompOffViewAdapter(ArrayList<CompffViewModel> itemList) {
        this.itemList = itemList;
    }
}
