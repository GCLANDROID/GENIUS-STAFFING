package io.cordova.myapp00d753.fragment;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

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
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.stream.Collectors;

import io.cordova.myapp00d753.R;
import io.cordova.myapp00d753.activity.ZoneSaleReportActivity;
import io.cordova.myapp00d753.module.ChartModel;
import io.cordova.myapp00d753.utility.AppData;
import io.cordova.myapp00d753.utility.Pref;

public class GrpahicalFragment extends Fragment {
  View view;
    CombinedChart comCYTargetChart,comCYPYTargetChart,comCYRevenueChart,comCYPYRevenueChart,comCYPYProductGrowthChart,comCYPYRevenueGrowthChart;

    ArrayList<Entry> cyTargetYvals=new ArrayList<>();
    ArrayList<BarEntry>cyTargetYYvals=new ArrayList<>();
    ArrayList cyTargetxvals = new ArrayList();

    ArrayList<Entry> cypyAchvYvals=new ArrayList<>();
    ArrayList<BarEntry>cypyAchvYYvals=new ArrayList<>();
    ArrayList cypyAchvxvals = new ArrayList();

    ArrayList<Entry> cyRevenueYvals=new ArrayList<>();
    ArrayList<BarEntry>cyRevenueYYvals=new ArrayList<>();
    ArrayList cyRevenuexvals = new ArrayList();

    ArrayList<Entry> cypyRevenueYvals=new ArrayList<>();
    ArrayList<BarEntry>cypyRevenueYYvals=new ArrayList<>();
    ArrayList cypyRevenuexvals = new ArrayList();


    ArrayList<Entry> cypyProductGrowthYvals=new ArrayList<>();
    ArrayList<BarEntry>cypyProductGrowthYYvals=new ArrayList<>();
    ArrayList cypyProductGrowthxvals = new ArrayList();

    ArrayList<Entry> cypyRevenueGrowthYvals=new ArrayList<>();
    ArrayList<BarEntry>cypyRevenueGrowthYYvals=new ArrayList<>();
    ArrayList cypyRevenueGrowthxvals = new ArrayList();

    Pref pref;
    String month,financialYear;
    int y;
    String year;
    ArrayList<ChartModel>itemList=new ArrayList<>();
    ArrayList<ChartModel>itemList1=new ArrayList<>();
    ArrayList<ChartModel>itemList2=new ArrayList<>();
    ArrayList<ChartModel>itemList3=new ArrayList<>();
    ArrayList<ChartModel>itemList4=new ArrayList<>();
    ArrayList<ChartModel>itemList5=new ArrayList<>();
    LinearLayout ll1;
    FloatingActionButton fab;
    TextView tvMonth,tvToolBar;
    ProgressDialog progressBar;

    String strNumber;
    ImageView imgBack,imgHome;
    LinearLayout llLoader;
    FrameLayout flMain;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_grpahical, container, false);
        initView();
        return view;
    }

    private void initView(){
        pref=new Pref(getContext());

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

        ll1=(LinearLayout)view.findViewById(R.id.ll1);
        ll1.setClickable(false);

        tvMonth = (TextView)view. findViewById(R.id.tvMonth);
        // tvMonth.setText("Synopsis For The Year Of " + financialYear );
        tvToolBar = (TextView) view.findViewById(R.id.tvToolBar);






        imgBack=(ImageView)view.findViewById(R.id.imgBack);
        imgHome=(ImageView)view.findViewById(R.id.imgHome);

       llLoader=(LinearLayout)view.findViewById(R.id.llLoader);
       flMain=(FrameLayout)view.findViewById(R.id.flMain);

       getItemList();


    }

    private void getItemList() {
        final ArrayList<Float>b=new ArrayList<>();
        final ArrayList<Float>c=new ArrayList<>();
        final ArrayList<String>a=new ArrayList<>();
        Log.d("Arpan", "arpan");

        flMain.setVisibility(View.GONE);
        llLoader.setVisibility(View.VISIBLE);
        String surl = AppData.url + "get_EmployeeSalesReport?ClientID=" + pref.getEmpClintId() + "&UserID=" + pref.getEmpId() + "&FinancialYear=" + financialYear + "&Month=" + month + "&RType=ZM&Operation=7&SecurityCode=" + pref.getSecurityCode();
        Log.d("input", surl);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onResponse(String response) {

                        Log.d("responseAttendance", response);
                        flMain.setVisibility(View.GONE);
                        llLoader.setVisibility(View.VISIBLE);
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
                                    float Sold = Float.parseFloat(obj.optString("Overall_Sold"));
                                    float Target = Float.parseFloat(obj.optString("Overall_Target"));
                                    String Month = obj.optString("Month");
                                    String Zonename = obj.optString("Zonename");
                                    String Overall_GrowthPercentage = obj.optString("Overall_GrowthPercentage");
                                    a.add(Zonename);
                                    b.add(Sold);
                                    c.add(Target);

                                }

                                ArrayList<String> uniqueList = (ArrayList) a.stream().distinct().collect(Collectors.toList());

                                Log.d("ps",uniqueList.toString());
                                ArrayList<String>bd=new ArrayList<>();
                                for (int i = 0; i < uniqueList.size(); i++) {
                                    cyTargetxvals.add(uniqueList.get(i));

                                }

                                Log.d("ss",cyTargetxvals.toString());

                                ArrayList<Float> uniqueList1 = (ArrayList) b.stream().distinct().collect(Collectors.toList());
                                for (int i=0;i<uniqueList1.size();i++){
                                    cyTargetYvals.add(new Entry(i,uniqueList1.get(i)));
                                }

                                ArrayList<Float> uniqueList2 = (ArrayList) c.stream().distinct().collect(Collectors.toList());
                                for (int i=0;i<uniqueList2.size();i++){
                                    cyTargetYYvals.add(new BarEntry(i,uniqueList2.get(i)));
                                }




                                //cy target and sold combined chart
                                comCYTargetChart=view.findViewById(R.id.comCYTargetChart);
                                comCYTargetChart.setTouchEnabled(false);


                                comCYTargetChart.getDescription().setText("");
                                comCYTargetChart.setDrawGridBackground(false);
                                comCYTargetChart.setDrawBarShadow(false);
                                comCYTargetChart.setHighlightFullBarEnabled(false);
                                comCYTargetChart.setDrawOrder(new CombinedChart.DrawOrder[]{
                                        CombinedChart.DrawOrder.BAR,  CombinedChart.DrawOrder.LINE
                                });
                                comCYTargetChart.animateX(1000);


                                Legend l = comCYTargetChart.getLegend();
                                l.setWordWrapEnabled(true);
                                l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
                                l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
                                l.setOrientation(Legend.LegendOrientation.HORIZONTAL);

                                YAxis rightAxis = comCYTargetChart.getAxisRight();
                                rightAxis.setEnabled(false);
                                rightAxis.setDrawGridLines(false);
                                rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

                                YAxis leftAxis = comCYTargetChart.getAxisLeft();
                                leftAxis.setDrawGridLines(false);
                                leftAxis.setAxisMinimum(0f);

                                XAxis xAxis = comCYTargetChart.getXAxis();
                                //xAxis.setPosition(XAxis.XAxisPosition.BOTH_SIDED);
                                xAxis.setAxisMinimum(0f);
                                xAxis.setGranularity(1f);
                                xAxis.setValueFormatter(new IndexAxisValueFormatter(cyTargetxvals));



                                CombinedData data = new CombinedData();

                                data.setData( generateBarDataForCYTarget());
                                data.setData(generateLineDataForCYSold());

                                // xAxis.setAxisMaximum(data.getXMax() + 0.25f);
                                comCYTargetChart.setData(data);
                                comCYTargetChart.invalidate();

                                getItemListForCYPYAchv();






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

    private void getItemListForCYPYAchv() {
        final ArrayList<Float>b=new ArrayList<>();
        final ArrayList<Float>c=new ArrayList<>();
        final ArrayList<String>a=new ArrayList<>();
        Log.d("Arpan", "arpan");

        flMain.setVisibility(View.GONE);
        llLoader.setVisibility(View.VISIBLE);
        String surl = AppData.url + "get_EmployeeSalesReport?ClientID=" + pref.getEmpClintId() + "&UserID=" + pref.getEmpId() + "&FinancialYear=" + financialYear + "&Month=" + month + "&RType=ZM&Operation=7&SecurityCode=" + pref.getSecurityCode();
        Log.d("input", surl);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onResponse(String response) {

                        Log.d("responseAttendance", response);
                        flMain.setVisibility(View.GONE);
                        llLoader.setVisibility(View.VISIBLE);
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
                                    float Sold = Float.parseFloat(obj.optString("Overall_Sold"));
                                    float Target = Float.parseFloat(obj.optString("Overall_Prev_AchvPer"));
                                    String Month = obj.optString("Month");
                                    Float prev_sold=Float.parseFloat(obj.optString("Overall_Prev_Sold"));
                                    String Zonename = obj.optString("Zonename");
                                    String Overall_GrowthPercentage = obj.optString("Overall_GrowthPercentage");
                                    a.add(Zonename);
                                    b.add(Sold);
                                    c.add(prev_sold);

                                }

                                ArrayList<String> uniqueList = (ArrayList) a.stream().distinct().collect(Collectors.toList());

                                Log.d("ps",uniqueList.toString());
                                ArrayList<String>bd=new ArrayList<>();
                                for (int i = 0; i < uniqueList.size(); i++) {
                                    cypyAchvxvals.add(uniqueList.get(i));
                                }



                                ArrayList<Float> uniqueList1 = (ArrayList) b.stream().distinct().collect(Collectors.toList());
                                for (int i=0;i<uniqueList1.size();i++){
                                    cypyAchvYvals.add(new Entry(i,uniqueList1.get(i)));
                                }

                                ArrayList<Float> uniqueList2 = (ArrayList) c.stream().distinct().collect(Collectors.toList());
                                for (int i=0;i<uniqueList2.size();i++){
                                    cypyAchvYYvals.add(new BarEntry(i,uniqueList2.get(i)));
                                }

                                //cy target and sold combined chart
                                comCYPYTargetChart=view.findViewById(R.id.comCYPYTargetChart);
                                comCYPYTargetChart.setTouchEnabled(false);

                                comCYPYTargetChart.getDescription().setText("");
                                comCYPYTargetChart.setDrawGridBackground(false);
                                comCYPYTargetChart.setDrawBarShadow(false);
                                comCYPYTargetChart.setHighlightFullBarEnabled(false);
                                comCYTargetChart.setDrawOrder(new CombinedChart.DrawOrder[]{
                                        CombinedChart.DrawOrder.BAR,  CombinedChart.DrawOrder.LINE
                                });
                                comCYPYTargetChart.animateX(1000);


                                Legend l = comCYPYTargetChart.getLegend();
                                l.setWordWrapEnabled(true);
                                l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
                                l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
                                l.setOrientation(Legend.LegendOrientation.HORIZONTAL);

                                YAxis rightAxis = comCYPYTargetChart.getAxisRight();
                                rightAxis.setEnabled(false);
                                rightAxis.setDrawGridLines(false);
                                rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

                                YAxis leftAxis = comCYPYTargetChart.getAxisLeft();
                                leftAxis.setDrawGridLines(false);
                                leftAxis.setAxisMinimum(0f);

                                XAxis xAxis = comCYPYTargetChart.getXAxis();
                                //  xAxis.setPosition(XAxis.XAxisPosition.BOTH_SIDED);
                                xAxis.setAxisMinimum(0f);
                                xAxis.setGranularity(1f);
                                xAxis.setValueFormatter(new IndexAxisValueFormatter(cypyAchvxvals));



                                CombinedData data = new CombinedData();

                                data.setData( generateLineDataForCYAchv());
                                data.setData(generateBarDataForPYAchv());

                                //  xAxis.setAxisMaximum(data.getXMax() + 0.25f);
                                comCYPYTargetChart.setData(data);
                                comCYPYTargetChart.invalidate();

                                getItemListForCYRevenue();






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

    private void getItemListForCYRevenue() {
        final ArrayList<Float>b=new ArrayList<>();
        final ArrayList<Float>c=new ArrayList<>();
        final ArrayList<String>a=new ArrayList<>();
        Log.d("Arpan", "arpan");

        flMain.setVisibility(View.GONE);
        llLoader.setVisibility(View.VISIBLE);
        String surl = AppData.url + "get_EmployeeSalesReport?ClientID=" + pref.getEmpClintId() + "&UserID=" + pref.getEmpId() + "&FinancialYear=" + financialYear + "&Month=" + month + "&RType=ZM&Operation=7&SecurityCode=" + pref.getSecurityCode();
        Log.d("input", surl);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onResponse(String response) {

                        Log.d("responseAttendance", response);
                        flMain.setVisibility(View.GONE);
                        llLoader.setVisibility(View.VISIBLE);
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
                                    float Sold = Float.parseFloat(obj.optString("Overall_Revenue_Sold"));
                                    Float prev_sold=Float.parseFloat(obj.optString("Overall_Prev_Revenue_Sold"));
                                    float Target = Float.parseFloat(obj.optString("Overall_Revenue_Target"));
                                    String Month = obj.optString("Month");
                                    String Zonename = obj.optString("Zonename");
                                    String Overall_GrowthPercentage = obj.optString("Overall_GrowthPercentage");
                                    a.add(Zonename);
                                    b.add(Sold);
                                    c.add(Target);

                                }

                                ArrayList<String> uniqueList = (ArrayList) a.stream().distinct().collect(Collectors.toList());

                                Log.d("ps",uniqueList.toString());
                                ArrayList<String>bd=new ArrayList<>();
                                for (int i = 0; i < uniqueList.size(); i++) {
                                    cyRevenuexvals.add(uniqueList.get(i));
                                }



                                ArrayList<Float> uniqueList1 = (ArrayList) b.stream().distinct().collect(Collectors.toList());
                                for (int i=0;i<uniqueList1.size();i++){
                                    cyRevenueYvals.add(new Entry(i,uniqueList1.get(i)));
                                }

                                ArrayList<Float> uniqueList2 = (ArrayList) c.stream().distinct().collect(Collectors.toList());
                                for (int i=0;i<uniqueList2.size();i++){
                                    cyRevenueYYvals.add(new BarEntry(i,uniqueList2.get(i)));
                                }


                                //cy target and sold combined chart
                                comCYRevenueChart=view.findViewById(R.id.comCYRevenueChart);
                                comCYRevenueChart.setTouchEnabled(false);

                                comCYRevenueChart.getDescription().setText("");
                                comCYRevenueChart.setDrawGridBackground(false);
                                comCYRevenueChart.setDrawBarShadow(false);
                                comCYRevenueChart.setHighlightFullBarEnabled(false);
                                comCYRevenueChart.setDrawOrder(new CombinedChart.DrawOrder[]{
                                        CombinedChart.DrawOrder.BAR,  CombinedChart.DrawOrder.LINE
                                });
                                comCYRevenueChart.animateX(1000);


                                Legend l = comCYRevenueChart.getLegend();
                                l.setWordWrapEnabled(true);
                                l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
                                l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
                                l.setOrientation(Legend.LegendOrientation.HORIZONTAL);

                                YAxis rightAxis = comCYRevenueChart.getAxisRight();
                                rightAxis.setEnabled(false);
                                rightAxis.setDrawGridLines(false);
                                rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

                                YAxis leftAxis = comCYRevenueChart.getAxisLeft();
                                leftAxis.setDrawGridLines(false);
                                leftAxis.setAxisMinimum(0f);

                                XAxis xAxis = comCYRevenueChart.getXAxis();
                                //  xAxis.setPosition(XAxis.XAxisPosition.BOTH_SIDED);
                                xAxis.setAxisMinimum(0f);
                                xAxis.setGranularity(1f);
                                xAxis.setValueFormatter(new IndexAxisValueFormatter(cyRevenuexvals));



                                CombinedData data = new CombinedData();

                                data.setData( generateLineDataForCYRevenue());
                                data.setData(generateBarDataForCYRevenue());

                                //  xAxis.setAxisMaximum(data.getXMax() + 0.25f);
                                comCYRevenueChart.setData(data);
                                comCYRevenueChart.invalidate();

                                getItemListForCYPYRevenue();






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

    private void getItemListForCYPYRevenue() {
        final ArrayList<Float>b=new ArrayList<>();
        final ArrayList<Float>c=new ArrayList<>();
        final ArrayList<String>a=new ArrayList<>();
        Log.d("Arpan", "arpan");

        flMain.setVisibility(View.GONE);
        llLoader.setVisibility(View.VISIBLE);
        String surl = AppData.url + "get_EmployeeSalesReport?ClientID=" + pref.getEmpClintId() + "&UserID=" + pref.getEmpId() + "&FinancialYear=" + financialYear + "&Month=" + month + "&RType=ZM&Operation=7&SecurityCode=" + pref.getSecurityCode();
        Log.d("input", surl);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onResponse(String response) {

                        Log.d("responseAttendance", response);
                        flMain.setVisibility(View.VISIBLE);
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
                                    float Sold = Float.parseFloat(obj.optString("Overall_Revenue_Sold"));
                                    float Target = Float.parseFloat(obj.optString("Overall_Prev_Revenue_Sold"));
                                    String Month = obj.optString("Month");
                                    String Zonename = obj.optString("Zonename");
                                    String Overall_GrowthPercentage = obj.optString("Overall_GrowthPercentage");
                                    a.add(Zonename);
                                    b.add(Sold);
                                    c.add(Target);

                                }

                                ArrayList<String> uniqueList = (ArrayList) a.stream().distinct().collect(Collectors.toList());

                                Log.d("ps",uniqueList.toString());
                                ArrayList<String>bd=new ArrayList<>();
                                for (int i = 0; i < uniqueList.size(); i++) {
                                    cypyRevenuexvals.add(uniqueList.get(i));
                                }



                                ArrayList<Float> uniqueList1 = (ArrayList) b.stream().distinct().collect(Collectors.toList());
                                for (int i=0;i<uniqueList1.size();i++){
                                    cypyRevenueYvals.add(new Entry(i,uniqueList1.get(i)));
                                }

                                ArrayList<Float> uniqueList2 = (ArrayList) c.stream().distinct().collect(Collectors.toList());
                                for (int i=0;i<uniqueList2.size();i++){
                                    cypyRevenueYYvals.add(new BarEntry(i,uniqueList2.get(i)));
                                }



                                //cy target and sold combined chart
                                comCYPYRevenueChart=view.findViewById(R.id.comCYPYRevenueChart);
                                comCYPYRevenueChart.setTouchEnabled(false);

                                comCYPYRevenueChart.getDescription().setText("");
                                comCYPYRevenueChart.setDrawGridBackground(false);
                                comCYPYRevenueChart.setDrawBarShadow(false);
                                comCYPYRevenueChart.setHighlightFullBarEnabled(false);
                                comCYPYRevenueChart.setDrawOrder(new CombinedChart.DrawOrder[]{
                                        CombinedChart.DrawOrder.BAR,  CombinedChart.DrawOrder.LINE
                                });
                                comCYPYRevenueChart.animateX(1000);


                                Legend l = comCYPYRevenueChart.getLegend();
                                l.setWordWrapEnabled(true);
                                l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
                                l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
                                l.setOrientation(Legend.LegendOrientation.HORIZONTAL);

                                YAxis rightAxis = comCYPYRevenueChart.getAxisRight();
                                rightAxis.setEnabled(false);
                                rightAxis.setDrawGridLines(false);
                                rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

                                YAxis leftAxis = comCYPYRevenueChart.getAxisLeft();
                                leftAxis.setDrawGridLines(false);
                                leftAxis.setAxisMinimum(0f);

                                XAxis xAxis = comCYPYRevenueChart.getXAxis();
                                //  xAxis.setPosition(XAxis.XAxisPosition.BOTH_SIDED);
                                xAxis.setAxisMinimum(0f);
                                xAxis.setGranularity(1f);
                                xAxis.setValueFormatter(new IndexAxisValueFormatter(cypyRevenuexvals));



                                CombinedData data = new CombinedData();

                                data.setData( generateLineDataForCYRevenueAchv());
                                data.setData(generateBarDataForPYRevenueAchv());

                                //  xAxis.setAxisMaximum(data.getXMax() + 0.25f);
                                comCYPYRevenueChart.setData(data);
                                comCYPYRevenueChart.invalidate();



                               // getItemListForCYPYProductGrowth();




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


    private void getItemListForCYPYProductGrowth() {
        final ArrayList<Float>b=new ArrayList<>();
        final ArrayList<Float>c=new ArrayList<>();
        final ArrayList<String>a=new ArrayList<>();
        Log.d("Arpan", "arpan");

        flMain.setVisibility(View.GONE);
        llLoader.setVisibility(View.VISIBLE);
        String surl = AppData.url + "get_EmployeeSalesReport?ClientID=" + pref.getEmpClintId() + "&UserID=" + pref.getEmpId() + "&FinancialYear=" + financialYear + "&Month=" + month + "&RType=ZM&Operation=7&SecurityCode=" + pref.getSecurityCode();
        Log.d("input", surl);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onResponse(String response) {

                        Log.d("responseAttendance", response);
                        flMain.setVisibility(View.GONE);
                        llLoader.setVisibility(View.VISIBLE);
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
                                    float Sold = Float.parseFloat(obj.optString("Overall_AchvPer"));
                                    float Target = Float.parseFloat(obj.optString("Overall_Prev_AchvPer"));
                                    String Month = obj.optString("Month");
                                    String Zonename = obj.optString("Zonename");
                                    String Overall_GrowthPercentage = obj.optString("Overall_GrowthPercentage");
                                    a.add(Zonename);
                                    b.add(Sold);
                                    c.add(Target);

                                }

                                ArrayList<String> uniqueList = (ArrayList) a.stream().distinct().collect(Collectors.toList());

                                Log.d("ps",uniqueList.toString());
                                ArrayList<String>bd=new ArrayList<>();
                                for (int i = 0; i < uniqueList.size(); i++) {
                                    cypyProductGrowthxvals.add(uniqueList.get(i));
                                }



                                ArrayList<Float> uniqueList1 = (ArrayList) b.stream().distinct().collect(Collectors.toList());
                                for (int i=0;i<uniqueList1.size();i++){
                                    cypyProductGrowthYvals.add(new Entry(i,uniqueList1.get(i)));
                                }

                                ArrayList<Float> uniqueList2 = (ArrayList) c.stream().distinct().collect(Collectors.toList());
                                for (int i=0;i<uniqueList2.size();i++){
                                    cypyProductGrowthYYvals.add(new BarEntry(i,uniqueList2.get(i)));
                                }


                                //cy target and sold combined chart
                                comCYPYProductGrowthChart=view.findViewById(R.id.comCYPYProductGrowthChart);
                                comCYPYProductGrowthChart.setTouchEnabled(false);

                                comCYPYProductGrowthChart.getDescription().setText("");
                                comCYPYProductGrowthChart.setDrawGridBackground(true);
                                comCYPYProductGrowthChart.setDrawBarShadow(true);
                                comCYPYProductGrowthChart.setHighlightFullBarEnabled(true);
                                comCYPYProductGrowthChart.setDrawOrder(new CombinedChart.DrawOrder[]{
                                        CombinedChart.DrawOrder.BAR,  CombinedChart.DrawOrder.LINE
                                });
                                comCYPYProductGrowthChart.animateX(1000);


                                Legend l = comCYPYProductGrowthChart.getLegend();
                                l.setWordWrapEnabled(true);
                                l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
                                l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
                                l.setOrientation(Legend.LegendOrientation.HORIZONTAL);

                                YAxis rightAxis = comCYPYProductGrowthChart.getAxisRight();
                                rightAxis.setEnabled(false);
                                rightAxis.setDrawGridLines(false);
                                rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

                                YAxis leftAxis = comCYPYProductGrowthChart.getAxisLeft();
                                leftAxis.setDrawGridLines(false);
                                leftAxis.setAxisMinimum(0f);

                                XAxis xAxis = comCYPYProductGrowthChart.getXAxis();
                                //  xAxis.setPosition(XAxis.XAxisPosition.BOTH_SIDED);
                                xAxis.setAxisMinimum(0f);
                                xAxis.setGranularity(1f);
                                xAxis.setValueFormatter(new IndexAxisValueFormatter(cypyProductGrowthxvals));



                                CombinedData data = new CombinedData();

                                data.setData( generateLineDataForCYProductGrowth());
                                data.setData(generateBarDataForPYProductGrowth());

                                //  xAxis.setAxisMaximum(data.getXMax() + 0.25f);
                                comCYPYProductGrowthChart.setData(data);
                                comCYPYProductGrowthChart.invalidate();

                                getItemListForCYPYRevenueGrowth();






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

    private void getItemListForCYPYRevenueGrowth() {
        final ArrayList<Float>b=new ArrayList<>();
        final ArrayList<Float>c=new ArrayList<>();
        final ArrayList<String>a=new ArrayList<>();
        Log.d("Arpan", "arpan");

        flMain.setVisibility(View.GONE);
        llLoader.setVisibility(View.VISIBLE);
        String surl = AppData.url + "get_EmployeeSalesReport?ClientID=" + pref.getEmpClintId() + "&UserID=" + pref.getEmpId() + "&FinancialYear=" + financialYear + "&Month=" + month + "&RType=ZM&Operation=7&SecurityCode=" + pref.getSecurityCode();
        Log.d("input", surl);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onResponse(String response) {

                        Log.d("responseAttendance", response);
                        flMain.setVisibility(View.VISIBLE);
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
                                    float Sold = Float.parseFloat(obj.optString("Overall_Revenue_AchvPer"));
                                    float Target = Float.parseFloat(obj.optString("Overall_Prev_Revenue_AchvPer"));
                                    String Month = obj.optString("Month");
                                    String Zonename = obj.optString("Zonename");
                                    String Overall_GrowthPercentage = obj.optString("Overall_GrowthPercentage");
                                    a.add(Zonename);
                                    b.add(Sold);
                                    c.add(Target);

                                }

                                ArrayList<String> uniqueList = (ArrayList) a.stream().distinct().collect(Collectors.toList());

                                Log.d("ps",uniqueList.toString());
                                ArrayList<String>bd=new ArrayList<>();
                                for (int i = 0; i < uniqueList.size(); i++) {
                                    cypyRevenueGrowthxvals.add(uniqueList.get(i));
                                }



                                ArrayList<Float> uniqueList1 = (ArrayList) b.stream().distinct().collect(Collectors.toList());
                                for (int i=0;i<uniqueList1.size();i++){
                                    cypyRevenueGrowthYvals.add(new Entry(i,uniqueList1.get(i)));
                                }

                                ArrayList<Float> uniqueList2 = (ArrayList) c.stream().distinct().collect(Collectors.toList());
                                for (int i=0;i<uniqueList2.size();i++){
                                    cypyRevenueGrowthYYvals.add(new BarEntry(i,uniqueList2.get(i)));
                                }
                                //cy target and sold combined chart
                                comCYPYRevenueGrowthChart=view.findViewById(R.id.comCYPYRevenueGrowthChart);
                                comCYPYRevenueGrowthChart.setTouchEnabled(false);

                                comCYPYRevenueGrowthChart.getDescription().setText("");
                                comCYPYRevenueGrowthChart.setDrawGridBackground(true);
                                comCYPYRevenueGrowthChart.setDrawBarShadow(true);
                                comCYPYRevenueGrowthChart.setHighlightFullBarEnabled(true);
                                comCYPYRevenueGrowthChart.setDrawOrder(new CombinedChart.DrawOrder[]{
                                        CombinedChart.DrawOrder.BAR,  CombinedChart.DrawOrder.LINE
                                });
                                comCYPYRevenueGrowthChart.animateX(1000);


                                Legend l = comCYPYRevenueGrowthChart.getLegend();
                                l.setWordWrapEnabled(true);
                                l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
                                l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
                                l.setOrientation(Legend.LegendOrientation.HORIZONTAL);

                                YAxis rightAxis = comCYPYRevenueGrowthChart.getAxisRight();
                                rightAxis.setEnabled(false);
                                rightAxis.setDrawGridLines(false);
                                rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

                                YAxis leftAxis = comCYPYRevenueGrowthChart.getAxisLeft();
                                leftAxis.setDrawGridLines(false);
                                leftAxis.setAxisMinimum(0f);

                                XAxis xAxis = comCYPYRevenueGrowthChart.getXAxis();
                                //  xAxis.setPosition(XAxis.XAxisPosition.BOTH_SIDED);
                                xAxis.setAxisMinimum(0f);
                                xAxis.setGranularity(1f);
                                xAxis.setValueFormatter(new IndexAxisValueFormatter(cypyRevenueGrowthxvals));



                                CombinedData data = new CombinedData();

                                data.setData( generateLineDataForCYRevenueGrowth());
                                data.setData(generateBarDataForPYRevenueGrowth());

                                //  xAxis.setAxisMaximum(data.getXMax() + 0.25f);
                                comCYPYRevenueGrowthChart.setData(data);
                                comCYPYRevenueGrowthChart.invalidate();






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

    private LineData generateLineDataForCYSold() {

        LineData d = new LineData();

        ArrayList<Entry> entries = new ArrayList<Entry>();

        entries = cyTargetYvals;

        LineDataSet set = new LineDataSet(entries, "CY Achievement (Line Chart)");
        //set.setColor(Color.rgb(240, 238, 70));
        set.setColors(Color.GRAY);
        set.setLineWidth(3.5f);

        set.setCircleColor(Color.BLACK);
        set.setCircleRadius(5f);
        set.setFillColor(Color.BLACK);
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set.setDrawValues(true);
        set.setValueTextSize(10f);
        set.setValueTextColor(Color.BLACK);


        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        d.addDataSet(set);

        return d;
    }

    private BarData generateBarDataForCYTarget() {

        ArrayList<BarEntry> entries = new ArrayList<BarEntry>();
        entries = cyTargetYYvals;

        BarDataSet set1 = new BarDataSet(entries, "CY Target (Bar Chart)");
        //set1.setColor(Color.rgb(60, 220, 78));
        set1.setColors(Color.rgb(107,142,35));
        set1.setValueTextColor(Color.BLACK);
        set1.setValueTextSize(10f);
        set1.setAxisDependency(YAxis.AxisDependency.LEFT);

        float barWidth = 0.45f; // x2 dataset

        BarData d = new BarData(set1);
        d.setBarWidth(barWidth);

        return d;
    }


    private LineData generateLineDataForCYAchv() {

        LineData d = new LineData();

        ArrayList<Entry> entries = new ArrayList<Entry>();

        entries = cypyAchvYvals;

        LineDataSet set = new LineDataSet(entries, "CY Achievement  (Line Chart)");
        //set.setColor(Color.rgb(240, 238, 70));
        set.setColors(Color.GRAY);
        set.setLineWidth(3.5f);

        set.setCircleColor(Color.BLACK);
        set.setCircleRadius(5f);
        set.setFillColor(Color.BLACK);
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set.setDrawValues(true);
        set.setValueTextSize(10f);
        set.setValueTextColor(Color.BLACK);


        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        d.addDataSet(set);

        return d;
    }

    private BarData generateBarDataForPYAchv() {

        ArrayList<BarEntry> entries = new ArrayList<BarEntry>();
        entries = cypyAchvYYvals;

        BarDataSet set1 = new BarDataSet(entries, "LY Achievement (Bar Chart)");
        //set1.setColor(Color.rgb(60, 220, 78));
        set1.setColors(Color.rgb(107,142,35));
        set1.setValueTextColor(Color.BLACK);
        set1.setValueTextSize(10f);
        set1.setAxisDependency(YAxis.AxisDependency.LEFT);

        float barWidth = 0.45f; // x2 dataset

        BarData d = new BarData(set1);
        d.setBarWidth(barWidth);

        return d;
    }

    private LineData generateLineDataForCYRevenue() {

        LineData d = new LineData();

        ArrayList<Entry> entries = new ArrayList<Entry>();

        entries = cyRevenueYvals;

        LineDataSet set = new LineDataSet(entries, "CY  Achievement  (Line Chart)");
        //set.setColor(Color.rgb(240, 238, 70));
        set.setColors(Color.GRAY);
        set.setLineWidth(3.5f);

        set.setCircleColor(Color.BLACK);
        set.setCircleRadius(5f);
        set.setFillColor(Color.BLACK);
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set.setDrawValues(true);
        set.setValueTextSize(8f);
        set.setValueTextColor(Color.BLACK);


        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        d.addDataSet(set);

        return d;
    }

    private BarData generateBarDataForCYRevenue() {

        ArrayList<BarEntry> entries = new ArrayList<BarEntry>();
        entries = cyRevenueYYvals;

        BarDataSet set1 = new BarDataSet(entries, "CY  Target  (Bar Chart)");
        //set1.setColor(Color.rgb(60, 220, 78));
        set1.setColors(Color.rgb(107,142,35));
        set1.setValueTextColor(Color.BLACK);
        set1.setValueTextSize(10f);
        set1.setAxisDependency(YAxis.AxisDependency.LEFT);

        float barWidth = 0.45f; // x2 dataset

        BarData d = new BarData(set1);
        d.setBarWidth(barWidth);

        return d;
    }

    private LineData generateLineDataForCYRevenueAchv() {

        LineData d = new LineData();

        ArrayList<Entry> entries = new ArrayList<Entry>();

        entries = cypyRevenueYvals;

        LineDataSet set = new LineDataSet(entries, "CY  Achievement  (Line Chart)");
        //set.setColor(Color.rgb(240, 238, 70));
        set.setColors(Color.GRAY);
        set.setLineWidth(3.5f);

        set.setCircleColor(Color.BLACK);
        set.setCircleRadius(5f);
        set.setFillColor(Color.BLACK);
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set.setDrawValues(true);
        set.setValueTextSize(8f);

        set.setValueTextColor(Color.BLACK);


        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        d.addDataSet(set);

        return d;
    }

    private BarData generateBarDataForPYRevenueAchv() {

        ArrayList<BarEntry> entries = new ArrayList<BarEntry>();
        entries = cypyRevenueYYvals;

        BarDataSet set1 = new BarDataSet(entries, "LY  Acheivement  (Bar Chart)");
        //set1.setColor(Color.rgb(60, 220, 78));
        set1.setColors(Color.rgb(107,142,35));
        set1.setValueTextColor(Color.BLACK);
        set1.setValueTextSize(10f);
        set1.setAxisDependency(YAxis.AxisDependency.LEFT);

        float barWidth = 0.45f; // x2 dataset

        BarData d = new BarData(set1);
        d.setBarWidth(barWidth);

        return d;
    }

    private LineData generateLineDataForCYProductGrowth() {

        LineData d = new LineData();

        ArrayList<Entry> entries = new ArrayList<Entry>();

        entries = cypyProductGrowthYvals;

        LineDataSet set = new LineDataSet(entries, "CY  Overall Growth (%) (Line Chart)");
        //set.setColor(Color.rgb(240, 238, 70));
        set.setColors(Color.GRAY);
        set.setLineWidth(3.5f);

        set.setCircleColor(Color.BLACK);
        set.setCircleRadius(5f);
        set.setFillColor(Color.BLACK);
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set.setDrawValues(true);
        set.setValueTextSize(10f);
        set.setValueTextColor(Color.BLACK);


        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        d.addDataSet(set);

        return d;
    }

    private BarData generateBarDataForPYProductGrowth() {

        ArrayList<BarEntry> entries = new ArrayList<BarEntry>();
        entries = cypyProductGrowthYYvals;

        BarDataSet set1 = new BarDataSet(entries, "LY Overall Growth (%) (Bar Chart)");
        //set1.setColor(Color.rgb(60, 220, 78));
        set1.setColors(Color.rgb(107,142,35));
        set1.setValueTextColor(Color.BLACK);
        set1.setValueTextSize(10f);
        set1.setAxisDependency(YAxis.AxisDependency.LEFT);

        float barWidth = 0.45f; // x2 dataset

        BarData d = new BarData(set1);
        d.setBarWidth(barWidth);

        return d;
    }

    private LineData generateLineDataForCYRevenueGrowth() {

        LineData d = new LineData();

        ArrayList<Entry> entries = new ArrayList<Entry>();

        entries = cypyRevenueGrowthYvals;

        LineDataSet set = new LineDataSet(entries, "CY Overall Growth (%) (Line Chart)");
        //set.setColor(Color.rgb(240, 238, 70));
        set.setColors(Color.GRAY);
        set.setLineWidth(3.5f);

        set.setCircleColor(Color.BLACK);
        set.setCircleRadius(5f);
        set.setFillColor(Color.BLACK);
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set.setDrawValues(true);
        set.setValueTextSize(10f);
        set.setValueTextColor(Color.BLACK);


        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        d.addDataSet(set);

        return d;
    }

    private BarData generateBarDataForPYRevenueGrowth() {

        ArrayList<BarEntry> entries = new ArrayList<BarEntry>();
        entries = cypyRevenueGrowthYYvals;

        BarDataSet set1 = new BarDataSet(entries, "LY Overall Growth (%) (Bar Chart)");
        //set1.setColor(Color.rgb(60, 220, 78));
        set1.setColors(Color.rgb(107,142,35));
        set1.setValueTextColor(Color.BLACK);
        set1.setValueTextSize(10f);
        set1.setAxisDependency(YAxis.AxisDependency.LEFT);

        float barWidth = 0.45f; // x2 dataset

        BarData d = new BarData(set1);
        d.setBarWidth(barWidth);

        return d;
    }

}