package io.cordova.myapp00d753.adapter;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.activity.FormSixteenActivity;
import io.cordova.myapp00d753.activity.SalaryActivity;
import io.cordova.myapp00d753.module.FormSixteenModule;
import io.cordova.myapp00d753.module.SalaryModule;

public class FormSixteenAdapter extends RecyclerView.Adapter<FormSixteenAdapter.MyViewHolder> {
    ArrayList<FormSixteenModule>itemList=new ArrayList<>();
    Context context;
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.formsixteen_raw,viewGroup,false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

        myViewHolder.llParAView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (itemList.get(i).getIsPaidService()==1){
                    ((FormSixteenActivity)context).paymentOption(i,"PartA");


                }else {
                    Uri uri = Uri.parse(itemList.get(i).getPartA()); // missing 'http://' will cause crashed
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }

            }
        });


        myViewHolder.llParBView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (itemList.get(i).getIsPaidService()==1){
                    ((FormSixteenActivity)context).paymentOption(i,"PartB");


                }else {
                    Uri uri = Uri.parse(itemList.get(i).getPartB()); // missing 'http://' will cause crashed
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }

            }
        });

        myViewHolder.tvFinYear.setText(itemList.get(i).getFinancialYear());




        if (itemList.get(i).getIsPaidService()==1){
            myViewHolder.tvPartBView.setText("Paid Service  ");
            myViewHolder.tvPartAView.setText("Paid Service  ");
        }else {
            myViewHolder.tvPartBView.setText("View Document  ");
            myViewHolder.tvPartAView.setText("View Document  ");
        }

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvFinYear,tvPartAView,tvPartBView;
        LinearLayout llPartA,llPartB,llParAView,llParBView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFinYear=(TextView)itemView.findViewById(R.id.tvFinYear);
            tvPartAView=(TextView)itemView.findViewById(R.id.tvPartAView);
            tvPartBView=(TextView)itemView.findViewById(R.id.tvPartBView);

            llPartA=(LinearLayout) itemView.findViewById(R.id.llPartA);
            llPartB=(LinearLayout) itemView.findViewById(R.id.llPartB);

            llParAView=(LinearLayout) itemView.findViewById(R.id.llParAView);
            llParBView=(LinearLayout) itemView.findViewById(R.id.llParBView);

        }
    }

    public FormSixteenAdapter(ArrayList<FormSixteenModule> itemList, Context context) {
        this.itemList = itemList;
        this.context=context;
    }
}
