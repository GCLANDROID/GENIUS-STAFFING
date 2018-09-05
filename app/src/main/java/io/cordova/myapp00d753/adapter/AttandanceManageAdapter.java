package io.cordova.myapp00d753.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;



import java.util.ArrayList;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.activity.SupAttenManageActivity;
import io.cordova.myapp00d753.module.SupAttendenceManageModule;
import io.cordova.myapp00d753.utility.Pref;


/**
 * Created by LENOVO on 12/8/2017.
 */

public class AttandanceManageAdapter extends RecyclerView.Adapter<AttandanceManageAdapter.MyViewHolder> {
    private ArrayList<SupAttendenceManageModule> mAttandanceModelList=new ArrayList<>();
    private Context context;
    Pref pref;




    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.attendencemanage_raw, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        pref=new Pref(context);
        final SupAttendenceManageModule attandanceModel = mAttandanceModelList.get(position);
        holder.tvEmpId.setText(mAttandanceModelList.get(position).getEmoId());
        holder.tvEmpName.setText(mAttandanceModelList.get(position).getEmpName());
        holder.tvEmpLocation.setText(mAttandanceModelList.get(position).getEmpLocation());
        holder.tvdate.setText(mAttandanceModelList.get(position).getDate());
        if (mAttandanceModelList.get(position).isSelected()){
            holder.imgLike.setVisibility(View.VISIBLE);
        }else {
            holder.imgLike.setVisibility(View.GONE);
        }



        // holder.view.setBackgroundColor(attandanceModel.isSelected() ? Color.CYAN : Color.WHITE);
        holder.llMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attandanceModel.setSelected(!attandanceModel.isSelected());
                // holder.view.setBackgroundColor(attandanceModel.isSelected() ? Color.CYAN : Color.WHITE);

                if (attandanceModel.isSelected()) {

                        holder.imgLike.setVisibility(View.VISIBLE);
                        pref.saveManageId(mAttandanceModelList.get(position).getEmoId());
                        mAttandanceModelList.get(position).setSelected(true);
                        notifyDataSetChanged();

                        ((SupAttenManageActivity) context).updateAttendanceStatus(position, true );



                } else {
                    holder.imgLike.setVisibility(View.GONE);
                    ((SupAttenManageActivity) context).updateAttendanceStatus(position, false);
                    mAttandanceModelList.get(position).setSelected(false);
                    notifyDataSetChanged();
                }

            }
        });

    }


    @Override
    public int getItemCount() {
        return mAttandanceModelList == null ? 0 : mAttandanceModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private View view;
      TextView tvEmpName,tvEmpId,tvEmpLocation,tvdate;
      ImageView imgLike;
      LinearLayout llMain;

        private MyViewHolder(View itemView) {
            super(itemView);
            llMain=(LinearLayout)itemView.findViewById(R.id.llMain);

            imgLike=(ImageView)itemView.findViewById(R.id.imgLike);

            tvEmpId=(TextView)itemView.findViewById(R.id.tvEmpId);
            tvEmpName=(TextView)itemView.findViewById(R.id.tvEmpName);
            tvEmpLocation=(TextView)itemView.findViewById(R.id.tvEmpLocation);
            tvdate=(TextView)itemView.findViewById(R.id.tvTime);



        }
    }

    public AttandanceManageAdapter(ArrayList<SupAttendenceManageModule> mAttandanceModelList, Context context) {
        this.mAttandanceModelList = mAttandanceModelList;
        this.context = context;
    }
}
