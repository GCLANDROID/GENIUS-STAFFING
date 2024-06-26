package io.cordova.myapp00d753.activity.metso.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collection;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.activity.metso.MetsoNewReimbursementClaimActivity;
import io.cordova.myapp00d753.module.SpineerItemModel;

public class WbsCodeFilterAdapter extends RecyclerView.Adapter<WbsCodeFilterAdapter.MyViewHolder>{
    Context context;
    ArrayList<SpineerItemModel>  wbsCodeList;
    ArrayList<SpineerItemModel> wbsCodeListAll;
    MetsoNewReimbursementClaimActivity.WbsCodeSelectListener mWbsCodeSelectListener;

    public WbsCodeFilterAdapter(Context context, ArrayList<SpineerItemModel> wbsCodeList) {
        this.context = context;
        this.wbsCodeList = wbsCodeList;
        this.wbsCodeListAll = new ArrayList<SpineerItemModel>(wbsCodeList);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.wbs_code_filter_layout,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.txtFilterItem.setText(wbsCodeList.get(position).getItemName());

        holder.txtFilterItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mWbsCodeSelectListener.onClick(wbsCodeList.get(position).getItemId(),wbsCodeList.get(position).getItemName());
            }
        });
    }

    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            ArrayList<SpineerItemModel> filteredList = new ArrayList<>();
            if (charSequence.toString().isEmpty()){
                filteredList.addAll(wbsCodeListAll);
            }else {
                for (SpineerItemModel wbsCode: wbsCodeListAll){
                    if (wbsCode.getItemName().toLowerCase().contains(charSequence.toString().toLowerCase())){
                        filteredList.add(wbsCode);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            wbsCodeList.clear();
            wbsCodeList.addAll((Collection<? extends SpineerItemModel>)filterResults.values);
            notifyDataSetChanged();
        }
    };

    @Override
    public int getItemCount() {
        return wbsCodeList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView txtFilterItem;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtFilterItem = itemView.findViewById(R.id.txtFilterItem);
        }
    }

    public void setWbsCodeSelectListener(MetsoNewReimbursementClaimActivity.WbsCodeSelectListener mWbsCodeSelectListener) {
        this.mWbsCodeSelectListener = mWbsCodeSelectListener;
    }
}
