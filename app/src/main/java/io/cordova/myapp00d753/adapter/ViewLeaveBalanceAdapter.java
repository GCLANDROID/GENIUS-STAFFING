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
import io.cordova.myapp00d753.module.ViewLeaveBalanceModel;

public class ViewLeaveBalanceAdapter extends RecyclerView.Adapter<ViewLeaveBalanceAdapter.MyViewHolder>{
    Context context;
    ArrayList<ViewLeaveBalanceModel> leaveBalanceArray;

    public ViewLeaveBalanceAdapter(Context context, ArrayList<ViewLeaveBalanceModel> leaveBalanceArray) {
        this.context = context;
        this.leaveBalanceArray = leaveBalanceArray;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_leave_balance_raw, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.tvDate.setText(leaveBalanceArray.get(position).getLeaveTypeName());
        holder.tvYear.setText(leaveBalanceArray.get(position).getLeaveYear());
        holder.tvOpening.setText(leaveBalanceArray.get(position).getOpening());
        holder.tvIncrement.setText(leaveBalanceArray.get(position).getIncrement());
        holder.tvAvailed.setText(leaveBalanceArray.get(position).getAvailed());
        holder.tvAdjusted.setText(leaveBalanceArray.get(position).getAdjusted());
        holder.tvClosing.setText(leaveBalanceArray.get(position).getClosing());
    }

    @Override
    public int getItemCount() {
        return leaveBalanceArray.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tvDate,tvOpening,tvIncrement,tvYear,tvAvailed,tvAdjusted,tvClosing;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvYear = itemView.findViewById(R.id.tvYear);
            tvOpening = itemView.findViewById(R.id.tvOpening);
            tvIncrement = itemView.findViewById(R.id.tvIncrement);
            tvAvailed = itemView.findViewById(R.id.tvAvailed);
            tvAdjusted = itemView.findViewById(R.id.tvAdjusted);
            tvClosing = itemView.findViewById(R.id.tvClosing);
        }
    }
}
