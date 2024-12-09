package io.cordova.myapp00d753.activity.SKF.adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.activity.SKF.SKF_AttendanceRegularizationActivity;
import io.cordova.myapp00d753.module.BackLogAttendanceModel;
import io.cordova.myapp00d753.utility.TimeConversion;

public class SKF_BacklogAdapter extends RecyclerView.Adapter<SKF_BacklogAdapter.MyViewholder> {
    private static final String TAG = "SKF_BacklogAdapter";
    ArrayList<BackLogAttendanceModel> itemList;
    Context mContext;
    ArrayList<String> dayTypeArray;

    public SKF_BacklogAdapter(ArrayList<BackLogAttendanceModel> itemList, ArrayList<String> dayTypeArray, Context mContext) {
        this.itemList = itemList;
        this.mContext = mContext;
        this.dayTypeArray = dayTypeArray;
    }

    @NonNull
    @Override
    public MyViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.skf_back_attendance_row, parent, false);
        return new MyViewholder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewholder holder, @SuppressLint("RecyclerView") int position) {
        if (itemList.get(position).isSelected()){
            holder.imgLike.setVisibility(View.VISIBLE);
            ((SKF_AttendanceRegularizationActivity) mContext).updateItemStatus(position, true );
        } else {
            holder.imgLike.setVisibility(View.GONE);
            ((SKF_AttendanceRegularizationActivity) mContext).updateItemStatus(position, false);
        }

        holder.tvDate.setText(itemList.get(position).getDate());
        holder.tvInTime.setText(itemList.get(position).getInTime());
        holder.tvOutTime.setText(itemList.get(position).getOutTime());
        holder.tvDayType.setText(itemList.get(position).getDayType());

        holder.llLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(TAG, "onClick: called");
                if (itemList.get(position).isSelected()){
                    holder.imgLike.setVisibility(View.GONE);
                    itemList.get(position).setSelected(false);
                    ((SKF_AttendanceRegularizationActivity) mContext).updateItemStatus(position, false);
                } else {
                    holder.imgLike.setVisibility(View.VISIBLE);
                    itemList.get(position).setSelected(true);
                    ((SKF_AttendanceRegularizationActivity) mContext).updateItemStatus(position, true);
                }
            }
        });

        holder.imgInTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mHour = c.get(Calendar.HOUR_OF_DAY);
                int mMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(mContext,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                //txtTime.setText(hourOfDay + ":" + minute);
                                String intime = TimeConversion.convert_HH_mm_To_HH_mm_ss(hourOfDay + ":" + minute);
                                itemList.get(position).setInTime(intime);
                                holder.tvInTime.setText(itemList.get(position).getInTime());
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });

        holder.imgOutTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mHour = c.get(Calendar.HOUR_OF_DAY);
                int mMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(mContext,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                //txtTime.setText(hourOfDay + ":" + minute);
                                String outTime = TimeConversion.convert_HH_mm_To_HH_mm_ss(hourOfDay + ":" + minute);
                                itemList.get(position).setOutTime(outTime);
                                holder.tvOutTime.setText(itemList.get(position).getOutTime());


                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });

        holder.tvDayType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDayTypePopup(mContext,holder,position);
            }
        });

        if (itemList.get(position).getRemarks().equals("C")){
            holder.txtRemarks.setVisibility(View.GONE);
        } else {
            holder.txtRemarks.setVisibility(View.VISIBLE);
            if (itemList.get(position).getRemarksCode().equals("0")){
                holder.txtRemarks.setTextColor(Color.parseColor("#FF4CAF50"));
                holder.txtRemarks.setText(itemList.get(position).getRemarks());
            } else if (itemList.get(position).getRemarksCode().equals("1")){
                holder.txtRemarks.setTextColor(Color.parseColor("#FFD1BF20"));
                holder.txtRemarks.setText(itemList.get(position).getRemarks());
            } else if (itemList.get(position).getRemarksCode().equals("2")){
                holder.txtRemarks.setTextColor(Color.parseColor("#000000"));
                holder.txtRemarks.setText(itemList.get(position).getRemarks());
            } else if (itemList.get(position).getRemarksCode().equals("3")){
                holder.txtRemarks.setTextColor(Color.parseColor("#FFF44336"));
                holder.txtRemarks.setText(itemList.get(position).getRemarks());
            }
        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    class MyViewholder extends RecyclerView.ViewHolder{
        TextView tvDate, tvInTime,tvOutTime,tvDayType,txtRemarks;
        ImageView imgLike,imgInTime,imgOutTime;
        LinearLayout llLike;
        public MyViewholder(@NonNull View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvInTime = itemView.findViewById(R.id.tvInTime);
            tvOutTime = itemView.findViewById(R.id.tvOutTime);
            txtRemarks = itemView.findViewById(R.id.txtRemarks);
            imgLike = itemView.findViewById(R.id.imgLike);
            llLike = itemView.findViewById(R.id.llLike);
            tvDayType = itemView.findViewById(R.id.tvDayType);
            imgInTime = itemView.findViewById(R.id.imgInTime);
            imgOutTime = itemView.findViewById(R.id.imgOutTime);
        }
    }

    public void selectAll(){
        notifyDataSetChanged();
    }

    private void openDayTypePopup(Context context, MyViewholder holder, int position) {
        Dialog openGradPopup = new Dialog(context,R.style.CustomDialogNew2);
        openGradPopup.setContentView(R.layout.grad_selection_popup);
        openGradPopup.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        openGradPopup.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        RecyclerView rvGrad = openGradPopup.findViewById(R.id.rvGrad);
        LinearLayout lnCancel = openGradPopup.findViewById(R.id.lnCancel);
        rvGrad.setLayoutManager(new LinearLayoutManager(context));
        SKF_DayTypeAdapter skfDayTypeAdapter = new SKF_DayTypeAdapter(mContext,dayTypeArray);
        rvGrad.setAdapter(skfDayTypeAdapter);
        skfDayTypeAdapter.setSetOnGradSelect(new setOnDayTypeSelect() {
            @Override
            public void onClick(int pos, String dayType) {
                itemList.get(position).setDayType(dayType);
                holder.tvDayType.setText(dayType);
                openGradPopup.cancel();
            }
        });

        lnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGradPopup.cancel();
            }
        });


        Window window = openGradPopup.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.NO_GRAVITY;
        openGradPopup.setCancelable(false);
        openGradPopup.show();
    }

    public interface setOnDayTypeSelect{
        void onClick(int position,String dayType);
    }
}
