package io.cordova.myapp00d753.adapter;

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

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.activity.EmpMappingActivity;
import io.cordova.myapp00d753.module.EmployeeMapiingModule;


public class EmployeeMappinglAdapter extends RecyclerView.Adapter<EmployeeMappinglAdapter.MyViewHolder> {
    ArrayList<EmployeeMapiingModule>attendanceInfoList=new ArrayList<>();
    Context context;
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.employeelistraw,viewGroup,false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {
        final EmployeeMapiingModule attandanceModel = attendanceInfoList.get(i);



        myViewHolder.tvEmpId.setText(attendanceInfoList.get(i).getEmpId());
        myViewHolder.tvEmpName.setText(attendanceInfoList.get(i).getEmpName());
        if (attendanceInfoList.get(i).getLocation().equals("")) {
            myViewHolder.tvMap.setText("Not map with any location");
        }else {
            myViewHolder.tvMap.setText("Map with "+attendanceInfoList.get(i).getLocation()+" Location");
        }


        if (attendanceInfoList.get(i).isSelected()){
            myViewHolder.imgLike.setVisibility(View.VISIBLE);
        }else {
            myViewHolder.imgLike.setVisibility(View.GONE);
        }
        myViewHolder.llMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attandanceModel.setSelected(!attandanceModel.isSelected());
                // holder.view.setBackgroundColor(attandanceModel.isSelected() ? Color.CYAN : Color.WHITE);

                if (attandanceModel.isSelected()) {

                    myViewHolder.imgLike.setVisibility(View.VISIBLE);
                    attendanceInfoList.get(i).setSelected(true);
                    notifyDataSetChanged();

                    ((EmpMappingActivity) context).updateAttendanceStatus(i, true );



                } else {
                    myViewHolder.imgLike.setVisibility(View.GONE);
                    ((EmpMappingActivity) context).updateAttendanceStatus(i, false);
                    attendanceInfoList.get(i).setSelected(false);
                    notifyDataSetChanged();
                }

            }
        });




    }

    @Override
    public int getItemCount() {
        return attendanceInfoList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvEmpId,tvEmpName,tvMap;
        ImageView imgLike;
        LinearLayout llMain;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvEmpId=(TextView)itemView.findViewById(R.id.tvEmpId);
            tvEmpName=(TextView)itemView.findViewById(R.id.tvEmpName);
            tvMap=(TextView)itemView.findViewById(R.id.tvMap);

            llMain=(LinearLayout) itemView.findViewById(R.id.rlMain);
            imgLike=(ImageView) itemView.findViewById(R.id.imgLike);
        }
    }

    public EmployeeMappinglAdapter(ArrayList<EmployeeMapiingModule> attendanceInfoList, Context context) {
        this.attendanceInfoList = attendanceInfoList;
        this.context = context;
    }
}
