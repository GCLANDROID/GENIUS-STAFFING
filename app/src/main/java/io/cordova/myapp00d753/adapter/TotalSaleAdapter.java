package io.cordova.myapp00d753.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.activity.SaleApprovalActivity;
import io.cordova.myapp00d753.module.SaleApprovalModule;

public class TotalSaleAdapter extends RecyclerView.Adapter<TotalSaleAdapter.MyViewHolder> {
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
        myViewHolder.llClick.setVisibility(View.GONE);







    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate,tvEmpName,tvToken,tvCusName,tvCategory;
        LinearLayout llSelect,llClick;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate=(TextView)itemView.findViewById(R.id.tvDate);
            tvEmpName=(TextView)itemView.findViewById(R.id.tvEmpName);
            tvToken=(TextView)itemView.findViewById(R.id.tvToken);
            tvCusName=(TextView)itemView.findViewById(R.id.tvCusName);
            tvCategory=(TextView)itemView.findViewById(R.id.tvCategory);
            llSelect=(LinearLayout)itemView.findViewById(R.id.llSelect);
            llClick=(LinearLayout)itemView.findViewById(R.id.llClick);



        }
    }

    public TotalSaleAdapter(ArrayList<SaleApprovalModule> itemList, Context context) {
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
