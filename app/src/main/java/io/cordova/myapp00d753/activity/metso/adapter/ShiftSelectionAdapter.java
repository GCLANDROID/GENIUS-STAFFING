package io.cordova.myapp00d753.activity.metso.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.activity.metso.model.MetsoShiftModel;


public class ShiftSelectionAdapter extends RecyclerView.Adapter<ShiftSelectionAdapter.MyViewHolder>{
    Context context;
    ArrayList<MetsoShiftModel> metsoShiftList;
    MetsoAttendanceAdapter.ShiftSelectionInterface shiftSelectionInterface;

    public ShiftSelectionAdapter(Context context, ArrayList<MetsoShiftModel> metsoShiftList) {
        this.context = context;
        this.metsoShiftList = metsoShiftList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shift_selection_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.txtSelectShift.setText(metsoShiftList.get(position).getColumn1());
        holder.txtSelectShift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shiftSelectionInterface.onSelect(metsoShiftList.get(position).workingShiftID,metsoShiftList.get(position).getColumn1());
            }
        });
    }

    @Override
    public int getItemCount() {
        return metsoShiftList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView txtSelectShift;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtSelectShift = itemView.findViewById(R.id.txtSelectShift);
        }
    }

    public void setShiftSelectionInterface(MetsoAttendanceAdapter.ShiftSelectionInterface shiftSelectionInterface) {
        this.shiftSelectionInterface = shiftSelectionInterface;
    }
}
