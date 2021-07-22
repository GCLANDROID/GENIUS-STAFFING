package io.cordova.myapp00d753.adapter;

import android.content.Context;
import android.content.Intent;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.activity.DocumentReportActivity;
import io.cordova.myapp00d753.module.AttendancereportModule;
import io.cordova.myapp00d753.module.DocumentNumberRawModel;

public class DocNumberReportAdapter extends RecyclerView.Adapter<DocNumberReportAdapter.MyViewHolder> {
    ArrayList<DocumentNumberRawModel>itemList=new ArrayList<>();
    Context context;
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.document_number_raw,viewGroup,false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int i) {
        myViewHolder.tvDocName.setText(itemList.get(i).getDocName());
        myViewHolder.tvTotalDoc.setText(itemList.get(i).getDocNumber());
        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, DocumentReportActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("status",itemList.get(i).getDocName());
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvDocName,tvTotalDoc;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDocName=(TextView)itemView.findViewById(R.id.tvDocName);
            tvTotalDoc=(TextView)itemView.findViewById(R.id.tvTotalDoc);

        }
    }

    public DocNumberReportAdapter(ArrayList<DocumentNumberRawModel> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
    }
}
