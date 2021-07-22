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
import io.cordova.myapp00d753.module.CounterVisitModel;

public class CounterWTDAdapter extends RecyclerView.Adapter<CounterWTDAdapter.MyViewHolder> {
    ArrayList<CounterVisitModel> itemList =new ArrayList<>();


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.counter_report_raw,viewGroup,false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

        myViewHolder.tvDate.setText(itemList.get(i).getDate());
        myViewHolder.tvCount.setText(itemList.get(i).getVisitCount());
        myViewHolder.llPrecount.setVisibility(View.GONE);
        myViewHolder.tvCYTitle.setText("Visit(s): ");










    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate,tvCount;
        LinearLayout llPrecount;
        TextView tvCYTitle;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate=(TextView)itemView.findViewById(R.id.tvDate);
            tvCount=(TextView)itemView.findViewById(R.id.tvCount);
            tvCYTitle=(TextView)itemView.findViewById(R.id.tvCYTitle);
            llPrecount=(LinearLayout)itemView.findViewById(R.id.llPrecount);




        }
    }

    public CounterWTDAdapter(ArrayList<CounterVisitModel> itemList) {
        this.itemList = itemList;
    }
}
