package io.cordova.myapp00d753.activity.metso.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.activity.metso.model.ApproverModel;


public class ApproverAutoCompleteAdapter extends ArrayAdapter<ApproverModel> {
    private List<ApproverModel> approverList;

    public ApproverAutoCompleteAdapter(Context context, List<ApproverModel> approverList) {
        super(context, 0, approverList);
        this.approverList = new ArrayList<>(approverList);
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return approverFilter;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.approver_autocomplete_layout, parent, false);
        }

        TextView txtApprover = convertView.findViewById(R.id.txtApprover);
        ApproverModel ageModel = getItem(position);
        if (ageModel != null){
            txtApprover.setText(ageModel.getApproverName());
        }

        return convertView;
    }

    private Filter approverFilter = new Filter() {

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            FilterResults filterResults = new FilterResults();
            List<ApproverModel> suggestions = new ArrayList<>();
            if (charSequence == null || charSequence.length() ==0 ){
                suggestions.addAll(approverList);
            }else {
                String filterPattern = charSequence.toString().toLowerCase().trim();
                for (ApproverModel item : approverList){
                    if (item.getApproverName().toLowerCase().contains(filterPattern)){
                        suggestions.add(item);
                    }
                }
            }
            filterResults.values = suggestions;
            filterResults.count = suggestions.size();
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            clear();
            addAll((List) filterResults.values);
            notifyDataSetChanged();
        }

        @Override
        public CharSequence convertResultToString(Object resultValue) {
            return ((ApproverModel) resultValue).getApproverName();
        }
    };
}
