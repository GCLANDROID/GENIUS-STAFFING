package io.cordova.myapp00d753.adapter;


import static android.content.Context.CLIPBOARD_SERVICE;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.module.AttendancereportModule;
import io.cordova.myapp00d753.module.InsuranceModel;

public class InsuranceAdapter extends RecyclerView.Adapter<InsuranceAdapter.MyViewHolder> {
    ArrayList<InsuranceModel>itemList=new ArrayList<>();
    Context context;
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.insurance_row,viewGroup,false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, @SuppressLint("RecyclerView") int i) {

        myViewHolder.tvPolicyName.setText(itemList.get(i).getPolicyName());
        myViewHolder.tvPolicyNumber.setText(itemList.get(i).getPolicyNumber());
        myViewHolder.tvUHID.setText(itemList.get(i).getUHID());
        myViewHolder.tvPassword.setText(itemList.get(i).getPassword());

        myViewHolder.tvCompanyURL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse(itemList.get(i).getCompanyURL()); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        myViewHolder.tvTutorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse(itemList.get(i).getTrainingURL()); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
        myViewHolder.imgCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("label", itemList.get(i).getPassword());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(context, "Text Copied", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvPolicyName,tvPolicyNumber,tvUHID,tvPassword,tvCompanyURL,tvTutorial;
        ImageView imgCopy;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPolicyName=(TextView)itemView.findViewById(R.id.tvPolicyName);
            tvPolicyNumber=(TextView)itemView.findViewById(R.id.tvPolicyNumber);
            tvUHID=(TextView)itemView.findViewById(R.id.tvUHID);
            tvPassword=(TextView)itemView.findViewById(R.id.tvPassword);
            tvCompanyURL=(TextView)itemView.findViewById(R.id.tvCompanyURL);
            tvTutorial=(TextView)itemView.findViewById(R.id.tvTutorial);
            imgCopy=(ImageView) itemView.findViewById(R.id.imgCopy);
        }
    }

    public InsuranceAdapter(ArrayList<InsuranceModel> itemList,Context context) {
        this.itemList = itemList;
        this.context=context;
    }
}
