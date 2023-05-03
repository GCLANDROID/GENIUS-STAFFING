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
import io.cordova.myapp00d753.module.OthersPayableModel;

public class OthersPayableAdapter extends RecyclerView.Adapter<OthersPayableAdapter.MyViewHolder> {
    ArrayList<OthersPayableModel>itemList=new ArrayList<>();
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.others_payout_row,viewGroup,false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.tvBillType.setText(itemList.get(i).getBilltype());
        myViewHolder.tvYear.setText(itemList.get(i).getYear());
        myViewHolder.tvMonth.setText(itemList.get(i).getMonth());
        myViewHolder.tvAmt.setText(itemList.get(i).getAmt());



    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvBillType,tvYear,tvMonth,tvAmt;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvBillType=(TextView)itemView.findViewById(R.id.tvBillType);
            tvYear=(TextView)itemView.findViewById(R.id.tvYear);
            tvMonth=(TextView)itemView.findViewById(R.id.tvMonth);
            tvAmt=(TextView)itemView.findViewById(R.id.tvAmt);

        }
    }

    public OthersPayableAdapter(ArrayList<OthersPayableModel> attendanceInfoList) {
        this.itemList = attendanceInfoList;
    }
}
