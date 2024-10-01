package io.cordova.myapp00d753.activity.SKF.adapter;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.activity.BacklogAttendanceActivity;
import io.cordova.myapp00d753.activity.SKF.SKF_AttendanceRegularizationActivity;
import io.cordova.myapp00d753.adapter.BackLogAdapter;
import io.cordova.myapp00d753.module.BackLogAttendanceModel;
import io.cordova.myapp00d753.utility.TimeConversion;

public class SKF_BacklogAdapter extends RecyclerView.Adapter<SKF_BacklogAdapter.MyViewholder> {
    private static final String TAG = "SKF_BacklogAdapter";
    ArrayList<BackLogAttendanceModel> itemList;
    Context mContext;

    public SKF_BacklogAdapter(ArrayList<BackLogAttendanceModel> itemList, Context mContext) {
        this.itemList = itemList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public MyViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.back_attendance_row, parent, false);
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

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    class MyViewholder extends RecyclerView.ViewHolder{
        TextView tvDate, tvInTime,tvOutTime,tvDayType;
        ImageView imgLike,imgInTime,imgOutTime;
        LinearLayout llLike;
        public MyViewholder(@NonNull View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvInTime = itemView.findViewById(R.id.tvInTime);
            tvOutTime = itemView.findViewById(R.id.tvOutTime);
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
}
