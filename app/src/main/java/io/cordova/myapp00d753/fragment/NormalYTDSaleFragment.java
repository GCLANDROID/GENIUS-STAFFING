package io.cordova.myapp00d753.fragment;


import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.adapter.SaleMTDAdapter;
import io.cordova.myapp00d753.module.ChartModel;
import io.cordova.myapp00d753.module.SaleQTDModel;
import io.cordova.myapp00d753.utility.AppController;
import io.cordova.myapp00d753.utility.AppData;
import io.cordova.myapp00d753.utility.Pref;

/**
 * A simple {@link Fragment} subclass.
 */
public class NormalYTDSaleFragment extends Fragment {


   View view;
   TextView tvTarget,tvSold,tvAchievement;

    int y;
    String cuyear,month;
    String financialYear;
    Pref pref;
    TextView tvMonth;
    RecyclerView rvItem;
    ArrayList<SaleQTDModel> itemList = new ArrayList<>();

    Float overallPercentage;
    ArrayList<PieEntry>pieValues=new ArrayList<>();
    ArrayList<String>xVals=new ArrayList<>();
    PieChart pieChart;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_normal_sale_ytd, container, false);
        initView();
        return view;
    }

    private void initView(){
        pref=new Pref(getContext());

        rvItem = (RecyclerView) view.findViewById(R.id.rvItem);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rvItem.setLayoutManager(layoutManager);


        y = Calendar.getInstance().get(Calendar.YEAR);
        cuyear = String.valueOf(y);
        Log.d("year", cuyear);

        int m = Calendar.getInstance().get(Calendar.MONTH) + 1;
        Log.d("month", String.valueOf(m));

            month = pref.getMonth();


        financialYear = pref.getFinacialYear();
        getItemList();
        tvMonth=(TextView)view.findViewById(R.id.tvMonth);
        tvMonth.setText("Showing Yearly Report:");





    }

    private void getItemList() {
        Log.d("Arpan", "arpan");
        final ProgressDialog progressBar = new ProgressDialog(getContext());
        progressBar.setMessage("Loading..");
        progressBar.setCancelable(false);
        progressBar.show();
        String surl = AppData.url + "get_EmployeeSalesReport?ClientID="+pref.getEmpClintId()+"&UserID="+pref.getEmpId()+"&FinancialYear="+financialYear+"&Month="+month+"&RType=YTD&Operation=2&SecurityCode="+pref.getSecurityCode();
        Log.d("input", surl);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("responseAttendance", response);
                        progressBar.dismiss();
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
                                    String FinancialYear = obj.optString("FinancialYear");
                                    String PrevFinanYear = obj.optString("PrevFinanYear");
                                    String Prev_Sold = obj.optString("Prev_Sold");
                                    String Prev_Target = obj.optString("Prev_Target");
                                    String Prev_AchvPer = obj.optString("Prev_AchvPer");
                                    String GrowthPercentage = obj.optString("GrowthPercentage");
                                    String Month = obj.optString("Month");

                                    SaleQTDModel cModel = new SaleQTDModel(Target, Sold, Month, AchvPer);
                                    cModel.setFinYear(FinancialYear);
                                    cModel.setPreYear(PrevFinanYear);
                                    cModel.setPreTarget(Prev_Target);
                                    cModel.setPreSold(Prev_Sold);
                                    cModel.setPrePercentage(Prev_AchvPer);
                                    cModel.setGrowth(GrowthPercentage);
                                    cModel.setMonth(Month);
                                    itemList.add(cModel);

                                    float per=Float.valueOf(GrowthPercentage);
                                    overallPercentage=per/12;
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
        AppController.getInstance().addToRequestQueue(stringRequest, "string_req");
    }

    private void setAdapter() {
        SaleMTDAdapter saleAdapter = new SaleMTDAdapter(itemList);
        rvItem.setAdapter(saleAdapter);
    }

}
