package io.cordova.myapp00d753.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.module.ClaimModule;

public class ClaimReportAdapter extends RecyclerView.Adapter<ClaimReportAdapter.MyViewHolder> {
    ArrayList<ClaimModule> claimList =new ArrayList<>();
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.claim_raw,viewGroup,false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.tvDate.setText(claimList.get(i).getClaimDate());
        myViewHolder.tvAmount.setText("Rs. "+claimList.get(i).getClaimAmount());
        myViewHolder.tvPur.setText(claimList.get(i).getClaimType());
        myViewHolder.tvStatus.setText(claimList.get(i).getClaimStatus());
        myViewHolder.tvApprvAmount.setText("Rs. "+claimList.get(i).getApprvAmount());



    }

    @Override
    public int getItemCount() {
        return claimList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate,tvAmount,tvPur,tvStatus,tvId,tvApprvAmount;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate=(TextView)itemView.findViewById(R.id.tvDate);
            tvAmount=(TextView)itemView.findViewById(R.id.tvAmount);
            tvPur=(TextView)itemView.findViewById(R.id.tvPur);
            tvStatus=(TextView)itemView.findViewById(R.id.tvStatus);
            tvApprvAmount=(TextView)itemView.findViewById(R.id.tvApprvAmount);

        }
    }

    public ClaimReportAdapter(ArrayList<ClaimModule> claimList) {
        this.claimList = claimList;
    }
}
