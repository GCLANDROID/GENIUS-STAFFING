package io.cordova.myapp00d753.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.activity.GeoFenceApprovalActivity;
import io.cordova.myapp00d753.module.GeoFenceApprovalModel;


public class GeoFenceApprovalAdapter extends RecyclerView.Adapter<GeoFenceApprovalAdapter.MyViewHolder> {
    ArrayList<GeoFenceApprovalModel>itemList=new ArrayList<>();
    Context context;

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.geofenceapproval_row,viewGroup,false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, @SuppressLint("RecyclerView") final int i) {
        final GeoFenceApprovalModel approvalModel = itemList.get(i);
        if (approvalModel.isSelected()){
            myViewHolder.imgTick.setVisibility(View.VISIBLE);
        }else {
            myViewHolder.imgTick.setVisibility(View.GONE);
        }

        myViewHolder.tvAddress.setText(itemList.get(i).getAddress()+" ( latt:- "+itemList.get(i).getLaat()+" , Long:- "+itemList.get(i).getLoong());
        myViewHolder.tvEmpName.setText(itemList.get(i).getEmpName());
        if (itemList.get(i).getApproverStatus()==-1){
            myViewHolder.lnTick.setVisibility(View.VISIBLE);
            myViewHolder.tvApprovalStatus.setVisibility(View.GONE);
        }else {
            myViewHolder.lnTick.setVisibility(View.GONE);
            myViewHolder.tvApprovalStatus.setVisibility(View.VISIBLE);
            if (itemList.get(i).getApproverStatus()==1){
                myViewHolder.tvApprovalStatus.setText("Approved");
                myViewHolder.tvApprovalStatus.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#f20dc24d")));
            }else if (itemList.get(i).getApproverStatus()==0){
                myViewHolder.tvApprovalStatus.setText("Rejected");
                myViewHolder.tvApprovalStatus.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#cc0512")));
            }
        }

        try {
            Picasso.with(context)
                    .load(itemList.get(i).getImageFile())
                    .placeholder(R.drawable.load)   // optional
                    .error(R.drawable.load)      // optional
                    .into(myViewHolder.imgLocation);

        } catch (Exception e) {
            e.printStackTrace();
        }



        myViewHolder.lnTick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                approvalModel.setSelected(!approvalModel.isSelected());
                // holder.view.setBackgroundColor(attandanceModel.isSelected() ? Color.CYAN : Color.WHITE);

                if (approvalModel.isSelected()) {

                    myViewHolder.imgTick.setVisibility(View.VISIBLE);
                    notifyDataSetChanged();

                    ((GeoFenceApprovalActivity) context).updateAttendanceStatus(i, true );



                } else {
                    /*myViewHolder.imgFrstHalf.setVisibility(View.GONE);
                    myViewHolder.imgScndHalf.setVisibility(View.GONE);
                    myViewHolder.imgFull.setVisibility(View.GONE);*/
                    myViewHolder.imgTick.setVisibility(View.GONE);
                    ((GeoFenceApprovalActivity) context).updateAttendanceStatus(i, false);
                    itemList.get(i).setSelected(false);
                    notifyDataSetChanged();
                }


            }
        });


    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvEmpName,tvAddress;
        ImageView imgLocation,imgTick;
        LinearLayout lnTick;
        TextView tvApprovalStatus;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAddress=(TextView)itemView.findViewById(R.id.tvAddress);
            tvEmpName=(TextView)itemView.findViewById(R.id.tvEmpName);
            tvApprovalStatus=(TextView)itemView.findViewById(R.id.tvApprovalStatus);

            imgLocation=(ImageView) itemView.findViewById(R.id.imgLocation);
            imgTick=(ImageView) itemView.findViewById(R.id.imgTick);

            lnTick=(LinearLayout) itemView.findViewById(R.id.lnTick);


        }
    }

    public GeoFenceApprovalAdapter(ArrayList<GeoFenceApprovalModel> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
    }
}
