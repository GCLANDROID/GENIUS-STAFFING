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
import io.cordova.myapp00d753.activity.EmailActivity;
import io.cordova.myapp00d753.module.ConfigReportModel;
import io.cordova.myapp00d753.module.SpokePersonModel;


public class SpokePersonAdapter extends RecyclerView.Adapter<SpokePersonAdapter.MyViewHolder> {
    ArrayList<SpokePersonModel>itemList=new ArrayList<>();
    Context context;

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.spokeperson_row,viewGroup,false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.tvBranch.setText(itemList.get(i).getBranch());
        myViewHolder.tvSpokePerson.setText(itemList.get(i).getPersonName());

        myViewHolder.imgCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+itemList.get(i).getMobileNumber()));
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
        myViewHolder.imgEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, EmailActivity.class);
                intent.putExtra("to",itemList.get(i).getEmail());
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
        TextView tvSpokePerson,tvBranch;
        ImageView imgEmail,imgCall;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imgCall=(ImageView) itemView.findViewById(R.id.imgCall);
            imgEmail=(ImageView) itemView.findViewById(R.id.imgEmail);

            tvSpokePerson=(TextView) itemView.findViewById(R.id.tvSpokePerson);
            tvBranch=(TextView) itemView.findViewById(R.id.tvBranch);

        }
    }

    public SpokePersonAdapter(ArrayList<SpokePersonModel> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
    }
}
