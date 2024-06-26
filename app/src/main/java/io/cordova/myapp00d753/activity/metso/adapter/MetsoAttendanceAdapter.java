package io.cordova.myapp00d753.activity.metso.adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;

import io.cordova.myapp00d753.R;

import io.cordova.myapp00d753.activity.metso.MetsoAttendanceRegularizationActivity;
import io.cordova.myapp00d753.activity.metso.model.MetsoShiftModel;
import io.cordova.myapp00d753.module.BackLogAttendanceModel;
import io.cordova.myapp00d753.utility.TimeConversion;
import io.cordova.myapp00d753.utility.TimeDateString;

public class MetsoAttendanceAdapter extends RecyclerView.Adapter<MetsoAttendanceAdapter.MyViewHolder>{
    private static final String TAG = "MetsoAttendanceAdapter";
    Context context;
    ArrayList<MetsoShiftModel> metsoShiftList;
    ArrayList<BackLogAttendanceModel> blockLogList;
    Dialog dialogShiftPopUp;

    public MetsoAttendanceAdapter(Context context, ArrayList<MetsoShiftModel> metsoShiftList, ArrayList<BackLogAttendanceModel> blockLogList) {
        this.context = context;
        this.metsoShiftList = metsoShiftList;
        this.blockLogList = blockLogList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.metso_attendance_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.tvDate.setText(TimeDateString.changeDateStringToIndianDateFormat(blockLogList.get(position).getDate()));

        holder.tvShift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openShiftPopUp(holder,position);
            }
        });

        holder.tvInTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                int mHour = c.get(Calendar.HOUR_OF_DAY);
                int mMinute = c.get(Calendar.MINUTE);
                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(context,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                //txtTime.setText(hourOfDay + ":" + minute);
                                String intime = hourOfDay + ":" + minute;
                                //itemList.get(i).setInTime(intime);
                                //myViewHolder.tvInTime.setText(itemList.get(i).getInTime());
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });

        holder.tvOutTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                int mHour = c.get(Calendar.HOUR_OF_DAY);
                int mMinute = c.get(Calendar.MINUTE);
                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(context,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                //txtTime.setText(hourOfDay + ":" + minute);
                                String intime = hourOfDay + ":" + minute;
                                //itemList.get(i).setOutTime(intime);
                                //myViewHolder.tvOutTime.setText(itemList.get(i).getOutTime());
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });

        holder.llLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.shift.isEmpty()){
                    Toast.makeText(context, "Please select shift", Toast.LENGTH_SHORT).show();
                } else {
                    if (blockLogList.get(position).isSelected()){
                        blockLogList.get(position).setSelected(false);
                        holder.imgLike.setVisibility(View.GONE);
                        ((MetsoAttendanceRegularizationActivity) context).updateItemStatus(position, false);
                    } else {
                        blockLogList.get(position).setSelected(true);
                        Log.e(TAG, "onClick: REMARKS: "+holder.edtRemarks.getText().toString());
                        ((MetsoAttendanceRegularizationActivity) context).updateItemStatus(position, true);
                        holder.imgLike.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        holder.edtRemarks.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                blockLogList.get(position).setRemarks2(editable.toString().trim());
            }
        });
    }

    private void openShiftPopUp(MyViewHolder holder,int pos) {
        dialogShiftPopUp = new Dialog(context,R.style.CustomDialogNew2);
        dialogShiftPopUp.setContentView(R.layout.shift_choose_dialog_layout);
        dialogShiftPopUp.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogShiftPopUp.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        RecyclerView rvSelectShift = dialogShiftPopUp.findViewById(R.id.rvSelectShift);
        rvSelectShift.setLayoutManager(new LinearLayoutManager(context));
        ImageView imgCancel = dialogShiftPopUp.findViewById(R.id.imgCancel);

        ShiftSelectionAdapter shiftSelectionAdapter;
        if (metsoShiftList != null){
            Log.e(TAG, "onResponse: SHIFT_LIST_SIZE: "+metsoShiftList.size());
            shiftSelectionAdapter = new ShiftSelectionAdapter(context,metsoShiftList);
            rvSelectShift.setAdapter(shiftSelectionAdapter);
            final String[] inTime1 = {""};
            String outTime="";
            shiftSelectionAdapter.setShiftSelectionInterface(new ShiftSelectionInterface() {
                @Override
                public void onSelect(long shiftID, String selectedShift) {
                    TimeDateString.getTimeFromString(selectedShift);
                    String conInTime = TimeConversion.convertAmPmTimeTo24HourFormat(TimeDateString.inTime);
                    String conOutTime = TimeConversion.convertAmPmTimeTo24HourFormat(TimeDateString.outTime);
                    holder.tvInTime.setText(conInTime);
                    holder.tvOutTime.setText(conOutTime);
                    blockLogList.get(pos).setInTime(conInTime);
                    blockLogList.get(pos).setOutTime(conOutTime);
                    holder.shift = selectedShift;
                    holder.tvShift.setText(selectedShift);
                    blockLogList.get(pos).setShiftID(shiftID);
                    dialogShiftPopUp.cancel();
                }
            });
        }

        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogShiftPopUp.cancel();
            }
        });
        dialogShiftPopUp.show();
    }

    @Override
    public int getItemCount() {
        return blockLogList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tvDate,tvInTime,tvOutTime,tvShift;
        LinearLayout llLike;
        ImageView imgLike;
        String shift="";
        EditText edtRemarks;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvInTime = itemView.findViewById(R.id.tvInTime);
            tvOutTime = itemView.findViewById(R.id.tvOutTime);
            tvShift = itemView.findViewById(R.id.tvShift);
            llLike = itemView.findViewById(R.id.llLike);
            imgLike = itemView.findViewById(R.id.imgLike);
            edtRemarks = itemView.findViewById(R.id.edtRemarks);
        }
    }

    public interface ShiftSelectionInterface{
        void onSelect(long shiftID,String selectedShift);
    }
}
