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
import io.cordova.myapp00d753.module.CTCModule;
import io.cordova.myapp00d753.module.PFManualModel;

public class PFManualAdapter extends RecyclerView.Adapter<PFManualAdapter.MyViewHolder> {
    ArrayList<PFManualModel>itemList=new ArrayList<>();
    Context context;
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.pf_manual_row,viewGroup,false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.tvPFDoc.setText(itemList.get(i).getCaption());
        if (itemList.get(i).getDoc_Type().equals("1")){
            myViewHolder.imgDoc.setImageDrawable(context.getResources().getDrawable(R.drawable.pdff));
        }else if (itemList.get(i).getDoc_Type().equals("2")){
            myViewHolder.imgDoc.setImageDrawable(context.getResources().getDrawable(R.drawable.youtube));
        }else if (itemList.get(i).getDoc_Type().equals("2")){
            myViewHolder.imgDoc.setImageDrawable(context.getResources().getDrawable(R.drawable.picture));
        }

        myViewHolder.tvViewDocument.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse(itemList.get(i).getDoc_URL()); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });



    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvPFDoc,tvViewDocument;
        ImageView imgDoc;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPFDoc=(TextView)itemView.findViewById(R.id.tvPFDoc);
            tvViewDocument=(TextView)itemView.findViewById(R.id.tvViewDocument);

            imgDoc=(ImageView) itemView.findViewById(R.id.imgDoc);

        }

    }

    public PFManualAdapter(ArrayList<PFManualModel> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
    }
}
