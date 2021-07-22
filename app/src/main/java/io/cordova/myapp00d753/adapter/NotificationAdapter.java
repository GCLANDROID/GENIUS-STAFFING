package io.cordova.myapp00d753.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.module.NotificationModule;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {
    ArrayList<NotificationModule>notificationList=new ArrayList<>();
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.notification_raw,viewGroup,false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
       myViewHolder.tvTitle.setText(notificationList.get(i).getTvTitle());
       myViewHolder.tvBody.setText(notificationList.get(i).getTvBody());
       myViewHolder.tvDate.setText(notificationList.get(i).getTvDate());

    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate,tvTitle,tvBody;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate=(TextView)itemView.findViewById(R.id.tvDate);
            tvTitle=(TextView)itemView.findViewById(R.id.tvNotificationTitle);
            tvBody=(TextView)itemView.findViewById(R.id.tvNotificationBody);



        }
    }

    public NotificationAdapter(ArrayList<NotificationModule> notificationList) {
        this.notificationList = notificationList;
    }
}
