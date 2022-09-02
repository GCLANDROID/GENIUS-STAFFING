package io.cordova.myapp00d753.adapter;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.module.SalaryModule;

public class SalaryAdapter extends RecyclerView.Adapter<SalaryAdapter.MyViewHolder> {
    ArrayList<SalaryModule>salryinfoList=new ArrayList<>();
    Context context;
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.salary_raw,viewGroup,false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.tvYear.setText(salryinfoList.get(i).getYear());
        myViewHolder.tvMonth.setText(salryinfoList.get(i).getMonth());
        myViewHolder.tvSalary.setText(salryinfoList.get(i).getAmount());

        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse(salryinfoList.get(i).getSurl()); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return salryinfoList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvYear,tvMonth,tvSalary;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvYear=(TextView)itemView.findViewById(R.id.tvYear);
            tvMonth=(TextView)itemView.findViewById(R.id.tvMonth);
            tvSalary=(TextView)itemView.findViewById(R.id.tvSalary);
        }
    }

    public SalaryAdapter(ArrayList<SalaryModule> salryinfoList,Context context) {
        this.salryinfoList = salryinfoList;
        this.context=context;
    }
}
