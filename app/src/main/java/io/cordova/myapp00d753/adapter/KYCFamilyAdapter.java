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

import io.cordova.myapp00d753.KYCFamilyModel;
import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.activity.KYCFamilyActivity;
import io.cordova.myapp00d753.module.EPSmodel;

public class KYCFamilyAdapter extends RecyclerView.Adapter<KYCFamilyAdapter.MyViewHolder> {
    ArrayList<KYCFamilyModel>itemList=new ArrayList<>();
    Context context;
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

        myViewHolder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((KYCFamilyActivity)context).deleteItem(i);
                notifyDataSetChanged();
            }
        });

        myViewHolder.imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    ((KYCFamilyActivity)context).editItem(i);
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
        TextView tvName,tvGender,tvDOB,tvRealtionShip;
        ImageView imgDelete,imgEdit;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName=(TextView)itemView.findViewById(R.id.tvName);
            tvGender=(TextView)itemView.findViewById(R.id.tvGender);
            tvDOB=(TextView)itemView.findViewById(R.id.tvDOB);
            tvRealtionShip=(TextView)itemView.findViewById(R.id.tvRealtionShip);
            imgDelete=(ImageView) itemView.findViewById(R.id.imgDelete);
            imgEdit=(ImageView) itemView.findViewById(R.id.imgEdit);
        }
    }

    public KYCFamilyAdapter(ArrayList<KYCFamilyModel> itemList,Context context) {
        this.itemList = itemList;
        this.context=context;
    }
}
