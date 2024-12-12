package io.cordova.myapp00d753.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;

import java.util.ArrayList;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.activity.EPSNominationActivity;
import io.cordova.myapp00d753.activity.KYCFamilyActivity;
import io.cordova.myapp00d753.module.EPSmodel;
import io.cordova.myapp00d753.utility.Util;

public class EPSNominationAdapter extends RecyclerView.Adapter<EPSNominationAdapter.MyViewHolder> {
    ArrayList<EPSmodel>itemList=new ArrayList<>();
    Context context;
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.eps_nomination_row,viewGroup,false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.tvName.setText(itemList.get(i).getName());
        myViewHolder.tvAddress.setText(itemList.get(i).getAddress());
        //myViewHolder.tvAge.setText(itemList.get(i).getAge());
        myViewHolder.tvAge.setText(Util.changeAnyDateFormat(itemList.get(i).getAge(),"dd MMMM yyyy","dd MMM yy"));
        myViewHolder.tvRealtionShip.setText(itemList.get(i).getRelationship());

        myViewHolder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((EPSNominationActivity)context).deleteItem(i);
                notifyDataSetChanged();
            }
        });

        myViewHolder.imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    ((EPSNominationActivity)context).editItem(i);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvName,tvAddress,tvAge,tvRealtionShip;
        ImageView imgDelete,imgEdit;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName=(TextView)itemView.findViewById(R.id.tvName);
            tvAddress=(TextView)itemView.findViewById(R.id.tvAddress);
            tvAge=(TextView)itemView.findViewById(R.id.tvAge);
            tvRealtionShip=(TextView)itemView.findViewById(R.id.tvRealtionShip);
            imgDelete=(ImageView) itemView.findViewById(R.id.imgDelete);
            imgEdit=(ImageView) itemView.findViewById(R.id.imgEdit);
        }
    }

    public EPSNominationAdapter(ArrayList<EPSmodel> itemList,Context context) {
        this.itemList = itemList;
        this.context=context;
    }
}
