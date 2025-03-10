package io.cordova.myapp00d753.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import io.cordova.myapp00d753.R;

public class NotiAdapter extends RecyclerView.Adapter<NotiAdapter.MyViewModel>{
    Context context;
    ArrayList<String> contentList;

    public NotiAdapter(Context context, ArrayList<String> contentList) {
        this.context = context;
        this.contentList = contentList;
    }

    @NonNull
    @Override
    public MyViewModel onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.noti_item,parent,false);
        return new MyViewModel(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewModel holder, int position) {
        holder.tvNotifcation.setText("* "+contentList.get(position));
    }

    @Override
    public int getItemCount() {
        return contentList.size();
    }

    class MyViewModel extends RecyclerView.ViewHolder{
        TextView tvNotifcation;
        public MyViewModel(@NonNull View itemView) {
            super(itemView);
            tvNotifcation = itemView.findViewById(R.id.tvNotifcation);
        }
    }
}
