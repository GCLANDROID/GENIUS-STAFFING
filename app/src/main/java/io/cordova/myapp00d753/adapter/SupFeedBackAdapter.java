package io.cordova.myapp00d753.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;



import java.util.ArrayList;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.activity.AttenApprovalActivity;
import io.cordova.myapp00d753.activity.SupFeedBackActivity;
import io.cordova.myapp00d753.module.AttendanceApprovalModule;
import io.cordova.myapp00d753.module.SupFeedBackModule;

public class SupFeedBackAdapter extends RecyclerView.Adapter<SupFeedBackAdapter.MyViewHolder> {
    ArrayList<SupFeedBackModule>feedbackList=new ArrayList<>();
    Context context;

    @NonNull
    @Override
    public SupFeedBackAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.supfeedback_raw,viewGroup,false);

        return new SupFeedBackAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final SupFeedBackAdapter.MyViewHolder myViewHolder, final int i) {
        final SupFeedBackModule supModel = feedbackList.get(i);
        myViewHolder.tvEmpId.setText(feedbackList.get(i).getEmpId());
        myViewHolder.tvEmpName.setText(feedbackList.get(i).getEmpName());
        myViewHolder.tvDate.setText(feedbackList.get(i).getDate());
        myViewHolder.tvIssueName.setText(feedbackList.get(i).getIssueName());
        myViewHolder.tvQuery.setText(feedbackList.get(i).getQuey());
        if (feedbackList.get(i).isSelected()){
            myViewHolder.imgLike.setVisibility(View.VISIBLE);
        }else {
            myViewHolder.imgLike.setVisibility(View.GONE);
        }
        myViewHolder.llMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                supModel.setSelected(!supModel.isSelected());
                // holder.view.setBackgroundColor(attandanceModel.isSelected() ? Color.CYAN : Color.WHITE);

                if (supModel.isSelected()) {

                    myViewHolder.imgLike.setVisibility(View.VISIBLE);
                    feedbackList.get(i).setSelected(true);
                    notifyDataSetChanged();

                    ((SupFeedBackActivity) context).updateAttendanceStatus(i, true );



                } else {
                    myViewHolder.imgLike.setVisibility(View.GONE);
                    ((SupFeedBackActivity) context).updateAttendanceStatus(i, false);
                    feedbackList.get(i).setSelected(false);
                    notifyDataSetChanged();
                }

            }
        });






    }

    @Override
    public int getItemCount() {
        return feedbackList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvEmpId,tvEmpName,tvDate,tvIssueName,tvQuery;
        LinearLayout llMain;
        ImageView imgLike;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvEmpId=(TextView)itemView.findViewById(R.id.tvEmpId);
            tvEmpName=(TextView)itemView.findViewById(R.id.tvEmpName);
            tvDate=(TextView)itemView.findViewById(R.id.tvDate);
            tvIssueName=(TextView)itemView.findViewById(R.id.tvIssueName);
            tvQuery=(TextView)itemView.findViewById(R.id.tvQuery);
            llMain=(LinearLayout)itemView.findViewById(R.id.llMain);
            imgLike=(ImageView)itemView.findViewById(R.id.imgLike);


        }
    }

    public SupFeedBackAdapter(ArrayList<SupFeedBackModule> feedbackList, Context context) {
        this.feedbackList = feedbackList;
        this.context = context;
    }
}
