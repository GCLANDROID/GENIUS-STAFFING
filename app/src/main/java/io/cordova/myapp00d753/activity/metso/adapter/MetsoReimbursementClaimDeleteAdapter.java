package io.cordova.myapp00d753.activity.metso.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.activity.ClaimDeletActivity;
import io.cordova.myapp00d753.activity.metso.MetsoReimbursementDeleteActivity;
import io.cordova.myapp00d753.module.ClaimDeleteModule;


public class MetsoReimbursementClaimDeleteAdapter extends RecyclerView.Adapter<MetsoReimbursementClaimDeleteAdapter.MyViewHolder>{

    ArrayList<ClaimDeleteModule> claimList;
    Context context;

    public MetsoReimbursementClaimDeleteAdapter(ArrayList<ClaimDeleteModule> claimList, Context context) {
        this.claimList = claimList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.metso_rim_claim_delete_row,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, @SuppressLint("RecyclerView") int i) {
        final ClaimDeleteModule delModel = claimList.get(i);
        myViewHolder.tvDate.setText(claimList.get(i).getClaimDate());
        myViewHolder.tvAmount.setText("Rs. "+claimList.get(i).getClaimAmount());
        myViewHolder.tvPur.setText(claimList.get(i).getClaimType());
        myViewHolder.tvStatus.setText(claimList.get(i).getClaimStatus());
        myViewHolder.tvCostCenter.setText(claimList.get(i).getCostCenter());
        myViewHolder.tvWbsCode.setText(claimList.get(i).getWbsCode());
        myViewHolder.tvSupervisor.setText(claimList.get(i).getSupervisor());
        myViewHolder.tvSiteName.setText(claimList.get(i).getSiteName());
        if (claimList.get(i).isSelected()){
            myViewHolder.imgLike.setVisibility(View.VISIBLE);
        }else {
            myViewHolder.imgLike.setVisibility(View.GONE);
        }
        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delModel.setSelected(!delModel.isSelected());
                // holder.view.setBackgroundColor(attandanceModel.isSelected() ? Color.CYAN : Color.WHITE);

                if (delModel.isSelected()) {
                    myViewHolder.imgLike.setVisibility(View.VISIBLE);
                    claimList.get(i).setSelected(true);
                    ((MetsoReimbursementDeleteActivity) context).updateFlag(true);
                    notifyDataSetChanged();
                    //((MetsoReimbursementDeleteActivity) context).updateAttendanceStatus(i, true );
                } else {
                    myViewHolder.imgLike.setVisibility(View.GONE);
                    //((MetsoReimbursementDeleteActivity) context).updateAttendanceStatus(i, false);
                    claimList.get(i).setSelected(false);
                    ((MetsoReimbursementDeleteActivity) context).updateFlag(false);
                    notifyDataSetChanged();
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return claimList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tvDate,tvAmount,tvPur,tvStatus,tvId,tvCostCenter,tvWbsCode,tvSupervisor,tvSiteName;
        ImageView imgLike;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate=(TextView)itemView.findViewById(R.id.tvDate);
            tvAmount=(TextView)itemView.findViewById(R.id.tvAmount);
            tvPur=(TextView)itemView.findViewById(R.id.tvPur);
            tvStatus=(TextView)itemView.findViewById(R.id.tvStatus);
            imgLike=(ImageView)itemView.findViewById(R.id.imgLike);
            tvCostCenter = itemView.findViewById(R.id.tvCostCenter);
            tvWbsCode = itemView.findViewById(R.id.tvWbsCode);
            tvSupervisor = itemView.findViewById(R.id.tvSupervisor);
            tvSiteName = itemView.findViewById(R.id.tvSiteName);
        }
    }
}
