package io.cordova.myapp00d753.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;



import java.util.ArrayList;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.module.PfModule;

public class PfAdapter extends RecyclerView.Adapter<PfAdapter.MyViewHolder> {
    ArrayList<PfModule>pfList=new ArrayList<>();
    @NonNull
    @Override
    public PfAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.pf_raw,viewGroup,false);

        return new PfAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PfAdapter.MyViewHolder myViewHolder, int i) {
        if (pfList.get(i).getPfYear().equals("-- All --")){
            myViewHolder.itemView.setVisibility(View.GONE);
            myViewHolder.tvPfYear.setVisibility(View.GONE);

        }else {
            myViewHolder.itemView.setVisibility(View.VISIBLE);
            myViewHolder.tvPfYear.setVisibility(View.VISIBLE);

        }
        myViewHolder.tvPfYear.setText(pfList.get(i).getPfYear());


    }

    @Override
    public int getItemCount() {
        return pfList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvPfYear;
        ImageView imgDownload;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPfYear=(TextView)itemView.findViewById(R.id.tvPfYear);
            imgDownload=(ImageView)itemView.findViewById(R.id.imgDownload);

        }
    }

    public PfAdapter(ArrayList<PfModule> pfList) {
        this.pfList = pfList;
    }
}
