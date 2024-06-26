package io.cordova.myapp00d753.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import io.cordova.myapp00d753.KYCFamilyModel;
import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.module.EducationalModel;

public class EducationAdapter extends RecyclerView.Adapter<EducationAdapter.MyViewHolder> {
    ArrayList<EducationalModel>itemList=new ArrayList<>();
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




    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvQualification,tvBoard,tvPassingYear,tvPercentage;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvQualification=(TextView)itemView.findViewById(R.id.tvQualification);
            tvBoard=(TextView)itemView.findViewById(R.id.tvBoard);
            tvPassingYear=(TextView)itemView.findViewById(R.id.tvPassingYear);
            tvPercentage=(TextView)itemView.findViewById(R.id.tvPercentage);


        }
    }

    public EducationAdapter(ArrayList<EducationalModel> itemList) {
        this.itemList = itemList;
    }
}
