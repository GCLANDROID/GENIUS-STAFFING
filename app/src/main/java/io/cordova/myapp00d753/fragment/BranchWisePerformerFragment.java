package io.cordova.myapp00d753.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.adapter.SaleMTDAdapter;
import io.cordova.myapp00d753.adapter.TopPerformerAdapter;
import io.cordova.myapp00d753.module.SaleQTDModel;
import io.cordova.myapp00d753.module.TopPerformerModel;
import io.cordova.myapp00d753.utility.AppController;
import io.cordova.myapp00d753.utility.AppData;
import io.cordova.myapp00d753.utility.Pref;


public class BranchWisePerformerFragment extends Fragment {

    View view;
    RecyclerView rvItem,rvItemBM,rvItemSA;
    ArrayList<TopPerformerModel> itemList = new ArrayList<>();
    ArrayList<TopPerformerModel> itemListBM = new ArrayList<>();
    ArrayList<TopPerformerModel> itemListSA = new ArrayList<>();

    int y;
    String cuyear, month;
    String financialYear;
    Pref pref;

    LinearLayout llUnitLike,llRevenueLike;
    ImageView imgUnitLike,imgRevenueLike;






    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_performer, container, false);
        initView();
        getItemList();
        onClick();

        return view;
    }

    private void initView() {
        pref = new Pref(getContext());
        rvItem = (RecyclerView) view.findViewById(R.id.rvItem);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rvItem.setLayoutManager(layoutManager);


        y = Calendar.getInstance().get(Calendar.YEAR);
        cuyear = String.valueOf(y);
        Log.d("year", cuyear);

        int m = Calendar.getInstance().get(Calendar.MONTH) + 1;
        month = pref.getMonth();
        financialYear = pref.getFinacialYear();

        llUnitLike=(LinearLayout)view.findViewById(R.id.llUnitLike);
        llRevenueLike=(LinearLayout)view.findViewById(R.id.llRevenueLike);

        imgUnitLike=(ImageView)view.findViewById(R.id.imgUnitLike);
        imgRevenueLike=(ImageView)view.findViewById(R.id.imgRevenueLike);





    }

    private void onClick(){
        llUnitLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imgUnitLike.getVisibility()==View.GONE){
                    imgUnitLike.setVisibility(View.VISIBLE);
                    imgRevenueLike.setVisibility(View.GONE);
                    getItemList();
                }else {
                    imgUnitLike.setVisibility(View.GONE);
                    imgRevenueLike.setVisibility(View.GONE);
                }

            }
        });

        llRevenueLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imgRevenueLike.getVisibility()==View.GONE){
                    imgUnitLike.setVisibility(View.GONE);
                    imgRevenueLike.setVisibility(View.VISIBLE);
                    getItemListRevenue();
                }else {
                    imgUnitLike.setVisibility(View.GONE);
                    imgRevenueLike.setVisibility(View.GONE);
                }

            }
        });
    }


    private void getItemList() {
        Log.d("Arpan", "arpan");
        final ProgressDialog progressBar = new ProgressDialog(getContext());
        progressBar.setMessage("Loading..");
        progressBar.setCancelable(false);
        progressBar.show();
        String surl = AppData.url + "get_EmployeeSalesReport?ClientID="+pref.getEmpClintId()+"&UserID="+pref.getEmpId()+"&FinancialYear="+financialYear+"&Month="+month+"&RType=sold&Operation=9&SecurityCode=0000";
        Log.d("input", surl);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("responseAttendance", response);
                        progressBar.dismiss();
                        itemList.clear();
                        // attendabceInfiList.clear();

                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("response12", "@@@@@@" + job1);
                            String responseText = job1.optString("responseText");

                            boolean responseStatus = job1.optBoolean("responseStatus");
                            if (responseStatus) {
                                // Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();
                                JSONArray responseData = job1.optJSONArray("responseData");
                                for (int i=0;i<responseData.length();i++) {

                                    JSONObject obj = responseData.getJSONObject(i);
                                    String Sold = obj.optString("Sold");
                                    String Target = obj.optString("Target");
                                    String AchvPer = obj.optString("AchvPer");
                                    String Name = obj.optString("Name");
                                    String ReportType = obj.optString("ReportType");
                                    String Zonename = obj.optString("Zonename");
                                    String BranchName = obj.optString("BranchName");
                                    if (ReportType.equals("BM")){
                                        TopPerformerModel tpModel=new TopPerformerModel(Name,Target+" Units",Sold+" Units",AchvPer);
                                        tpModel.setBranch("");
                                        tpModel.setZone(Zonename);
                                        itemList.add(tpModel);
                                    }



                                }
                                setAdapter();






                            } else {

                                Toast.makeText(getContext(), "No data found", Toast.LENGTH_LONG).show();

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            // Toast.makeText(AttendanceReportActivity.this, "Volly Error", Toast.LENGTH_LONG).show();

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


                // Toast.makeText(AttendanceReportActivity.this, "volly 2"+error.toString(), Toast.LENGTH_LONG).show();
                Log.e("ert", error.toString());
            }
        }) {

        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }

    private void getItemListRevenue() {
        Log.d("Arpan", "arpan");
        final ProgressDialog progressBar = new ProgressDialog(getContext());
        progressBar.setMessage("Loading..");
        progressBar.setCancelable(false);
        progressBar.show();
        String surl = AppData.url + "get_EmployeeSalesReport?ClientID="+pref.getEmpClintId()+"&UserID="+pref.getEmpId()+"&FinancialYear="+financialYear+"&Month="+month+"&RType=REV&Operation=9&SecurityCode=0000";
        Log.d("input", surl);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("responseAttendance", response);
                        progressBar.dismiss();
                        itemList.clear();
                        // attendabceInfiList.clear();

                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("response12", "@@@@@@" + job1);
                            String responseText = job1.optString("responseText");

                            boolean responseStatus = job1.optBoolean("responseStatus");
                            if (responseStatus) {
                                // Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();
                                JSONArray responseData = job1.optJSONArray("responseData");
                                for (int i=0;i<responseData.length();i++) {

                                    JSONObject obj = responseData.getJSONObject(i);
                                    String Sold = obj.optString("Revenue_Sold");
                                    String Target = obj.optString("Revenue_Target");
                                    String AchvPer = obj.optString("Revenue_AchvPer");
                                    String Name = obj.optString("Name");
                                    String ReportType = obj.optString("ReportType");
                                    String Zonename = obj.optString("Zonename");
                                    String BranchName = obj.optString("BranchName");
                                    if (ReportType.equals("BM")){
                                        TopPerformerModel tpModel=new TopPerformerModel(Name,"Rs."+Target+" Lacs","Rs."+Sold+" Lacs",AchvPer);
                                        tpModel.setBranch("");
                                        tpModel.setZone(Zonename);
                                        itemList.add(tpModel);
                                    }



                                }
                                setAdapter();






                            } else {

                                Toast.makeText(getContext(), "No data found", Toast.LENGTH_LONG).show();

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            // Toast.makeText(AttendanceReportActivity.this, "Volly Error", Toast.LENGTH_LONG).show();

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


                // Toast.makeText(AttendanceReportActivity.this, "volly 2"+error.toString(), Toast.LENGTH_LONG).show();
                Log.e("ert", error.toString());
            }
        }) {

        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }






    private void setAdapter() {
        TopPerformerAdapter saleAdapter = new TopPerformerAdapter(itemList);
        rvItem.setAdapter(saleAdapter);


    }


}