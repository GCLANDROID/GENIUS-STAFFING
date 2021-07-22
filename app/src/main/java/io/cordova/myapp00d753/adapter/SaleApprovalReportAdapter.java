package io.cordova.myapp00d753.adapter;

import android.annotation.SuppressLint;
import android.content.Context;

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
import io.cordova.myapp00d753.activity.SaleApprovalActivity;
import io.cordova.myapp00d753.fragment.SaleApprovalFragment;
import io.cordova.myapp00d753.fragment.SaleRejectFragment;
import io.cordova.myapp00d753.module.EmpSaleModel;
import io.cordova.myapp00d753.module.SaleApprovalModule;
import io.cordova.myapp00d753.module.SaleApprovalReportModel;

public class SaleApprovalReportAdapter extends RecyclerView.Adapter<SaleApprovalReportAdapter.MyViewHolder> {
    ArrayList<SaleApprovalReportModel>itemList=new ArrayList<>();
    Context context;
    Fragment fm;
    boolean isSelectedAll;
    boolean isAll;
    @NonNull
    @Override
    public SaleApprovalReportAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.sale_approval_report_raw,viewGroup,false);

        return new SaleApprovalReportAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final SaleApprovalReportAdapter.MyViewHolder myViewHolder, @SuppressLint("RecyclerView") final int i) {
        final SaleApprovalReportModel attandanceModel = itemList.get(i);

        myViewHolder.tvDate.setText(itemList.get(i).getDate());
        myViewHolder.tvEmpName.setText(itemList.get(i).getEmpName());
        myViewHolder.tvToken.setText(itemList.get(i).getToken());
        myViewHolder.tvModel.setText(itemList.get(i).getModel());
        myViewHolder.tvCusName.setText(" "+itemList.get(i).getCusName());




        if (isAll){
            if (isSelectedAll){
                myViewHolder.imgLike.setVisibility(View.VISIBLE);

            }else {
                myViewHolder.imgLike.setVisibility(View.GONE);
            }
        }else {
            if (itemList.get(i).isSelected()){
                myViewHolder.imgLike.setVisibility(View.VISIBLE);
            }else {
                myViewHolder.imgLike.setVisibility(View.GONE);
            }
            myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    attandanceModel.setSelected(!attandanceModel.isSelected());
                    // holder.view.setBackgroundColor(attandanceModel.isSelected() ? Color.CYAN : Color.WHITE);

                    if (attandanceModel.isSelected()) {

                        myViewHolder.imgLike.setVisibility(View.VISIBLE);
                        itemList.get(i).setSelected(true);
                        notifyDataSetChanged();

                        ((SaleApprovalFragment) fm).updateAttendanceStatus(i, true );



                    } else {
                        myViewHolder.imgLike.setVisibility(View.GONE);
                        ((SaleApprovalFragment) fm).updateAttendanceStatus(i, false );
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
        TextView tvDate,tvEmpName,tvToken,tvCusName,tvModel,tvCusPhn,tvCusEmail,tvRemarks;
        LinearLayout llClick;
        ImageView imgLike;



        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate=(TextView)itemView.findViewById(R.id.tvDate);
            tvEmpName=(TextView)itemView.findViewById(R.id.tvEmpName);
            tvToken=(TextView)itemView.findViewById(R.id.tvToken);
            tvCusName=(TextView)itemView.findViewById(R.id.tvCusName);
            tvModel=(TextView)itemView.findViewById(R.id.tvModel);

            llClick=(LinearLayout)itemView.findViewById(R.id.llClick);
            imgLike=(ImageView)itemView.findViewById(R.id.imgLike);



        }
    }

    public SaleApprovalReportAdapter(ArrayList<SaleApprovalReportModel> itemList, Context context, Fragment fm) {
        this.itemList = itemList;
        this.context = context;
        this.fm = fm;
    }

    public void updateList(ArrayList<SaleApprovalReportModel> list){
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
