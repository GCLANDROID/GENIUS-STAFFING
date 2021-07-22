package io.cordova.myapp00d753.adapter;

import android.content.Context;
import android.content.Intent;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.activity.SupCounterVisitReportActivity;
import io.cordova.myapp00d753.activity.SupSaleReportActivity;
import io.cordova.myapp00d753.module.EmpSaleModel;

public class EmpVisitAdapter extends RecyclerView.Adapter<EmpVisitAdapter.MyViewHolder> {
    ArrayList<EmpSaleModel> itemList =new ArrayList<>();
    Context context;


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.emp_raw,viewGroup,false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int i) {
        myViewHolder.tvName.setText(itemList.get(i).getEmpName()+"("+itemList.get(i).getEmpId()+")");
        myViewHolder.tvPercentage.setVisibility(View.GONE);
        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, SupCounterVisitReportActivity.class);
                intent.putExtra("empId",itemList.get(i).getEmpId());
                intent.putExtra("empName",itemList.get(i).getEmpName());
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });









    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvName,tvPercentage;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName=(TextView)itemView.findViewById(R.id.tvName);
            tvPercentage=(TextView)itemView.findViewById(R.id.tvPercentage);



        }
    }

    public EmpVisitAdapter(ArrayList<EmpSaleModel> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
    }

    public void updateList(ArrayList<EmpSaleModel> list){
        itemList = list;
        notifyDataSetChanged();
    }
}
