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
import io.cordova.myapp00d753.module.DocumentManageModule;

public class DocumentAdapter extends RecyclerView.Adapter<DocumentAdapter.MyViewHolder> {
    ArrayList<DocumentManageModule>documentList=new ArrayList<>();
    Context context;
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.document_raw,viewGroup,false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.tvDocumentName.setText(documentList.get(i).getDocumentName());
        myViewHolder.tvDocumentType.setText( documentList.get(i).getDocumentType());
        myViewHolder.tvCreatedOn.setText(documentList.get(i).getCreatedOn());
        myViewHolder.tvAEMStatusName.setText(documentList.get(i).getaEMStatusName());

        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(documentList.get(i).getDocLink()); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                if (!documentList.get(i).getDocLink().equals("") && documentList.get(i).getDocLink() != null) {
                    context.startActivity(intent);
                } else {

                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return documentList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvDocumentName,tvDocumentType,tvApprovalRemarks,tvCreatedOn,tvAEMStatusName;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDocumentName=(TextView)itemView.findViewById(R.id.tvDocumentName);
            tvDocumentType=(TextView)itemView.findViewById(R.id.tvDocumentType);

            tvCreatedOn=(TextView)itemView.findViewById(R.id.tvCreatedOn);
            tvAEMStatusName=(TextView)itemView.findViewById(R.id.tvAEMStatusName);

        }
    }

    public DocumentAdapter(ArrayList<DocumentManageModule> documentList, Context context) {
        this.documentList = documentList;
        this.context = context;
    }
}
