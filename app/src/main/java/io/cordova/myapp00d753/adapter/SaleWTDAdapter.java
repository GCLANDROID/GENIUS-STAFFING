package io.cordova.myapp00d753.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.module.SaleQTDModel;

public class SaleWTDAdapter extends RecyclerView.Adapter<SaleWTDAdapter.MyViewHolder> {
    ArrayList<SaleQTDModel> itemList =new ArrayList<>();


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.saleqtd,viewGroup,false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.tvMonth.setText(itemList.get(i).getWeek());
        myViewHolder.tvFinYear.setText(itemList.get(i).getFinYear());
        myViewHolder.tvFinTarget.setText(itemList.get(i).getTarget()+" Unit(s)");
        myViewHolder.tvFinSold.setText(itemList.get(i).getSold()+" Unit(s)");
        myViewHolder.tvFinPercentage.setText(itemList.get(i).getPercentage()+" %");


        myViewHolder.tvPreYear.setText(itemList.get(i).getPreYear());
        myViewHolder.tvPreTarget.setText(itemList.get(i).getPreTarget()+" Unit(s)");
        myViewHolder.tvPreSold.setText(itemList.get(i).getPreSold()+" Unit(s)");
        myViewHolder.tvPrePercentage.setText(itemList.get(i).getPrePercentage()+" %");

        double grow=Double.valueOf(itemList.get(i).getGrowth());

        if (grow>0.00){
            myViewHolder.imgUpArrow.setVisibility(View.VISIBLE);
            myViewHolder.imgDownArrow.setVisibility(View.GONE);

            myViewHolder.tvGrowth.setText(itemList.get(i).getGrowth()+" %");
        }else if (grow<0.00){
            myViewHolder.imgUpArrow.setVisibility(View.GONE);
            myViewHolder.imgDownArrow.setVisibility(View.VISIBLE);
            myViewHolder.tvGrowth.setText(itemList.get(i).getGrowth()+" %");
        }else {
            myViewHolder.imgUpArrow.setVisibility(View.GONE);
            myViewHolder.imgDownArrow.setVisibility(View.GONE);
            myViewHolder.tvGrowth.setText("");
        }

        myViewHolder.llPre.setVisibility(View.GONE);
        myViewHolder.llCY.setVisibility(View.GONE);
        myViewHolder.llGrowth.setVisibility(View.GONE);











    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvMonth,tvFinYear,tvFinTarget,tvFinSold,tvFinPercentage,tvPreYear,tvPreTarget,tvPreSold,tvPrePercentage,tvGrowth;
        ImageView imgUpArrow,imgDownArrow;
        LinearLayout llPre,llCY,llGrowth;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvMonth=(TextView)itemView.findViewById(R.id.tvMonth);
            tvFinYear=(TextView)itemView.findViewById(R.id.tvFinYear);
            tvFinTarget=(TextView)itemView.findViewById(R.id.tvFinTarget);
            tvFinSold=(TextView)itemView.findViewById(R.id.tvFinSold);
            tvFinPercentage=(TextView)itemView.findViewById(R.id.tvFinPercentage);
            tvPreYear=(TextView)itemView.findViewById(R.id.tvPreYear);
            tvPreTarget=(TextView)itemView.findViewById(R.id.tvPreTarget);
            tvPreSold=(TextView)itemView.findViewById(R.id.tvPreSold);
            tvPrePercentage=(TextView)itemView.findViewById(R.id.tvPrePercentage);
            tvGrowth=(TextView)itemView.findViewById(R.id.tvGrowth);

            imgUpArrow=(ImageView)itemView.findViewById(R.id.imgUpArrow);
            imgDownArrow=(ImageView)itemView.findViewById(R.id.imgDownArrow);
            llPre=(LinearLayout)itemView.findViewById(R.id.llPre);
            llCY=(LinearLayout)itemView.findViewById(R.id.llCY);
            llGrowth=(LinearLayout)itemView.findViewById(R.id.llGrowth);



        }
    }

    public SaleWTDAdapter(ArrayList<SaleQTDModel> itemList) {
        this.itemList = itemList;
    }
}
