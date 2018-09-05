package io.cordova.myapp00d753.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;



import java.util.ArrayList;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.module.MenuModule;

public class MenuServiceAdapter extends RecyclerView.Adapter<MenuServiceAdapter.MyViewHolder> {
    ArrayList<MenuModule>menuList=new ArrayList<>();
    @NonNull
    @Override
    public MenuServiceAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.menu_raw,viewGroup,false);

        return new MenuServiceAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MenuServiceAdapter.MyViewHolder myViewHolder, int i) {
        myViewHolder.tvMenuName.setText(menuList.get(i).getMenuName());
        myViewHolder.tvMenuDetails.setText(menuList.get(i).getMenuDetails());
        if (menuList.get(i).getMenuName().equals("Services")){
            myViewHolder.llMenu.setVisibility(View.GONE);
            myViewHolder.llMenuDetails.setVisibility(View.GONE);
        }
        myViewHolder.llMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (myViewHolder.imgMenuPlus.getVisibility()==View.VISIBLE){
                    myViewHolder.imgMenuPlus.setVisibility(View.GONE);
                    myViewHolder.imgMenuMins.setVisibility(View.VISIBLE);
                    myViewHolder.llMenu.setVisibility(View.VISIBLE);
                    myViewHolder.llMenuDetails.setVisibility(View.VISIBLE);
                }else {
                    myViewHolder.imgMenuPlus.setVisibility(View.VISIBLE);
                    myViewHolder.imgMenuMins.setVisibility(View.GONE);
                    myViewHolder.llMenu.setVisibility(View.VISIBLE);
                    myViewHolder.llMenuDetails.setVisibility(View.GONE);
                }
            }
        });



    }

    @Override
    public int getItemCount() {
        return menuList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvMenuDetails,tvMenuName;
        ImageView imgMenuMins,imgMenuPlus;
        LinearLayout llMenu,llMenuDetails;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMenuDetails=(TextView)itemView.findViewById(R.id.tvMenuDetails);
            tvMenuName=(TextView)itemView.findViewById(R.id.tvMenuName);
            imgMenuPlus=(ImageView)itemView.findViewById(R.id.imgMenuPlus);
            imgMenuMins=(ImageView)itemView.findViewById(R.id.imgMenuMins);
            llMenu=(LinearLayout)itemView.findViewById(R.id.llMenu);
            llMenuDetails=(LinearLayout)itemView.findViewById(R.id.llMenuDetails);


        }
    }

    public MenuServiceAdapter(ArrayList<MenuModule> menuList) {
        this.menuList = menuList;
    }
}
