package io.cordova.myapp00d753.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.module.SupSalaryModule;

public class SupSalaryAdapter extends RecyclerView.Adapter<SupSalaryAdapter.MyViewHolder> {
    ArrayList<SupSalaryModule>salryinfoList=new ArrayList<>();
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.supsalary_raw,viewGroup,false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

        myViewHolder.tvEmpName.setText(salryinfoList.get(i).getEmpName());
        myViewHolder.tvYear.setText(salryinfoList.get(i).getYear());
        myViewHolder.tvMonth.setText(salryinfoList.get(i).getMonth());
        myViewHolder.tvAmount.setText(salryinfoList.get(i).getAmount());
        myViewHolder.tvCTC.setText(salryinfoList.get(i).getCtcGros());

    }

    @Override
    public int getItemCount() {
        return salryinfoList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvYear,tvMonth,tvAmount,tvEmpName,tvCTC;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvYear=(TextView)itemView.findViewById(R.id.tvYear);
            tvMonth=(TextView)itemView.findViewById(R.id.tvMonth);
            tvAmount=(TextView)itemView.findViewById(R.id.tvAmount);
            tvEmpName=(TextView)itemView.findViewById(R.id.tvEmpName);
            tvCTC=(TextView)itemView.findViewById(R.id.tvCTC);
        }
    }

    public SupSalaryAdapter(ArrayList<SupSalaryModule> salryinfoList) {
        this.salryinfoList = salryinfoList;
    }
}
