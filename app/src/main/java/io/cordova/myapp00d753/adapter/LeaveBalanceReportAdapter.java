package io.cordova.myapp00d753.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.module.LeaveBalanceDetailsModel;

public class LeaveBalanceReportAdapter extends RecyclerView.Adapter<LeaveBalanceReportAdapter.MyViewHolder>{
    Context context;
    ArrayList<LeaveBalanceDetailsModel> leaveBalanceArray;

    public LeaveBalanceReportAdapter(Context context, ArrayList<LeaveBalanceDetailsModel> leaveBalanceArray) {
        this.context = context;
        this.leaveBalanceArray = leaveBalanceArray;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.balance_raw, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.tvDate.setText(leaveBalanceArray.get(position).getLeave());
        holder.tvOpening.setText(leaveBalanceArray.get(position).getLeaveBalance());
        holder.tvTaken.setText(leaveBalanceArray.get(position).getLeaveTaken());
        holder.tvAvailable.setText(leaveBalanceArray.get(position).getAvaliable());
    }

    @Override
    public int getItemCount() {
        return leaveBalanceArray.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tvDate,tvOpening,tvTaken,tvAvailable;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvOpening = itemView.findViewById(R.id.tvOpening);
            tvTaken = itemView.findViewById(R.id.tvTaken);
            tvAvailable = itemView.findViewById(R.id.tvAvailable);
        }
    }
}
