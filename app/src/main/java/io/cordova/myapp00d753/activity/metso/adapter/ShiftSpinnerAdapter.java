package io.cordova.myapp00d753.activity.metso.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.activity.metso.model.MetsoShiftModel;



public class ShiftSpinnerAdapter  extends ArrayAdapter<MetsoShiftModel> {

    public ShiftSpinnerAdapter(Context context, ArrayList<MetsoShiftModel> shitItemList) {
        super(context, 0, shitItemList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable
            View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    private View initView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.custom_spinner_list, parent, false);
        }

        TextView txtShiftTime = convertView.findViewById(R.id.txtShiftTime);
        MetsoShiftModel currentItem = getItem(position);

        // It is used the name to the TextView when the
        // current item is not null.
        if (currentItem != null) {
            txtShiftTime.setText(currentItem.getColumn1());
        }
        return convertView;
    }
}
