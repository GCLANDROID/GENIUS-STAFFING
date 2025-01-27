package io.cordova.myapp00d753.activity.SKF.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.module.HolidayMarkModel;
import io.cordova.myapp00d753.module.HolidayModel;
import io.cordova.myapp00d753.utility.DateCalculation;

public class HolidayViewAdapter extends RecyclerView.Adapter<HolidayViewAdapter.MyViewHolder>{
    Context context;
    ArrayList<HolidayMarkModel> holidayList;

    public HolidayViewAdapter(Context context, ArrayList<HolidayMarkModel> holidayList) {
        this.context = context;
        this.holidayList = holidayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.holiday_view_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.txtHolidayDate.setText(holidayList.get(position).getHolidayDate());
        holder.txtHoliday.setText(holidayList.get(position).getHoliday());

        if (holidayList.get(position).isBefore()){
            holder.llMainCard.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFE91E63")));
        } else {
            holder.llMainCard.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#008EFF")));
        }
    }

    @Override
    public int getItemCount() {
        return holidayList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView txtHolidayDate,txtHoliday;
        LinearLayout llMainCard;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtHolidayDate = itemView.findViewById(R.id.txtHolidayDate);
            txtHoliday = itemView.findViewById(R.id.txtHoliday);
            llMainCard = itemView.findViewById(R.id.llMainCard);
        }
    }
}
