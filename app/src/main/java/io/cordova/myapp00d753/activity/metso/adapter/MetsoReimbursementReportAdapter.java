package io.cordova.myapp00d753.activity.metso.adapter;

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
import io.cordova.myapp00d753.module.ClaimModule;

public class MetsoReimbursementReportAdapter extends RecyclerView.Adapter<MetsoReimbursementReportAdapter.MyViewHolder> {
    Context context;
    ArrayList<ClaimModule> claimList =new ArrayList<>();

    public MetsoReimbursementReportAdapter(Context context, ArrayList<ClaimModule> claimList) {
        this.context = context;
        this.claimList = claimList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.metso_rim_clam_row,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.tvDate.setText(claimList.get(i).getClaimDate());
        myViewHolder.tvAmount.setText("Rs. "+claimList.get(i).getClaimAmount());
        myViewHolder.tvApprvAmount.setText("Rs. "+claimList.get(i).getApprvAmount());
        myViewHolder.tvPur.setText(claimList.get(i).getClaimType());
        myViewHolder.tvStatus.setText(claimList.get(i).getClaimStatus());
        myViewHolder.tvApprvAmount.setText("Rs. "+claimList.get(i).getApprvAmount());
        myViewHolder.tvCostCenter.setText(claimList.get(i).getCostCenter());
        myViewHolder.tvWbsCode.setText(claimList.get(i).getWbsCode());
        myViewHolder.tvSupervisor.setText(claimList.get(i).getSupervisor());
        myViewHolder.tvSiteName.setText(claimList.get(i).getSiteName());
    }

    @Override
    public int getItemCount() {
        return claimList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tvDate,tvAmount,tvApprvAmount,tvPur,tvStatus,tvId,tvCostCenter,tvWbsCode,tvSupervisor,tvSiteName;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate=(TextView)itemView.findViewById(R.id.tvDate);
            tvAmount=(TextView)itemView.findViewById(R.id.tvAmount);
            tvApprvAmount=(TextView)itemView.findViewById(R.id.tvApprvAmount);
            tvPur=(TextView)itemView.findViewById(R.id.tvPur);
            tvStatus=(TextView)itemView.findViewById(R.id.tvStatus);
            //imgLike=(ImageView)itemView.findViewById(R.id.imgLike);
            tvCostCenter = itemView.findViewById(R.id.tvCostCenter);
            tvWbsCode = itemView.findViewById(R.id.tvWbsCode);
            tvSupervisor = itemView.findViewById(R.id.tvSupervisor);
            tvSiteName = itemView.findViewById(R.id.tvSiteName);
        }
    }
}
