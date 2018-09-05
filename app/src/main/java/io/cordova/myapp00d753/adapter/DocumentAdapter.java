package io.cordova.myapp00d753.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;



import java.util.ArrayList;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.module.DocumentManageModule;

public class DocumentAdapter extends RecyclerView.Adapter<DocumentAdapter.MyViewHolder> {
    ArrayList<DocumentManageModule>documentList=new ArrayList<>();
    @NonNull
    @Override
    public DocumentAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.document_raw,viewGroup,false);

        return new DocumentAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DocumentAdapter.MyViewHolder myViewHolder, int i) {
        myViewHolder.tvDocumentName.setText(documentList.get(i).getDocumentName());
        myViewHolder.tvDocumentType.setText(documentList.get(i).getDocumentType());
        myViewHolder.tvApprovalRemarks.setText(documentList.get(i).getApprovalRemarks());
        myViewHolder.tvCreatedOn.setText(documentList.get(i).getCreatedOn());
        myViewHolder.tvAEMStatusName.setText(documentList.get(i).getaEMStatusName());


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
            tvApprovalRemarks=(TextView)itemView.findViewById(R.id.tvApprovalRemarks);
            tvCreatedOn=(TextView)itemView.findViewById(R.id.tvCreatedOn);
            tvAEMStatusName=(TextView)itemView.findViewById(R.id.tvAEMStatusName);

        }
    }

    public DocumentAdapter(ArrayList<DocumentManageModule> documentList) {
        this.documentList = documentList;
    }
}
