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
import io.cordova.myapp00d753.module.PFDocumentModule;

public class NewPFDocumentAdapter extends RecyclerView.Adapter<NewPFDocumentAdapter.MyViewHolder> {
    ArrayList<PFDocumentModule>itemList=new ArrayList<>();
    Context context;
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.new_pf_document_link_layout,viewGroup,false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.tvDocName.setText(itemList.get(i).getDoc_Info());
        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse(itemList.get(i).getDoc_Url()); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        //TextView tvdocInfo;
        TextView tvDocName,tvViewDocument;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            //tvdocInfo=(TextView)itemView.findViewById(R.id.tvdocInfo);
            tvDocName=(TextView)itemView.findViewById(R.id.tvDocName);
            tvViewDocument=(TextView)itemView.findViewById(R.id.tvViewDocument);

        }
    }

    public NewPFDocumentAdapter(ArrayList<PFDocumentModule> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
    }
}
