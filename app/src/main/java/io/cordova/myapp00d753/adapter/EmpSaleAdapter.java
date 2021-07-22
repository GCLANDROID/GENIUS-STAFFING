package io.cordova.myapp00d753.adapter;

import android.content.Context;
import android.content.Intent;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.activity.SupCounterVisitReportActivity;
import io.cordova.myapp00d753.activity.SupSaleReportActivity;
import io.cordova.myapp00d753.module.EmpSaleModel;
import io.cordova.myapp00d753.module.SaleQTDModel;
import io.cordova.myapp00d753.utility.Pref;

public class EmpSaleAdapter extends RecyclerView.Adapter<EmpSaleAdapter.MyViewHolder> {
    ArrayList<EmpSaleModel> itemList =new ArrayList<>();
    Context context;
    Pref p;


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.emp_raw,viewGroup,false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int i) {
        p=new Pref(context);
        myViewHolder.tvName.setText(itemList.get(i).getEmpName()+" ( "+itemList.get(i).getEmpId()+" )");
        myViewHolder.btnSale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, SupSaleReportActivity.class);
                intent.putExtra("empId",itemList.get(i).getEmpId());
                intent.putExtra("empName",itemList.get(i).getEmpName());
                intent.putExtra("zone",itemList.get(i).getZone());
                intent.putExtra("branch",itemList.get(i).getBranch());
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        myViewHolder.btnVisit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, SupCounterVisitReportActivity.class);
                intent.putExtra("empId",itemList.get(i).getEmpId());
                intent.putExtra("empName",itemList.get(i).getEmpName());
                intent.putExtra("zone",itemList.get(i).getZone());
                intent.putExtra("branch",itemList.get(i).getBranch());
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
        Button btnSale,btnVisit;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName=(TextView)itemView.findViewById(R.id.tvName);
            tvPercentage=(TextView)itemView.findViewById(R.id.tvPercentage);
            btnSale=(Button)itemView.findViewById(R.id.btnSale);
            btnVisit=(Button)itemView.findViewById(R.id.btnVisit);



        }
    }

    public EmpSaleAdapter(ArrayList<EmpSaleModel> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
    }

    public void updateList(ArrayList<EmpSaleModel> list){
        itemList = list;
        notifyDataSetChanged();
    }


}
