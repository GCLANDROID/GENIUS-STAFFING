package io.cordova.myapp00d753.activity.honnasa.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collection;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.activity.honnasa.HonasaSalesActivity;
import io.cordova.myapp00d753.activity.honnasa.model.ViewSalesModel;
import io.cordova.myapp00d753.module.SpineerItemModel;


public class HanasaSalesAdapter extends RecyclerView.Adapter<HanasaSalesAdapter.MyViewHolder>{
    private static final String TAG = "HanasaSalesAdapter";
    Context context;
    ArrayList<ViewSalesModel> viewSalesList;
    ArrayList<ViewSalesModel> viewSalesListAll;
    public HanasaSalesAdapter(Context context, ArrayList<ViewSalesModel> viewSalesList) {
        this.context = context;
        this.viewSalesList = viewSalesList;
        this.viewSalesListAll = new ArrayList<ViewSalesModel>(viewSalesList);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.honasa_sales_entry_row,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.txtProductName.setText(viewSalesList.get(position).productName);
        holder.txtStockInHand.setText(String.valueOf(viewSalesList.get(position).inStock));
        Log.e(TAG, viewSalesList.get(position).productName+" : "+viewSalesList.get(position).productValue);
        holder.txtReceivingStock.setText("");
        holder.txtSalesDone.setText("");
        holder.txtSalesValues.setText("");
        holder.txtClosingStock.setText("");

        holder.txtReceivingStock.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    if (!editable.toString().isEmpty()){
                        Log.e(TAG, "afterTextChanged: 1");
                        if (!holder.txtSalesDone.getText().toString().isEmpty()){
                            Log.e(TAG, "afterTextChanged: 1.1");
                            int sum = (Integer.parseInt(holder.txtStockInHand.getText().toString().trim())+
                                    Integer.parseInt(holder.txtReceivingStock.getText().toString()))-Integer.parseInt(holder.txtSalesDone.getText().toString());
                            holder.txtClosingStock.setText(String.valueOf(sum));
                        } else {
                            Log.e(TAG, "afterTextChanged: 1.2");
                            int sum = Integer.parseInt(holder.txtStockInHand.getText().toString().trim())+Integer.parseInt(holder.txtReceivingStock.getText().toString().trim());
                            holder.txtClosingStock.setText(String.valueOf(sum));
                        }
                        viewSalesList.get(position).setReceivingStock(holder.txtReceivingStock.getText().toString().trim());
                    } else {
                        if (!holder.txtSalesDone.getText().toString().isEmpty()){
                            Log.e(TAG, "afterTextChanged: 1.1");

                            int sum = (Integer.parseInt(holder.txtStockInHand.getText().toString().trim())+
                                    0)-Integer.parseInt(holder.txtSalesDone.getText().toString());
                            holder.txtClosingStock.setText(String.valueOf(sum));
                        } else {
                            Log.e(TAG, "afterTextChanged: 1.2");
                            //int sum = Integer.parseInt(holder.txtStockInHand.getText().toString().trim()) + 0;
                            holder.txtClosingStock.setText("");
                        }
                        viewSalesList.get(position).setReceivingStock(holder.txtReceivingStock.getText().toString().trim());
                    }
                } catch (IndexOutOfBoundsException e){

                }

            }
        });

        holder.txtSalesDone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    if (!holder.txtSalesDone.getText().toString().isEmpty()){
                        Log.e(TAG, "afterTextChanged: 2");
                        if (!holder.txtReceivingStock.getText().toString().isEmpty()){
                            Log.e(TAG, "afterTextChanged: 2.1");
                            int sum = (Integer.parseInt(holder.txtStockInHand.getText().toString().trim()) +
                                    Integer.parseInt(holder.txtReceivingStock.getText().toString()))-Integer.parseInt(holder.txtSalesDone.getText().toString());
                            holder.txtClosingStock.setText(String.valueOf(sum));

                            if (sum < 0){
                                holder.txtError.setVisibility(View.VISIBLE);
                                ((HonasaSalesActivity) context).isSalesDoneIsLessThenClosingStock = false;
                            } else {
                                holder.txtError.setVisibility(View.GONE);
                                ((HonasaSalesActivity) context).isSalesDoneIsLessThenClosingStock = true;
                            }

                            viewSalesList.get(position).setClosingStock(String.valueOf(sum));

                            double salesValue = Double.valueOf(viewSalesList.get(position).productValue) * Integer.parseInt(holder.txtSalesDone.getText().toString().trim());
                            holder.txtSalesValues.setText(String.valueOf(salesValue));

                            viewSalesList.get(position).setSalesValues(String.valueOf(salesValue));
                            viewSalesList.get(position).setSalesDone(holder.txtSalesDone.getText().toString().trim());
                        } else {
                            Log.e(TAG, "afterTextChanged: 2.2");
                            //int sum = Integer.parseInt(holder.txtStockInHand.getText().toString().trim())+Integer.parseInt(holder.txtReceivingStock.getText().toString().trim());
                            int sum = (Integer.parseInt(holder.txtStockInHand.getText().toString().trim()) + 0)-Integer.parseInt(holder.txtSalesDone.getText().toString());

                            holder.txtClosingStock.setText(String.valueOf(sum));
                            viewSalesList.get(position).setClosingStock(String.valueOf(sum));


                            double salesValue = Double.valueOf(viewSalesList.get(position).productValue) * Integer.parseInt(holder.txtSalesDone.getText().toString().trim());
                            holder.txtSalesValues.setText(String.valueOf(salesValue));

                            viewSalesList.get(position).setSalesValues(String.valueOf(salesValue));
                            viewSalesList.get(position).setSalesDone(holder.txtSalesDone.getText().toString().trim());
                        }

                    } else {
                        int sum = (Integer.parseInt(holder.txtStockInHand.getText().toString().trim()) + Integer.parseInt((holder.txtReceivingStock.getText().toString().isEmpty()) ? "0":  holder.txtReceivingStock.getText().toString() ))-0;

                        holder.txtClosingStock.setText((String.valueOf(sum).toString().equals("0")?"":String.valueOf(sum)));
                        viewSalesList.get(position).setClosingStock(String.valueOf(sum));

                        //double salesValue = Double.valueOf(viewSalesList.get(position).productValue) * Integer.parseInt(holder.txtSalesDone.getText().toString().trim());
                        holder.txtSalesValues.setText("");

                        viewSalesList.get(position).setSalesValues(String.valueOf(0));
                        viewSalesList.get(position).setSalesDone(holder.txtSalesDone.getText().toString().trim());
                    }
                } catch (IndexOutOfBoundsException e){

                }

            }
        });


        holder.txtClosingStock.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                Log.e(TAG, "txtClosingStock: called");
                try {
                    if (holder.txtClosingStock.getText().toString().isEmpty()){
                        viewSalesList.get(position).setModified(false);
                    } else {
                        viewSalesList.get(position).setModified(true);
                    }
                } catch (IndexOutOfBoundsException e){
                    Log.e(TAG, "afterTextChanged: called");
                }

            }
        });
    }



    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            ArrayList<ViewSalesModel> filteredList = new ArrayList<>();
            if (charSequence.toString().isEmpty()){
                filteredList.addAll(viewSalesListAll);
            } else {
                for (ViewSalesModel sellingItem: viewSalesListAll){
                    if (sellingItem.productName.toLowerCase().contains(charSequence.toString().toLowerCase())){
                        filteredList.add(sellingItem);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            viewSalesList.clear();
            viewSalesList.addAll((Collection<? extends ViewSalesModel>)filterResults.values);
            notifyDataSetChanged();
        }
    };

    @Override
    public int getItemCount() {
        return viewSalesList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView txtProductName,txtError;
        EditText txtStockInHand,txtReceivingStock,txtSalesDone,txtSalesValues,txtClosingStock;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtProductName = itemView.findViewById(R.id.txtProductName);
            txtStockInHand = itemView.findViewById(R.id.txtStockInHand);
            txtReceivingStock = itemView.findViewById(R.id.txtReceivingStock);
            txtSalesDone = itemView.findViewById(R.id.txtSalesDone);
            txtSalesValues = itemView.findViewById(R.id.txtSalesValues);
            txtClosingStock = itemView.findViewById(R.id.txtClosingStock);
            txtError = itemView.findViewById(R.id.txtError);
        }
    }
}
