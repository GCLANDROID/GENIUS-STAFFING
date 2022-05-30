package io.cordova.myapp00d753.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;



import java.util.ArrayList;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.fragment.ApproverFragment;
import io.cordova.myapp00d753.module.ApprovalModel;

public class ApproverAdapter extends RecyclerView.Adapter<ApproverAdapter.MyViewHolder> {
    ArrayList<ApprovalModel>itemList=new ArrayList<>();
    Fragment context;
    Context mContex;
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.approver_raw,viewGroup,false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {

        final ApprovalModel approvalModel = itemList.get(i);

        myViewHolder.tvStrtDate.setText(itemList.get(i).getStartDate());
        myViewHolder.tvEndDate.setText(itemList.get(i).getEndDate());
        myViewHolder.tvValue.setText(itemList.get(i).getValue());

        if (itemList.get(i).getApprovalStatus().equals("Pending")){
            myViewHolder.llTick.setVisibility(View.VISIBLE);
            myViewHolder.llGreen.setVisibility(View.GONE);
            myViewHolder.llYellow.setVisibility(View.GONE);
        }else {
            myViewHolder.llTick.setVisibility(View.GONE);
            myViewHolder.llGreen.setVisibility(View.GONE);
            myViewHolder.llYellow.setVisibility(View.GONE);
        }
        if (approvalModel.isSelected()){
            myViewHolder.imgTick.setVisibility(View.VISIBLE);
        }else {
            myViewHolder.imgTick.setVisibility(View.GONE);
        }

        myViewHolder.llTick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                approvalModel.setSelected(!approvalModel.isSelected());
                // holder.view.setBackgroundColor(attandanceModel.isSelected() ? Color.CYAN : Color.WHITE);

                if (approvalModel.isSelected()) {

                    myViewHolder.imgTick.setVisibility(View.VISIBLE);
                    notifyDataSetChanged();

                    ((ApproverFragment) context).updateAttendanceStatus(i, true );



                } else {
                    /*myViewHolder.imgFrstHalf.setVisibility(View.GONE);
                    myViewHolder.imgScndHalf.setVisibility(View.GONE);
                    myViewHolder.imgFull.setVisibility(View.GONE);*/
                    myViewHolder.imgTick.setVisibility(View.GONE);
                    ((ApproverFragment) context).updateAttendanceStatus(i, false);
                    itemList.get(i).setSelected(false);
                    notifyDataSetChanged();
                }


            }
        });


            myViewHolder.tvName.setText("Emp. Name:");
            myViewHolder.tvType.setText("Leave:");
            myViewHolder.tvleaveStrtDate.setText("Start date:");
            myViewHolder.tvLeaveEndDate.setText("End date:");
            myViewHolder.tvLeaveValue.setText("Value:");
            myViewHolder.tvLeaveReason.setText("Reason:");



        //empname


            myViewHolder.tvEmpName.setText(itemList.get(i).getEmpName());


        //type


            myViewHolder.tvLeaveType.setText(itemList.get(i).getLeave());


        //reason

        myViewHolder.tvReason.setText(itemList.get(i).getReason());

        myViewHolder.tvStatus.setText(itemList.get(i).getApprovalStatus());




    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvEmpName,tvLeaveType,tvStrtDate,tvEndDate,tvValue,tvReason,tvStatus;
        LinearLayout llTick,llGreen,llYellow;
        ImageView imgTick;
        TextView tvLeaveReason,tvLeaveValue,tvLeaveEndDate,tvleaveStrtDate,tvType,tvName;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvEmpName=(TextView)itemView.findViewById(R.id.tvEmpName);
            tvLeaveType=(TextView)itemView.findViewById(R.id.tvLeaveType);
            tvStrtDate=(TextView)itemView.findViewById(R.id.tvStrtDate);
            tvValue=(TextView)itemView.findViewById(R.id.tvValue);
            tvEndDate=(TextView)itemView.findViewById(R.id.tvEndDate);
            tvReason=(TextView)itemView.findViewById(R.id.tvReason);
            imgTick=(ImageView)itemView.findViewById(R.id.imgTick);
            llTick=(LinearLayout)itemView.findViewById(R.id.llTick);
            llGreen=(LinearLayout)itemView.findViewById(R.id.llGreen);
            llYellow=(LinearLayout)itemView.findViewById(R.id.llYellow);

            tvLeaveReason=(TextView)itemView.findViewById(R.id.tvLeaveReason);
            tvLeaveValue=(TextView)itemView.findViewById(R.id.tvLeaveValue);
            tvLeaveEndDate=(TextView)itemView.findViewById(R.id.tvLeaveEndDate);
            tvleaveStrtDate=(TextView)itemView.findViewById(R.id.tvleaveStrtDate);
            tvType=(TextView)itemView.findViewById(R.id.tvType);
            tvName=(TextView)itemView.findViewById(R.id.tvName);
            tvStatus=(TextView)itemView.findViewById(R.id.tvStatus);


        }
    }

    public ApproverAdapter(ArrayList<ApprovalModel> itemList, Fragment context, Context mContext) {
        this.itemList = itemList;
        this.context = context;
        this.mContex=mContext;
    }
}
