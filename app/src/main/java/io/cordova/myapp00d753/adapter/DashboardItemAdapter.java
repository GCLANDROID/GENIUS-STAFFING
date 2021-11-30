package io.cordova.myapp00d753.adapter;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.activity.AboutUsActivity;
import io.cordova.myapp00d753.activity.ContactUsActivity;
import io.cordova.myapp00d753.activity.ServicesActivity;
import io.cordova.myapp00d753.module.AttendanceModule;
import io.cordova.myapp00d753.module.DashboardItemModel;

public class DashboardItemAdapter extends RecyclerView.Adapter<DashboardItemAdapter.MyViewHolder> {
    ArrayList<DashboardItemModel>itemList=new ArrayList<>();
    Context context;
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.dashboard_item_raw,viewGroup,false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int i) {
        myViewHolder.tvTitle.setText(itemList.get(i).getItemName());
        myViewHolder.imgItemPic.setImageResource(itemList.get(i).getItempic());
        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (i==0){
                    Intent intent=new Intent(context, ServicesActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }else if (i==1){
                    Intent intent=new Intent(context, AboutUsActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }else if (i==2){
                    Intent intent=new Intent(context, ContactUsActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }else if (i==3){
                    Uri uri = Uri.parse("https://www.geniusconsultant.com/PDF-doc/GCL_BROCHURE.pdf"); // missing 'http://' will cause crashed
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    context.startActivity(intent);
                }
            }
        });



    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        ImageView imgItemPic;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle=(TextView)itemView.findViewById(R.id.tvTitle);
            imgItemPic=(ImageView) itemView.findViewById(R.id.imgItemPic);

        }
    }

    public DashboardItemAdapter(ArrayList<DashboardItemModel> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
    }
}
