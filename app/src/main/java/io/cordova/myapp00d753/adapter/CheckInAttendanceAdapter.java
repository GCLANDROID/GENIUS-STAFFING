package io.cordova.myapp00d753.adapter;


import android.app.TimePickerDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.module.AttendancereportModule;
import io.cordova.myapp00d753.module.CheckInOutAttendanceModel;

public class CheckInAttendanceAdapter extends RecyclerView.Adapter<CheckInAttendanceAdapter.MyViewHolder> {
    ArrayList<CheckInOutAttendanceModel>itemList=new ArrayList<>();
    Context context;
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.check_in_attendance_row,viewGroup,false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        CheckInOutAttendanceModel model = itemList.get(i);
        myViewHolder.tvEmpName.setText(model.getEmpName());
        myViewHolder.tvEmpID.setText(model.getAEMEmployeeID());
        myViewHolder.tvTime.setText(model.getInTime());

        if (model.isSelected()) {
            myViewHolder.imgTick.setVisibility(View.VISIBLE);
        } else {
            myViewHolder.imgTick.setVisibility(View.GONE);
        }

        myViewHolder.llTick.setOnClickListener(v -> {
            model.setSelected(!model.isSelected());
            notifyItemChanged(i);
        });


        if (i % 2 == 0) {
            myViewHolder.itemView.setBackgroundColor(
                    context.getResources().getColor(R.color.nessecarycolor)); // yellow
        } else {
            myViewHolder.itemView.setBackgroundColor(
                    context.getResources().getColor(R.color.democolor)); // sky blue
        }

        myViewHolder.llTime.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(
                    context,
                    (view, selectedHour, selectedMinute) -> {
                        // 24-hour format: 6:30 PM becomes 18:30
                        String selectedTime = String.format("%02d:%02d", selectedHour, selectedMinute);

                        myViewHolder.tvTime.setText(selectedTime);
                        itemList.get(i).setInTime(selectedTime);
                    },
                    hour,
                    minute,
                    false   // false = user sees AM/PM picker
            );

            timePickerDialog.show();
        });



    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvEmpName,tvEmpID,tvTime;
        LinearLayout llTime,llTick;
        ImageView imgTick;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvEmpName=(TextView)itemView.findViewById(R.id.tvEmpName);
            tvEmpID=(TextView)itemView.findViewById(R.id.tvEmpID);
            tvTime=(TextView)itemView.findViewById(R.id.tvTime);
            llTime=itemView.findViewById(R.id.llTime);
            llTick=itemView.findViewById(R.id.llTick);

            imgTick=itemView.findViewById(R.id.imgTick);

        }
    }

    public CheckInAttendanceAdapter(ArrayList<CheckInOutAttendanceModel> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
    }
}
