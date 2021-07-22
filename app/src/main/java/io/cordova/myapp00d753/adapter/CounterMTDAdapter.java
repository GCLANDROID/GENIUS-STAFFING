package io.cordova.myapp00d753.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.module.CounterVisitModel;

public class CounterMTDAdapter extends RecyclerView.Adapter<CounterMTDAdapter.MyViewHolder> {
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
        myViewHolder.tvPreCount.setText(itemList.get(i).getPreCount());
        myViewHolder.tvLYTitle.setText("LY Monthly Visit(s): ");
        myViewHolder.tvCYTitle.setText("CY Monthly Visit(s): ");









    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate,tvCount,tvPreCount;
        TextView tvLYTitle,tvCYTitle;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate=(TextView)itemView.findViewById(R.id.tvDate);
            tvCount=(TextView)itemView.findViewById(R.id.tvCount);
            tvPreCount=(TextView)itemView.findViewById(R.id.tvPreCount);
            tvLYTitle=(TextView)itemView.findViewById(R.id.tvLYTitle);
            tvCYTitle=(TextView)itemView.findViewById(R.id.tvCYTitle);




        }
    }

    public CounterMTDAdapter(ArrayList<CounterVisitModel> itemList) {
        this.itemList = itemList;
    }
}
