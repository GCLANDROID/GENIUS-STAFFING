package io.cordova.myapp00d753.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collection;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.activity.TempBankActivity;
import io.cordova.myapp00d753.activity.TempProfileActivity;
import io.cordova.myapp00d753.activity.metso.adapter.ComponentrFilterAdapter;
import io.cordova.myapp00d753.module.MainDocModule;
import io.cordova.myapp00d753.module.SpineerItemModel;

public class TempCommonFilterAdapter extends RecyclerView.Adapter<TempCommonFilterAdapter.MyViewHolder> /*implements Filterable*/ {

    Context context;
    ArrayList<MainDocModule> itemList;
    ArrayList<MainDocModule> allItemList;
    String selectFor;
    public TempCommonFilterAdapter(Context context, ArrayList<MainDocModule> itemList,String selectFor) {
        this.context = context;
        this.itemList = itemList;
        this.selectFor = selectFor;
        this.allItemList = new ArrayList<MainDocModule>(itemList);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.wbs_code_filter_layout,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.txtFilterItem.setText(itemList.get(position).getDocumentType());

        holder.txtFilterItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectFor.equalsIgnoreCase("bank_name")){
                    ((TempBankActivity) context).setText(itemList.get(position).getDocID(),itemList.get(position).getDocumentType(),selectFor);
                } else {
                    ((TempProfileActivity) context).setText(itemList.get(position).getDocID(),itemList.get(position).getDocumentType(),selectFor);
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }



    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            ArrayList<MainDocModule> filteredList = new ArrayList<>();
            if (charSequence.toString().isEmpty()){
                filteredList.addAll(allItemList);
            }else {
                for (MainDocModule item: allItemList){
                    if (item.getDocumentType().toLowerCase().contains(charSequence.toString().toLowerCase())){
                        filteredList.add(item);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            itemList.clear();
            itemList.addAll((Collection<? extends MainDocModule>) filterResults.values);
            notifyDataSetChanged();
        }

        /*@Override
        public CharSequence convertResultToString(Object resultValue) {
            return ((MainDocModule) resultValue).getDocumentType();
        }*/
    };




    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView txtFilterItem;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtFilterItem = itemView.findViewById(R.id.txtFilterItem);
        }
    }
}
