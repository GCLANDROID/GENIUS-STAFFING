package io.cordova.myapp00d753.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.activity.SaleApprovalActivity;
import io.cordova.myapp00d753.activity.SaleArpprovalReportActivity;
import io.cordova.myapp00d753.activity.SupEmpSaleActivity;
import io.cordova.myapp00d753.activity.TotalSaleActivity;
import io.cordova.myapp00d753.module.ConSaleApproval;
import io.cordova.myapp00d753.module.TeamSaleModule;
import io.cordova.myapp00d753.utility.Pref;

public class ConSaleApprovalAdapter extends RecyclerView.Adapter<ConSaleApprovalAdapter.MyViewHolder> {
    ArrayList<ConSaleApproval> itemList =new ArrayList<>();
    Context context;
    Pref p;


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.con_approval_raw,viewGroup,false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int i) {
        myViewHolder.tvCount.setText(itemList.get(i).getTotalSale());
        myViewHolder.tvName.setText(itemList.get(i).getName());
        myViewHolder.tvPending.setText(itemList.get(i).getPendingSale());
        myViewHolder.tvApproval.setText(itemList.get(i).getAppSale());
        myViewHolder.tvReject.setText(itemList.get(i).getRejSale());
        myViewHolder.llPending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, SaleApprovalActivity.class);
                intent.putExtra("zoneId",itemList.get(i).getZoneId());
                intent.putExtra("branchId",itemList.get(i).getBranchId());
                 intent.putExtra("zoneName",itemList.get(i).getName());
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        myViewHolder.llApproval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, SaleArpprovalReportActivity.class);
                intent.putExtra("zoneId",itemList.get(i).getZoneId());
                intent.putExtra("branchId",itemList.get(i).getBranchId());
                intent.putExtra("zoneName",itemList.get(i).getName());
                intent.putExtra("xy","1");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        myViewHolder.llReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, SaleArpprovalReportActivity.class);
                intent.putExtra("zoneId",itemList.get(i).getZoneId());
                intent.putExtra("branchId",itemList.get(i).getBranchId());
                intent.putExtra("zoneName",itemList.get(i).getName());
                intent.putExtra("xy","2");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });


        myViewHolder.llTotal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, TotalSaleActivity.class);
                intent.putExtra("zoneId",itemList.get(i).getZoneId());
                intent.putExtra("branchId",itemList.get(i).getBranchId());
                intent.putExtra("zoneName",itemList.get(i).getName());
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
        TextView tvName,tvCount,tvPending,tvApproval,tvReject;
        LinearLayout llPending,llApproval,llReject,llTotal;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName=(TextView)itemView.findViewById(R.id.tvName);
            tvCount=(TextView)itemView.findViewById(R.id.tvCount);
            tvPending=(TextView)itemView.findViewById(R.id.tvPending);
            tvApproval=(TextView)itemView.findViewById(R.id.tvApproval);
            tvReject=(TextView)itemView.findViewById(R.id.tvReject);

            llPending=(LinearLayout)itemView.findViewById(R.id.llPending);
            llApproval=(LinearLayout)itemView.findViewById(R.id.llApproval);
            llReject=(LinearLayout)itemView.findViewById(R.id.llReject);
            llTotal=(LinearLayout)itemView.findViewById(R.id.llTotal);



        }
    }

    public ConSaleApprovalAdapter(ArrayList<ConSaleApproval> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
    }


}
