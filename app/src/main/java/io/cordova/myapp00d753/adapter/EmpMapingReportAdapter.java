package io.cordova.myapp00d753.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.module.EmpMapiingReportModel;


public class EmpMapingReportAdapter extends RecyclerView.Adapter<EmpMapingReportAdapter.MyViewHolder> {
    ArrayList<EmpMapiingReportModel>itemList=new ArrayList<>();

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.employeemapraw,viewGroup,false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.tvEmpName.setText(itemList.get(i).getEmpName());


    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvEmpName;
        LinearLayout rlMain;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvEmpName=(TextView)itemView.findViewById(R.id.tvEmpName);
            rlMain=(LinearLayout)itemView.findViewById(R.id.rlMain);


        }
    }

    public EmpMapingReportAdapter(ArrayList<EmpMapiingReportModel> itemList) {
        this.itemList = itemList;
    }
}
