package io.cordova.myapp00d753.activity.honnasa.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.activity.honnasa.model.ReportModel;


public class HanasaSalesReportAdapter extends RecyclerView.Adapter<HanasaSalesReportAdapter.MyViewHolder> {
    Context context;
    ArrayList<ReportModel> salesReportList;

    public HanasaSalesReportAdapter(Context context, ArrayList<ReportModel> salesReportList) {
        this.context = context;
        this.salesReportList = salesReportList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.honasa_sales_entry_row,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.txtProductName.setText(salesReportList.get(position).Name);
        holder.txtStockInHand.setEnabled(false);
        holder.txtStockInHand.setText(String.valueOf(salesReportList.get(position).instock));
        holder.txtReceivingStock.setEnabled(false);
        holder.txtReceivingStock.setText(String.valueOf(salesReportList.get(position).ReceiveStock));
        holder.txtSalesDone.setEnabled(false);
        holder.txtSalesDone.setText(String.valueOf(salesReportList.get(position).SalesStock));
        holder.txtSalesValues.setEnabled(false);
        holder.txtSalesValues.setText(String.valueOf(salesReportList.get(position).StockValue));
        holder.txtClosingStock.setEnabled(false);
        holder.txtClosingStock.setText(String.valueOf(salesReportList.get(position).getClosingstock()));
        holder.txtReceivingDate.setVisibility(View.VISIBLE);
        holder.txtReceivingDate.setText(salesReportList.get(position).getReceiveDate());
    }

    @Override
    public int getItemCount() {
        return salesReportList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView txtProductName,txtReceivingDate;
        EditText txtStockInHand,txtReceivingStock,txtSalesDone,txtSalesValues,txtClosingStock;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtProductName = itemView.findViewById(R.id.txtProductName);
            txtStockInHand = itemView.findViewById(R.id.txtStockInHand);
            txtReceivingStock = itemView.findViewById(R.id.txtReceivingStock);
            txtSalesDone = itemView.findViewById(R.id.txtSalesDone);
            txtSalesValues = itemView.findViewById(R.id.txtSalesValues);
            txtClosingStock = itemView.findViewById(R.id.txtClosingStock);
            txtReceivingDate = itemView.findViewById(R.id.txtReceivingDate);
        }
    }
}
