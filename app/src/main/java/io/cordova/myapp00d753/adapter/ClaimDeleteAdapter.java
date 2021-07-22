package io.cordova.myapp00d753.adapter;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.activity.AttenApprovalActivity;
import io.cordova.myapp00d753.activity.ClaimDeletActivity;
import io.cordova.myapp00d753.module.AttendanceApprovalModule;
import io.cordova.myapp00d753.module.ClaimDeleteModule;
import io.cordova.myapp00d753.module.ClaimModule;

public class ClaimDeleteAdapter extends RecyclerView.Adapter<ClaimDeleteAdapter.MyViewHolder> {
    ArrayList<ClaimDeleteModule> claimList =new ArrayList<>();
    Context context;
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.claim_delraw,viewGroup,false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {
        final ClaimDeleteModule delModel = claimList.get(i);
        myViewHolder.tvDate.setText(claimList.get(i).getClaimDate());
        myViewHolder.tvAmount.setText("Rs. "+claimList.get(i).getClaimAmount());
        myViewHolder.tvPur.setText(claimList.get(i).getClaimType());
        myViewHolder.tvStatus.setText(claimList.get(i).getClaimStatus());
        if (claimList.get(i).isSelected()){
            myViewHolder.imgLike.setVisibility(View.VISIBLE);
        }else {
            myViewHolder.imgLike.setVisibility(View.GONE);
        }
        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delModel.setSelected(!delModel.isSelected());
                // holder.view.setBackgroundColor(attandanceModel.isSelected() ? Color.CYAN : Color.WHITE);

                if (delModel.isSelected()) {

                    myViewHolder.imgLike.setVisibility(View.VISIBLE);
                    claimList.get(i).setSelected(true);
                    notifyDataSetChanged();

                    ((ClaimDeletActivity) context).updateAttendanceStatus(i, true );



                } else {
                    myViewHolder.imgLike.setVisibility(View.GONE);
                    ((ClaimDeletActivity) context).updateAttendanceStatus(i, false);
                    claimList.get(i).setSelected(false);
                    notifyDataSetChanged();
                }

            }
        });



    }

    @Override
    public int getItemCount() {
        return claimList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate,tvAmount,tvPur,tvStatus,tvId;
        ImageView imgLike;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate=(TextView)itemView.findViewById(R.id.tvDate);
            tvAmount=(TextView)itemView.findViewById(R.id.tvAmount);
            tvPur=(TextView)itemView.findViewById(R.id.tvPur);
            tvStatus=(TextView)itemView.findViewById(R.id.tvStatus);
            imgLike=(ImageView)itemView.findViewById(R.id.imgLike);

        }
    }

    public ClaimDeleteAdapter(ArrayList<ClaimDeleteModule> claimList, Context context) {
        this.claimList = claimList;
        this.context = context;
    }
}
