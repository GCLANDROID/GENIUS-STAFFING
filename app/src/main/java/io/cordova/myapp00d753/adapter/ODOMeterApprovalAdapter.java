package io.cordova.myapp00d753.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.activity.AttenApprovalActivity;
import io.cordova.myapp00d753.bluedart.ODOmeterApprvalActivity;
import io.cordova.myapp00d753.module.AttendanceApprovalModule;
import io.cordova.myapp00d753.module.ODOMeterApprovalModule;

public class ODOMeterApprovalAdapter extends RecyclerView.Adapter<ODOMeterApprovalAdapter.MyViewHolder> {
    ArrayList<ODOMeterApprovalModule>itemList=new ArrayList<>();
    Context context;
    boolean isSelectedAll,isAll;
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.odometer_approvalraw,viewGroup,false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {
        final ODOMeterApprovalModule attandanceModel = itemList.get(i);


            myViewHolder.tvDate.setText(itemList.get(i).getDate());
        myViewHolder.tvPunchType.setText(itemList.get(i).getJrType());
        myViewHolder.tvReading.setText(itemList.get(i).getOdometereading());

        byte[] decodedStrtString = Base64.decode(itemList.get(i).getImageData(), Base64.DEFAULT);
        Bitmap strtBitmap = BitmapFactory.decodeByteArray(decodedStrtString, 0, decodedStrtString.length);
        myViewHolder.imgImage.setImageBitmap(strtBitmap);




        if (isAll) {
            if (isSelectedAll) {
                myViewHolder.imgLike.setVisibility(View.VISIBLE);

            } else {
                myViewHolder.imgLike.setVisibility(View.GONE);
            }
        }else {
            if (itemList.get(i).isSelected()){
                myViewHolder.imgLike.setVisibility(View.VISIBLE);
            }else {
                myViewHolder.imgLike.setVisibility(View.GONE);
            }
            myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    attandanceModel.setSelected(!attandanceModel.isSelected());
                    // holder.view.setBackgroundColor(attandanceModel.isSelected() ? Color.CYAN : Color.WHITE);

                    if (attandanceModel.isSelected()) {

                        myViewHolder.imgLike.setVisibility(View.VISIBLE);
                        itemList.get(i).setSelected(true);
                        notifyDataSetChanged();

                        ((ODOmeterApprvalActivity) context).updateAttendanceStatus(i, true );



                    } else {
                        myViewHolder.imgLike.setVisibility(View.GONE);
                        ((ODOmeterApprvalActivity) context).updateAttendanceStatus(i, false);
                        itemList.get(i).setSelected(false);
                        notifyDataSetChanged();
                    }

                }
            });
        }





    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate,tvReading,tvPunchType;
        ImageView imgLike,imgImage;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate=(TextView)itemView.findViewById(R.id.tvDate);
            tvReading=(TextView)itemView.findViewById(R.id.tvReading);
            tvPunchType=(TextView)itemView.findViewById(R.id.tvPunchType);


            imgLike=(ImageView) itemView.findViewById(R.id.imgLike);
            imgImage=(ImageView) itemView.findViewById(R.id.imgImage);
        }
    }

    public ODOMeterApprovalAdapter(ArrayList<ODOMeterApprovalModule> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
    }

    public void selectAll(){
        isSelectedAll=true;
        isAll=true;
        notifyDataSetChanged();
    }
    public void unselectall(){
        isSelectedAll=false;
        isAll=false;
        notifyDataSetChanged();
    }
}
