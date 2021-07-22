package io.cordova.myapp00d753.adapter;

import android.annotation.SuppressLint;
import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.activity.AttenApprovalActivity;
import io.cordova.myapp00d753.activity.SaleApprovalActivity;
import io.cordova.myapp00d753.module.AttendanceApprovalModule;
import io.cordova.myapp00d753.module.SaleApprovalModule;
import io.cordova.myapp00d753.module.SaleApprovalReportModel;

public class SaleApprovalAdapter extends RecyclerView.Adapter<SaleApprovalAdapter.MyViewHolder> {
    ArrayList<SaleApprovalModule>itemList=new ArrayList<>();
    Context context;
    boolean isSelectedAll;
    boolean isAll;
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.sale_approval_raw,viewGroup,false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, @SuppressLint("RecyclerView") final int i) {
        final SaleApprovalModule attandanceModel = itemList.get(i);

        myViewHolder.tvDate.setText(itemList.get(i).getDate());
        myViewHolder.tvEmpName.setText(itemList.get(i).getEmpName());
        myViewHolder.tvToken.setText(itemList.get(i).getToken());
        myViewHolder.tvCusName.setText(itemList.get(i).getCusName());
        myViewHolder.tvCategory.setText(itemList.get(i).getCateGory());







        if (isAll){
            if (isSelectedAll){
                myViewHolder.llSelect.setVisibility(View.VISIBLE);

            }else {
                myViewHolder.llSelect.setVisibility(View.GONE);
            }
        }else {
            if (itemList.get(i).isSelected()){
                myViewHolder.llSelect.setVisibility(View.VISIBLE);
            }else {
                myViewHolder.llSelect.setVisibility(View.GONE);
            }
            myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    attandanceModel.setSelected(!attandanceModel.isSelected());
                    // holder.view.setBackgroundColor(attandanceModel.isSelected() ? Color.CYAN : Color.WHITE);

                    if (attandanceModel.isSelected()) {

                        myViewHolder.llSelect.setVisibility(View.VISIBLE);
                        itemList.get(i).setSelected(true);
                        notifyDataSetChanged();

                        ((SaleApprovalActivity) context).updateAttendanceStatus(i, true );



                    } else {
                        myViewHolder.llSelect.setVisibility(View.GONE);
                        ((SaleApprovalActivity) context).updateAttendanceStatus(i, false);
                        itemList.get(i).setSelected(false);
                        notifyDataSetChanged();
                    }

                }
            });
        }







    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate,tvEmpName,tvToken,tvCusName,tvCategory,tvLocation;
        LinearLayout llSelect;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate=(TextView)itemView.findViewById(R.id.tvDate);
            tvEmpName=(TextView)itemView.findViewById(R.id.tvEmpName);
            tvToken=(TextView)itemView.findViewById(R.id.tvToken);
            tvCusName=(TextView)itemView.findViewById(R.id.tvCusName);
            tvCategory=(TextView)itemView.findViewById(R.id.tvCategory);
            llSelect=(LinearLayout)itemView.findViewById(R.id.llSelect);


        }
    }

    public SaleApprovalAdapter(ArrayList<SaleApprovalModule> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
    }

    public void updateList(ArrayList<SaleApprovalModule> list){
        itemList = list;
        notifyDataSetChanged();
    }

    public void selectAll(){
        isSelectedAll=true;
        isAll=true;
        notifyDataSetChanged();
    }
    public void unselectall(){
        isSelectedAll=false;
        isAll=false;
        notifyDataSetChanged();
    }


}
