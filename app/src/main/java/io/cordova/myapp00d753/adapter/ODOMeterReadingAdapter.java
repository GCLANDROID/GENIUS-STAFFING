package io.cordova.myapp00d753.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.module.ODOMeterReadingModule;
import io.cordova.myapp00d753.module.OfflineDailyModel;


public class ODOMeterReadingAdapter extends RecyclerView.Adapter<ODOMeterReadingAdapter.MyViewHolder> {
    ArrayList<ODOMeterReadingModule>itemList=new ArrayList<>();
    Context context;
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.odometer_reading_row,viewGroup,false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int i) {

        myViewHolder.tvJrStrt.setText(itemList.get(i).getJrstrtreading());
        myViewHolder.tvJrEnd.setText(itemList.get(i).getJrendreading());
        myViewHolder.tvDistance.setText(itemList.get(i).getDistance());
        myViewHolder.tvApprovalStatus.setText(itemList.get(i).getApprovalstatus());




        myViewHolder.tvDate.setText(itemList.get(i).getDate());

        try {
            Picasso.with(context)
                    .load(itemList.get(i).getJrstrtimage()).error(R.drawable.noimage)
                    .into(myViewHolder.imgJrStrt);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            Picasso.with(context)
                    .load(itemList.get(i).getJrendimage()).error(R.drawable.noimage)
                    .into(myViewHolder.imgJrEnd);


        } catch (Exception e) {
            e.printStackTrace();
        }








    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imgJrStrt,imgJrEnd;
        TextView tvJrStrt,tvJrEnd,tvDistance,tvApprovalStatus,tvDate;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvJrStrt=(TextView)itemView.findViewById(R.id.tvJrStrt);
            tvJrEnd=(TextView)itemView.findViewById(R.id.tvJrEnd);
            tvDistance=(TextView)itemView.findViewById(R.id.tvDistance);
            tvApprovalStatus=(TextView)itemView.findViewById(R.id.tvApprovalStatus);
            imgJrEnd=(ImageView) itemView.findViewById(R.id.imgJrEnd);
            imgJrStrt=(ImageView) itemView.findViewById(R.id.imgJrStrt);
            tvDate=(TextView) itemView.findViewById(R.id.tvDate);



        }
    }

    public ODOMeterReadingAdapter(ArrayList<ODOMeterReadingModule> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
    }
}
