package io.cordova.myapp00d753.adapter;


import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.module.SaleQTDModel;
import io.cordova.myapp00d753.module.TopPerformerModel;

public class TopPerformerAdapter extends RecyclerView.Adapter<TopPerformerAdapter.MyViewHolder> {
    ArrayList<TopPerformerModel> itemList =new ArrayList<>();


    @NonNull
    @Override
    public TopPerformerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.toperformer_raw,viewGroup,false);

        return new TopPerformerAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TopPerformerAdapter.MyViewHolder myViewHolder, int i) {
        myViewHolder.tvPerformer.setText(itemList.get(i).getName());
        myViewHolder.tvTarget.setText(itemList.get(i).getTarget());
        myViewHolder.tvSold.setText(itemList.get(i).getSold());
        myViewHolder.tvAchivement.setText(itemList.get(i).getAchv()+" %");




    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvPerformer,tvTarget,tvSold,tvAchivement,tvLocation;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPerformer=(TextView)itemView.findViewById(R.id.tvPerformer);
            tvTarget=(TextView)itemView.findViewById(R.id.tvTarget);
            tvSold=(TextView)itemView.findViewById(R.id.tvSold);
            tvAchivement=(TextView)itemView.findViewById(R.id.tvAchivement);
            tvLocation=(TextView)itemView.findViewById(R.id.tvLocation);





        }
    }

    public TopPerformerAdapter(ArrayList<TopPerformerModel> itemList) {
        this.itemList = itemList;
    }
}
