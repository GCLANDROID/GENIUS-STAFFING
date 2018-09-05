package io.cordova.myapp00d753.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;



import java.util.ArrayList;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.module.SupSalaryModule;

public class SupSalaryAdapter extends RecyclerView.Adapter<SupSalaryAdapter.MyViewHolder> {
    ArrayList<SupSalaryModule>salryinfoList=new ArrayList<>();
    @NonNull
    @Override
    public SupSalaryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.supsalary_raw,viewGroup,false);

        return new SupSalaryAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SupSalaryAdapter.MyViewHolder myViewHolder, int i) {
        myViewHolder.tvEmpId.setText(salryinfoList.get(i).getEmpId());
        myViewHolder.tvEmpName.setText(salryinfoList.get(i).getEmpName());
        myViewHolder.tvYear.setText(salryinfoList.get(i).getYear());
        myViewHolder.tvMonth.setText(salryinfoList.get(i).getMonth());
        myViewHolder.tvAmount.setText(salryinfoList.get(i).getAmount());

    }

    @Override
    public int getItemCount() {
        return salryinfoList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvYear,tvMonth,tvAmount,tvEmpId,tvEmpName;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvYear=(TextView)itemView.findViewById(R.id.tvYear);
            tvMonth=(TextView)itemView.findViewById(R.id.tvMonth);
            tvAmount=(TextView)itemView.findViewById(R.id.tvAmount);
            tvEmpId=(TextView)itemView.findViewById(R.id.tvEmpId);
            tvEmpName=(TextView)itemView.findViewById(R.id.tvEmpName);
        }
    }

    public SupSalaryAdapter(ArrayList<SupSalaryModule> salryinfoList) {
        this.salryinfoList = salryinfoList;
    }
}
