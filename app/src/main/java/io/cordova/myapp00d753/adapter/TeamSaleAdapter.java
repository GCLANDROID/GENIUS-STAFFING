package io.cordova.myapp00d753.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.activity.SupEmpSaleActivity;
import io.cordova.myapp00d753.module.TeamSaleModule;
import io.cordova.myapp00d753.utility.Pref;

/* renamed from: io.cordova.myapp00d753.adapter.TeamSaleAdapter */
public class TeamSaleAdapter extends RecyclerView.Adapter<TeamSaleAdapter.MyViewHolder> {
    Context context;
    ArrayList<TeamSaleModule> itemList = new ArrayList<>();

    /* renamed from: p */
    Pref f363p;

    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.team_sales_raw, viewGroup, false));
    }

    public void onBindViewHolder(MyViewHolder myViewHolder, final int i) {
        myViewHolder.tvCount.setText(this.itemList.get(i).getCount());
        myViewHolder.tvName.setText(this.itemList.get(i).getName());
        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(TeamSaleAdapter.this.context, SupEmpSaleActivity.class);
                intent.putExtra("zoneId", TeamSaleAdapter.this.itemList.get(i).getId());
                intent.putExtra("branchId", TeamSaleAdapter.this.itemList.get(i).getBranchId());
                intent.putExtra("zoneName", TeamSaleAdapter.this.itemList.get(i).getName());
                TeamSaleAdapter.this.context.startActivity(intent);
            }
        });
    }

    public int getItemCount() {
        return this.itemList.size();
    }

    /* renamed from: io.cordova.myapp00d753.adapter.TeamSaleAdapter$MyViewHolder */
    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvCount;
        TextView tvName;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.tvName = (TextView) itemView.findViewById(R.id.tvName);
            this.tvCount = (TextView) itemView.findViewById(R.id.tvCount);
        }
    }

    public TeamSaleAdapter(ArrayList<TeamSaleModule> itemList2, Context context2) {
        this.itemList = itemList2;
        this.context = context2;
    }
}
