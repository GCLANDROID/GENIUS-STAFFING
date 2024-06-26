package io.cordova.myapp00d753.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import io.cordova.myapp00d753.KYCFamilyModel;
import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.module.EPSmodel;

public class KYCFamilyAdapter extends RecyclerView.Adapter<KYCFamilyAdapter.MyViewHolder> {
    ArrayList<KYCFamilyModel>itemList=new ArrayList<>();
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.kyc_family_row,viewGroup,false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.tvName.setText(itemList.get(i).getFamilyMemberName());
        myViewHolder.tvGender.setText(itemList.get(i).getGender());
        myViewHolder.tvDOB.setText(itemList.get(i).getDob());
        myViewHolder.tvRealtionShip.setText(itemList.get(i).getRealationship());




    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvName,tvGender,tvDOB,tvRealtionShip;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName=(TextView)itemView.findViewById(R.id.tvName);
            tvGender=(TextView)itemView.findViewById(R.id.tvGender);
            tvDOB=(TextView)itemView.findViewById(R.id.tvDOB);
            tvRealtionShip=(TextView)itemView.findViewById(R.id.tvRealtionShip);


        }
    }

    public KYCFamilyAdapter(ArrayList<KYCFamilyModel> itemList) {
        this.itemList = itemList;
    }
}
