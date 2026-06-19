package io.cordova.myapp00d753.adapter;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.module.DDInfoModel;
import io.cordova.myapp00d753.module.NeedToActModel;
import retrofit2.http.Header;

public class DDInfoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    Context context;
    //ArrayList<DDInfoModel> ddInfoList;
    ArrayList<NeedToActModel> ddInfoList;
    public DDInfoAdapter(Context context, ArrayList<NeedToActModel> ddInfoList) {
        this.context = context;
        this.ddInfoList = ddInfoList;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 0;
        } else {
            return 1;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 0){
            View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.ddinfo_header_layout,parent,false);
            return new HeaderViewHolder(itemView);
        } else {
            View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.ddinfo_item_layout,parent,false);
            return new MyViewholder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MyViewholder) {
            ((MyViewholder) holder).tvDocType.setText(ddInfoList.get(position).getDocName());
            //((MyViewholder) holder).tvDescription.setText(ddInfoList.get(position).getDocumentType());
            if (position%2 == 0){
                ((MyViewholder) holder).llMain.setBackgroundResource(R.drawable.background_12);
            }
            /*if (ddInfoList.get(position).getStatus().equals("1")){
                ((MyViewholder) holder).imgStatus.setImageDrawable(
                        ContextCompat.getDrawable(context, R.drawable.available)
                );
            } else if(ddInfoList.get(position).getStatus().equals("2")){
                ((MyViewholder) holder).imgStatus.setImageDrawable(
                        ContextCompat.getDrawable(context, R.drawable.pending)
                );
            } else if(ddInfoList.get(position).getStatus().equals("3")){
                ((MyViewholder) holder).imgStatus.setImageDrawable(
                        ContextCompat.getDrawable(context, R.drawable.missing)
                );
            }*/

            /*if (needToActModelList.get(position).acceptanceType == 0 || needToActModelList.get(position).acceptanceType == 3){
                holder.txtButton.setText("View Document");
                holder.actionImage.setImageResource(R.drawable.check_mark);
                holder.llMain.setBackgroundResource(R.drawable.design_green_outline);
            } else {
                holder.txtButton.setText("Accept");
                holder.actionImage.setImageResource(R.drawable.double_tap);
                holder.llMain.setBackgroundResource(R.drawable.lldesign_error_2);
            }*/
        } else {

        }
    }

    @Override
    public int getItemCount() {
        return ddInfoList.size();
    }

    class HeaderViewHolder extends RecyclerView.ViewHolder{

        public HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    class MyViewholder extends RecyclerView.ViewHolder{
        TextView tvDocType, tvDescription;
        ImageView imgStatus;
        LinearLayout llMain;
        public MyViewholder(@NonNull View itemView) {
            super(itemView);
            tvDocType = itemView.findViewById(R.id.tvDocType);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            imgStatus = itemView.findViewById(R.id.imgStatus);
            llMain = itemView.findViewById(R.id.llMain);
        }
    }
}
