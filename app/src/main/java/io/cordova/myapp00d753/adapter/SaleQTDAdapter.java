package io.cordova.myapp00d753.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.BarChart;

import java.util.ArrayList;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.module.ChartModel;
import io.cordova.myapp00d753.module.SaleQTDModel;

public class SaleQTDAdapter extends RecyclerView.Adapter<SaleQTDAdapter.MyViewHolder> {
    ArrayList<SaleQTDModel> itemList =new ArrayList<>();


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.saleqtd,viewGroup,false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {


/* myViewHolder.tvTarget.setText("Target: "+itemList.get(i).getTarget());
        myViewHolder.tvSold.setText("Sold: "+itemList.get(i).getSold());
        myViewHolder.tvYear.setText("Quater: "+itemList.get(i).getQuater());
        myViewHolder.tvPercentage.setText("Achievement(%): "+itemList.get(i).getPercentage());

*/





    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvTarget,tvSold,tvYear,tvPercentage;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            /*tvTarget=(TextView)itemView.findViewById(R.id.tvTarget);
            tvSold=(TextView)itemView.findViewById(R.id.tvSold);
            tvYear=(TextView)itemView.findViewById(R.id.tvYear);
            tvPercentage=(TextView)itemView.findViewById(R.id.tvPercentage);
*/


        }
    }

    public SaleQTDAdapter(ArrayList<SaleQTDModel> itemList) {
        this.itemList = itemList;
    }
}
