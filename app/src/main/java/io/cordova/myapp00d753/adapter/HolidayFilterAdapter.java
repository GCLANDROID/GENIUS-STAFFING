package io.cordova.myapp00d753.adapter;

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
import io.cordova.myapp00d753.activity.HolidayMarkingActivity;
import io.cordova.myapp00d753.activity.metso.adapter.SupervisorFilterAdapter;
import io.cordova.myapp00d753.module.HolidayMarkModel;
import io.cordova.myapp00d753.module.SpineerItemModel;

public class HolidayFilterAdapter extends RecyclerView.Adapter<HolidayFilterAdapter.MyViewHolder>{

    Context context;
    ArrayList<HolidayMarkModel> holidayList;
    ArrayList<HolidayMarkModel> holidayListAll;
    public HolidayFilterAdapter(Context context, ArrayList<HolidayMarkModel> holidayList) {
        this.context = context;
        this.holidayList = holidayList;
        this.holidayListAll = new ArrayList<HolidayMarkModel>(holidayList);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.wbs_code_filter_layout,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.txtFilterItem.setText(holidayList.get(position).getHoliday());

        holder.txtFilterItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((HolidayMarkingActivity) context).dateFormat(holidayList.get(position).getHoliday(),holidayList.get(position).getHolidayDate());
            }
        });
    }

    @Override
    public int getItemCount() {
        return holidayList.size();
    }

    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            ArrayList<HolidayMarkModel> filteredList = new ArrayList<>();
            if (charSequence.toString().isEmpty()){
                filteredList.addAll(holidayListAll);
            }else {
                for (HolidayMarkModel  holiday: holidayListAll){
                    if (holiday.getHoliday().toLowerCase().contains(charSequence.toString().toLowerCase())){
                        filteredList.add(holiday);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            holidayList.clear();
            holidayList.addAll((Collection<? extends HolidayMarkModel>)filterResults.values);
            notifyDataSetChanged();
        }
    };



    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView txtFilterItem;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtFilterItem = itemView.findViewById(R.id.txtFilterItem);
        }
    }
}
