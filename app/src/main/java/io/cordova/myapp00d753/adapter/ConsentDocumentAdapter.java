package io.cordova.myapp00d753.adapter;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.module.ConsentDocModule;
import io.cordova.myapp00d753.module.PFDocumentModule;

public class ConsentDocumentAdapter extends RecyclerView.Adapter<ConsentDocumentAdapter.MyViewHolder> {
    ArrayList<ConsentDocModule>itemList=new ArrayList<>();

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.consent_doc_row,viewGroup,false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {


        myViewHolder.tvdocInfo.setText(itemList.get(i).getDocument());

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvdocInfo;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvdocInfo=(TextView)itemView.findViewById(R.id.tvdocInfo);

        }
    }

    public ConsentDocumentAdapter(ArrayList<ConsentDocModule> itemList) {
        this.itemList = itemList;

    }
}
