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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.module.ChartModel;
import io.cordova.myapp00d753.utility.AppController;
import io.cordova.myapp00d753.utility.AppData;
import io.cordova.myapp00d753.utility.Pref;


public class SupGraphicalMTDFragment extends Fragment {


    View view;
    ArrayList<ChartModel> itemList = new ArrayList<>();
    ArrayList<ChartModel> itemList1 = new ArrayList<>();
    BarChart chart,barTargetchart;
    ArrayList NoOfEmp = new ArrayList();
    ArrayList year = new ArrayList();

    ArrayList target = new ArrayList();
    ArrayList quater = new ArrayList();

    int y;
    String cuyear,month;
    String financialYear;
    Pref pref;
    TextView tvMonth;
    ArrayList<PieEntry> pieValues = new ArrayList<>();

    ArrayList<PieEntry> pieValues1 = new ArrayList<>();
    PieChart pieChart, pieChart1;

    TextView tvCYTarget,tvCYAchieved;
    ArrayList<Float>cytarget=new ArrayList<>();
    ArrayList<Float>cysold=new ArrayList<>();
    Float cytargetSum,cysoldSum,lytargetSum,lysoldSum;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_graphical_m_t_d, container, false);
        initView();
        getItemList();
        return view;
    }

    private void initView() {
        pref = new Pref(getContext());

        y = Calendar.getInstance().get(Calendar.YEAR);
        cuyear = String.valueOf(y);
        Log.d("year", cuyear);

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
        financialYear = pref.getFinacialYear();
        // tvMonth = (TextView) view.findViewById(R.id.tvMonth);
//        tvMonth.setText("Showing All Month Report :");
        pieChart = (PieChart) view.findViewById(R.id.pieChart);
        pieChart1 = (PieChart) view.findViewById(R.id.pieChart1);


        getItemList();


    }

    private void getItemList() {
        Log.d("Arpan", "arpan");
        final ProgressDialog progressBar = new ProgressDialog(getContext());
        progressBar.setMessage("Loading..");
        progressBar.setCancelable(false);
        progressBar.show();
        String surl = AppData.url + "get_EmployeeSalesReport?ClientID=" + pref.getEmpClintId() + "&UserID=" + pref.getID() + "&FinancialYear=" + financialYear + "&Month=" + pref.getMonth() + "&RType=MTD&Operation=2&SecurityCode=" + pref.getSecurityCode();
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


                                JSONObject obj = responseData.getJSONObject(0);
                                float Sold = Float.parseFloat(obj.optString("Sold"));
                                float Target = Float.parseFloat(obj.optString("Target"));
                                float AchvPer =  Float.parseFloat(obj.optString("AchvPer"));
                                float Prev_AchvPer = Float.parseFloat( obj.optString("Prev_AchvPer"));
                                String PrevFinanYear =  obj.optString("PrevFinanYear");
                                String FinancialYear =  obj.optString("FinancialYear");


                                pieValues.add(new PieEntry(Sold, "Achieved"));
                                pieValues.add(new PieEntry(Target, "Target"));

                                pieValues1.add(new PieEntry(AchvPer,FinancialYear));
                                pieValues1.add(new PieEntry(Prev_AchvPer,PrevFinanYear));

                                PieDataSet dataSet = new PieDataSet(pieValues, "");
                                PieData pieData = new PieData(dataSet);
                                pieData.setValueFormatter(new LargeValueFormatter());
                                pieChart.setData(pieData);
                                dataSet.setColors(ColorTemplate.JOYFUL_COLORS);
                                dataSet.setSliceSpace(2f);
                                dataSet.setValueTextColor(Color.WHITE);
                                dataSet.setValueTextSize(10f);
                                dataSet.setSliceSpace(5f);
                                Legend l = pieChart.getLegend();
                                l.setEnabled(false);
                                pieChart.setDrawHoleEnabled(true);
                                pieChart.setTransparentCircleRadius(25f);
                                pieChart.setHoleRadius(25f);
                                pieChart.animateXY(1000, 1000);




                                PieDataSet dataSet1 = new PieDataSet(pieValues1, "");
                                PieData pieData1 = new PieData(dataSet1);
                                pieData1.setValueFormatter(new PercentFormatter());
                                pieChart1.setData(pieData1);
                                dataSet1.setColors(ColorTemplate.PASTEL_COLORS);
                                dataSet1.setSliceSpace(2f);
                                dataSet1.setValueTextColor(Color.WHITE);
                                dataSet1.setValueTextSize(10f);
                                dataSet1.setSliceSpace(5f);

                                Legend ll = pieChart1.getLegend();
                                ll.setEnabled(false);


                                pieChart1.setDrawHoleEnabled(true);
                                pieChart1.setTransparentCircleRadius(25f);
                                pieChart1.setHoleRadius(25f);


                                pieChart1.animateXY(1000, 1000);



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

}