package io.cordova.myapp00d753.adapter;

import android.content.Context;
import android.graphics.Color;

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
import io.cordova.myapp00d753.activity.AboutUsActivity;
import io.cordova.myapp00d753.activity.AttenApprovalActivity;
import io.cordova.myapp00d753.activity.PFActivity;
import io.cordova.myapp00d753.module.PfModule;

public class PfAdapter extends RecyclerView.Adapter<PfAdapter.MyViewHolder> {
    ArrayList<PfModule>pfList=new ArrayList<>();
    Context context;
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.pf_raw,viewGroup,false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {
        if (pfList.get(i).getPfYear().equals("-- All --")){
            myViewHolder.itemView.setVisibility(View.GONE);
            myViewHolder.tvPfYear.setVisibility(View.GONE);

        }else {
            myViewHolder.itemView.setVisibility(View.VISIBLE);
            myViewHolder.tvPfYear.setVisibility(View.VISIBLE);

        }
        myViewHolder.tvPfYear.setText(pfList.get(i).getPfYear());
        myViewHolder.llDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((PFActivity) context).operBrowser(pfList.get(i).getPfUrl() );


            }
        });




    }

    @Override
    public int getItemCount() {
        return pfList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvPfYear;
        ImageView imgDownload;
        LinearLayout llDownload,llDownloadD1,llDownloadD;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPfYear=(TextView)itemView.findViewById(R.id.tvPfYear);
            imgDownload=(ImageView)itemView.findViewById(R.id.imgDownload);
            llDownload=(LinearLayout)itemView.findViewById(R.id.llDownload);
            llDownloadD=(LinearLayout)itemView.findViewById(R.id.llDownloadD);
            llDownloadD1=(LinearLayout)itemView.findViewById(R.id.llDownloadD1);

        }
    }

    public PfAdapter(ArrayList<PfModule> pfList, Context context) {
        this.pfList = pfList;
        this.context = context;
    }
}
