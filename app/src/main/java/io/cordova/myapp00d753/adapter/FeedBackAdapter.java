package io.cordova.myapp00d753.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;



import java.util.ArrayList;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.module.FeedBackModule;

public class FeedBackAdapter extends RecyclerView.Adapter<FeedBackAdapter.MyViewHolder> {
    ArrayList<FeedBackModule>feedbackList=new ArrayList<>();
    @NonNull
    @Override
    public FeedBackAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.feedback_raw,viewGroup,false);

        return new FeedBackAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedBackAdapter.MyViewHolder myViewHolder, int i) {
        myViewHolder.tvDate.setText(feedbackList.get(i).getFeedbackDate());
        myViewHolder.tvRemark.setText(feedbackList.get(i).getFeedbackRemark());
        myViewHolder.tvReply.setText(feedbackList.get(i).getFeedbackReply());

    }

    @Override
    public int getItemCount() {
        return feedbackList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate,tvReply,tvIssue,tvRemark;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate=(TextView)itemView.findViewById(R.id.tvDate);
            tvReply=(TextView)itemView.findViewById(R.id.tvReply);
            tvRemark=(TextView)itemView.findViewById(R.id.tvRemark);

        }
    }

    public FeedBackAdapter(ArrayList<FeedBackModule> feedbackList) {
        this.feedbackList = feedbackList;
    }
}
