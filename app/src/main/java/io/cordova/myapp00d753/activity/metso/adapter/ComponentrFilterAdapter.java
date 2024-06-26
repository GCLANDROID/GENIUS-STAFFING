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

public class ComponentrFilterAdapter extends RecyclerView.Adapter<ComponentrFilterAdapter.MyViewHolder>{

    Context context;
    ArrayList<SpineerItemModel> costCenterList;
    ArrayList<SpineerItemModel> costCenterListAll;
    MetsoNewReimbursementClaimActivity.CostCenterSelectListener mCostCenterSelectListener;
    public ComponentrFilterAdapter(Context context, ArrayList<SpineerItemModel> costCenterList) {
        this.context = context;
        this.costCenterList = costCenterList;
        this.costCenterListAll = new ArrayList<SpineerItemModel>(costCenterList);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.wbs_code_filter_layout,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.txtFilterItem.setText(costCenterList.get(position).getItemName());

        holder.txtFilterItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCostCenterSelectListener.onClick(costCenterList.get(position).getItemId(),costCenterList.get(position).getItemName());
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
                filteredList.addAll(costCenterListAll);
            }else {
                for (SpineerItemModel costCenter: costCenterListAll){
                    if (costCenter.getItemName().toLowerCase().contains(charSequence.toString().toLowerCase())){
                        filteredList.add(costCenter);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            costCenterList.clear();
            costCenterList.addAll((Collection<? extends SpineerItemModel>)filterResults.values);
            notifyDataSetChanged();
        }
    };

    @Override
    public int getItemCount() {
        return costCenterList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView txtFilterItem;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtFilterItem = itemView.findViewById(R.id.txtFilterItem);
        }
    }

    public void setCostCenterSelectListener(MetsoNewReimbursementClaimActivity.CostCenterSelectListener mCostCenterSelectListener) {
        this.mCostCenterSelectListener = mCostCenterSelectListener;
    }
}
