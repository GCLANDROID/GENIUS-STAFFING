package io.cordova.myapp00d753.adapter;


import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;



import java.util.ArrayList;
import java.util.Calendar;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.activity.AttenApprovalActivity;
import io.cordova.myapp00d753.activity.BacklogAttendanceActivity;
import io.cordova.myapp00d753.module.AttendanceApprovalModule;
import io.cordova.myapp00d753.module.BackLogAttendanceModel;
import io.cordova.myapp00d753.utility.Util;


public class BackLogAdapter extends RecyclerView.Adapter<BackLogAdapter.MyViewHolder> {
    ArrayList<BackLogAttendanceModel> itemList = new ArrayList<>();
    Context mContex;
    ArrayList<String> item = new ArrayList<>();
    boolean isSelectedAll,isAll;
    public BackLogAdapter(ArrayList<BackLogAttendanceModel> blockLogList, BacklogAttendanceActivity backlogActivity) {
        this.itemList = blockLogList;
        this.mContex = backlogActivity;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.back_attendance_row, viewGroup, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, @SuppressLint("RecyclerView") final int i) {
        final BackLogAttendanceModel attandanceModel = itemList.get(i);
       if (itemList.get(i).isSelected()){
           myViewHolder.imgLike.setVisibility(View.VISIBLE);
       }else {
           myViewHolder.imgLike.setVisibility(View.GONE);
       }


        if (isAll) {
            if (isSelectedAll) {
                myViewHolder.imgLike.setVisibility(View.VISIBLE);


            } else {
                myViewHolder.imgLike.setVisibility(View.GONE);

            }
        }else {
            if (itemList.get(i).isSelected()) {
                myViewHolder.imgLike.setVisibility(View.VISIBLE);
            } else {
                myViewHolder.imgLike.setVisibility(View.GONE);
            }
        }

        myViewHolder.tvDate.setText(Util.changeAnyDateFormat(itemList.get(i).getDate(),"MM/dd/yyyy","dd MMM yyyy"));
        myViewHolder.tvInTime.setText(itemList.get(i).getInTime());
        myViewHolder.tvOutTime.setText(itemList.get(i).getOutTime());



        myViewHolder.imgInTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mHour = c.get(Calendar.HOUR_OF_DAY);
                int mMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(mContex,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                //txtTime.setText(hourOfDay + ":" + minute);
                                String intime = hourOfDay + ":" + minute;
                                itemList.get(i).setInTime(intime);
                                myViewHolder.tvInTime.setText(itemList.get(i).getInTime());


                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });

        myViewHolder.imgOutTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mHour = c.get(Calendar.HOUR_OF_DAY);
                int mMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(mContex,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                //txtTime.setText(hourOfDay + ":" + minute);
                                String intime = hourOfDay + ":" + minute;
                                itemList.get(i).setOutTime(intime);
                                myViewHolder.tvOutTime.setText(itemList.get(i).getOutTime());


                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });


        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attandanceModel.setSelected(!attandanceModel.isSelected());
                // holder.view.setBackgroundColor(attandanceModel.isSelected() ? Color.CYAN : Color.WHITE);

                if (attandanceModel.isSelected()) {

                    myViewHolder.imgLike.setVisibility(View.VISIBLE);
                    itemList.get(i).setSelected(true);
                    notifyDataSetChanged();

                    ((BacklogAttendanceActivity) mContex).updateItemStatus(i, true );



                } else {
                    myViewHolder.imgLike.setVisibility(View.GONE);
                    ((AttenApprovalActivity) mContex).updateAttendanceStatus(i, false);
                    itemList.get(i).setSelected(false);
                    notifyDataSetChanged();
                }

            }
        });


    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate, tvInTime, tvOutTime;
        ImageView imgInTime, imgOutTime, imgLike;
        LinearLayout llLike;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate = (TextView) itemView.findViewById(R.id.tvDate);
            tvInTime = (TextView) itemView.findViewById(R.id.tvInTime);
            tvOutTime = (TextView) itemView.findViewById(R.id.tvOutTime);
            imgInTime = (ImageView) itemView.findViewById(R.id.imgInTime);
            imgOutTime = (ImageView) itemView.findViewById(R.id.imgOutTime);
            imgLike = (ImageView) itemView.findViewById(R.id.imgLike);
            llLike = (LinearLayout) itemView.findViewById(R.id.llLike);


        }
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
