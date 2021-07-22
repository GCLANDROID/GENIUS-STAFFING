package io.cordova.myapp00d753.adapter;

import android.app.Activity;
import android.graphics.Color;

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
import io.cordova.myapp00d753.activity.AboutUsActivity;
import io.cordova.myapp00d753.activity.ContactUsActivity;
import io.cordova.myapp00d753.module.MenuModule;

public class ContactMenuAdapter extends RecyclerView.Adapter<ContactMenuAdapter.MyViewHolder> {
    ArrayList<MenuModule>menuList=new ArrayList<>();
    Activity activity;
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.menu_raw,viewGroup,false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {
        myViewHolder.tvMenuName.setText(menuList.get(i).getMenuName());
        myViewHolder.tvMenuDetails.setText(menuList.get(i).getMenuDetails());
        if (menuList.get(i).getMenuName().equals("AboutUs")){
            myViewHolder.llMenu.setVisibility(View.GONE);
            myViewHolder.llMenuDetails.setVisibility(View.GONE);
        }
        myViewHolder.llMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (!menuList.get(i).isExpanded()){
                    ((ContactUsActivity)activity).updateStatus(i,true);
                }else {
                    ((ContactUsActivity)activity).updateStatus(i,false);
                }
            }
        });


        if (menuList.get(i).isExpanded())
        {
            myViewHolder.imgMenuPlus.setVisibility(View.GONE);
            myViewHolder.imgMenuMins.setVisibility(View.VISIBLE);
            myViewHolder.llMenu.setVisibility(View.VISIBLE);
            myViewHolder.llMenuDetails.setVisibility(View.VISIBLE);
            myViewHolder.imgForward.setVisibility(View.VISIBLE);
            myViewHolder.imgForward1.setVisibility(View.GONE);
            myViewHolder.llMenu.setBackgroundColor(Color.parseColor("#279616"));
            myViewHolder.tvMenuName.setTextColor(Color.parseColor("#ffffff"));
        }
        else
        {
            myViewHolder.imgMenuPlus.setVisibility(View.VISIBLE);
            myViewHolder.imgMenuMins.setVisibility(View.GONE);
            myViewHolder.llMenu.setVisibility(View.VISIBLE);
            myViewHolder.llMenuDetails.setVisibility(View.GONE);
            myViewHolder.imgForward.setVisibility(View.GONE);
            myViewHolder.imgForward1.setVisibility(View.VISIBLE);
            myViewHolder.llMenu.setBackgroundColor(Color.parseColor("#c8c9ca"));
            myViewHolder.tvMenuName.setTextColor(Color.parseColor("#000000"));
        }

    }

    @Override
    public int getItemCount() {
        return menuList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvMenuDetails,tvMenuName;
        ImageView imgMenuMins,imgMenuPlus;
        LinearLayout llMenu,llMenuDetails;
        ImageView imgForward,imgForward1;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMenuDetails=(TextView)itemView.findViewById(R.id.tvMenuDetails);
            tvMenuName=(TextView)itemView.findViewById(R.id.tvMenuName);
            imgMenuPlus=(ImageView)itemView.findViewById(R.id.imgMenuPlus);
            imgMenuMins=(ImageView)itemView.findViewById(R.id.imgMenuMins);
            llMenu=(LinearLayout)itemView.findViewById(R.id.llMenu);
            llMenuDetails=(LinearLayout)itemView.findViewById(R.id.llMenuDetails);
            imgForward=(ImageView)itemView.findViewById(R.id.imgForward);
            imgForward1=(ImageView)itemView.findViewById(R.id.imgForward1);


        }
    }

    public ContactMenuAdapter(ArrayList<MenuModule> menuList, Activity activity) {
        this.menuList = menuList;
        this.activity = activity;
    }
}
