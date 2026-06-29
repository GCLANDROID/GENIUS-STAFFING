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
import io.cordova.myapp00d753.module.NewPFLinkModel;

public class NewPFDocumentLinkAdapter extends RecyclerView.Adapter<NewPFDocumentLinkAdapter.MyViewHolder>{
    Context context;
    ArrayList<NewPFLinkModel> newPfLinkList;

    public NewPFDocumentLinkAdapter(Context context, ArrayList<NewPFLinkModel> newPfLinkList) {
        this.context = context;
        this.newPfLinkList = newPfLinkList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.new_pf_document_link_layout,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.tvDocName.setText(newPfLinkList.get(position).getDocumentName());
    }

    @Override
    public int getItemCount() {
        return newPfLinkList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tvDocName,tvViewDocument;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDocName = itemView.findViewById(R.id.tvDocName);
            tvViewDocument = itemView.findViewById(R.id.tvViewDocument);
        }
    }
}
