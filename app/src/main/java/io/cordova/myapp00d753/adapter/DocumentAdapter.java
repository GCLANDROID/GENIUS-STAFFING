package io.cordova.myapp00d753.adapter;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.module.DocumentManageModule;
import io.cordova.myapp00d753.utility.ClientID;
import io.cordova.myapp00d753.utility.Pref;
import io.cordova.myapp00d753.utility.Util;

public class DocumentAdapter extends RecyclerView.Adapter<DocumentAdapter.MyViewHolder> {
    ArrayList<DocumentManageModule>documentList=new ArrayList<>();
    Context context;
    Pref pref;
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.document_raw,viewGroup,false);

        return new MyViewHolder(itemView);
    }

    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        if (pref.getEmpClintId().equals(ClientID.DALMIA_BHARAT_CEMENT_LTD)){
            myViewHolder.clUploadedOn.setVisibility(View.GONE);
        }
        myViewHolder.tvDocumentName.setText(documentList.get(i).getDocumentName());
        myViewHolder.tvDocumentType.setText( documentList.get(i).getDocumentType());
        myViewHolder.tvAEMStatusName.setText(documentList.get(i).getaEMStatusName());
        if (documentList.get(i).getaEMStatusName().equalsIgnoreCase("approved")){
            myViewHolder.tvAEMStatusName.setTextColor(ContextCompat.getColor(context, R.color.green_2));
        } else if(documentList.get(i).getaEMStatusName().equalsIgnoreCase("pending")){
            myViewHolder.tvAEMStatusName.setTextColor(ContextCompat.getColor(context, R.color.yellow));
        } else if(documentList.get(i).getaEMStatusName().equalsIgnoreCase("reject")
                || documentList.get(i).getaEMStatusName().equalsIgnoreCase("rejected")){
            myViewHolder.tvAEMStatusName.setTextColor(ContextCompat.getColor(context, R.color.red));
        }

        myViewHolder.tvCreatedOn.setText(Util.changeAnyDateFormat(documentList.get(i).getCreatedOn(),"yyyy-MM-dd'T'HH:mm:ss.SS","dd MMM yyyy"));        myViewHolder.tvAEMStatusName.setText(documentList.get(i).getaEMStatusName());
        Log.e("info", "onBindViewHolder: "+documentList.get(i).getDocLink() );
        myViewHolder.btnViewDoc.setOnClickListener(new View.OnClickListener() {
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
        Button btnViewDoc;
        ConstraintLayout clUploadedOn;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDocumentName=(TextView)itemView.findViewById(R.id.tvDocumentName);
            tvDocumentType=(TextView)itemView.findViewById(R.id.tvDocumentType);

            tvCreatedOn=(TextView)itemView.findViewById(R.id.tvCreatedOn);
            tvAEMStatusName=(TextView)itemView.findViewById(R.id.tvAEMStatusName);
            btnViewDoc=(Button) itemView.findViewById(R.id.btnViewDoc);
            clUploadedOn = (ConstraintLayout) itemView.findViewById(R.id.clUploadedOn);
        }
    }

    public DocumentAdapter(ArrayList<DocumentManageModule> documentList, Context context) {
        this.documentList = documentList;
        this.context = context;
        pref = new Pref(context);
    }
}
