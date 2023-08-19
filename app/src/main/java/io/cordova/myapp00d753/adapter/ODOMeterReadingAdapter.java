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


        byte[] decodedStrtString = Base64.decode(itemList.get(i).getJrstrtimage(), Base64.DEFAULT);
        Bitmap strtBitmap = BitmapFactory.decodeByteArray(decodedStrtString, 0, decodedStrtString.length);
        myViewHolder.imgJrStrt.setImageBitmap(strtBitmap);

        byte[] decodedEndString = Base64.decode(itemList.get(i).getJrendimage(), Base64.DEFAULT);
        Bitmap endBitmap = BitmapFactory.decodeByteArray(decodedEndString, 0, decodedEndString.length);
        myViewHolder.imgJrEnd.setImageBitmap(endBitmap);

        myViewHolder.tvDate.setText(itemList.get(i).getDate());






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
