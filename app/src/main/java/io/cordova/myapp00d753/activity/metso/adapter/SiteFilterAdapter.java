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

public class SiteFilterAdapter extends RecyclerView.Adapter<SiteFilterAdapter.myViewHolder>{

    Context context;
    ArrayList<SpineerItemModel> siteList;
    ArrayList<SpineerItemModel> siteListAll;
    MetsoNewReimbursementClaimActivity.SiteSelectListener mSiteSelectListener;

    public SiteFilterAdapter(Context context, ArrayList<SpineerItemModel> siteList) {
        this.context = context;
        this.siteList = siteList;
        this.siteListAll = new ArrayList<SpineerItemModel>(siteList);
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.wbs_code_filter_layout,parent,false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.txtFilterItem.setText(siteList.get(position).getItemName());

        holder.txtFilterItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSiteSelectListener.onClick(siteList.get(position).getItemId(),siteList.get(position).getItemName());
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
                filteredList.addAll(siteListAll);
            }else {
                for (SpineerItemModel site: siteListAll){
                    if (site.getItemName().toLowerCase().contains(charSequence.toString().toLowerCase())){
                        filteredList.add(site);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            siteList.clear();
            siteList.addAll((Collection<? extends SpineerItemModel>)filterResults.values);
            notifyDataSetChanged();
        }
    };

    @Override
    public int getItemCount() {
        return siteList.size();
    }

    class myViewHolder extends RecyclerView.ViewHolder{
        TextView txtFilterItem;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            txtFilterItem = itemView.findViewById(R.id.txtFilterItem);
        }
    }

    public void setSiteSelectListener(MetsoNewReimbursementClaimActivity.SiteSelectListener mSiteSelectListener) {
        this.mSiteSelectListener = mSiteSelectListener;
    }
}
