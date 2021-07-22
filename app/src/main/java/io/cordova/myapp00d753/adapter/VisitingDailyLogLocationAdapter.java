package io.cordova.myapp00d753.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.module.VisitingDailyLogLocationModel;
import io.cordova.myapp00d753.module.VisitingLocationModel;


public class VisitingDailyLogLocationAdapter extends RecyclerView.Adapter<VisitingDailyLogLocationAdapter.MyViewHolder> {
    ArrayList<VisitingDailyLogLocationModel>itemList=new ArrayList<>();

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.visiting_dailyloglocation_raw,viewGroup,false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.tvLocation.setText(itemList.get(i).getLocation());
        myViewHolder.tvTime.setText("In Time: "+itemList.get(i).getTime());
        myViewHolder.tvStore.setText("Store: "+itemList.get(i).getStore());


    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvLocation,tvTime,tvStore;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvLocation=(TextView)itemView.findViewById(R.id.tvLocation);
            tvTime=(TextView)itemView.findViewById(R.id.tvTime);
            tvStore=(TextView)itemView.findViewById(R.id.tvStore);


        }
    }

    public VisitingDailyLogLocationAdapter(ArrayList<VisitingDailyLogLocationModel> itemList) {
        this.itemList = itemList;
    }
}
