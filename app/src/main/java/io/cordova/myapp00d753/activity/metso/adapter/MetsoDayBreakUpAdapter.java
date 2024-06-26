package io.cordova.myapp00d753.activity.metso.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.activity.metso.fragment.MetsoLeaveApplicationFragment;
import io.cordova.myapp00d753.fragment.ApplicationFragment;
import io.cordova.myapp00d753.module.DayBreakUpModel;


public class MetsoDayBreakUpAdapter extends RecyclerView.Adapter<MetsoDayBreakUpAdapter.MyViewHolder> {
    ArrayList<DayBreakUpModel> itemList = new ArrayList<>();
    Fragment context;
    Context context1;

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.day_breakuo_raw, viewGroup, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {
        final DayBreakUpModel dayModel = itemList.get(i);


            myViewHolder.tvDateName.setText(itemList.get(i).getDateName());
            myViewHolder.tvFullDay.setText("Full day");
            myViewHolder.tvFirstHalf.setText("First half");
            myViewHolder.tvScndHalf.setText("Second half");


        myViewHolder.tvLeaveDate.setText(itemList.get(i).getBrkupDate());
        if (itemList.get(i).getDayAccess().equals("-1")) {
            myViewHolder.llDayBreakUp.setVisibility(View.GONE);
            myViewHolder.tvStatus.setVisibility(View.VISIBLE);
            myViewHolder.tvStatus.setText(itemList.get(i).getDayAccessDesc());

        } else {
            myViewHolder.llDayBreakUp.setVisibility(View.VISIBLE);
            myViewHolder.tvStatus.setVisibility(View.GONE);
        }

        myViewHolder.llFirstHalf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dayModel.setSelected(!dayModel.isSelected());


                // holder.view.setBackgroundColor(attandanceModel.isSelected() ? Color.CYAN : Color.WHITE);

                if (dayModel.isSelected()) {

                    myViewHolder.imgFrstHalf.setVisibility(View.VISIBLE);
                    myViewHolder.imgScndHalf.setVisibility(View.GONE);
                    myViewHolder.imgFull.setVisibility(View.GONE);
                    itemList.get(i).setDayModeValue("1");
                    itemList.get(i).setBalance("0.5");
                    itemList.get(i).setSelected(true);
                    notifyDataSetChanged();

                    ((MetsoLeaveApplicationFragment) context).updateStatus(i, true );



                } else {
                    /*myViewHolder.imgFrstHalf.setVisibility(View.GONE);
                    myViewHolder.imgScndHalf.setVisibility(View.GONE);
                    myViewHolder.imgFull.setVisibility(View.GONE);*/
                    ((MetsoLeaveApplicationFragment) context).updateStatus(i, false);
                    itemList.get(i).setSelected(false);
                    notifyDataSetChanged();
                }

            }
        });


        myViewHolder.llScndHalf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dayModel.setSelected(!dayModel.isSelected());
                // holder.view.setBackgroundColor(attandanceModel.isSelected() ? Color.CYAN : Color.WHITE);

                if (dayModel.isSelected()) {

                    myViewHolder.imgFrstHalf.setVisibility(View.GONE);
                    myViewHolder.imgScndHalf.setVisibility(View.VISIBLE);
                    myViewHolder.imgFull.setVisibility(View.GONE);
                    itemList.get(i).setDayModeValue("2");
                    itemList.get(i).setBalance("0.5");
                    itemList.get(i).setSelected(true);
                    notifyDataSetChanged();

                    ((MetsoLeaveApplicationFragment) context).updateStatus(i, true );



                } else {
                    /*myViewHolder.imgFrstHalf.setVisibility(View.GONE);
                    myViewHolder.imgScndHalf.setVisibility(View.GONE);
                    myViewHolder.imgFull.setVisibility(View.GONE);*/
                    ((MetsoLeaveApplicationFragment) context).updateStatus(i, false);
                    itemList.get(i).setSelected(false);
                    notifyDataSetChanged();
                }

            }
        });

        myViewHolder.llFullDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dayModel.setSelected(!dayModel.isSelected());
                // holder.view.setBackgroundColor(attandanceModel.isSelected() ? Color.CYAN : Color.WHITE);

                if (dayModel.isSelected()) {

                    myViewHolder.imgFrstHalf.setVisibility(View.GONE);
                    myViewHolder.imgScndHalf.setVisibility(View.GONE);
                    myViewHolder.imgFull.setVisibility(View.VISIBLE);
                    itemList.get(i).setDayModeValue("0");
                    itemList.get(i).setBalance("1");
                    itemList.get(i).setSelected(true);
                    notifyDataSetChanged();

                    ((MetsoLeaveApplicationFragment) context).updateStatus(i, true );



                } else {
                    /*myViewHolder.imgFrstHalf.setVisibility(View.GONE);
                    myViewHolder.imgScndHalf.setVisibility(View.GONE);
                    myViewHolder.imgFull.setVisibility(View.GONE);*/
                    ((MetsoLeaveApplicationFragment) context).updateStatus(i, false);
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
        TextView tvLeaveDate, tvDateName, tvStatus;
        LinearLayout llDayBreakUp, llFirstHalf, llScndHalf, llFullDay;
        ImageView imgFrstHalf,imgScndHalf,imgFull;
        TextView tvFullDay,tvFirstHalf,tvScndHalf;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvLeaveDate = (TextView) itemView.findViewById(R.id.tvLeaveDate);
            tvDateName = (TextView) itemView.findViewById(R.id.tvDateName);
            tvStatus = (TextView) itemView.findViewById(R.id.tvStatus);

            tvFullDay = (TextView) itemView.findViewById(R.id.tvFullDay);
            tvFirstHalf = (TextView) itemView.findViewById(R.id.tvFirtsHalf);
            tvScndHalf = (TextView) itemView.findViewById(R.id.tvScndHalf);

            llDayBreakUp = (LinearLayout) itemView.findViewById(R.id.llDayBreakUp);
            llFullDay = (LinearLayout) itemView.findViewById(R.id.llFullDay);
            llScndHalf = (LinearLayout) itemView.findViewById(R.id.llScndHalf);
            llFirstHalf = (LinearLayout) itemView.findViewById(R.id.llFirstHalf);

            imgFrstHalf=(ImageView)itemView.findViewById(R.id.imgFrstHalf);
            imgScndHalf=(ImageView)itemView.findViewById(R.id.imgScndHalf);
            imgFull=(ImageView)itemView.findViewById(R.id.imgFull);
        }
    }

    public MetsoDayBreakUpAdapter(ArrayList<DayBreakUpModel> itemList, Fragment context, Context context1) {
        this.itemList = itemList;
        this.context = context;
        this.context1=context1;
    }
}
