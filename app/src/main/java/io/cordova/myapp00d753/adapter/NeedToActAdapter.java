package io.cordova.myapp00d753.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import io.cordova.myapp00d753.module.NeedToActModel;

public class NeedToActAdapter extends RecyclerView.Adapter<NeedToActAdapter.MyViewHolder>{
    Context context;
    ArrayList<NeedToActModel> needToActModelList;

    public NeedToActAdapter(Context context, ArrayList<NeedToActModel> needToActModelList) {
        this.context = context;
        this.needToActModelList = needToActModelList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.need_to_act_item,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.tvDocInfo.setText(needToActModelList.get(position).docName);
        if (needToActModelList.get(position).acceptanceType == 0){
            holder.txtButton.setText("View Document");
            holder.actionImage.setImageResource(R.drawable.check_mark);
            holder.llMain.setBackgroundResource(R.drawable.design_green_outline);
        } else {
            holder.txtButton.setText("Accept");
            holder.actionImage.setImageResource(R.drawable.double_tap);
            holder.llMain.setBackgroundResource(R.drawable.lldesign_error_2);
        }
        holder.txtButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(needToActModelList.get(position).actUrl));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return needToActModelList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tvDocInfo,txtButton;
        ImageView actionImage;
        LinearLayout llMain;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDocInfo = itemView.findViewById(R.id.tvDocInfo);
            txtButton = itemView.findViewById(R.id.txtButton);
            actionImage = itemView.findViewById(R.id.actionImage);
            llMain = itemView.findViewById(R.id.llMain);
        }
    }
}
