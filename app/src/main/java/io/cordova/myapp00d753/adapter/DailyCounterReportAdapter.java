package io.cordova.myapp00d753.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.module.DailyCounterModel;
import io.cordova.myapp00d753.module.SaleQTDModel;

public class DailyCounterReportAdapter extends RecyclerView.Adapter<DailyCounterReportAdapter.MyViewHolder> {
    ArrayList<DailyCounterModel> itemList =new ArrayList<>();
    Context context;


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.daily_counter_raw,viewGroup,false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int i) {

        myViewHolder.tvPunchIn.setText(itemList.get(i).getPunchInDate()+"-"+itemList.get(i).getPunchInTime());
        myViewHolder.tvPunchInRemark.setText(itemList.get(i).getPunchInRemark());
        myViewHolder.tvPunchInAddr.setText(itemList.get(i).getPunchInAddr());

        myViewHolder.tvCounter.setText(itemList.get(i).getCounter());

        myViewHolder.llInImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!itemList.get(i).getPunchInImage().equals("")) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(itemList.get(i).getPunchInImage()));
                    context.startActivity(browserIntent);
                }else {
                    Toast.makeText(context,"Sorry!No Image found",Toast.LENGTH_LONG).show();
                }
            }
        });

        myViewHolder.llOutImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!itemList.get(i).getPunchInImage().equals("")) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(itemList.get(i).getPunchOutImage()));
                    context.startActivity(browserIntent);
                }else {
                    Toast.makeText(context,"Sorry!No Image found",Toast.LENGTH_LONG).show();
                }
            }
        });

        if (!itemList.get(i).getPunchOutTime().equals("")){
            myViewHolder.tvPunchOut.setText("-");


        }else {
            myViewHolder.tvPunchOut.setText(itemList.get(i).getPunchOutDate()+"-"+itemList.get(i).getPunchOutTime());

        }

        if (!itemList.get(i).getPunchOutRemark().equals("")){
            myViewHolder.tvPunchOutRemark.setText("-");
        }else {
            myViewHolder.tvPunchOutRemark.setText(itemList.get(i).getPunchOutRemark());
        }

        if (!itemList.get(i).getPunchOutAddr().equals("")){
            myViewHolder.tvPunchOutAddr.setText("-");
        }else {
            myViewHolder.tvPunchOutAddr.setText(itemList.get(i).getPunchOutAddr());
        }

        if (!itemList.get(i).getPunchOutImage().equals("")){
            myViewHolder.llOutImage.setVisibility(View.VISIBLE);
        }else {
            myViewHolder.llOutImage.setVisibility(View.GONE);

        }












    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvPunchIn,tvPunchInRemark,tvPunchInAddr,tvPunchOut,tvPunchOutRemark,tvPunchOutAddr,tvCounter;
        LinearLayout llInImage,llOutImage;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvPunchIn=(TextView)itemView.findViewById(R.id.tvPunchIn);
            tvPunchInRemark=(TextView)itemView.findViewById(R.id.tvPunchInRemark);
            tvPunchInAddr=(TextView)itemView.findViewById(R.id.tvPunchInAddr);
            tvPunchOut=(TextView)itemView.findViewById(R.id.tvPunchOut);
            tvPunchOutRemark=(TextView)itemView.findViewById(R.id.tvPunchOutRemark);
            tvPunchOutAddr=(TextView)itemView.findViewById(R.id.tvPunchOutAddr);
            tvCounter=(TextView)itemView.findViewById(R.id.tvCounter);

            llInImage=(LinearLayout)itemView.findViewById(R.id.llInImage);
            llOutImage=(LinearLayout)itemView.findViewById(R.id.llOutImage);





        }
    }

    public DailyCounterReportAdapter(ArrayList<DailyCounterModel> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
    }
}
