package io.cordova.myapp00d753.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;

import java.util.ArrayList;

import io.cordova.myapp00d753.KYCFamilyModel;
import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.activity.TempEducationaActivity;
import io.cordova.myapp00d753.module.EducationalModel;

public class EducationAdapter extends RecyclerView.Adapter<EducationAdapter.MyViewHolder> {
    Context context;
    ArrayList<EducationalModel>itemList=new ArrayList<>();

    public EducationAdapter(Context context, ArrayList<EducationalModel> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.education_row,viewGroup,false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.tvQualification.setText(itemList.get(i).getQualification());
        myViewHolder.tvBoard.setText(itemList.get(i).getBoard());
        myViewHolder.tvPassingYear.setText(itemList.get(i).getPassingyear());
        myViewHolder.tvPercentage.setText(itemList.get(i).getPercentage());

        myViewHolder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((TempEducationaActivity) context).deleteItem(i);
                notifyDataSetChanged();
            }
        });

        myViewHolder.imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    ((TempEducationaActivity) context).editItem(i);
                    notifyDataSetChanged();
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvQualification,tvBoard,tvPassingYear,tvPercentage;
        ImageView imgEdit,imgDelete;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvQualification=(TextView)itemView.findViewById(R.id.tvQualification);
            tvBoard=(TextView)itemView.findViewById(R.id.tvBoard);
            tvPassingYear=(TextView)itemView.findViewById(R.id.tvPassingYear);
            tvPercentage=(TextView)itemView.findViewById(R.id.tvPercentage);
            imgEdit=(ImageView) itemView.findViewById(R.id.imgEdit);
            imgDelete=(ImageView) itemView.findViewById(R.id.imgDelete);


        }
    }


}
