package io.cordova.myapp00d753.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.module.EPSmodel;
import io.cordova.myapp00d753.module.GratuityModel;

public class GratuityNominationAdapter extends RecyclerView.Adapter<GratuityNominationAdapter.MyViewHolder> {
    ArrayList<GratuityModel>itemList=new ArrayList<>();
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.gratuity_nomination_row,viewGroup,false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.tvName.setText(itemList.get(i).getName());
        myViewHolder.tvAddress.setText(itemList.get(i).getAddress());
        myViewHolder.tvAge.setText(itemList.get(i).getAge());
        myViewHolder.tvRealtionShip.setText(itemList.get(i).getRelationship());
        myViewHolder.tvProportion.setText(itemList.get(i).getPortion());




    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvName,tvAddress,tvAge,tvRealtionShip,tvProportion;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName=(TextView)itemView.findViewById(R.id.tvName);
            tvAddress=(TextView)itemView.findViewById(R.id.tvAddress);
            tvAge=(TextView)itemView.findViewById(R.id.tvAge);
            tvRealtionShip=(TextView)itemView.findViewById(R.id.tvRealtionShip);
            tvProportion=(TextView) itemView.findViewById(R.id.tvProportion);

        }
    }

    public GratuityNominationAdapter(ArrayList<GratuityModel> itemList) {
        this.itemList = itemList;
    }
}
