package io.cordova.myapp00d753.activity.metso.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.activity.metso.MetsoNewReimbursementClaimActivity;

public class MonthAdapter extends RecyclerView.Adapter<MonthAdapter.MyViewHolder>{
    Context context;
    ArrayList<String> monthlyList;
    MetsoNewReimbursementClaimActivity.SelectMonthListener mSelectMonthListener;

    public MonthAdapter(Context context, ArrayList<String> monthlyList) {
        this.context = context;
        this.monthlyList = monthlyList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.month_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.tvJan.setText(monthlyList.get(position));
        holder.llMainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSelectMonthListener.onClick(monthlyList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return monthlyList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tvJan;
        LinearLayout llMainLayout;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvJan = itemView.findViewById(R.id.tvJan);
            llMainLayout = itemView.findViewById(R.id.llMainLayout);
        }
    }

    public void setSelectMonthListener(MetsoNewReimbursementClaimActivity.SelectMonthListener mSelectMonthListener) {
        this.mSelectMonthListener = mSelectMonthListener;
    }
}
