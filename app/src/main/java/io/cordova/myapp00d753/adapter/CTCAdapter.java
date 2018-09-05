package io.cordova.myapp00d753.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;



import java.util.ArrayList;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.module.CTCModule;

public class CTCAdapter extends RecyclerView.Adapter<CTCAdapter.MyViewHolder> {
    ArrayList<CTCModule>ctcList=new ArrayList<>();
    @NonNull
    @Override
    public CTCAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.ctc_raw,viewGroup,false);

        return new CTCAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CTCAdapter.MyViewHolder myViewHolder, int i) {

        myViewHolder.tvEmpId.setText(ctcList.get(i).getEmpId());
        myViewHolder.tvEmpName.setText(ctcList.get(i).getEmpName());
        myViewHolder.tvSalary.setText(ctcList.get(i).getSalary());

    }

    @Override
    public int getItemCount() {
        return ctcList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvEmpName,tvEmpId,tvSalary;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvEmpName=(TextView)itemView.findViewById(R.id.tvEmpName);
            tvEmpId=(TextView)itemView.findViewById(R.id.tvEmpId);
            tvSalary=(TextView)itemView.findViewById(R.id.tvSalary);

        }

    }

    public CTCAdapter(ArrayList<CTCModule> ctcList) {
        this.ctcList = ctcList;
    }
}
