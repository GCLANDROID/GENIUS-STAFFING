package io.cordova.myapp00d753.activity.SKF.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.cordova.myapp00d753.R;

public class SKF_DayTypeAdapter extends RecyclerView.Adapter<SKF_DayTypeAdapter.MyViewHolder> {

    Context context;
    ArrayList<String> dayTypeArray;
    SKF_BacklogAdapter.setOnDayTypeSelect setOnDayTypeSelect;
    public SKF_DayTypeAdapter(Context context,  ArrayList<String> dayTypeArray) {
        this.context = context;
        this.dayTypeArray = dayTypeArray;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.grad_layout,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.tvGrad.setText(dayTypeArray.get(position));
        holder.tvGrad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setOnDayTypeSelect.onClick(position,dayTypeArray.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return dayTypeArray.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvGrad;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvGrad = itemView.findViewById(R.id.tvGrad);
        }
    }

    public void setSetOnGradSelect(SKF_BacklogAdapter.setOnDayTypeSelect setOnGradSelect) {
        this.setOnDayTypeSelect = setOnGradSelect;
    }
}
