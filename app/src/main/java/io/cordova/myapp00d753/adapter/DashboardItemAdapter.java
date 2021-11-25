package io.cordova.myapp00d753.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.module.AttendanceModule;
import io.cordova.myapp00d753.module.DashboardItemModel;

public class DashboardItemAdapter extends RecyclerView.Adapter<DashboardItemAdapter.MyViewHolder> {
    ArrayList<DashboardItemModel>itemList=new ArrayList<>();
    Context context;
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.dashboard_item_raw,viewGroup,false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.tvTitle.setText(itemList.get(i).getItemName());
        myViewHolder.imgItemPic.setImageResource(itemList.get(i).getItempic());



    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        ImageView imgItemPic;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle=(TextView)itemView.findViewById(R.id.tvTitle);
            imgItemPic=(ImageView) itemView.findViewById(R.id.imgItemPic);

        }
    }

    public DashboardItemAdapter(ArrayList<DashboardItemModel> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
    }
}
