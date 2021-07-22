package io.cordova.myapp00d753.fragment;

import android.os.Build;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
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
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.stream.Collectors;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.adapter.SaleConsolidatedAdapter;
import io.cordova.myapp00d753.module.SaleConsolidatedModel;
import io.cordova.myapp00d753.utility.AppData;
import io.cordova.myapp00d753.utility.Pref;

public class ZoneProductWiseFragment extends Fragment {
    View view;
    RecyclerView rvItem;
    ArrayList<SaleConsolidatedModel>itemList=new ArrayList<>();
    ArrayList<SaleConsolidatedModel>itemList1=new ArrayList<>();

    Pref pref;
    int y;
    String year,month,financialYear;
    LinearLayout llLoader;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_zone_product_wise, container, false);
        iniTView();
        getItemList();
        return view;
    }

    private void iniTView(){
        pref = new Pref(getContext());
        rvItem = (RecyclerView) view.findViewById(R.id.rvItem);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rvItem.setLayoutManager(layoutManager);

        y = Calendar.getInstance().get(Calendar.YEAR);
        year = String.valueOf(y);
        Log.d("year", year);
        int m = Calendar.getInstance().get(Calendar.MONTH) + 1;
        Log.d("month", String.valueOf(m));
        if (m == 1) {
            month = "January";
        } else if (m == 2) {
            month = "February";
        } else if (m == 3) {
            month = "March";
        } else if (m == 4) {
            month = "April";
        } else if (m == 5) {
            month = "May";
        } else if (m == 6) {
            month = "June";
        } else if (m == 7) {
            month = "July";
        } else if (m == 8) {
            month = "August";
        } else if (m == 9) {
            month = "September";
        } else if (m == 10) {
            month = "October";
        } else if (m == 11) {
            month = "November";
        } else if (m == 12) {
            month = "December";
        }

        if(month.equals("January")){
            int futureyear = y - 1;
            financialYear = futureyear+"-"+year;
        }else if (month.equals("February")){
            int futureyear = y - 1;
            financialYear = futureyear+"-"+year;
        }else if (month.equals("March")){
            int futureyear = y - 1;
            financialYear = futureyear+"-"+year;
        }else {
            int futureyear = y + 1;
            financialYear = year+"-"+futureyear;
        }

        llLoader=(LinearLayout)view.findViewById(R.id.llLoader);

    }

    private void getItemList() {



        rvItem.setVisibility(View.GONE);
        llLoader.setVisibility(View.VISIBLE);
        String surl = AppData.url + "get_EmployeeSalesReport?ClientID=" + pref.getEmpClintId() + "&UserID=" + pref.getEmpId() + "&FinancialYear=" + financialYear + "&Month=" + month + "&RType=ZM&Operation=7&SecurityCode=" + pref.getSecurityCode();
        Log.d("input", surl);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onResponse(String response) {

                        Log.d("responseAttendance", response);
                        rvItem.setVisibility(View.VISIBLE);
                        llLoader.setVisibility(View.GONE);
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
                                    String Zonename = obj.optString("Zonename");
                                    String Sold = obj.optString("Overall_Sold");
                                    String Target = obj.optString("Overall_Target");
                                    String Overall_AchvPer = obj.optString("Overall_AchvPer");
                                    String FinancialYear = obj.optString("FinancialYear");

                                    String PrevFinanYear = obj.optString("PrevFinanYear");
                                    String Overall_Prev_Target = obj.optString("Overall_Prev_Target");
                                    String Overall_Prev_Sold = obj.optString("Overall_Prev_Sold");
                                    String Overall_Prev_AchvPer = obj.optString("Overall_Prev_AchvPer");
                                    String Overall_GrowthPercentage = obj.optString("Overall_GrowthPercentage");
                                    SaleConsolidatedModel cModel=new SaleConsolidatedModel(Zonename,Target,Sold,Overall_AchvPer,Overall_Prev_Target,Overall_Prev_Sold,Overall_Prev_AchvPer,Overall_GrowthPercentage,FinancialYear,PrevFinanYear);
                                    itemList.add(cModel);
                                }

                                HashSet<SaleConsolidatedModel> filter = new HashSet(itemList);
                                ArrayList<SaleConsolidatedModel> persons = new ArrayList<SaleConsolidatedModel>(filter);
                                for (int i=0;i<persons.size();i++){
                                    SaleConsolidatedModel Comel=new SaleConsolidatedModel(persons.get(i).getName(),persons.get(i).getCyTarget(),persons.get(i).getCySold(),persons.get(i).getCyAchievement(),persons.get(i).getLyTarget(),persons.get(i).getLySold(),persons.get(i).getLyAchievement(),persons.get(i).getGrowth(),persons.get(i).getYear(),persons.get(i).getLy());
                                    itemList1.add(Comel) ;
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

    private void setAdapter(){
        SaleConsolidatedAdapter saleAdapter=new SaleConsolidatedAdapter(itemList1);
        rvItem.setAdapter(saleAdapter);
    }
}