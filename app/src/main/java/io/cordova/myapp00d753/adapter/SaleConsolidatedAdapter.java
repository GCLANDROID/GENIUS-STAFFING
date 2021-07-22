package io.cordova.myapp00d753.adapter;


import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.module.SaleConsolidatedModel;
import io.cordova.myapp00d753.module.SaleQTDModel;

public class SaleConsolidatedAdapter extends RecyclerView.Adapter<SaleConsolidatedAdapter.MyViewHolder> {
    ArrayList<SaleConsolidatedModel> itemList =new ArrayList<>();
    ArrayList<SaleConsolidatedModel> itemList1 =new ArrayList<>();
    ArrayList<SaleConsolidatedModel> itemList2 =new ArrayList<>();
    ArrayList<SaleConsolidatedModel> itemList3 =new ArrayList<>();
    ArrayList<SaleConsolidatedModel> itemList4 =new ArrayList<>();
    ArrayList<SaleConsolidatedModel> itemList5 =new ArrayList<>();
    ArrayList<SaleConsolidatedModel> itemList6 =new ArrayList<>();
    ArrayList<SaleConsolidatedModel> itemList7 =new ArrayList<>();
    int y;
    String year,month,financialYear,prevYear;


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.sale_consolidated_raw,viewGroup,false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {





        myViewHolder.tvMonth.setText(itemList.get(i).getName());
        myViewHolder.tvFinYear.setText("2020-2021");
        myViewHolder.tvFinTarget.setText(itemList.get(i).getCyTarget()+" Unit(s)");
        myViewHolder.tvFinSold.setText(itemList.get(i).getCySold()+" Unit(s)");



        myViewHolder.tvPreYear.setText("2019-2020");
        myViewHolder.tvPreTarget.setText(itemList.get(i).getLyTarget()+" Unit(s)");
        myViewHolder.tvPreSold.setText(itemList.get(i).getLySold()+" Unit(s)");




        if (!itemList.get(i).getGrowth().contains("-")){
            myViewHolder.imgUpArrow.setVisibility(View.VISIBLE);
            myViewHolder.imgDownArrow.setVisibility(View.GONE);

            myViewHolder.tvGrowth.setText(itemList.get(i).getGrowth()+" %");
            myViewHolder.tvGrowth.setTextColor(Color.parseColor("#279916"));
        }else if (itemList.get(i).equals("0.00")) {
            myViewHolder.imgUpArrow.setVisibility(View.GONE);
            myViewHolder.imgDownArrow.setVisibility(View.GONE);
            myViewHolder.tvGrowth.setText(itemList.get(i).getGrowth() + " %");
            myViewHolder.tvGrowth.setTextColor(Color.parseColor("#F2B61111"));
        }else {
            myViewHolder.imgUpArrow.setVisibility(View.GONE);
            myViewHolder.imgDownArrow.setVisibility(View.VISIBLE);
            myViewHolder.tvGrowth.setText(itemList.get(i).getGrowth() + " %");
            myViewHolder.tvGrowth.setTextColor(Color.parseColor("#F2B61111"));
        }

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvMonth,tvFinYear,tvFinTarget,tvFinSold,tvPreYear,tvPreTarget,tvPreSold,tvGrowth;
        ImageView imgUpArrow,imgDownArrow;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvMonth=(TextView)itemView.findViewById(R.id.tvMonth);
            tvFinYear=(TextView)itemView.findViewById(R.id.tvFinYear);
            tvFinTarget=(TextView)itemView.findViewById(R.id.tvFinTarget);
            tvFinSold=(TextView)itemView.findViewById(R.id.tvFinSold);
            tvPreYear=(TextView)itemView.findViewById(R.id.tvPreYear);
            tvPreTarget=(TextView)itemView.findViewById(R.id.tvPreTarget);
            tvPreSold=(TextView)itemView.findViewById(R.id.tvPreSold);
            tvGrowth=(TextView)itemView.findViewById(R.id.tvGrowth);

            imgUpArrow=(ImageView)itemView.findViewById(R.id.imgUpArrow);
            imgDownArrow=(ImageView)itemView.findViewById(R.id.imgDownArrow);



        }
    }

    public SaleConsolidatedAdapter(ArrayList<SaleConsolidatedModel> itemList) {
        this.itemList = itemList;

    }
}
